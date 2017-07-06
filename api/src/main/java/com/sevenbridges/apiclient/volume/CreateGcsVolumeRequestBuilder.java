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

/**
 * Builder object, used to build a {@link CreateVolumeRequest} for Google cloud storage volumes,
 * builder can be instantiated from a task factory {@link com.sevenbridges.apiclient.user.UserActions#getVolumeRequestFactory()}
 */
public interface CreateGcsVolumeRequestBuilder extends CreateVolumeRequestBuilder {

  /**
   * Sets the name of the future volume. Name of the volume is not unique.
   *
   * @param name String name of the future volume
   * @return builder instance with name set
   */
  CreateGcsVolumeRequestBuilder setName(String name);

  /**
   * Sets the optional description of the future volume.
   *
   * @param description String description of the future volume
   * @return builder instance with description set
   */
  CreateGcsVolumeRequestBuilder setDescription(String description);

  /**
   * Sets the {@link AccessMode} of the future volume. For GCS only read mode is currently
   * supported.
   *
   * @param accessMode enum access mode of the future volume
   * @return builder instance with access mode set
   */
  CreateGcsVolumeRequestBuilder setAccessMode(AccessMode accessMode);

  /**
   * Sets the optional string prefix for the volume. This is service-specific prefix to prepend to
   * all objects created in this volume. If the service supports folders, and this prefix includes
   * them, the API will attempt to create any missing folders when it outputs a file.
   *
   * @param prefix String prefix of the future volume
   * @return builder instance with prefix set
   */
  CreateGcsVolumeRequestBuilder setPrefix(String prefix);

  /**
   * Sets the required bucket name of the future volume. Bucket is unique identifier of a storage.
   *
   * @param bucket String bucket of the future volume
   * @return builder instance with prefix set
   */
  CreateGcsVolumeRequestBuilder setBucket(String bucket);

  /**
   * Sets the optional root url of the future volume. Root url is a cloud provider API endpoint to
   * use when accessing this bucket. Default of the S3 storage is 'https://s3.amazonaws.com', and
   * for GCS 'https://www.googleapis.com'
   *
   * @param rootUrl String root url of the future volume
   * @return builder instance with root url set
   */
  CreateGcsVolumeRequestBuilder setRootUrl(String rootUrl);

  /**
   * Sets the required client email of the future volume. Client email is a Google Cloud Storage
   * user identifier used for accessing GCS storage.
   *
   * @param clientEmail String client email
   * @return builder instance with client email set
   */
  CreateGcsVolumeRequestBuilder setClientEmail(String clientEmail);

  /**
   * Sets the required private key of the future volume. This is Google Cloud Storage private key
   * paired with GCS client email.
   *
   * @param privateKey String private key of the future volume
   * @return builder instance with private key set
   */
  CreateGcsVolumeRequestBuilder setPrivateKey(String privateKey);

}
