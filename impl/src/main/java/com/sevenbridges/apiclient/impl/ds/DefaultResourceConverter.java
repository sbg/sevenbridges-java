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
package com.sevenbridges.apiclient.impl.ds;

import com.sevenbridges.apiclient.impl.resource.AbstractResource;
import com.sevenbridges.apiclient.impl.resource.ReferenceFactory;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.lang.Collections;
import com.sevenbridges.apiclient.resource.Resource;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultResourceConverter implements ResourceConverter {

  private final ReferenceFactory referenceFactory;

  public DefaultResourceConverter(ReferenceFactory referenceFactory) {
    Assert.notNull(referenceFactory, "referenceFactory cannot be null.");
    this.referenceFactory = referenceFactory;
  }

  @Override
  public Map<String, Object> convert(AbstractResource resource) {
    Assert.notNull(resource, "resource cannot be null.");
    return toMap(resource, false);
  }

  @Override
  public Map<String, Object> convertNonSubResources(AbstractResource resource) {
    Assert.notNull(resource, "resource cannot be null.");
    return toMap(resource, true);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Map<String, Object>> convertSubResources(AbstractResource resource) {
    Assert.notNull(resource, "resource cannot be null.");

    Set<String> subResourceNames = resource.getUpdatedSubResourceNames();

    LinkedHashMap<String, Map<String, Object>> props = new LinkedHashMap<>(subResourceNames.size());

    for (String subResourceName : subResourceNames) {
      Map<String, Object> value = resource.getMapProperty(subResourceName);
      props.put(subResourceName, value);
    }

    return props;
  }

  private LinkedHashMap<String, Object> toMap(final AbstractResource resource, boolean partialUpdate) {

    Set<String> propNames;

    if (partialUpdate) {
      propNames = resource.getUpdatedPropertyNames();
      Set<String> subResourceNames = resource.getUpdatedSubResourceNames();
      propNames.removeAll(subResourceNames);
    } else {
      propNames = resource.getPropertyNames();
      Set<String> updatedProps = resource.getUpdatedPropertyNames();
      propNames.addAll(updatedProps);
    }

    LinkedHashMap<String, Object> props = new LinkedHashMap<>(propNames.size());

    for (String propName : propNames) {
      Object value = resource.getProperty(propName);
      value = toMapValue(resource, propName, value, partialUpdate);
      props.put(propName, value);
    }

    return props;
  }

  private Object toMapValue(final AbstractResource resource,
                            final String propName,
                            Object value,
                            boolean partialUpdate) {

    if (value instanceof Map && ((Map) value).containsKey(AbstractResource.HREF_PROP_NAME)) {
      //if the property is a reference, don't write the entire object - just the href will do:
      //need to change this to write the entire object because this code defeats the purpose of entity expansion
      // when this code gets called (returning the reference instead of the whole object that is returned from SevenBridges)
      return this.referenceFactory.createReference(propName, (Map) value);
    }

    if (value instanceof Resource) {
      return this.referenceFactory.createReference(propName, (Resource) value);
    }

    return value;
  }

  @SuppressWarnings("unchecked")
  private Object convertAttributeStatementMappingRulesToMap(Object object) {

    Set<Object> elementsSet = (Set<Object>) object;
    Map<String, List<Object>> map = new HashMap<>();

    List<Object> list = Collections.toList(elementsSet.toArray());

    map.put("items", list);
    return map;
  }
}
