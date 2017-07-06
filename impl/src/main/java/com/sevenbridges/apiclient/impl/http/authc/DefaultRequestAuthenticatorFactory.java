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

import com.sevenbridges.apiclient.client.AuthenticationScheme;
import com.sevenbridges.apiclient.impl.http.support.RequestAuthenticationException;
import com.sevenbridges.apiclient.lang.Classes;

/**
 * This default factory is responsible of creating a {@link RequestAuthenticator} out of a given
 * {@link AuthenticationScheme}.
 * <p>
 * This implementation returns a {@link AuthTokenRequestAuthenticator} when the authentication
 * scheme is undefined.
 */
public class DefaultRequestAuthenticatorFactory implements RequestAuthenticatorFactory {

  /**
   * Creates a {@link RequestAuthenticator} out of the given {@link AuthenticationScheme}.
   *
   * @param scheme the authentication scheme enum defining the request authenticator to be created
   * @return the corresponding `RequestAuthenticator` for the given `AuthenticationScheme`. Returns
   * `AuthTokenRequestAuthenticator` if the authentication scheme is undefined.
   */
  @Override
  public RequestAuthenticator create(AuthenticationScheme scheme) {

    if (scheme == null) {
      //By default, this factory creates a digest authentication when a scheme is not defined
      return new AuthTokenRequestAuthenticator();
    }

    RequestAuthenticator requestAuthenticator;

    try {
      requestAuthenticator = Classes.newInstance(scheme.getRequestAuthenticatorClassName());
    } catch (RuntimeException ex) {
      throw new RequestAuthenticationException("There was an error instantiating " + scheme.getRequestAuthenticatorClassName());
    }

    return requestAuthenticator;
  }
}
