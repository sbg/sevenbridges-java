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

import java.util.HashMap;
import java.util.Map;

abstract class AbstractConfigProvider implements ConfigProvider {

  private Map<String, Map<String, String>> profilesConfig = new HashMap<>();

  AbstractConfigProvider() {

  }

  public String getPropertyForProfile(String profile, String propertyName) {
    if (profilesConfig.containsKey(profile)) {
      return profilesConfig.get(profile).get(propertyName);
    }
    return null;
  }

  public String getPropertyForProfileOrDefault(String profile, String propertyName) {
    if (profilesConfig.containsKey(profile) && profilesConfig.get(profile).get(propertyName) != null) {
      return profilesConfig.get(profile).get(propertyName);
    } else if (profilesConfig.containsKey("default")) {
      return profilesConfig.get("default").get(propertyName);
    }
    return null;
  }

  public boolean hasPropertyForProfile(String profile, String propertyName) {
    return profilesConfig.containsKey(profile) && profilesConfig.get(profile).get(propertyName) != null;
  }

  public boolean hasPropertyForProfileOrDefault(String profile, String propertyName) {
    if (profilesConfig.containsKey(profile)) {
      if (profilesConfig.get(profile).containsKey(propertyName)) {
        String prop = profilesConfig.get(profile).get(propertyName);
        if ((prop != null) && (!"".equals(prop))) {
          return true;
        }
      }
    }
    if (profilesConfig.containsKey("default")) {
      if (profilesConfig.get("default").containsKey(propertyName)) {
        String prop = profilesConfig.get("default").get(propertyName);
        return ((prop != null) && (!"".equals(prop)));
      }
    }
    return false;
  }

  void mergeWithProvided(Map<String, Map<String, String>> provided) {
    for (Map.Entry<String, Map<String, String>> parsedProfile : provided.entrySet()) {
      if (profilesConfig.containsKey(parsedProfile.getKey())) {
        Map<String, String> oldConf = profilesConfig.get(parsedProfile.getKey());
        Map<String, String> newConf = parsedProfile.getValue();
        if (newConf != null) {
          oldConf.putAll(newConf);
        }
      } else {
        Map<String, String> newConf = parsedProfile.getValue();
        if (newConf != null) {
          profilesConfig.put(parsedProfile.getKey(), new HashMap<>(newConf));
        }
      }
    }
  }
}
