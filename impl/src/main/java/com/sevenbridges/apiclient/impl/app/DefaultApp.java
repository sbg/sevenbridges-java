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
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractInstanceResource;
import com.sevenbridges.apiclient.impl.resource.IntegerProperty;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.project.Project;

import java.util.HashMap;
import java.util.Map;

public class DefaultApp extends AbstractInstanceResource implements App {

  // SIMPLE PROPERTIES:
  static final StringProperty ID = new StringProperty("id");
  static final StringProperty NAME = new StringProperty("name");
  static final StringProperty PROJECT = new StringProperty("project");
  static final IntegerProperty REVISION = new IntegerProperty("revision");
  static final MapProperty RAW = new MapProperty("raw");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      ID, NAME, PROJECT, REVISION, RAW
  );

  public DefaultApp(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultApp(InternalDataStore dataStore, Map<String, Object> properties) {
    super(dataStore, properties);
  }

  ////////////////////////////////////////////////////////////////////////
  // SIMPLE PROPERTIES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public Map<String, Property> getPropertyDescriptors() {
    return PROPERTY_DESCRIPTORS;
  }

  @Override
  public int getPropertiesCount() {
    return PROPERTY_DESCRIPTORS.size();
  }

  @Override
  public String getId() {
    return getString(ID);
  }

  @Override
  public String getName() {
    return getString(NAME);
  }

  @Override
  public String getProject() {
    return getString(PROJECT);
  }

  @Override
  public Integer getRevision() {
    return getInt(REVISION);
  }

  @Override
  public Map<String, Object> getRaw() {
    return getMap(RAW);
  }

  ////////////////////////////////////////////////////////////////////////
  // RESOURCE ACTIONS
  ////////////////////////////////////////////////////////////////////////

  @Override
  public App copy(Project destination) {
    return copy(destination, null);
  }

  @Override
  public App copy(Project destination, String newName) {
    Assert.notNull(destination, "Destination object cannot be null");
    String copyHref = String.format("/apps/%s/actions/copy", getId());
    Map<String, Object> properties = new HashMap<>(2);
    properties.put(PROJECT.getName(), destination.getId());
    if (newName != null) {
      properties.put(NAME.getName(), newName);
    }
    App appCopy = getDataStore().instantiate(App.class, properties);
    return getDataStore().create(copyHref, appCopy);
  }

  @Override
  public App synchronize() {
    String hrefNoRev = getHref().substring(0, getHref().lastIndexOf("/"));
    Map<String, Object> properties = new HashMap<>(1);
    properties.put(HREF_PROP_NAME, hrefNoRev);
    App appSync = getDataStore().instantiate(App.class, properties);
    return getDataStore().resourceAction("/actions/sync", appSync, App.class, null);
  }

}
