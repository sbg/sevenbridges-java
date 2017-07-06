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
package com.sevenbridges.apiclient.error;

/**
 * The {@code Error} is a interface representing Seven Bridges errors.
 */
public interface Error {

  /**
   * The corresponding HTTP status code.
   *
   * @return the corresponding HTTP status code
   */
  int getStatus();

  /**
   * Internal Seven Bridges error code, that can be used to obtain more information.
   *
   * @return internal Seven Bridges error code, that can be used to obtain more information
   * @see <a href="http://docs.sevenbridges.com/reference#api-status-codes">Seven Bridges Docs --
   * API status codes</a>
   */
  int getCode();

  /**
   * A simple, easy to understand message about the error.
   *
   * @return a simple, easy to understand message about the error
   */
  String getMessage();

  /**
   * A fully qualified URL that may be accessed to obtain more information about the error.
   *
   * @return a fully qualified URL that may be accessed to obtain more information about the error
   */
  String getMoreInfo();
}
