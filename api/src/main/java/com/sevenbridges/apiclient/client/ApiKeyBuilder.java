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
package com.sevenbridges.apiclient.client;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

//@formatter:off
/**
 * A <a href="http://en.wikipedia.org/wiki/Builder_pattern">Builder design pattern</a> used to
 * construct {@link ApiKey} instances.
 * <p>
 * The {@code ApiKeyBuilder} is especially useful for constructing Client ApiKey instances with
 * Seven Bridges API Key information loaded from an external {@code credentials} file (or Properties
 * instance) to ensure the API Key secret (password) does not reside in plaintext in code.
 *
 * <h3>Usage</h3>
 * <p>
 * The simplest usage is to just set the token with {@link ApiKeyBuilder#setSecret(String)} and
 * then call {@link ApiKeyBuilder#build()}, which will build an api key with specified secret auth
 * token.
 * <p>
 * While an API Key ID may be configured anywhere (and be visible by anyone), it is recommended
 * to use a private read-only file or an environment variable to represent API Key secrets.
 * <b>Never</b> commit secrets to source code or version control.
 *
 * <h4>Explicit Configuration</h4>
 *
 * <ul>
 *   <li>
 *     {@link #setFileLocation(String) fileLocation}:  Any properties discovered in this .properties
 *     file will override any previously automatically discovered values.  The {@code fileLocation}
 *     String can be an absolute file path, or it can be a URL or a classpath value by using the
 *     {@code classpath:} prefixes respectively.
 *   </li>
 *   <li>
 *     {@link #setInputStream(java.io.InputStream) inputStream}: Properties discovered after reading
 *     the input stream will override any previously discovered values.
 *   </li>
 *   <li>
 *     {@link #setReader(java.io.Reader) reader}: Properties discovered after reading the Reader
 *     will override any previously discovered values.
 *   </li>
 *   <li>
 *     {@link #setProperties(java.util.Properties) properties} instance: Properties discovered in
 *     the instance will override any previously discovered values.
 *   </li>
 *   <li>
 *     Directly specified {@link #setId(String) id} and {@link #setSecret(String) secret} property
 *     values. If specified, they will override any previously discovered value.
 *   </li>
 * </ul>
 *
 * <h3>Supporting the ClientBuilder</h3>
 * <p>
 * Once you have built your {@code ApiKey} instance, you can then build your {@link Client} instance
 * that you can use during your application's lifecycle.  For example:
 * <pre>
 * ApiKey apiKey = {@link ApiKeys ApiKeys}.builder()
 *                 //optional specific configuration
 *                 .{@link #build build()};
 *
 * //use this same Client instance everywhere in your application:
 * Client client = {@link Clients Clients}.builder().setApiKey(apiKey).build();
 * </pre>
 *
 * @see #setFileLocation(String)
 * @see ClientBuilder#setApiKey(com.sevenbridges.apiclient.client.ApiKey)
 */
//@formatter:on
public interface ApiKeyBuilder {

  String DEFAULT_ID_PROPERTY_NAME = "apiKey.id";
  String DEFAULT_SECRET_PROPERTY_NAME = "apiKey.auth_token";

  /**
   * Allows specifying the client's API Key {@code id} value directly.
   *
   * @param id the {@link ApiKey#getId() ApiKey id} to use when communicating with Seven Bridges.
   * @return the ApiKeyBuilder instance for method chaining.
   * @see ClientBuilder#setApiKey(ApiKey)
   */
  ApiKeyBuilder setId(String id);

