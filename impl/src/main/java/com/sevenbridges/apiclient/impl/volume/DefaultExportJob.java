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
import com.sevenbridges.apiclient.impl.resource.DateProperty;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.volume.ExportJob;
import com.sevenbridges.apiclient.volume.VolumeJobState;

import java.util.Date;
import java.util.Map;

public class DefaultExportJob extends AbstractInstanceResource implements ExportJob {

  static final StringProperty ID = new StringProperty("id");
  static final StringProperty STATE = new StringProperty("state");
  static final MapProperty SOURCE = new MapProperty("source");
  static final MapProperty DESTINATION = new MapProperty("destination");
  static final DateProperty STARTED_ON = new DateProperty("started_on");
  static final MapProperty PROPERTIES = new MapProperty("properties");
  static final MapProperty ERROR = new MapProperty("error");
  static final BooleanProperty OVERWRITE = new BooleanProperty("overwrite");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      ID, STATE, SOURCE, DESTINATION, STARTED_ON, PROPERTIES, OVERWRITE, ERROR
  );

  public DefaultExportJob(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultExportJob(InternalDataStore dataStore, Map<String, Object> properties) {
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
  public String getSourceFileId() {
    return (String) getMapPropertyEntry(SOURCE, "file");
  }

  @Override
  public String getDestinationVolumeId() {
    return (String) getMapPropertyEntry(DESTINATION, "volume");
  }

  @Override
  public String getDestinationLocation() {
    return (String) getMapPropertyEntry(DESTINATION, "location");
  }

  @Override
  public Date getStartedOn() {
    return getDate(STARTED_ON);
  }

  @Override
  public Map<String, String> getProperties() {
    return getMap(PROPERTIES);
  }

  @Override
  public Boolean getOverwrite() {
    return getBoolean(OVERWRITE);
  }

  @Override
  public Map<String, String> getError() {
    return getMap(ERROR);
  }

}
