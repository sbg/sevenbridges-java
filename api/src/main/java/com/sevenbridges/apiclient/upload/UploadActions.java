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
package com.sevenbridges.apiclient.upload;

import com.sevenbridges.apiclient.file.File;

import java.util.Map;

/**
 * The {@code UploadActions} interface represents common user actions (behaviors) that can be
 * executed for {@link Upload} resources.
 */
interface UploadActions {

  /**
   * Performs the abort action on the current upload. This will abort the upload and delete all of
   * the uploaded parts in the cleanup phase. Already finished uploads can not be aborted.
   */
  void abortUpload();

  /**
   * Gets the {@link PartUpload} for a given partNumber and current upload instance. PartUpload
   * object contains the information needed to upload one part of the upload, such as signed URL on
   * which you can make a PUT request to, with the file part as the request body.
   * <p>
   * A little unusual bit for the programing world, but the partNumber starts from 1 not 0.
   *
   * @param partNumber Part number to get PartUpload for, starts from 1.
   * @return created PartUpload resource
   */
  PartUpload getPartUpload(int partNumber);

  /**
   * Reports a successfully uploaded part for the current upload instance. After you successfully
   * uploaded a part on a link provided by the {@link #getPartUpload(int)} action, you should report
   * that upload to the Platform and provide the uploadResponse object from the response of the
   * storage provider.
   * <p>
   * Usually, upload response is just a map containing headers of the response (ETag of the part).
   *
   * @param partNumber     Number of the part is being reported
   * @param uploadResponse UploadResponse map
   * @see <a href="http://docs.sevenbridges.com/reference#report-an-uploaded-file-part">More about
   * uploading files</a>
   */
  void reportUploadedPart(int partNumber, Map<String, Object> uploadResponse);

  /**
   * Performs the complete upload action on the current upload instance. After you have uploaded
   * each part and reported success of the uploads for each part with {@link
   * #reportUploadedPart(int, Map)} action, you should complete whole upload by calling this action.
   * After invoking this action of the upload, {@link File} associated with this upload is fully
   * formed and available. Its resource object is returned as a result of this action.
   *
   * @return File created from this upload
   */
  File completeUpload();

}