  //@formatter:off
  /**
   * Allows specifying the client's API Key {@code secret} value directly
   *
   * <h3>Usage Warning</h3>
   *
   * It is strongly recommended that you never embed raw API Key secret values in source code!
   * API Key Secret values are tied to an individual <em>person</em> and should never be shared with
   * anyone or embedded in source code that can be viewed by multiple people.  For example:
   *
   * <span><b>THIS IS AN ANTI-PATTERN! DO NOT DO THIS! THIS IS A SECURITY RISK!</b></span>
   * <pre>
   * String id = "myRawApiKeyId";
   * String secret = "secretValueThatAnyoneCouldSeeIfTheyCheckedOutMySourceCode";
   * ApiKey apiKey = ApiKeys.builder().setId(id).setSecret(secret).build();
   * Client client = Clients.builder().setApiKey(apiKey).build();
   * </pre>
   *
   * @param secret the {@link ApiKey#getId() ApiKey id} to use when communicating with Seven Bridges.
   * @return the ApiKeyBuilder instance for method chaining.
   * @see #setId(String)
   * @see ClientBuilder#setApiKey(ApiKey)
   */
  //@formatter:on
  ApiKeyBuilder setSecret(String secret);

  /**
   * Allows usage of a Properties instance instead of loading a {@code .properties} file via {@link
   * #setFileLocation(String) fileLocation} configuration.
   * <p>
   * The {@code Properties} contents and property name overrides function the same as described in
   * the {@link #setFileLocation(String) setFileLocation} JavaDoc.
   *
   * @param properties the properties instance to use to load the API Key ID and Secret.
   * @return the ApiKeyBuilder instance for method chaining.
   */
  ApiKeyBuilder setProperties(Properties properties);

  /**
   * Creates an API Key Properties instance based on the specified Reader instead of loading a
   * {@code .properties} file via  {@link #setFileLocation(String) fileLocation} configuration.
   * <p>
   * The constructed {@code Properties} contents and property name overrides function the same as
   * described in the {@link #setFileLocation(String) setFileLocation} JavaDoc.
   *
   * @param reader the reader to use to construct a Properties instance.
   * @return the ApiKeyBuilder instance for method chaining.
   */
  ApiKeyBuilder setReader(Reader reader);

  /**
   * Creates an API Key Properties instance based on the specified InputStream instead of loading a
   * {@code .properties} file via {@link #setFileLocation(String) fileLocation} configuration.
   * <p>
   * The constructed {@code Properties} contents and property name overrides function the same as
   * described in the {@link #setFileLocation(String) setFileLocation} JavaDoc.
   *
   * @param is the InputStream to use to construct a Properties instance.
   * @return the ApiKeyBuilder instance for method chaining.
   */
  ApiKeyBuilder setInputStream(InputStream is);

  //@formatter:off
  /**
   * Sets the location of the {@code .properties} file to load containing the API Key (Id and
   * secret) used by the Client to communicate with the Seven Bridges REST API.
   * <p>
   * You may load files from the filesystem, classpath, or URLs by prefixing the location path
   * with {@code file:}, or {@code classpath:} respectively.  If no prefix is found, {@code file:}
   * is assumed by default.
   *
   * <h3>File Contents</h3>
   * When the file is loaded, the following name/value pairs are expected to be present by default:
   * <table summary="">
   *   <tr>
   *     <th>Key</th>
   *     <th>Value</th>
   *   </tr>
   *   <tr>
   *     <td>apiKey.id</td>
   *     <td>An individual account's API Key ID</td>
   *   </tr>
   *   <tr>
   *     <td>apiKey.auth_token</td>
   *     <td>The API Key secret authentication token (password) that verifies the paired API Key ID.</td>
   *   </tr>
   * </table>
   *
   * <pre>
   * String location = "absolute or relative location to custom properties file";
   * ApiKey apiKey = ApiKeys.builder().setFileLocation(location).build();
   * Client client = Clients.builder().setApiKey(apiKey).build();
   * </pre>
   *
   * @param location the file, classpath or url location of the API Key {@code .properties} file to
   *                 load when constructing the API Key to use for communicating with the
   *                 Seven Bridges REST API.
   * @return the ApiKeyBuilder instance for method chaining.
   */
  //@formatter:on
  ApiKeyBuilder setFileLocation(String location);

  /**
   * Constructs a new {@link ApiKey} instance based on the ApiKeyBuilder's current configuration
   * state.
   *
   * @return a new {@link ApiKey} instance based on the ApiKeyBuilder's current configuration state.
   */
  ApiKey build();
}
