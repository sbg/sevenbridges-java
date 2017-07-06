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

import java.util.LinkedList;

import static com.sevenbridges.apiclient.client.ClientBuilder.DEFAULT_PROFILE;

public class ChainedConfigProvider implements ConfigProvider {

  private LinkedList<ConfigProvider> providers;

  public ChainedConfigProvider() {
    providers = new LinkedList<>();
  }

  public ChainedConfigProvider prependProvider(ConfigProvider provider) {
    if (provider != null) {
      providers.addFirst(provider);
    }
    return this;
  }

  public ChainedConfigProvider appendProvider(ConfigProvider provider) {
    if (provider != null) {
      providers.add(provider);
    }
    return this;
  }

  @Override
  public String getPropertyForProfile(String profile, String propertyName) {
    for (ConfigProvider provider : providers) {
      if (provider.hasPropertyForProfile(profile, propertyName)) {
        return provider.getPropertyForProfile(profile, propertyName);
      }
    }
    return null;
  }

  /**
   * Searches for specified profile and property in the filter chain. If none is found, goes
   * through the chain again searching for the property with DEFAULT profile specified.
   *
   * @param profile      profile to search
   * @param propertyName config key to search
   * @return Found property value or null if none found
   */
  @Override
  public String getPropertyForProfileOrDefault(String profile, String propertyName) {
    for (ConfigProvider provider : providers) {
      if (provider.hasPropertyForProfile(profile, propertyName)) {
        return provider.getPropertyForProfile(profile, propertyName);
      }
    }
    for (ConfigProvider provider : providers) {
      if (provider.hasPropertyForProfile(DEFAULT_PROFILE, propertyName)) {
        return provider.getPropertyForProfile(DEFAULT_PROFILE, propertyName);
      }
    }
    return null;
  }

  @Override
  public boolean hasPropertyForProfile(String profile, String propertyName) {
    for (ConfigProvider provider : providers) {
      if (provider.hasPropertyForProfile(profile, propertyName)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean hasPropertyForProfileOrDefault(String profile, String propertyName) {
    for (ConfigProvider provider : providers) {
      if (provider.hasPropertyForProfileOrDefault(profile, propertyName)) {
        return true;
      }
    }
    return false;
  }
}
