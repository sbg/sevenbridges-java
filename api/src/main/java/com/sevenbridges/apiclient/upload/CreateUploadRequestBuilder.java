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

import com.sevenbridges.apiclient.project.Project;

/**
 * A Builder to construct {@link CreateUploadRequest}s, used to initialize manual uploads
 * (initUpload function), or the client managed uploads (submitUpload function).
 *
 * @see com.sevenbridges.apiclient.user.User#submitUpload(CreateUploadRequest)
 * @see com.sevenbridges.apiclient.user.User#initUpload(CreateUploadRequest)
 */
public interface CreateUploadRequestBuilder {

  /**
   * Sets the name of the file that is being uploaded. If there already exists a file with that name
   * in the Seven Bridges project that is destination of the upload, uploaded file will either
   * overwrite that file, or be renamed with underscore and a number prefix, based on the
   * 'overwrite' property of the upload set by the builder.
   *
   * @param name Platform name of the file to be uploaded
   * @return builder instance with name set
   */
  CreateUploadRequestBuilder setName(String name);

  /**
   * Sets the String project ID to upload file to. Builder will NOT do any validation on Platform to
   * see if the specified project exists and if the current user has write permission in it.
   *
   * @param projectId String unique identifier of the {@link Project} to upload the file to
   * @return builder instance with project set
   */
  CreateUploadRequestBuilder setProject(String projectId);

  /**
   * Sets the {@link Project} to upload file to. Builder will NOT do any validation on Platform to
   * see if the specified project exists and if the current user has write permission in it.
   *
   * @param project Project resource to upload the file to
   * @return builder instance with project set
   */
  CreateUploadRequestBuilder setProject(Project project);

  /**
   * Sets the overwrite flag of the upload. Overwrite specifies behavior of the upload in the case
   * of upload being named the same as the file that is already in the project. If overwrite is true
   * upload will overwrite old file, and if it is set to false, uploaded file will be renamed with
   * underscore character and a first free number.
   * <p>
   * If omitted default value for overwrite param is 'false'.
   *
   * @param overwrite boolean value of the overwrite control flag
   * @return builder instance with overwrite set
   */
  CreateUploadRequestBuilder setOverwrite(boolean overwrite);

  /**
   * Sets the local file to be uploaded to the Platform. Provided {@link java.io.File} must be a
   * file (not a directory/imaginary), and accessible in read mode. File must be available for the
   * whole duration of the upload.
   *
   * @param file local file to be uploaded
   * @return builder instance with file set
   */
  CreateUploadRequestBuilder setFile(java.io.File file);

  /**
   * Sets the local file to be uploaded to the Platform given with a String full file path. {@link
   * java.io.File} at the provided path must be a file (not a directory/imaginary), and accessible
   * in read mode. File must be available for the whole duration of the upload.
   *
   * @param filePath String path to the file to be uploaded
   * @return builder instance with file set
   */
  CreateUploadRequestBuilder setFile(String filePath);

  /**
   * Sets the SUGGESTED part size of the upload in BYTES. If this provided part size is smaller than
   * the part size threshold (currently 32MB) part size will be set to default (256MB or larger
   * depending on the whole file size). After upload creation, you should recheck the {@link
   * Upload#getPartSize()} field, to se the real part size negotiated by the Platform.
   * <p>
   * This is not a required param for upload.
   *
   * @param partSize Suggested part size in bytes
   * @return builder instance with part size set
   */
  CreateUploadRequestBuilder setPartSize(long partSize);

  /**
   * Sets the MD5 hash digest of the file to be uploaded. This value is not validated, only stored,
   * and is not a required param for upload.
   *
   * @param digest String md5 digest of the file to upload
   * @return builder instance with md5 digest set
   */
  CreateUploadRequestBuilder setMD5(String digest);

  /**
   * Creates a new {@code CreateUploadRequest} instance based on the current builder state.
   *
   * @return a new {@code CreateUploadRequest} instance based on the current builder state.
   */
  CreateUploadRequest build();
}
