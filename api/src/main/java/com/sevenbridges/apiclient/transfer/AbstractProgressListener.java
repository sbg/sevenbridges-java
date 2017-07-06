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
package com.sevenbridges.apiclient.transfer;

/**
 * Abstract progress listener that can be implemented and used with {@link
 * com.sevenbridges.apiclient.user.UserActions#submitUpload(com.sevenbridges.apiclient.upload.CreateUploadRequest,
 * ProgressListener)} calls. This way you can get information about events of the upload. Similar to
 * the {@link ProgressListener}, but you can implement only methods that you need so it might be
 * more convenient for you.
 */
public abstract class AbstractProgressListener implements ProgressListener {

  /** {@inheritDoc} */
  @Override
  public void uploadStarted() {

  }

  /** {@inheritDoc} */
  @Override
  public void uploadFailed(Exception ex) {

  }

  /** {@inheritDoc} */
  @Override
  public void uploadFinished() {

  }

  /** {@inheritDoc} */
  @Override
  public void partUploadStarted(int partNumber) {

  }

  /** {@inheritDoc} */
  @Override
  public void partUploadFinished(int partNumber, long partSize) {

  }

  /** {@inheritDoc} */
  @Override
  public void partUploadFailed(int partNumber, int retryCnt, Exception executionException) {

  }
}
