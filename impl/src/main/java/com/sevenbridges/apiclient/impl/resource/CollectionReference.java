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
package com.sevenbridges.apiclient.impl.resource;

import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.resource.CollectionResource;
import com.sevenbridges.apiclient.resource.Resource;

public class CollectionReference<C extends CollectionResource<T>, T extends Resource> extends ResourceReference<C> {

  private final Class<T> instanceType;

  public CollectionReference(String name, Class<C> type, Class<T> instanceType) {
    super(name, type);
    Assert.notNull(instanceType, "instanceType argument cannot be null.");
    this.instanceType = instanceType;
  }

  public Class<T> getInstanceType() {
    return instanceType;
  }
}
