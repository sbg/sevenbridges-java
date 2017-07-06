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
 * This implementation represents the api key that is used to authenticate a User to SevenBridges
 * Platform.
 */
public class ClientApiKey implements ApiKey {

  private final String id;
  private final String secret;

  public ClientApiKey(String id, String secret) {
    Assert.hasText(id, "API key id cannot be null or empty.");
    Assert.hasText(secret, "API key secret cannot be null or empty.");
    this.id = id;
    this.secret = secret;
  }

  public String getId() {
    return id;
  }

  public String getSecret() {
    return secret;
  }

  @Override
  public String toString() {
    return getId(); //never ever print the secret
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ClientApiKey that = (ClientApiKey) o;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    return secret != null ? secret.equals(that.secret) : that.secret == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (secret != null ? secret.hashCode() : 0);
    return result;
  }
}
