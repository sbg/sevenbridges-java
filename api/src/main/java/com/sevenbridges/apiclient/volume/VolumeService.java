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
package com.sevenbridges.apiclient.volume;

import com.sevenbridges.apiclient.resource.Saveable;

/**
 * Information about service represented by this {@link Volume}.
 */
public interface VolumeService {

  /**
   * Type of volume service (currently GCS or S3).
   *
   * @return String type of volume service
   */
  String getType();

  /**
   * Sets the type of volume service locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param type type of the volume
   * @return Current volume service instance
   */
  VolumeService setType(VolumeType type);

  /**
   * String optional string prefix for the volume. This is service-specific prefix to prepend to all
   * objects created in this volume. If the service supports folders, and this prefix includes them,
   * the API will attempt to create any missing folders when it outputs a file.
   *
   * @return String prefix
   */
  String getPrefix();

  /**
   * Sets the optional string volume prefix.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param prefix String prefix
   * @return Current volume service instance
   */
  VolumeService setPrefix(String prefix);

  /**
   * Bucket name for this volume service.
   *
   * @return String bucket name
   */
  String getBucket();

  /**
   * Sets the bucket name of the volume service locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param bucket String bucket to be set
   * @return Current volume service instance
   */
  VolumeService setBucket(String bucket);

  /**
   * String root url of a volume service.
   *
   * @return String root url
   */
  String getRootUrl();

  /**
   * Sets the root url of the volume service locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param rootUrl String root url
   * @return Current volume service instance
   */
  VolumeService setRootUrl(String rootUrl);

}
