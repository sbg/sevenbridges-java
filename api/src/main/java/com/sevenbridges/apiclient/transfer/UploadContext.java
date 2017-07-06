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

import com.sevenbridges.apiclient.file.File;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This object allows user to take control of upload submitted to internal transfer manager via the
 * {@link com.sevenbridges.apiclient.user.UserActions#submitUpload(com.sevenbridges.apiclient.upload.CreateUploadRequest)}
 * calls.
 */
public interface UploadContext {

  /**
   * <b>BLOCKING CALL</b>
   * <p>
   * This is a blocking call, similar to invoking get on a {@link java.util.concurrent.Future}
   * instance. Current thread will block until the file is fully uploaded to the Seven Bridges
   * Platform, or until some exception happen. If the thread is interrupted from sleep, a runtime
   * exception wrapping the interrupted exception will be thrown. If some other thread aborts, or
   * pauses the upload, another runtime exception will be called, and will wake up the current
   * sleeping thread.
   * <p>
   * If the upload is completed successfully this call will return the {@link File} resource
   * instance that is uploaded.
   *
   * @return File 'file' resource that is uploaded
   * @throws PausedUploadException if the current upload the thread is blocked on is paused by
   *                               {@link #pauseTransfer()} call by some other thread
   */
  File getFile() throws PausedUploadException;

  /**
   * <b>BLOCKING CALL</b>
   * <p>
   * This is a timed blocking call, similar to invoking get(timeValue, timeUnit) on a {@link
   * java.util.concurrent.Future} instance. Current thread will wait specified time until the file
   * is fully uploaded to the Seven Bridges Platform, or until some exception happen. If the thread
   * is interrupted from sleep, a runtime exception wrapping the interrupted exception will be
   * thrown. If some other thread aborts, or pauses the upload, another runtime exception will be
   * called, and will wake up the current sleeping thread.
   * <p>
   * If the upload is completed successfully this call will return the {@link File} resource
   * instance that is uploaded.
   *
   * @param timeValue the maximum number of TimeUnits to wait
   * @param timeUnit  durations of one unit of timeValue
   * @return File 'file' resource that is uploaded
   * @throws TimeoutException      if the wait time times out, and the upload is still not
   *                               completed
   * @throws PausedUploadException if the current upload the thread is blocked on is paused by
   *                               {@link #pauseTransfer()} call by some other thread
   */
  File getFile(long timeValue, TimeUnit timeUnit) throws TimeoutException, PausedUploadException;

  /**
   * Checks if the current {@link com.sevenbridges.apiclient.upload.Upload}, managed by this
   * UploadContext is finished successfully.
   *
   * @return Boolean indicator is upload finished
   */
  boolean isFinished();

  /**
   * Aborts the {@link com.sevenbridges.apiclient.upload.Upload} managed by this UploadContext. Any
   * thread blocked on the getFile() call on this uploadContext will be woken up by the
   * RuntimeException. Upload is aborted totally, and any progress on this upload will be lost.
   */
  void abortTransfer();

  /**
   * Gets the current state of the upload managed by this UploadContext.
   *
   * @return UploadState current state
   */
  UploadState getState();

  /**
   * Pauses the {@link com.sevenbridges.apiclient.upload.Upload} managed by this UploadContext. Any
   * thread blocked on the getFile() call on this uploadContext will be woken up by the
   * RuntimeException that indicates that the upload is paused. Paused upload is not aborted on the
   * Platform, and your progress (measured in file parts) is saved.
   * <p>
   * You can use this UploadContext object to resume upload via {@link
   * com.sevenbridges.apiclient.user.UserActions#resumeUpload(UploadContext, java.io.File)} call.
   * That call will provide a new instance of UploadContext, this one is useless after that call.
   * <p>
   * Pause action is not instantaneous, the call is not blocking, and it will put upload in the
   * PAUSING state. After the first running part upload is finished, the upload state will change to
   * PAUSED state.
   */
  void pauseTransfer();

  /**
   * Gets summed number of bytes transferred by the {@link com.sevenbridges.apiclient.upload.Upload}
   * managed by this UploadContext. This is a pretty low level byte counter, and it will update much
   * more often than the part upload finished event.
   *
   * @return Current bytes transferred for this upload
   */
  long getBytesTransferred();

  /**
   * Size of the whole upload in bytes.
   *
   * @return long upload size
   */
  long getUploadSize();

  /**
   * Name of the {@link com.sevenbridges.apiclient.upload.Upload} managed by this UploadContext.
   *
   * @return String upload name
   */
  String getUploadName();

  /**
   * ID of the {@link com.sevenbridges.apiclient.upload.Upload} managed by this UploadContext.
   *
   * @return String ID of the upload managed by this upload context
   */
  String getUploadId();

}
