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
import com.sevenbridges.apiclient.impl.ds.DefaultDataStore;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.volume.AccessMode;
import com.sevenbridges.apiclient.volume.CreateGcsVolumeRequestBuilder;
import com.sevenbridges.apiclient.volume.CreateVolumeRequest;
import com.sevenbridges.apiclient.volume.VolumeType;

import java.util.HashMap;
import java.util.Map;

final class DefaultCreateGcsVolumeRequestBuilder extends AbstractCreateVolumeRequestBuilder implements CreateGcsVolumeRequestBuilder {

  private String clientEmail;
  private String privateKey;

  DefaultCreateGcsVolumeRequestBuilder(DataStore dataStore) {
    super(dataStore);
    this.volumeType = VolumeType.GCS;
    this.rootUrl = "https://www.googleapis.com/";
  }

  @Override
  public DefaultCreateGcsVolumeRequestBuilder setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public DefaultCreateGcsVolumeRequestBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public DefaultCreateGcsVolumeRequestBuilder setAccessMode(AccessMode accessMode) {
    this.accessMode = accessMode;
    return this;
  }

  @Override
  public DefaultCreateGcsVolumeRequestBuilder setPrefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  @Override
  public DefaultCreateGcsVolumeRequestBuilder setBucket(String bucket) {
    this.bucket = bucket;
    return this;
  }

  @Override
  public DefaultCreateGcsVolumeRequestBuilder setRootUrl(String rootUrl) {
    this.rootUrl = rootUrl;
    return this;
  }

  @Override
  public DefaultCreateGcsVolumeRequestBuilder setClientEmail(String clientEmail) {
    this.clientEmail = clientEmail;
    return this;
  }

  @Override
  public DefaultCreateGcsVolumeRequestBuilder setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
    return this;
  }

  @Override
  public CreateVolumeRequest build() {
    // Check if required fields are set
    Assert.hasText(this.name, "Field 'name' is required");
    Assert.hasText(this.bucket, "Field 'bucket' is required");
    Assert.hasText(this.clientEmail, "Field 'clientEmail' is required");
    Assert.hasText(this.privateKey, "Field 'privateKey' is required");

    Map<String, Object> volumeMap = new HashMap<>(4);
    Map<String, Object> serviceMap = new HashMap<>(5);
    Map<String, Object> credentialsMap = new HashMap<>(2);

    volumeMap.put(DefaultVolume.NAME.getName(), name);
    volumeMap.put(DefaultVolume.ACCESS_MODE.getName(), accessMode);
    if (description != null) {
      volumeMap.put(DefaultVolume.DESCRIPTION.getName(), description);
    }

    credentialsMap.put(DefaultVolume.DefaultGcsCredentials.CLIENT_MAIL, this.clientEmail);
    credentialsMap.put(DefaultVolume.DefaultGcsCredentials.PRIVATE_KEY, this.privateKey);

    serviceMap.put(DefaultVolume.DefaultGcsVolumeService.TYPE, this.volumeType.name());
    if (rootUrl != null) {
      serviceMap.put(DefaultVolume.DefaultGcsVolumeService.ROOT_URL, this.rootUrl);
    }
    serviceMap.put(DefaultVolume.DefaultGcsVolumeService.PREFIX, this.prefix);
    serviceMap.put(DefaultVolume.DefaultGcsVolumeService.TYPE, this.volumeType.name());
    serviceMap.put(DefaultVolume.DefaultGcsCredentials.CREDENTIALS, credentialsMap);
    serviceMap.put(DefaultVolume.DefaultGcsVolumeService.BUCKET, bucket);

    volumeMap.put(DefaultVolume.SERVICE.getName(), serviceMap);

    DefaultVolume volume = ((DefaultDataStore) this.dataStore).instantiate(DefaultVolume.class, volumeMap);

    return new DefaultCreateVolumeRequest(volume);
  }

}
