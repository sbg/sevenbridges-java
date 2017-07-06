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
package com.sevenbridges.apiclient.impl.app;

import com.sevenbridges.apiclient.app.App;
import com.sevenbridges.apiclient.app.AppList;
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractCollectionResource;
import com.sevenbridges.apiclient.impl.resource.ArrayProperty;
import com.sevenbridges.apiclient.impl.resource.Property;

import java.util.Map;

public class DefaultAppList extends AbstractCollectionResource<App> implements AppList {

  private static final ArrayProperty<App> ITEMS = new ArrayProperty<>("items", App.class);

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(OFFSET, LIMIT, ITEMS);

  public DefaultAppList(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultAppList(InternalDataStore dataStore, Map<String, Object> properties) {
    super(dataStore, properties);
  }

  public DefaultAppList(InternalDataStore dataStore, Map<String, Object> properties, Map<String, Object> queryParams) {
    super(dataStore, properties, queryParams);
  }

  @Override
  protected Class<App> getItemType() {
    return App.class;
  }

  @Override
  public Map<String, Property> getPropertyDescriptors() {
    return PROPERTY_DESCRIPTORS;
  }

}
