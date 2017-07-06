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
package com.sevenbridges.apiclient.impl.volume;

import com.sevenbridges.apiclient.ds.DataStore;
import com.sevenbridges.apiclient.volume.CreateGcsVolumeRequestBuilder;
import com.sevenbridges.apiclient.volume.CreateS3VolumeRequestBuilder;
import com.sevenbridges.apiclient.volume.VolumeRequestFactory;

public class DefaultVolumeRequestFactory implements VolumeRequestFactory {

  private final DataStore dataStore;

  public DefaultVolumeRequestFactory(DataStore dataStore) {
    this.dataStore = dataStore;
  }

  @Override
  public CreateS3VolumeRequestBuilder s3Builder() {
    return new DefaultCreateS3VolumeRequestBuilder(dataStore);
  }

  @Override
  public CreateGcsVolumeRequestBuilder gcsBuilder() {
    return new DefaultCreateGcsVolumeRequestBuilder(dataStore);
  }
}
