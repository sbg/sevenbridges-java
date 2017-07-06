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

import com.sevenbridges.apiclient.impl.config.DefaultEnvVarNameConverter;
import com.sevenbridges.apiclient.impl.config.EnvVarNameConverter;
import com.sevenbridges.apiclient.lang.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnvironmentVariableConfigProvider extends AbstractConfigProvider {

  private static final Logger log = LoggerFactory.getLogger(EnvironmentVariableConfigProvider.class);

  public EnvironmentVariableConfigProvider() {
    super();
    Map<String, String> envVars = java.util.Collections.emptyMap();
    EnvVarNameConverter converter = new DefaultEnvVarNameConverter();
    log.debug("Fetching environment variables");
    try {
      envVars = System.getenv();
    } catch (Exception e) {
      log.debug("Error while fetching environment variables for config purposes", e);
    }
    Map<String, String> config = new LinkedHashMap<>();
    if (!Collections.isEmpty(envVars)) {
      for (Map.Entry<String, String> entry : envVars.entrySet()) {
        config.put(converter.toDottedPropertyName(entry.getKey()), entry.getValue());
      }
    }
    Map<String, Map<String, String>> provided = new LinkedHashMap<>();
    provided.put("default", config);
    mergeWithProvided(provided);
  }

}
