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
package com.sevenbridges.apiclient.impl.http.authc;

import com.sevenbridges.apiclient.client.ApiKey;
import com.sevenbridges.apiclient.impl.http.Request;
import com.sevenbridges.apiclient.impl.http.support.RequestAuthenticationException;

public class AuthTokenRequestAuthenticator implements RequestAuthenticator {

  public static final String AUTH_TOKEN_HEADER = "X-SBG-Auth-Token";

  @Override
  public void authenticate(Request request, ApiKey apiKey) throws RequestAuthenticationException {
    request.getHeaders().set(AUTH_TOKEN_HEADER, apiKey.getSecret());
  }
}
