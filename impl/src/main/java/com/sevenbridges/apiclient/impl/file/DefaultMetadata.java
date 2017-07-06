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
package com.sevenbridges.apiclient.impl.file;

import com.sevenbridges.apiclient.file.Metadata;
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractInstanceResource;
import com.sevenbridges.apiclient.impl.resource.Property;

import java.util.Map;

public class DefaultMetadata extends AbstractInstanceResource implements Metadata {

  public DefaultMetadata(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultMetadata(InternalDataStore dataStore, Map<String, Object> properties) {
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

}
