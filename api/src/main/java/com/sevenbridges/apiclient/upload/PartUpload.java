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

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Resource containing information about a part upload.
 */
public interface PartUpload extends Resource {

  /**
   * HTTP method to perform on a given URL to upload a file part (usually {@link
   * com.sevenbridges.apiclient.http.HttpMethod#PUT}).
   *
   * @return String http method
   */
  String getMethod();

  /**
   * String URL to which to make the HTTP part upload request.
   *
   * @return String url
   */
  String getUrl();

  /**
   * Date and time representation  by when the HTTP part upload request should be made. After that
   * moment the HTTP link expires, and you should get a new one for this part number.
   *
   * @return Date and time of expiration of this upload link
   */
  Date getExpires();

  /**
   * A map of headers and values that should be set when making the HTTP part upload request.
   *
   * @return Headers map
   */
  Map<String, String> getHeaders();

  /**
   * Success codes expected after making the HTTP part upload request.
   *
   * @return List of success codes (usually singular list with 200 code)
   */
  List<Integer> getReportSuccessCodes();

  /**
   * List of headers gathered from the storage provider response that you should include while
   * reporting the successful part upload (usually ETag header is needed).
   *
   * @return List of needed header keys
   */
  List<String> getReportHeaders();

}
