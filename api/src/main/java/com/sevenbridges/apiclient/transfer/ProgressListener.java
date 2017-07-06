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
 * Interface for progress listener that can be implemented and used with {@link
 * com.sevenbridges.apiclient.user.UserActions#submitUpload(com.sevenbridges.apiclient.upload.CreateUploadRequest,
 * ProgressListener)} calls. This way you can get information about events of the upload. With
 * interface you need to implement all of the event functions, but, if you need only some of the
 * events it might be more convenient to extend {@link AbstractProgressListener}.
 */
public interface ProgressListener {

  /**
   * Event indicating that the upload this listener listens to has started its execution.
   */
  void uploadStarted();

  /**
   * Event indicating that the upload this listener listens to has failed in execution. Provided is
   * the error that happened during execution.
   *
   * @param ex Exception that happened during execution
   */
  void uploadFailed(Exception ex);

  /**
   * Event indicating that the upload this listener listens to has finished successfully.
   */
  void uploadFinished();

  /**
   * Event indicating that the upload this listener listens to has started upload on a new part.
   * Uploads are always considered as a multipart uploads, even if the size of the one part is
   * smaller then the whole file to be uploaded. This event fires when new part upload is started.
   * If the part upload fails in some way, same partNumber can fire this event more than once.
   *
   * @param partNumber Number of the part that started
   */
  void partUploadStarted(int partNumber);

  /**
   * Event indicating that the upload this listener listens to has successfully uploaded and
   * reported one part. Uploads are always considered as a multipart uploads, even if the size of
   * the one part is smaller then the whole file to be uploaded. This event fires when the part
   * upload is finished. This event should fire only once per partNumber of the singular upload.
   *
   * @param partNumber Number of the part that uploaded successfully
   * @param partSize   Long size in bytes of the part that uploaded successfully
   */
  void partUploadFinished(int partNumber, long partSize);

  /**
   * Event indicating that the upload this listener listens to has failed to upload a part. Failure
   * of one part doesn't mean that the whole upload failed, this part will be retried until its
   * retryCount reaches zero, and only then the whole upload will fail. This event can fire more
   * than once per partNumber of the singular upload.
   *
   * @param partNumber         Number of the part that failed
   * @param retryCnt           Retries left of the current part upload
   * @param executionException Exception that caused this upload part to fail
   */
  void partUploadFailed(int partNumber, int retryCnt, Exception executionException);

}
