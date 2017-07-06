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
package com.sevenbridges.apiclient.impl.transfer.model;

import java.util.HashMap;
import java.util.Map;

public class UploadPartContext {

  private final int partNumber;
  private final String uploadUrl;

  //CHECKSTYLE:OFF
  private final String eTag;
  //CHECKSTYLE:ON

  private final Exception ex;
  private final int retryCnt;

  private UploadPartContext(int partNumber) {
    this.partNumber = partNumber;
    this.uploadUrl = null;
    this.eTag = null;
    this.ex = null;
    this.retryCnt = -1;
  }

  private UploadPartContext(int partNumber, String eTag, String uploadUrl) {
    this.partNumber = partNumber;
    this.uploadUrl = uploadUrl;
    this.eTag = eTag;
    this.ex = null;
    this.retryCnt = -1;
  }

  private UploadPartContext(int partNumber, String uploadUrl, Exception ex, int retryCnt) {
    this.partNumber = partNumber;
    this.uploadUrl = uploadUrl;
    this.ex = ex;
    this.retryCnt = retryCnt;
    this.eTag = null;
  }

  public static UploadPartContext buildNewContext(int partNumber) {
    return new UploadPartContext(partNumber);
  }

  public static UploadPartContext buildSucceedContext(int partNumber, String eTag, String uploadUrl) {
    return new UploadPartContext(partNumber, eTag, uploadUrl);
  }

  public static UploadPartContext buildFailedContext(int partNumber, String uploadUrl, Exception ex, int retryCnt) {
    return new UploadPartContext(partNumber, uploadUrl, ex, retryCnt);
  }

  public boolean isNew() {
    return uploadUrl == null;
  }

  public boolean isSucceeded() {
    return (ex == null) && (eTag != null);
  }

  public boolean isFailed() {
    return ex != null;
  }

  public boolean isFailedBeyondRetry() {
    return ex != null && retryCnt <= 1;
  }

  public int getPartNumber() {
    return partNumber;
  }

  public String getUploadUrl() {
    return uploadUrl;
  }

  public String geteTag() {
    return eTag;
  }

  public Exception getEx() {
    return ex;
  }

  public int getRetryCnt() {
    return retryCnt;
  }

  public Map<String, Object> getUploadResponse() {
    Map<String, String> etagMap = new HashMap<>(1);
    etagMap.put("ETag", eTag);
    Map<String, Object> uploadResponse = new HashMap<>();
    uploadResponse.put("headers", etagMap);
    return uploadResponse;
  }
}
