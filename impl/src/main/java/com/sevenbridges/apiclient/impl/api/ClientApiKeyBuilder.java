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
package com.sevenbridges.apiclient.impl.api;

import com.sevenbridges.apiclient.client.ApiKey;
import com.sevenbridges.apiclient.client.ApiKeyBuilder;
import com.sevenbridges.apiclient.impl.io.DefaultResourceFactory;
import com.sevenbridges.apiclient.impl.io.ResourceFactory;
import com.sevenbridges.apiclient.lang.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

public class ClientApiKeyBuilder implements ApiKeyBuilder {

  private static final Logger log = LoggerFactory.getLogger(ClientApiKeyBuilder.class);

  private String apiKeyId;
  private String apiKeySecret;
  private String apiKeyFileLocation;
  private InputStream apiKeyInputStream;
  private Reader apiKeyReader;
  private Properties apiKeyProperties;
  private String apiKeyIdPropertyName = DEFAULT_ID_PROPERTY_NAME;
  private String apiKeySecretPropertyName = DEFAULT_SECRET_PROPERTY_NAME;
  private ResourceFactory resourceFactory = new DefaultResourceFactory();

  @Override
  public ApiKeyBuilder setId(String id) {
    this.apiKeyId = id;
    return this;
  }

  @Override
  public ApiKeyBuilder setSecret(String secret) {
    this.apiKeySecret = secret;
    return this;
  }

  @Override
  public ApiKeyBuilder setProperties(Properties properties) {
    this.apiKeyProperties = properties;
    return this;
  }

  @Override
  public ApiKeyBuilder setReader(Reader reader) {
    this.apiKeyReader = reader;
    return this;
  }

  @Override
  public ApiKeyBuilder setInputStream(InputStream is) {
    this.apiKeyInputStream = is;
    return this;
  }

  @Override
  public ApiKeyBuilder setFileLocation(String location) {
    this.apiKeyFileLocation = location;
    return this;
  }

  @Override
  public ApiKey build() {

    String id = null;
    String secret = null;
    Properties props;

    //1. Try any configured properties files:
    if (Strings.hasText(this.apiKeyFileLocation)) {
      try {
        Reader reader = createFileReader(this.apiKeyFileLocation);
        props = toProperties(reader);
      } catch (IOException e) {
        String msg = "Unable to read properties from specified apiKeyFileLocation [" + this.apiKeyFileLocation + "].";
        throw new IllegalArgumentException(msg, e);
      }

      id = getPropertyValue(props, this.apiKeyIdPropertyName, id);
      secret = getPropertyValue(props, this.apiKeySecretPropertyName, secret);
    }

    //2. Try any configured input stream
    if (this.apiKeyInputStream != null) {
      try {
        Reader reader = toReader(this.apiKeyInputStream);
        props = toProperties(reader);
      } catch (IOException e) {
        throw new IllegalArgumentException("Unable to read properties from specified apiKeyInputStream.", e);
      }

      id = getPropertyValue(props, this.apiKeyIdPropertyName, id);
      secret = getPropertyValue(props, this.apiKeySecretPropertyName, secret);
    }

    //3. Try any configured reader
    if (this.apiKeyReader != null) {
      try {
        props = toProperties(this.apiKeyReader);
      } catch (IOException e) {
        throw new IllegalArgumentException("Unable to read properties from specified apiKeyReader.", e);
      }

      id = getPropertyValue(props, this.apiKeyIdPropertyName, id);
      secret = getPropertyValue(props, this.apiKeySecretPropertyName, secret);
    }

    //4. Try any configured properties
    if (this.apiKeyProperties != null && !this.apiKeyProperties.isEmpty()) {
      id = getPropertyValue(this.apiKeyProperties, this.apiKeyIdPropertyName, id);
      secret = getPropertyValue(this.apiKeyProperties, this.apiKeySecretPropertyName, secret);
    }

    //5. Explicitly-configured values always take precedence:
    id = valueOf(this.apiKeyId, id);
    secret = valueOf(this.apiKeySecret, secret);

    if (!Strings.hasText(secret)) {
      String msg = "Unable to find an API Key 'secret' from explicit configuration " +
          "Please ensure you manually configure an API Key Secret or ensure that it exists in one of " +
          "these fallback locations.";
      throw new IllegalStateException(msg);
    }

    if (!Strings.hasText(id)) {
      return createTokenApiKey(secret);
    }

    return createApiKey(id, secret);
  }

  protected ApiKey createApiKey(String id, String secret) {
    return new ClientApiKey(id, secret);
  }

  protected ApiKey createTokenApiKey(String secret) {
    return new ClientTokenApiKey(secret);
  }

  private static String getPropertyValue(Properties properties, String propName) {
    String value = properties.getProperty(propName);
    if (value != null) {
      value = value.trim();
      if ("".equals(value)) {
        value = null;
      }
    }
    return value;
  }

  private static String valueOf(String discoveredValue, String defaultValue) {
    if (!Strings.hasText(discoveredValue)) {
      return defaultValue;
    }
    return discoveredValue;

  }

  private static String getPropertyValue(Properties properties, String propName, String defaultValue) {
    String value = getPropertyValue(properties, propName);
    return valueOf(value, defaultValue);
  }

  protected Reader createFileReader(String apiKeyFileLocation) throws IOException {
    InputStream is = this.resourceFactory.createResource(apiKeyFileLocation).getInputStream();
    return toReader(is);
  }

  private static Reader toReader(InputStream is) throws IOException {
    return new InputStreamReader(is, "ISO-8859-1");
  }

  private static Properties toProperties(Reader reader) throws IOException {
    Properties properties = new Properties();
    properties.load(reader);
    return properties;
  }
}
