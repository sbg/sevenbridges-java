/*
 * Copyright 2017 Seven Bridges Genomics, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sevenbridges.apiclient.impl.transfer;

import com.sevenbridges.apiclient.file.File;
import com.sevenbridges.apiclient.impl.transfer.http.protocol.ZeroCopyChannelRangePut;
import com.sevenbridges.apiclient.impl.transfer.model.UploadPartContext;
import com.sevenbridges.apiclient.resource.ResourceException;
import com.sevenbridges.apiclient.transfer.PausedUploadException;
import com.sevenbridges.apiclient.transfer.UploadState;
import com.sevenbridges.apiclient.upload.PartUpload;
import com.sevenbridges.apiclient.upload.Upload;
import com.sevenbridges.apiclient.upload.UploadedPart;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

public class UploadCallable implements Callable<File> {

  private final CloseableHttpAsyncClient client;
  private final DefaultUploadContext uploadContext;
  private final int partRetry;
  private final int maxParallelUploads;

  private final int partsNumber;
  private final long partSize;

  private final CountDownLatch partsLatch;
  private final PartsProducer producer;
  private int parallelUploadsCnt;

  UploadCallable(CloseableHttpAsyncClient client,
                 DefaultUploadContext cntx,
                 int maxPartRetry,
                 int maxParallelUploads) {
    this.client = client;
    this.uploadContext = cntx;
    this.partRetry = maxPartRetry;
    this.maxParallelUploads = maxParallelUploads;

    this.partsNumber = getPartsNumber(cntx);
    this.partSize = uploadContext.getUpload().getPartSize();
    List<UploadedPart> finishedParts = uploadContext.getUpload().getUploadedParts();
    if (finishedParts == null) {
      finishedParts = Collections.emptyList();
    }
    this.producer = new PartsProducer(partsNumber, finishedParts);
    this.partsLatch = new CountDownLatch(partsNumber - finishedParts.size());
    this.parallelUploadsCnt = 0;
  }


  @Override
  public File call() throws Exception {

    FileChannel fileChannel = uploadContext.getRandomAccessFile().getChannel();
    Map<Integer, Future<HttpResponse>> responses = new HashMap<>(partsNumber);
    try {
      client.start();
      uploadContext.setState(UploadState.RUNNING);
      uploadContext.getListener().uploadStarted();

      while (partsLatch.getCount() > 0) {
        // check if upload still exists
        try {
          Upload currUpload = uploadContext.getUpload();
          currUpload.reload();
        } catch (ResourceException e) {
          if (e.getCode() == 404) {
            throw new RuntimeException("Upload not found, probably aborted by some external use");
          }
          throw e;
        } catch (Exception e) {
          throw new RuntimeException("Error while trying to check upload state, aborting upload", e);
        }

        if (UploadState.PAUSING.equals(uploadContext.getState())) {
          uploadContext.setState(UploadState.PAUSED);
          List<UploadPartContext> successfulParts = producer.flushFinishedAndGetSuccessful();
          for (UploadPartContext uploadedPart : successfulParts) {
            // this part finished OK
            uploadContext.getUpload().reportUploadedPart(uploadedPart.getPartNumber(), uploadedPart.getUploadResponse());
            // decrement latch
            partsLatch.countDown();
            // report to listener
            uploadContext.getListener().partUploadFinished(uploadedPart.getPartNumber(), getCurrentPartSize(uploadedPart.getPartNumber()));
            // decrement parallel uploads cnt
            parallelUploadsCnt--;
            // remove from responses map (not needed, but cleaner)
            responses.remove(uploadedPart.getPartNumber());
          }
          for (Future<HttpResponse> response : responses.values()) {
            response.cancel(true);
          }
          throw new PausedUploadException("Upload is paused, this upload is aborted locally, but can be resumed with the same ID - " + uploadContext.getUploadId());
        }

        // get part for processing
        UploadPartContext partContext;
        if (parallelUploadsCnt < maxParallelUploads) {
          partContext = producer.takeNewOrFinished();
        } else {
          partContext = producer.takeFinished();
        }

        // process part depending of its state
        if (partContext.isFailedBeyondRetry()) { // this part failed with no more retries
          // report failed to listener
          uploadContext.getListener().partUploadFailed(partContext.getPartNumber(), partContext.getRetryCnt(), partContext.getEx());
          // abort upload
          abortUpload(responses);
          // throw execution exception
          throw new RuntimeException("Upload failed, part " + partContext.getPartNumber() + " failed beyond retry ", partContext.getEx());

        } else if (partContext.isNew()) { // this part was not initialized
          // get part upload from API server
          PartUpload partUploadResponse = uploadContext.getUpload().getPartUpload(partContext.getPartNumber());
          // submit part upload to async client
          Future<HttpResponse> execute = submitPartUpload(partUploadResponse.getUrl(), partContext.getPartNumber(), fileChannel, partRetry);
          // add future to the responses map, so it can be cancelled if upload fail
          responses.put(partContext.getPartNumber(), execute);
          // increment parallel uploads cnt
          parallelUploadsCnt++;
          // notify listener that new upload part started
          uploadContext.getListener().partUploadStarted(partContext.getPartNumber());

        } else if (partContext.isFailed()) { // this part failed, but can be retried
          // this part failed
          // report to listener
          uploadContext.getListener().partUploadFailed(partContext.getPartNumber(), partContext.getRetryCnt(), partContext.getEx());
          // set bytesAdder cell for this part to 0
          uploadContext.getBytesTransferredAdder().set(partContext.getPartNumber(), 0);
          // resubmit failed upload, decremented part retry
          PartUpload partUploadResponse = uploadContext.getUpload().getPartUpload(partContext.getPartNumber());
          Future<HttpResponse> execute = submitPartUpload(partUploadResponse.getUrl(), partContext.getPartNumber(), fileChannel, partRetry - 1);
          // add future to the responses map, so it can be cancelled if upload fail
          responses.put(partContext.getPartNumber(), execute);
          // notify listener that new upload part started
          uploadContext.getListener().partUploadStarted(partContext.getPartNumber());

        } else if (partContext.isSucceeded()) { // this part succeeded
          // this part finished OK
          uploadContext.getUpload().reportUploadedPart(partContext.getPartNumber(), partContext.getUploadResponse());
          // decrement latch
          partsLatch.countDown();
          // report to listener
          uploadContext.getListener().partUploadFinished(partContext.getPartNumber(), getCurrentPartSize(partContext.getPartNumber()));
          // decrement parallel uploads cnt
          parallelUploadsCnt--;
          // remove from responses map (not needed, but cleaner)
          responses.remove(partContext.getPartNumber());
        }
      }

      // finalize upload on API server
      File finishedFile = uploadContext.getUpload().completeUpload();
      // set state to finished
      uploadContext.setState(UploadState.FINISHED);
      // notify listener
      uploadContext.getListener().uploadFinished();
      return finishedFile;
    } catch (Exception e1) {
      // if paused, just propagate exception to cancel execution
      if (UploadState.PAUSED.equals(uploadContext.getState())) {
        throw e1;
      }
      // unrecoverable exception, abort upload
      uploadContext.getListener().uploadFailed(e1);
      try {
        abortUpload(responses);
      } catch (Exception e2) {
        throw new RuntimeException("Upload failed BUT aborting upload failed too due to exception - '"
            + e2.getMessage() + "'. Please abort this upload with id '"
            + uploadContext.getUploadId() + "' manually", e1);
      }
      throw new RuntimeException("Upload is aborted due to errors in execution", e1);
    } finally {
      try {
        if (fileChannel.isOpen()) {
          fileChannel.close();
        }
        uploadContext.getRandomAccessFile().close();
      } catch (IOException e) {
        // error while closing
        throw e;
      }
    }
  }

  private long getCurrentPartSize(int partNumber) {
    return partNumber == partsNumber
        ? uploadContext.getUploadSize() - (partNumber - 1) * partSize
        : partSize;
  }

  private Future<HttpResponse> submitPartUpload(String uploadUrl, int partNumber, FileChannel fileChannel, int retryCnt) {
    long fileOffset = (partNumber - 1) * partSize;
    long currentPartSize = getCurrentPartSize(partNumber);

    final ZeroCopyChannelRangePut httpPut = new ZeroCopyChannelRangePut(
        uploadUrl,
        fileChannel,
        fileOffset,
        currentPartSize,
        null,
        uploadContext.getBytesTransferredAdder(),
        partNumber);
    BasicAsyncResponseConsumer consumer = new BasicAsyncResponseConsumer();

    return client.execute(httpPut, consumer, new UploadCallable.PartUploadCallback(uploadUrl, partNumber, retryCnt));
  }

  private void abortUpload(Map<Integer, Future<HttpResponse>> responses) {
    for (Future<HttpResponse> response : responses.values()) {
      response.cancel(true);
    }
    uploadContext.getUpload().abortUpload();
  }

  private int getPartsNumber(DefaultUploadContext context) {
    return context.getUploadSize() % context.getUpload().getPartSize() == 0
        ? (int) (context.getUploadSize() / context.getUpload().getPartSize())
        : (int) (context.getUploadSize() / context.getUpload().getPartSize() + 1);
  }

  private class PartUploadCallback implements FutureCallback<HttpResponse> {

    private final int partNumber;
    private final String uploadUrl;
    private final int retryCnt;

    PartUploadCallback(String uploadUrl, int partNumber, int retryCnt) {
      this.partNumber = partNumber;
      this.uploadUrl = uploadUrl;
      this.retryCnt = retryCnt;
    }

    @Override
    public void completed(HttpResponse result) {
      if (checkUploadCompleted(result)) {
        UploadCallable.this.producer.putFinished(getSucceededPartContext(result));
      } else {
        UploadCallable.this.producer.putFinished(UploadPartContext
            .buildFailedContext(partNumber, uploadUrl, new RuntimeException("Malformed response from storage provider"), partRetry));
      }
    }

    @Override
    public void failed(Exception ex) {
      UploadCallable.this.producer.putFinished(UploadPartContext
          .buildFailedContext(partNumber, uploadUrl, ex, retryCnt));
    }

    @Override
    public void cancelled() {
      // do nothing, everything is cancelled
    }

    private boolean checkUploadCompleted(HttpResponse result) {
      int statusCode = result.getStatusLine().getStatusCode();
      return statusCode == HttpStatus.SC_OK && result.getFirstHeader("ETag") != null;
    }

    private UploadPartContext getSucceededPartContext(HttpResponse result) {
      final String eTag = result.getFirstHeader("ETag").getValue().replaceAll("\"", "");
      return UploadPartContext.buildSucceedContext(partNumber, eTag, uploadUrl);
    }

  }
}
