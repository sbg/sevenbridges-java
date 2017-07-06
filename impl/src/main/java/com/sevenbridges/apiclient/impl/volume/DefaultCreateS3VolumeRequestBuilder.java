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
import com.sevenbridges.apiclient.volume.CreateS3VolumeRequestBuilder;
import com.sevenbridges.apiclient.volume.CreateVolumeRequest;
import com.sevenbridges.apiclient.volume.VolumeType;

import java.util.HashMap;
import java.util.Map;

final class DefaultCreateS3VolumeRequestBuilder extends AbstractCreateVolumeRequestBuilder implements CreateS3VolumeRequestBuilder {

  private String accessKey;
  private String secretKey;
  private String sseAlgorithm = "AES256";
  private String awsCanedAcl = null;
  private String sseAwsKmsKeyId = null;

  DefaultCreateS3VolumeRequestBuilder(DataStore dataStore) {
    super(dataStore);
    this.volumeType = VolumeType.S3;
    this.rootUrl = "https://s3.amazonaws.com";
    this.accessMode = AccessMode.RW;
  }

  @Override
  public DefaultCreateS3VolumeRequestBuilder setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public DefaultCreateS3VolumeRequestBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public DefaultCreateS3VolumeRequestBuilder setAccessMode(AccessMode accessMode) {
    this.accessMode = accessMode;
    return this;
  }

  @Override
  public DefaultCreateS3VolumeRequestBuilder setPrefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  @Override
  public DefaultCreateS3VolumeRequestBuilder setBucket(String bucket) {
    this.bucket = bucket;
    return this;
  }

  @Override
  public DefaultCreateS3VolumeRequestBuilder setRootUrl(String rootUrl) {
    this.rootUrl = rootUrl;
    return this;
  }

  public String getAccessKey() {
    return accessKey;
  }

  @Override
  public DefaultCreateS3VolumeRequestBuilder setAccessKey(String accessKey) {
    this.accessKey = accessKey;
    return this;
  }

  public String getSecretKey() {
    return secretKey;
  }

  @Override
  public DefaultCreateS3VolumeRequestBuilder setSecretKey(String secretKey) {
    this.secretKey = secretKey;
    return this;
  }

  public String getSseAlgorithm() {
    return sseAlgorithm;
  }

  @Override
  public DefaultCreateS3VolumeRequestBuilder setSseAlgorithm(String sseAlgorithm) {
    this.sseAlgorithm = sseAlgorithm;
    return this;
  }

  public String getAwsCanedAcl() {
    return awsCanedAcl;
  }

  @Override
  public DefaultCreateS3VolumeRequestBuilder setAwsCannedAcl(String awsCanedAcl) {
    this.awsCanedAcl = awsCanedAcl;
    return this;
  }

  @Override
  public CreateS3VolumeRequestBuilder setSseKmsKeyId(String kmsKeyId) {
    this.sseAwsKmsKeyId = kmsKeyId;
    return this;
  }

  @Override
  public CreateVolumeRequest build() {
    // Check if required fields are set
    Assert.hasText(this.name, "Field 'name' is required");
    Assert.hasText(this.bucket, "Field 'bucket' is required");
    Assert.hasText(this.accessKey, "Field 'accessKey' is required");
    Assert.hasText(this.secretKey, "Field 'secretKey' is required");

    Map<String, Object> volumeMap = new HashMap<>(4);
    Map<String, Object> serviceMap = new HashMap<>(5);
    Map<String, Object> credentialsMap = new HashMap<>(2);
    Map<String, String> propertiesMap = new HashMap<>(2);

    volumeMap.put(DefaultVolume.NAME.getName(), name);
    volumeMap.put(DefaultVolume.ACCESS_MODE.getName(), accessMode);
    if (description != null) {
      volumeMap.put(DefaultVolume.DESCRIPTION.getName(), description);
    }

    credentialsMap.put(DefaultVolume.DefaultS3Credentials.ACCESS_KEY, accessKey);
    credentialsMap.put(DefaultVolume.DefaultS3Credentials.SECRET_KEY, secretKey);

    serviceMap.put(DefaultVolume.DefaultS3VolumeService.TYPE, this.volumeType.name());
    if (rootUrl != null) {
      serviceMap.put(DefaultVolume.DefaultS3VolumeService.ROOT_URL, this.rootUrl);
    }
    serviceMap.put(DefaultVolume.DefaultS3VolumeService.PREFIX, this.prefix);
    serviceMap.put(DefaultVolume.DefaultS3VolumeService.TYPE, this.volumeType.name());
    serviceMap.put(DefaultVolume.DefaultS3Credentials.CREDENTIALS, credentialsMap);
    serviceMap.put(DefaultVolume.DefaultS3VolumeService.BUCKET, bucket);

    propertiesMap.put(DefaultVolume.DefaultS3VolumeService.SSE_ALGORITHM, sseAlgorithm);
    if (sseAwsKmsKeyId != null) {
      propertiesMap.put("sse_aws_kms_key_id", sseAwsKmsKeyId);
    }
    if (awsCanedAcl != null) {
      propertiesMap.put(DefaultVolume.DefaultS3VolumeService.AWS_CANNED_ACL, awsCanedAcl);
    }
    serviceMap.put("properties", propertiesMap);

    volumeMap.put(DefaultVolume.SERVICE.getName(), serviceMap);

    DefaultVolume volume = ((DefaultDataStore) this.dataStore).instantiate(DefaultVolume.class, volumeMap);

    return new DefaultCreateVolumeRequest(volume);
  }
}
