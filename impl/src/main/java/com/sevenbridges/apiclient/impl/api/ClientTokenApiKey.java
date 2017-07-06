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
package com.sevenbridges.apiclient.impl.api;

import com.sevenbridges.apiclient.client.ApiKey;
import com.sevenbridges.apiclient.lang.Assert;

/**
 * This implementation represents the authorization token that is used to authenticate a User with
 * SevenBridges Platform.
 */
public class ClientTokenApiKey implements ApiKey {

  private final String authToken;

  private static final String METHOD_ERROR_MESSAGE = "This method is not accessible from a auth token api key. Only getSecret() is available from this instance.";

  public ClientTokenApiKey(String authToken) {
    Assert.hasText(authToken, "API key auth token cannot be null or empty.");
    this.authToken = authToken;
  }

  @Override
  public String getId() {
    throw new IllegalAccessError(METHOD_ERROR_MESSAGE);
  }

  @Override
  public String getSecret() {
    return authToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ClientTokenApiKey that = (ClientTokenApiKey) o;

    return authToken != null ? authToken.equals(that.authToken) : that.authToken == null;
  }

  @Override
  public int hashCode() {
    return authToken != null ? authToken.hashCode() : 0;
  }
}
