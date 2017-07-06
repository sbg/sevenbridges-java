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
package com.sevenbridges.apiclient.impl.upload;

import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractInstanceResource;
import com.sevenbridges.apiclient.impl.resource.DateProperty;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.upload.PartUpload;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DefaultPartUpload extends AbstractInstanceResource implements PartUpload {

  // SIMPLE PROPERTIES:
  static final StringProperty METHOD = new StringProperty("method");
  static final StringProperty URL = new StringProperty("url");
  static final DateProperty EXPIRES = new DateProperty("expires");
  static final MapProperty HEADERS = new MapProperty("headers");
  static final MapProperty REPORT = new MapProperty("report");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      METHOD, URL, EXPIRES, HEADERS, REPORT
  );

  private static final int PROPERTIES_COUNT = PROPERTY_DESCRIPTORS.size();

  public DefaultPartUpload(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultPartUpload(InternalDataStore dataStore, Map<String, Object> properties) {
    super(dataStore, properties);
  }

  @Override
  public Map<String, Property> getPropertyDescriptors() {
    return null;
  }

  @Override
  public int getPropertiesCount() {
    return 0;
  }

  ////////////////////////////////////////////////////////////////////////
  // SIMPLE PROPERTIES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public String getMethod() {
    return getString(METHOD);
  }

  @Override
  public String getUrl() {
    return getString(URL);
  }

  @Override
  public Date getExpires() {
    return getDate(EXPIRES);
  }

  @Override
  public Map<String, String> getHeaders() {
    return getMap(HEADERS);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Integer> getReportSuccessCodes() {
    return (List<Integer>) getMapPropertyEntry(REPORT, "success_codes");
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<String> getReportHeaders() {
    return (List<String>) getMapPropertyEntry(REPORT, "headers");
  }

}
