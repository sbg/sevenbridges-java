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
package com.sevenbridges.apiclient.resource;

import com.sevenbridges.apiclient.error.Error;
import com.sevenbridges.apiclient.lang.Assert;

/**
 * Exception created from error reported by the Seven Bridges API server.
 */
public class ResourceException extends RuntimeException implements Error {

  private final Error error;

  /**
   * Ensures the message used for the exception (i.e. exception.getMessage()) reports the {@code
   * developerMessage} returned by the Seven Bridges API Server.  The regular Seven Bridges response
   * body {@code message} field is targeted at application end-users that could very likely be
   * non-technical.  Since an exception should be helpful to developers, it is better to show a more
   * technical message.
   *
   * @param error the response Error. Cannot be null.
   * @return {@code error.getDeveloperMessage()}
   */
  private static String buildExceptionMessage(Error error) {
    Assert.notNull(error, "Error argument cannot be null.");
    StringBuilder sb = new StringBuilder();
    sb.append("HTTP ").append(error.getStatus())
        .append(", SevenBridges ").append(error.getCode())
        .append(" - ").append(error.getMessage())
        .append(" (").append(error.getMoreInfo() == null ? "no additional info" : error.getMoreInfo()).append(")");

    return sb.toString();
  }

  public ResourceException(Error error) {
    super(buildExceptionMessage(error));
    this.error = error;
  }

  @Override
  public int getStatus() {
    return error.getStatus();
  }

  /**
   * Get the Seven Bridges Error Code Check <a href="http://docs.sevenbridges.com/reference#api-status-codes">API
   * status codes</a> for the list of Seven Bridges Error Codes.
   *
   * @return the code of the error
   */
  @Override
  public int getCode() {
    return error.getCode();
  }

  /**
   * More information about the error is described in the Seven Bridges Error Codes documentation
   * Check <a href="http://docs.sevenbridges.com/reference#api-status-codes">API status codes</a>
   * for the list of Seven Bridges Error Codes.
   *
   * @return the URI to the error documentation
   */
  @Override
  public String getMoreInfo() {
    return error.getMoreInfo();
  }

  public Error getSevenBridgesError() {
    return this.error;
  }

}
