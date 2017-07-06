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
import com.sevenbridges.apiclient.impl.transfer.http.HttpAsyncClientPool;
import com.sevenbridges.apiclient.transfer.ProgressListener;
import com.sevenbridges.apiclient.upload.Upload;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TransferManager {

  private static final Logger log = LoggerFactory.getLogger(TransferManager.class);

  private final TransferManagerConfiguration conf;
  private final ExecutorService executor;
  private final CloseableHttpAsyncClient client;

  TransferManager(CloseableHttpAsyncClient client) {
    this(client, new TransferManagerConfiguration());
  }

  TransferManager(CloseableHttpAsyncClient client, TransferManagerConfiguration conf) {
    this.client = client;
    this.conf = conf;
    this.executor = Executors.newFixedThreadPool(this.conf.getNumberOfWorkerThreads());
  }

  public void stopService() {
    this.executor.shutdownNow();
    try {
      this.client.close();
    } catch (IOException e) {
      log.debug("Error while closing async client", e);
    }
    try {
      HttpAsyncClientPool.shutdown();
    } catch (InterruptedException e) {
      log.debug("Error while shutting down async client pool", e);
    }
  }


  public synchronized DefaultUploadContext upload(Upload upload, RandomAccessFile file, ProgressListener listener) {

    // we have initialized upload object. Now we need to submit upload for completion and return awaitable object
    DefaultUploadContext uploadContext = new DefaultUploadContext(upload, file, listener);
    Future<File> fileFuture = executor.submit(new UploadCallable(client, uploadContext, conf.getMaxPartRetry(), conf.getMaxParallelUploads()));
    uploadContext.setFileFuture(fileFuture);

    return uploadContext;
  }

}


