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

public class TransferManagerConfiguration {

  private static final int DEFAULT_NUMBER_OF_WORKER_THREADS = 4;
  private static final int DEFAULT_MAX_PART_RETRY = 5;
  private static final int DEFAULT_MAX_PARALLEL_UPLOADS = 2;

  private int numberOfWorkerThreads = DEFAULT_NUMBER_OF_WORKER_THREADS;
  private int maxPartRetry = DEFAULT_MAX_PART_RETRY;
  private int maxParallelUploads = DEFAULT_MAX_PARALLEL_UPLOADS;

  TransferManagerConfiguration() { /* Prevent construction from outside of package */ }

  public TransferManagerConfiguration(int numberOfWorkerThreads, int maxPartRetry, int maxParallelUploads) {
    this.numberOfWorkerThreads = numberOfWorkerThreads > 0 ? numberOfWorkerThreads : DEFAULT_NUMBER_OF_WORKER_THREADS;
    this.maxPartRetry = maxPartRetry > 0 ? maxPartRetry : DEFAULT_MAX_PART_RETRY;
    this.maxParallelUploads = maxParallelUploads > 0 ? maxParallelUploads : DEFAULT_MAX_PARALLEL_UPLOADS;
  }

  int getNumberOfWorkerThreads() {
    return numberOfWorkerThreads;
  }

  int getMaxPartRetry() {
    return maxPartRetry;
  }

  int getMaxParallelUploads() {
    return maxParallelUploads;
  }
}
