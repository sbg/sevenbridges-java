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
package com.sevenbridges.apiclient.ds;

import com.sevenbridges.apiclient.client.ApiKey;
import com.sevenbridges.apiclient.query.Options;
import com.sevenbridges.apiclient.resource.Resource;

/**
 * A {@code DataStore} is the liaison between client Seven Bridges components and the raw
 * Seven Bridges REST API.  It is responsible for converting Seven Bridges Java objects (User,
 * Project, File instances, etc) into REST HTTP requests, executing those requests, and converting
 * REST HTTP responses back into Seven Bridges Java objects.
 */
public interface DataStore {

  /**
   * Returns the ApiKey used to authenticate HTTPS requests sent to the Seven Bridges API server.
   *
   * @return the ApiKey used to authenticate HTTPS requests sent to the Seven Bridges API server.
   */
  ApiKey getApiKey();

  /**
   * Instantiates and returns a new instance of the specified Resource type.  The instance is merely
   * instantiated and is not saved/synchronized with the server in any way.
   * <p>
   * This method effectively replaces the {@code new} keyword that would have been used otherwise if
   * the concrete implementation was known (Resource implementation classes are intentionally not
   * exposed to Seven Bridges Java end-users).
   *
   * @param clazz the Resource class to instantiate.
   * @param <T>   the Resource sub-type
   * @return a new instance of the specified Resource.
   */
  <T extends Resource> T instantiate(Class<T> clazz);

  /**
   * Looks up (retrieves) the resource at the specified {@code href} URL and returns the resource as
   * an instance of the specified {@code class}.
   * <p>
   * The {@code Class} argument must represent an interface that is a sub-interface of {@link
   * Resource}, for example {@link com.sevenbridges.apiclient.billing.BillingGroup BillingGroup},
   * {@link com.sevenbridges.apiclient.project.Project Project}, etc.
   *
   * @param href  the resource URL of the resource to retrieve
   * @param clazz the {@link Resource} sub-interface to instantiate
   * @param <T>   type parameter indicating the returned value is a {@link Resource} instance.
   * @return an instance of the specified class based on the data returned from the specified {@code
   * href} URL.
   */
  <T extends Resource> T getResource(String href, Class<T> clazz);

  /**
   * Retrieves the resource at the specified {@code href} according to the specified {@code Options}
   * and returns the resource as an instance of the specified {@code clazz}.
   *
   * @param href    the URL of the resource to retrieve
   * @param clazz   the {@link Resource} sub-interface to instantiate
   * @param <T>     type parameter indicating the returned value is a {@link Resource} instance.
   * @param options the {@link Options} sub-interface with the properties to expand
   * @param <O>     type parameter of the options, subtype of {@link Options} class
   * @return an instance of the specified class based on the data returned from the specified {@code
   * href} URL.
   */
  <T extends Resource, O extends Options> T getResource(String href, Class<T> clazz, O options);

}
