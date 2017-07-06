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
package com.sevenbridges.apiclient.impl.error;

import com.sevenbridges.apiclient.error.Error;
import com.sevenbridges.apiclient.impl.resource.AbstractResource;
import com.sevenbridges.apiclient.impl.resource.IntegerProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;

import java.util.Map;

public class DefaultError extends AbstractResource implements Error {

  static final IntegerProperty STATUS = new IntegerProperty("status");
  static final IntegerProperty CODE = new IntegerProperty("code");
  static final StringProperty MESSAGE = new StringProperty("message");
  static final StringProperty MORE_INFO = new StringProperty("more_info");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      STATUS, CODE, MESSAGE, MORE_INFO
  );

  private static final int PROPERTIES_COUNT = PROPERTY_DESCRIPTORS.size();

  public DefaultError(Map<String, Object> body) {
    super(null, body);
  }

  @Override
  public Map<String, Property> getPropertyDescriptors() {
    return PROPERTY_DESCRIPTORS;
  }

  @Override
  public int getPropertiesCount() {
    return PROPERTIES_COUNT;
  }

  @Override
  public int getStatus() {
    return getInt(STATUS);
  }

  @Override
  public int getCode() {
    return getInt(CODE);
  }

  @Override
  public String getMessage() {
    return getString(MESSAGE);
  }

  @Override
  public String getMoreInfo() {
    return getString(MORE_INFO);
  }
}
