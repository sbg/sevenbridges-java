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
 * Service property {@link VolumeService} of a {@link Volume} for the volumes of S3 type.
 */
public interface S3VolumeService extends VolumeService {

  /**
   * Gets the credentials of the current VolumeService instance.
   *
   * @return S3Credentials
   */
  S3Credentials getCredentials();

  /**
   * Gets the Sse algorithm of the current VolumeService instance.
   *
   * @return String sseAlgorithm
   */
  String getSseAlgorithm();

  /**
   * Sets the sse algorithm of the current VolumeService instance
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param sseAlgorithm String sseAlgorithm
   * @return Current S3VolumeService instance
   */
  S3VolumeService setSseAlgorithm(String sseAlgorithm);

  /**
   * Gets the aws canned acl of the current VolumeService instance.
   *
   * @return String aws canned acl
   */
  String getAwsCannedAcl();

  /**
   * Sets the aws canned acl of the current VolumeService instance locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param awsCannedAcl new awsCannedAcl of the current instance
   * @return Current S3VolumeService instance
   */
  S3VolumeService setAwsCannedAcl(String awsCannedAcl);

}
