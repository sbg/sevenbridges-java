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
 * Factory for volume builders {@link CreateS3VolumeRequestBuilder} and {@link
 * CreateGcsVolumeRequestBuilder}.
 * <p>
 * Can be instantiated by the {@link com.sevenbridges.apiclient.user.UserActions#getVolumeRequestFactory()}
 * call.
 */
public interface VolumeRequestFactory {

  /**
   * Creates and returns new instance of the volume builder for the S3 type of volume.
   *
   * @return {@link CreateS3VolumeRequestBuilder} builder
   */
  CreateS3VolumeRequestBuilder s3Builder();

  /**
   * Creates and returns new instance of the volume builder for the GCS type of volume.
   *
   * @return {@link CreateGcsVolumeRequestBuilder} builder
   */
  CreateGcsVolumeRequestBuilder gcsBuilder();

}
