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

//@formatter:off
/**
 * A <a href="http://en.wikipedia.org/wiki/Builder_pattern">Builder design pattern</a> used to
 * construct {@link com.sevenbridges.apiclient.client.Client} instances.
 * <p>
 * The {@code ClientBuilder} is used to construct Client instances with Seven Bridges API Key and
 * Proxy.
 *
 * <h3>Usage</h3>
 * <p>
 * The simplest usage is to just call the {@link #build() build()} method, for example:
 * <pre>
 * Client client = {@link Clients Clients}.builder().{@link #build() build()};
 * </pre>
 * <p>
 * This will:
 * <ul>
 *   <li>
 *     Automatically attempt to find your API Key values in a number of default/conventional
 *     locations and then use the discovered values. Without any other configuration, the following
 *     locations will be each be checked, in order:
 *   </li>
 * </ul>
 *
 * <ol>
 *    <li>
 *     From a 'sevenbridges.properties' file that can be found on the classpath of your application
 *     that is using `sevenbridges-java` client. Usually such file can be found in the root of your
 *     'Resources' folder. The syntax of that file is explained in more details below
 *   </li>
 *   <li>
 *     Environment variables for specified SB_AUTH_TOKEN, SB_API_ENDPOINT, or other properties.
 *     Notice the distinction between environment variable and properties files for SB_AUTH_TOKEN
 *     and SB_API_ENDPOINT variables. In other places those variables are named without the
 *     'SB_' prefix.
 *   </li>
 *   <li>
 *     The default credentials file location of <code>System.getProperty("user.home") + "/.sevenbridges/credentials"</code>
 *     This file is expected to have a basic '.ini' structure, with sections separated with header
 *     line (name of the section writen inside square brackets). File can have multiple profiles
 *     inside. If no header file is specified, variables are considered to belong to the
 *     <code>DEFAULT_PROFILE = "profile"</code> profile. This is a special credentials file that may
 *     be shared with other sevenbridges client applications, so you should specify only this 2 keys
 *     in it (if any).  For example:
 *     <pre>
 *       [default]
 *       api_endpoint = https://api.sbgenomics.com/v2
 *       auth_token = 7eab08223339a1723d7d14c2e3e5373f
 *
 *       [my_other_profile]
 *       api_endpoint = https://api.sbgenomics.com/v2
 *       auth_token = 1e43ff221419a5523dfd14c3d7e2e3e5
 *     </pre>
 *   </li>
 *   <li>
 *     A properties file specific for the `sevenbridges-java` client at location <code>System.getProperty("user.home") +
 *     "/.sevenbridges/sevenbridges-java/sevenbridges.properties"</code>. In this file you can
 *     include any of the 'sevenbridges-java'-specific properties, though it is not obligatory to
 *     include all of them. The file has the same structure as the credentials file.  For example:
 *     <pre>
 *       [default]
 *       api_endpoint = https://api.sbgenomics.com/v2
 *       auth_token = 7eab08223339a1723d7d14c2e3e5373f
 *       sevenbridges.client.connection_timeout = 60000
 *       sevenbridges.client.proxy.host = http://my-proxy-host.com
 *       sevenbridges.client.proxy.port = 8080
 *     </pre>
 *   </li>
 *   <li>
 *     From a default sevenbridges java configuration. Default values are:
 *     <pre>
 *       api_endpoint = https://api.sbgenomics.com/v2
 *       sevenbridges.client.connection_timeout = 30000
 *       sevenbridges.client.authentication_scheme = AUTH_TOKEN
 *     </pre>
 *     In other words, every required field, except 'auth_token'.
 *   </li>
 * </ol>
 * <p>
 * While an API Key ID may be configured anywhere (and be visible by anyone), it is recommended to
 * use a private read-only file or an environment variable to represent API Key secrets.
 * <b>Never</b> commit secrets to source code or version control.
 *
 * <h4>Explicit API Configuration</h4>
 * <p>
 * The above default configuration-searching heuristics may not be suitable to your needs. In that
 * case, you will likely need to explicitly configure your client configuration using this builder.
 * Every configurable field has a corresponding setter function on this builder. Every config
 * property that is set manually with the builder will have higher priority than those found in
 * above mentioned resources.
 *
 * <h3>Profiles</h3>
 * <p>
 * You can specify a profile while building client. If you are setting all of the client
 * configuration fields manually this profile has no effect. But if you want to use properties
 * specified in some of the various properties files (mentioned above), you can specify a profile
 * name, and select values from section with specified profile name. If no values are found with
 * specified profile name, client will try to use 'default' sections of files if any. So for
 * example:
 * <p>
 *   Default properties file in home folder has specified KEY_1 = VAL_1, and no KEY_2 under profile [MY_PROFILE],
 *   while properties file on classpath has specified KEY_1 = VAL_2 and KEY_2 = VAL_2 under profile [default].
 *   <br>
 *   If you specify profile 'MY_PROFILE', KEY_1 will be taken from first file (VAL_1) as it has the key specified
 *   under given profile, but KEY_2 will be taken from second file (VAL_2) even though second property file doesn't have
 *   MY_PROFILE section, as it has special that key/value pair specified under 'default' section
 * </p>
 *
 * <h3>Single Instance</h3>
 * <p>
 * Finally, it should be noted that, after building a {@code client} instance, that same instance
 * should be used everywhere in your application. Creating multiple client instances in a single
 * application can have negative side effects.
 *
 * @see com.sevenbridges.apiclient.client.ApiKeyBuilder ApiKeyBuilder
 */
