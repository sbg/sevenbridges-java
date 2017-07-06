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
import com.sevenbridges.apiclient.resource.Resource;

import java.util.HashMap;
import java.util.Map;

public class ReferenceFactory {

  public ReferenceFactory() { /* No instance methods */ }

  public Map<String, String> createReference(Map map) {
    Assert.isTrue(!map.isEmpty() && map.containsKey(AbstractResource.HREF_PROP_NAME),
        "Reference resource must have an 'href' property.");
    String href = String.valueOf(map.get(AbstractResource.HREF_PROP_NAME));

    Map<String, String> reference = new HashMap<>(1);
    reference.put(AbstractResource.HREF_PROP_NAME, href);

    return reference;
  }

  public Map<String, String> createReference(String resourceName, Map map) {
    Assert.isTrue(!map.isEmpty() && map.containsKey(AbstractResource.HREF_PROP_NAME),
        "'" + resourceName + "' resource must have an 'href' property.");
    String href = String.valueOf(map.get(AbstractResource.HREF_PROP_NAME));

    Map<String, String> reference = new HashMap<>(1);
    reference.put(AbstractResource.HREF_PROP_NAME, href);

    return reference;
  }

  public Map<String, String> createReference(String resourceName, Resource resource) {
    Assert.notNull(resource, "Resource argument cannot be null.");
    String href = resource.getHref();
    Assert.hasText(href, "'" + resourceName + "' resource must have an 'href' property.");

    Map<String, String> reference = new HashMap<>(1);
    reference.put(AbstractResource.HREF_PROP_NAME, href);

    return reference;
  }
}
