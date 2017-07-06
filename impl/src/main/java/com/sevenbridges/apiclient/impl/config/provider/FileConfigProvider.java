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
package com.sevenbridges.apiclient.impl.config.provider;

import com.sevenbridges.apiclient.impl.config.IniPropertiesParser;
import com.sevenbridges.apiclient.impl.config.ProfilePropertiesParser;
import com.sevenbridges.apiclient.impl.io.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileConfigProvider extends AbstractConfigProvider {

  private static final Logger log = LoggerFactory.getLogger(FileConfigProvider.class);

  public FileConfigProvider(Resource resources) {
    super();
    initPropertiesMapFromResources(Collections.singletonList(resources));
  }

  public FileConfigProvider(List<Resource> resources) {
    super();
    initPropertiesMapFromResources(resources);
  }

  private void initPropertiesMapFromResources(List<Resource> resources) {
    ProfilePropertiesParser parser = new IniPropertiesParser();
    for (Resource resource : resources) {
      Map<String, Map<String, String>> parsed = new HashMap<>();
      try {
        parsed = parser.parse(resource);
      } catch (Exception e) {
        log.debug("Error while trying to parse resource {} for config ", resource.toString(), e);
      }
      mergeWithProvided(parsed);
    }
  }

}
