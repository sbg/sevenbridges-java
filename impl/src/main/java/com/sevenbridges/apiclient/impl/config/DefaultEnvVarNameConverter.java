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
package com.sevenbridges.apiclient.impl.config;

import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.lang.Strings;

public class DefaultEnvVarNameConverter implements EnvVarNameConverter {

  @Override
  public String toEnvVarName(String dottedPropertyName) {
    Assert.hasText(dottedPropertyName, "dottedPropertyName argument cannot be null or empty.");
    dottedPropertyName = Strings.trimWhitespace(dottedPropertyName);

    //special cases:
    if ("api_endpoint".equals(dottedPropertyName)) {
      return "SB_API_ENDPOINT";
    }
    if ("auth_token".equals(dottedPropertyName)) {
      return "SB_AUTH_TOKEN";
    }
    if ("sevenbridges.client.authentication_scheme".equals(dottedPropertyName)) {
      return "SB_AUTHENTICATION_SCHEME";
    }

    StringBuilder sb = new StringBuilder();

    for (char c : dottedPropertyName.toCharArray()) {
      if (c == '.') {
        sb.append('_');
        continue;
      }
      if (Character.isUpperCase(c)) {
        sb.append('_');
      }
      sb.append(Character.toUpperCase(c));
    }

    return sb.toString();
  }

  @Override
  public String toDottedPropertyName(String envVarName) {
    Assert.hasText(envVarName, "envVarName argument cannot be null or empty.");
    envVarName = Strings.trimWhitespace(envVarName);

    //special cases:
    if ("SB_API_ENDPOINT".equals(envVarName)) {
      return "api_endpoint";
    }
    if ("SB_AUTH_TOKEN".equals(envVarName)) {
      return "auth_token";
    }
    if ("SB_AUTHENTICATION_SCHEME".equals(envVarName)) {
      return "sevenbridges.client.authentication_scheme";
    }

    //default cases:
    StringBuilder sb = new StringBuilder();

    for (char c : envVarName.toCharArray()) {
      if (c == '_') {
        sb.append('.');
        continue;
      }
      sb.append(Character.toLowerCase(c));
    }

    return sb.toString();
  }
}
