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
 * Builder object, used to build a {@link CreateVolumeRequest} S3 storage volumes, builder can be
 * instantiated from a task factory {@link com.sevenbridges.apiclient.user.UserActions#getVolumeRequestFactory()}
 */
public interface CreateS3VolumeRequestBuilder extends CreateVolumeRequestBuilder {

  /**
   * Sets the name of the future volume. Name of the volume is not unique.
   *
   * @param name String name of the future volume
   * @return builder instance with name set
   */
  CreateS3VolumeRequestBuilder setName(String name);

  /**
   * Sets the optional description of the future volume.
   *
   * @param description String description of the future volume
   * @return builder instance with description set
   */
  CreateS3VolumeRequestBuilder setDescription(String description);

  /**
   * Sets the {@link AccessMode} of the future volume. For S3 both read and read/write modes are
   * supported.
   *
   * @param accessMode enum access mode of the future volume
   * @return builder instance with access mode set
   */
  CreateS3VolumeRequestBuilder setAccessMode(AccessMode accessMode);

  /**
   * Sets the optional string prefix for the volume. This is service-specific prefix to prepend to
   * all objects created in this volume. If the service supports folders, and this prefix includes
   * them, the API will attempt to create any missing folders when it outputs a file.
   *
   * @param prefix String prefix of the future volume
   * @return builder instance with prefix set
   */
  CreateS3VolumeRequestBuilder setPrefix(String prefix);

  /**
   * Sets the required bucket name of the future volume. Bucket is unique identifier of a storage.
   *
   * @param bucket String bucket of the future volume
   * @return builder instance with prefix set
   */
  CreateS3VolumeRequestBuilder setBucket(String bucket);

  /**
   * Sets the optional root url of the future volume. Root url is a cloud provider API endpoint to
   * use when accessing this bucket. Default of the S3 storage is 'https://s3.amazonaws.com', and
   * for GCS 'https://www.googleapis.com'.
   *
   * @param rootUrl String root url of the future volume
   * @return builder instance with root url set
   */
  CreateS3VolumeRequestBuilder setRootUrl(String rootUrl);

  /**
   * Sets the required access key of the future volume. Access key is the AWS identifier of the
   * IAM user shared with Seven Bridges to access this bucket.
   *
   * @param accessKey String access key of the future volume
   * @return builder instance with access key set
   */
  CreateS3VolumeRequestBuilder setAccessKey(String accessKey);

  /**
   * Sets the required secret key of the future volume. Secret key is the AWS generated key of the
   * IAM user shared with Seven Bridges to access this bucket.
   *
   * @param secretKey String secret key of the future volume
   * @return builder instance with secret key set
   */
  CreateS3VolumeRequestBuilder setSecretKey(String secretKey);

  /**
   * Sets the optional sse algorithm of the future volume. Use default AES256 server-side encryption
   * or AWS KMS encryption when writing to this bucket.
   * <p>
   * This value can be either `AES256` (default) or `aws:kms`.
   *
   * @param sseAlgorithm String sse algorithm of the future volume
   * @return builder instance with sse algorithm set
   */
  CreateS3VolumeRequestBuilder setSseAlgorithm(String sseAlgorithm);

  /**
   * Sets the optional KMS key ID of the future volume. If AWS KMS encryption is used, this should
   * be set to the required KMS key. If not set and `aws:kms` is set as sse_algorithm, default KMS
   * key is used.
   *
   * @param kmsKeyId String kms key ID of the future volume
   * @return builder instance with kms key ID set
   */
  CreateS3VolumeRequestBuilder setSseKmsKeyId(String kmsKeyId);

  /**
   * Sets the optional AWS canned ACL to be used during exports to the future S3 volume. Supported
   * values are all of the supported S3 canned ACL values, or null if you don't want to apply any
   * ACLs during exports. Default is {@code null}.
   *
   * @param awsCanedAcl String canned acl of the future volume
   * @return builder instance with canned acl set
   * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html#canned-acl">AWS
   * S3 canned acl</a>
   */
  CreateS3VolumeRequestBuilder setAwsCannedAcl(String awsCanedAcl);


}
