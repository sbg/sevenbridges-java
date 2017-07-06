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

/**
 * Interface to be implemented by {@link Resource Resources} that allows the resource instance to be
 * saved on the Seven Bridges Platform by sending the currently specified resource properties on a
 * proper endpoint for the current resource type with POST method.
 */
public interface Saveable {

  /**
   * Invoking this method will first gather all of the locally changed, dirty properties of a
   * resource, serialize them into a single request and invoke that request on a proper API endpoint
   * that differs between types and instances of resources via the http POST method. After a
   * successful invocation of a save method, resource will be clean of any dirty properties and will
   * be considered up to date with a Platform. Further invocations of the save method will have no
   * actions until some properties are changed on a current resource.
   * <p>
   * If the resource is changed by some external usage (not from this client instance), you can
   * reload this instance with a {@link Resource#reload()} call.
   */
  void save();
}
