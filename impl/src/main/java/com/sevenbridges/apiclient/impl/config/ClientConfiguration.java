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

import com.sevenbridges.apiclient.client.AuthenticationScheme;

/**
 * This class holds the default configuration properties.
 * <p>
 * During application initialization all the properties found in the pre-defined locations that are
 * defined by the user will be added here in the order defined in {@link
 * com.sevenbridges.apiclient.impl.client.DefaultClientBuilder}. Unset values will use default
 * values from {@code com/sevenbridges/apiclient/config/sevenbridges.properties}.
 */
public class ClientConfiguration {

  private String apiKeyFile;
  private String apiKeyId;
  private String apiKeySecret;
  private String baseUrl;
  private int connectionTimeout;
  private AuthenticationScheme authenticationScheme;
  private int proxyPort;
  private String proxyHost;
  private String proxyUsername;
  private String proxyPassword;

  public String getApiKeyFile() {
    return apiKeyFile;
  }

  public void setApiKeyFile(String apiKeyFile) {
    this.apiKeyFile = apiKeyFile;
  }

  public String getApiKeyId() {
    return apiKeyId;
  }

  public void setApiKeyId(String apiKeyId) {
    this.apiKeyId = apiKeyId;
  }

  public String getApiKeySecret() {
    return apiKeySecret;
  }

  public void setApiKeySecret(String apiKeySecret) {
    this.apiKeySecret = apiKeySecret;
  }

  public AuthenticationScheme getAuthenticationScheme() {
    return authenticationScheme;
  }

  public void setAuthenticationScheme(AuthenticationScheme authenticationScheme) {
    this.authenticationScheme = authenticationScheme;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public String getProxyHost() {
    return proxyHost;
  }

  public void setProxyHost(String proxyHost) {
    this.proxyHost = proxyHost;
  }

  public String getProxyPassword() {
    return proxyPassword;
  }

  public void setProxyPassword(String proxyPassword) {
    this.proxyPassword = proxyPassword;
  }

  public int getProxyPort() {
    return proxyPort;
  }

  public void setProxyPort(int proxyPort) {
    this.proxyPort = proxyPort;
  }

  public String getProxyUsername() {
    return proxyUsername;
  }

  public void setProxyUsername(String proxyUsername) {
    this.proxyUsername = proxyUsername;
  }

  @Override
  public String toString() {
    return "ClientConfiguration{" +
        "apiKeyFile='" + apiKeyFile + '\'' +
        ", apiKeyId='" + apiKeyId + '\'' +
        ", apiKeySecret='" + apiKeySecret + '\'' +
        ", baseUrl='" + baseUrl + '\'' +
        ", connectionTimeout=" + connectionTimeout +
        ", authenticationScheme=" + authenticationScheme +
        ", proxyPort=" + proxyPort +
        ", proxyHost='" + proxyHost + '\'' +
        ", proxyUsername='" + proxyUsername + '\'' +
        ", proxyPassword='" + proxyPassword + '\'' +
        '}';
  }
}
