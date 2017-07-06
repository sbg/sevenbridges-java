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
package com.sevenbridges.apiclient.client;

import com.sevenbridges.apiclient.lang.Classes;

/**
 * Static utility/helper methods for working with {@link ApiKey} resources.
 * <p>
 * Most methods are <a href="http://en.wikipedia.org/wiki/Factory_method_pattern">factory
 * method</a>s used for forming ApiKey-specific <a href="http://en.wikipedia.org/wiki/Fluent_interface">fluent
 * DSL</a> queries. For example:
 * <pre>
 * <b>ApiKeys.builder()</b>
 *     .setFileLocation(path)
 *     .build();
 * </pre>
 */
public final class ApiKeys {

  //prevent instantiation
  private ApiKeys() {
  }

  /**
   * Returns a new {@link ApiKeyBuilder} instance, used to construct {@link ApiKey} instances to
   * authenticate the calls to Seven Bridges.
   *
   * @return a new {@link ApiKeyBuilder} instance, used to construct {@link ApiKey} instances to
   * authenticate the calls to Seven Bridges.
   */
  public static ApiKeyBuilder builder() {
    return (ApiKeyBuilder) Classes.newInstance("com.sevenbridges.apiclient.impl.api.ClientApiKeyBuilder");
  }
}
