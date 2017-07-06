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

import com.sevenbridges.apiclient.transfer.ProgressListener;

public final class NoopProgressListener implements ProgressListener {

  private static NoopProgressListener INSTANCE = new NoopProgressListener();

  private NoopProgressListener() { /* Intentionally private to enforce singleton pattern. */ }

  public static NoopProgressListener getInstance() {
    return INSTANCE;
  }

  @Override
  public void uploadStarted() {
    // do nothing
  }

  @Override
  public void uploadFailed(Exception ex) {
    // do nothing
  }

  @Override
  public void uploadFinished() {
    // do nothing
  }

  @Override
  public void partUploadStarted(int partNumber) {
    // do nothing
  }

  @Override
  public void partUploadFinished(int partNumber, long partSize) {
    // do nothing
  }

  @Override
  public void partUploadFailed(int partNumber, int retryCnt, Exception executionException) {
    // do nothing
  }
}
