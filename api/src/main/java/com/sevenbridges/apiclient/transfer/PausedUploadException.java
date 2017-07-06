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
package com.sevenbridges.apiclient.transfer;

/**
 * Exception indicating that the upload managed by the internal transfer manager service is paused
 * locally.
 */
public class PausedUploadException extends RuntimeException {

  public PausedUploadException() {
    super();
  }

  public PausedUploadException(String message) {
    super(message);
  }

  public PausedUploadException(String message, Throwable cause) {
    super(message, cause);
  }

  public PausedUploadException(Throwable cause) {
    super(cause);
  }

  // Stack trace not needed
  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }

}
