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
import com.sevenbridges.apiclient.impl.io.ClasspathResource;
import com.sevenbridges.apiclient.impl.io.DefaultResourceFactory;
import com.sevenbridges.apiclient.impl.io.Resource;
import com.sevenbridges.apiclient.impl.io.ResourceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import static com.sevenbridges.apiclient.client.ClientBuilder.SEVENBRIDGES_PROPERTIES_FILE_NAME;

public final class DefaultConfigProvider extends AbstractConfigProvider {

  private static final Logger log = LoggerFactory.getLogger(DefaultConfigProvider.class);

  private DefaultConfigProvider() {
    super();
  }

  private static String DEFAULT_CONFIG = ClasspathResource.SCHEME_PREFIX + "com/sevenbridges/apiclient/config/" + SEVENBRIDGES_PROPERTIES_FILE_NAME;

  private static DefaultConfigProvider INSTANCE = null;

  public static synchronized DefaultConfigProvider instance() {
    if (INSTANCE == null) {
      IniPropertiesParser parser = new IniPropertiesParser();
      ResourceFactory resourceFactory = new DefaultResourceFactory();
      Resource resource = resourceFactory.createResource(DEFAULT_CONFIG);
      INSTANCE = new DefaultConfigProvider();
      try {
        Map<String, Map<String, String>> parsed = parser.parse(resource);
        INSTANCE.mergeWithProvided(parsed);
      } catch (IOException e) {
        log.debug("Error while parsing default resource config", e);
      }
    }
    return INSTANCE;
  }
}
