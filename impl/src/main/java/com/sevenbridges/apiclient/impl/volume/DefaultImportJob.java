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
package com.sevenbridges.apiclient.impl.volume;

import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractInstanceResource;
import com.sevenbridges.apiclient.impl.resource.BooleanProperty;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.volume.ImportJob;
import com.sevenbridges.apiclient.volume.VolumeJobState;

import java.util.Map;

public class DefaultImportJob extends AbstractInstanceResource implements ImportJob {

  static final StringProperty ID = new StringProperty("id");
  static final StringProperty STATE = new StringProperty("state");
  static final BooleanProperty OVERWRITE = new BooleanProperty("overwrite");
  static final MapProperty SOURCE = new MapProperty("source");
  static final MapProperty DESTINATION = new MapProperty("destination");
  static final BooleanProperty ACTIVE = new BooleanProperty("active");
  static final MapProperty ERROR = new MapProperty("error");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      ID, STATE, OVERWRITE, SOURCE, DESTINATION, ACTIVE, ERROR
  );

  public DefaultImportJob(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultImportJob(InternalDataStore dataStore, Map<String, Object> properties) {
    super(dataStore, properties);
  }

  @Override
  public Map<String, Property> getPropertyDescriptors() {
    return PROPERTY_DESCRIPTORS;
  }

  @Override
  public int getPropertiesCount() {
    return PROPERTY_DESCRIPTORS.size();
  }

  ////////////////////////////////////////////////////////////////////////
  // SIMPLE PROPERTIES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public String getId() {
    return getString(ID);
  }

  @Override
  public VolumeJobState getState() {
    String stateValue = getString(STATE);
    return stateValue == null ? null : VolumeJobState.fromValue(stateValue);
  }

  @Override
  public Boolean getOverwrite() {
    return getBoolean(OVERWRITE);
  }

  @Override
  public String getSourceVolumeId() {
    return (String) getMapPropertyEntry(SOURCE, "volume");
  }

  @Override
  public String getSourceLocation() {
    return (String) getMapPropertyEntry(SOURCE, "location");
  }

  @Override
  public String getDestinationProjectId() {
    return (String) getMapPropertyEntry(DESTINATION, "project");
  }

  @Override
  public String getDestinationName() {
    return (String) getMapPropertyEntry(DESTINATION, "name");
  }

  @Override
  public Map<String, String> getError() {
    return getMap(ERROR);
  }
}
