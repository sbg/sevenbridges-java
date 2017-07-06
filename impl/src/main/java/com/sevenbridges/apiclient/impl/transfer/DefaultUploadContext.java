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
import com.sevenbridges.apiclient.impl.transfer.atomic.StripedLongAdder;
import com.sevenbridges.apiclient.transfer.PausedUploadException;
import com.sevenbridges.apiclient.transfer.ProgressListener;
import com.sevenbridges.apiclient.transfer.UploadContext;
import com.sevenbridges.apiclient.transfer.UploadState;
import com.sevenbridges.apiclient.upload.Upload;
import com.sevenbridges.apiclient.upload.UploadedPart;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class DefaultUploadContext implements UploadContext {

  private UploadState state;
  private Upload upload;
  private RandomAccessFile randomAccessFile;
  private ProgressListener listener;
  private Future<File> fileFuture;
  private StripedLongAdder bytesTransferredAdder;
  private Long uploadSize = null;
  private final Object waitObject = new Object();


  DefaultUploadContext(Upload upload, RandomAccessFile raf, ProgressListener listener) {
    this.upload = upload;
    this.randomAccessFile = raf;
    this.listener = listener;
    this.bytesTransferredAdder = new StripedLongAdder();
    if (upload.getUploadedPartsCount() > 0) {
      this.state = UploadState.QUEUED;
      List<UploadedPart> parts = upload.getUploadedParts();
      for (UploadedPart part : parts) {
        this.bytesTransferredAdder.set(part.getPartNumber(), upload.getPartSize());
      }
    } else {
      this.state = UploadState.CREATED;
    }
    try {
      uploadSize = randomAccessFile.length();
    } catch (IOException e) {
      throw new RuntimeException("Error while checking uploading file size", e);
    }
  }

  Upload getUpload() {
    return upload;
  }

  RandomAccessFile getRandomAccessFile() {
    return randomAccessFile;
  }

  ProgressListener getListener() {
    return listener;
  }

  StripedLongAdder getBytesTransferredAdder() {
    return bytesTransferredAdder;
  }

  void setFileFuture(Future<File> fileFuture) {
    this.fileFuture = fileFuture;
  }

  UploadState setState(UploadState newState) {
    UploadState oldState;
    synchronized (this) {
      oldState = this.state;
      this.state = newState;
      if (UploadState.FINISHED.equals(this.state)) {
        synchronized (waitObject) {
          waitObject.notifyAll();
        }
      }
    }
    return oldState;
  }

  @Override
  public File getFile() {
    try {
      return fileFuture.get();
    } catch (InterruptedException ie) {
      throw new RuntimeException("Interrupted while waiting on file", ie);
    } catch (ExecutionException ee) {
      if ((ee.getCause() != null) && (ee.getCause() instanceof PausedUploadException)) {
        throw (PausedUploadException) ee.getCause();
      }
      throw new RuntimeException("Error while uploading file", ee.getCause());
    }
  }

  @Override
  public File getFile(long timeValue, TimeUnit timeUnit) throws TimeoutException {
    try {
      timeUnit.timedWait(waitObject, timeValue);
      if (isFinished()) {
        return fileFuture.get();
      } else {
        throw new TimeoutException("Timed out while waiting for file");
      }
    } catch (InterruptedException ie) {
      throw new RuntimeException("Interrupted while waiting on file", ie);
    } catch (ExecutionException ee) {
      if ((ee.getCause() != null) && (ee.getCause() instanceof PausedUploadException)) {
        throw (PausedUploadException) ee.getCause();
      }
      throw new RuntimeException("Error while uploading file", ee.getCause());
    }
  }

  @Override
  public boolean isFinished() {
    return fileFuture.isDone() && UploadState.FINISHED.equals(getState());
  }

  @Override
  public void abortTransfer() {
    if (!UploadState.FINISHED.equals(getState())) {
      fileFuture.cancel(true);
      setState(UploadState.ABORTED);
    }
  }

  @Override
  public UploadState getState() {
    UploadState currentState;
    synchronized (this) {
      currentState = UploadState.fromValue(this.state.name());
    }
    return currentState;
  }

  @Override
  public void pauseTransfer() {
    if (UploadState.RUNNING.equals(getState())) {
      setState(UploadState.PAUSING);
    } else {
      throw new RuntimeException("Transfer is not in 'running' state so it can not be paused");
    }
  }

  @Override
  public long getBytesTransferred() {
    return bytesTransferredAdder.sum();
  }

  @Override
  public long getUploadSize() {
    return uploadSize;
  }

  @Override
  public String getUploadName() {
    return upload.getName();
  }

  @Override
  public String getUploadId() {
    return upload.getUploadId();
  }

}
