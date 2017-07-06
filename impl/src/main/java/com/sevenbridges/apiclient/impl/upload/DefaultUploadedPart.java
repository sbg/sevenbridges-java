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
import com.sevenbridges.apiclient.impl.resource.IntegerProperty;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.upload.UploadedPart;
import com.sevenbridges.apiclient.upload.UploadedPartResponse;

import java.util.Collections;
import java.util.Map;

public class DefaultUploadedPart extends AbstractInstanceResource implements UploadedPart {

  // SIMPLE PROPERTIES:
  static final IntegerProperty PART_NUMBER = new IntegerProperty("part_number");
  static final MapProperty RESPONSE = new MapProperty("response");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      PART_NUMBER, RESPONSE
  );

  private static final int PROPERTIES_COUNT = PROPERTY_DESCRIPTORS.size();

  public DefaultUploadedPart(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultUploadedPart(InternalDataStore dataStore, Map<String, Object> properties) {
    super(dataStore, properties);
  }

  @Override
  public Map<String, Property> getPropertyDescriptors() {
    return PROPERTY_DESCRIPTORS;
  }

  @Override
  public int getPropertiesCount() {
    return PROPERTIES_COUNT;
  }

  ////////////////////////////////////////////////////////////////////////
  // SIMPLE PROPERTIES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public int getPartNumber() {
    return getInt(PART_NUMBER);
  }

  @Override
  public UploadedPart setPartNumber(int partNumber) {
    setProperty(PART_NUMBER, partNumber);
    return this;
  }

  @Override
  public UploadedPartResponse getResponse() {
    Map<String, Object> map = getMap(RESPONSE);
    if (map.containsKey("headers")) {
      return new DefaultUploadedPartResponse((Map<String, Object>) map.get("headers"));
    } else {
      return new DefaultUploadedPartResponse(Collections.<String, Object>emptyMap());
    }
  }

  @Override
  public UploadedPart setResponse(UploadedPartResponse response) {
    return null;
  }

  private static class DefaultUploadedPartResponse implements UploadedPartResponse {

    private Map<String, Object> headers;

    private DefaultUploadedPartResponse(Map<String, Object> headers) {
      this.headers = headers;
    }

    @Override
    public Map<String, Object> getHeaders() {
      return headers;
    }

    @Override
    public UploadedPartResponse setHeaders(Map<String, Object> headers) {
      this.headers = headers;
      return this;
    }
  }
}