//@formatter:on
public interface ClientBuilder {

  /**
   * Name of the special profile, 'default' profile, that is being used by default, and if specified
   * in any of the properties files, its key/value pairs will be used if none can be found for the
   * specified profile.
   */
  String DEFAULT_PROFILE = "default";
  /**
   * Name of the folder in home directory of the current user where client expects the credentials
   * file and properties file.  For example:
   * <p>
   * '$home/.sevenbridges/credentials' and '$home/.sevenbridges/sevenbridges-java/sevenbridges.properties'
   */
  String SEVENBRIDGES_ROOT_FOLDER = ".sevenbridges";
  /**
   * Name of the folder inside the sevenbridges root folder where you can put properties files
   * specific for the `sevenbridges-java` client.
   */
  String SEVENBRIDGES_JAVA_CONFIGURATION_FOLDER_NAME = "sevenbridges-java";
  /**
   * Name of the credentials file, the file with AUTH_TOKEN and API_ENDPOINT specified in profiles
   * sections.
   */
  String SEVENBRIDGES_CREDENTIALS_FILE_NAME = "credentials";
  /**
   * Name of the properties file, the file with `sevenbridges-java` specific properties key/values.
   */
  String SEVENBRIDGES_PROPERTIES_FILE_NAME = "sevenbridges.properties";

  /*
   * CONFIGURABLE PROPERTY NAMES
   */
  String CLIENT_API_ENDPOINT_PROPERTY_NAME = "api_endpoint";
  String CLIENT_AUTH_TOKEN_PROPERTY_NAME = "auth_token";
  String CLIENT_CONNECTION_TIMEOUT_PROPERTY_NAME = "sevenbridges.client.connection_timeout";
  String CLIENT_AUTHENTICATION_SCHEME_PROPERTY_NAME = "sevenbridges.client.authentication_scheme";
  String CLIENT_PROXY_PORT_PROPERTY_NAME = "sevenbridges.client.proxy.port";
  String CLIENT_PROXY_HOST_PROPERTY_NAME = "sevenbridges.client.proxy.host";
  String CLIENT_PROXY_USERNAME_PROPERTY_NAME = "sevenbridges.client.proxy.username";
  String CLIENT_PROXY_PASSWORD_PROPERTY_NAME = "sevenbridges.client.proxy.password";
  String CLIENT_MAXIMUM_UPLOAD_PART_RETRY = "sevenbridges.client.upload.max_part_retry";
  String CLIENT_MAXIMUM_PARALLEL_UPLOADS = "sevenbridges.client.upload.max_parallel_uploads";
  String CLIENT_MAXIMUM_PARALLEL_PARTS_PER_UPLOAD = "sevenbridges.client.upload.max_parallel_parts";

  /**
   * Sets the profile to be used when searching for config in configuration files. This is optional
   * property, if none is provided default config name is 'default'.
   *
   * @param profile String name of the profile
   * @return the ClientBuilder instance for method chaining
   */
  ClientBuilder setProfile(String profile);

  /**
   * Sets the base URL of the Seven Bridges REST API endpoint to use.  If unspecified, this value
   * defaults to {@code https://api.sbgenomics.com/v2} - the most common use case for Seven
   * Bridges's public cloud.
   *
   * @param apiEndpoint the api endpoint URL of the Seven Bridges REST API to use.
   * @return the ClientBuilder instance for method chaining
   */
  ClientBuilder setApiEndpoint(String apiEndpoint);

  /**
   * Allows specifying an {@code authToken} directly instead of relying on the default location +
   * override/fallback behavior defined in the {@link ClientBuilder documentation above}.
   *
   * @param authToken the authToken to use to authenticate requests to the Seven Bridges API
   *                  server.
   * @return the ClientBuilder instance for method chaining.
   */
  ClientBuilder setAuthToken(String authToken);

