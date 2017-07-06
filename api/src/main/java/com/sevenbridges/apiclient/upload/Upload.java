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

import com.sevenbridges.apiclient.resource.Resource;
import com.sevenbridges.apiclient.resource.Saveable;

import java.util.Date;
import java.util.List;

/**
 * Resource representing an Upload instance.
 */
public interface Upload extends Resource, Saveable, UploadActions {

  /**
   * ID field, unique identifier of upload on the Platform.
   *
   * @return String 'id' property of the current instance
   */
  String getUploadId();

  /**
   * ProjectId field, unique identifier of the project in with the file is being uploaded.
   *
   * @return String 'projectId' property of the current instance
   */
  String getProjectId();

  /**
   * Sets the property 'projectId' of the upload locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param projectId String projectId of the upload
   * @return Current upload instance
   */
  Upload setProjectId(String projectId);

  /**
   * Name field, name of the file on Platform that will be created with this upload.
   *
   * @return String 'name' property of the current instance
   */
  String getName();

  /**
   * Sets the property 'name' of the upload locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param name String name of the upload
   * @return Current upload instance
   */
  Upload setName(String name);

  /**
   * MD5 filed, hash digest of file content if provided.
   *
   * @return String 'md5' property of the current instance
   */
  String getMD5();

  /**
   * Sets the property 'md5' of the upload locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param md5 String md5 of the upload
   * @return Current upload instance
   */
  Upload setMD5(String md5);

  /**
   * Gets the local upload size. Upload sizes are stored in the resource but not returned. If there
   * is no local value -1L will be returned.
   *
   * @return long Size of the upload if able
   */
  long getSize();

  /**
   * Sets the property 'size' of the upload locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param size long size in bytes of the upload
   * @return Current upload instance
   */
  Upload setSize(long size);

  /**
   * PartSize filed, long size in bytes of the upload.
   *
   * @return long 'partSize' property of the current instance
   */
  long getPartSize();

  /**
   * Sets the property 'size' of the upload locally with provided long value in bytes.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param partSize long bytes partSize
   * @return Current upload instance
   */
  Upload setPartSize(long partSize);

  /**
   * InitiatedOn field, date of the initiation of the upload.
   *
   * @return Date 'initiatedOn' property of the current instance
   */
  Date getInitiatedOn();

  /**
   * ParallelUploads field, boolean flag indicating is parallel upload.
   *
   * @return Boolean 'parallelUploads' property of the current instance
   */
  boolean isParallelUploads();

  /**
   * UploadedPartsCount field, long number of parts that are completed.
   *
   * @return long 'UploadedPartsCount' property of the current instance
   */
  long getUploadedPartsCount();

  /**
   * UploadedParts field, list of {@link UploadedPart}s that are completed.
   *
   * @return list 'UploadedParts' property of the current instance
   */
  List<UploadedPart> getUploadedParts();

}