  /**
   * Allows specifying an {@code ApiKey} instance directly instead of relying on the default
   * location + override/fallback behavior defined in the {@link ClientBuilder documentation
   * above}.
   * <p>
   * Consider using an {@link com.sevenbridges.apiclient.client.ApiKeyBuilder ApiKeyBuilder} to
   * construct your {@code ApiKey} instance.
   * <p>
   * This is HIGHLY optional, and should be used only if you need more flexibility in specifying
   * your authentication token.
   *
   * @param apiKey the ApiKey to use to authenticate requests to the Seven Bridges API server.
   * @return the ClientBuilder instance for method chaining.
   * @see com.sevenbridges.apiclient.client.ApiKeyBuilder
   */
  ClientBuilder setApiKey(ApiKey apiKey);

  /**
   * Sets the HTTP proxy to be used when communicating with the Seven Bridges API server.  For
   * example:
   * <pre>
   * Proxy proxy = new Proxy("whatever.domain.com", 443);
   * Client client = {@link Clients Clients}.builder().setProxy(proxy).build();
   * </pre>
   *
   * @param proxy the {@code Proxy} you need to use.
   * @return the ClientBuilder instance for method chaining.
   */
  ClientBuilder setProxy(Proxy proxy);

  //@formatter:off
  /**
   * It is not recommended that you override this setting <em>unless</em> your application is
   * deployed in an environment that, outside of your application's control, manipulates request
   * headers on outgoing HTTP requests.
   * <p>
   * As such, in these environments only, an alternative authentication mechanism is necessary, such
   * as <a href="http://docs.sevenbridges.com/page/api-overview#section-authentication">Auth Token
   * Authentication</a>.
   * <pre>
   * Client client = Clients.builder()...
   *    .setAuthenticationScheme(AuthenticationScheme.AUTH_TOKEN) //set the auth token authentication scheme
   *    .build(); //build the Client
   * </pre>
   *
   * @param authenticationScheme the type of authentication to be used for communication with the
   *                             Seven Bridges API server.
   * @return the ClientBuilder instance for method chaining
   */
  //@formatter:on
  ClientBuilder setAuthenticationScheme(AuthenticationScheme authenticationScheme);

  /**
   * Sets both the timeout until a connection is established and the socket timeout (i.e. a maximum
   * period of inactivity between two consecutive data packets).  A timeout value of zero is
   * interpreted as an infinite timeout.
   * <p>
   * Default value is 30s (30_000 ms).
   *
   * @param timeout connection and socket timeout in milliseconds
   * @return the ClientBuilder instance for method chaining
   */
  ClientBuilder setConnectionTimeout(int timeout);

  /**
   * Sets the maximum number of retries for failed part uploads while using internal transfer
   * manager for uploads. If a part fails more then maximum part retry times, whole upload will be
   * stopped and aborted.
   * <p>
   * Default value is 5.
   *
   * @param partRetry maximum number of part retries for uploads
   * @return the ClientBuilder instance for method chaining
   */
  ClientBuilder setUploadMaximumPartRetry(int partRetry);

  /**
   * Sets the maximum number of parallel uploads while using internal transfer manager for uploads.
   * You can submit any number of uploads for uploading on transfer service, but this number limits
   * the amount of active uploads at any given time. Setting this number optimally depends on the IO
   * capabilities of your system, but also number and size of files that you want to upload.
   * <p>
   * Default value is 4, and is usually more than enough to saturate output bandwidth of around
   * 100Mb/s. Tweak this number carefully.
   *
   * @param maximumParallelUploads maximum number of parallel active uploads
   * @return the ClientBuilder instance for method chaining
   */
  ClientBuilder setMaximumParallelUploads(int maximumParallelUploads);

  /**
   * Sets the maximum number of parts that can be uploaded in parallel for each upload while using
   * the internal transfer manager service. With this in mind, maximum sum number of uploading parts
   * in any given moment is multiple of this number and 'maximum parallel uploads' number. For
   * example, for maximumParallelParts = 2 and maximumParallelUploads = 4, the maximum number of
   * parallel connections that are uploading data is 4 * 2 = 8
   * <p>
   * Default value is 2, raising this number more than 4 usually gains little benefits even on non
   * IO bound machines. Tweak this number carefully.
   *
   * @param maximumParallelParts maximum number of parallel parts per active upload
   * @return the ClientBuilder instance for method chaining
   */
  ClientBuilder setMaximumParallelParts(int maximumParallelParts);


  /**
   * Constructs a new {@link Client} instance based on the ClientBuilder's current configuration
   * state.
   *
   * @return a new {@link Client} instance based on the ClientBuilder's current configuration state.
   */
  Client build();
}
