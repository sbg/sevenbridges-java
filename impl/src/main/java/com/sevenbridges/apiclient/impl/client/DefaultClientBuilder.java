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
package com.sevenbridges.apiclient.impl.client;

import com.sevenbridges.apiclient.client.ApiKey;
import com.sevenbridges.apiclient.client.ApiKeyBuilder;
import com.sevenbridges.apiclient.client.AuthenticationScheme;
import com.sevenbridges.apiclient.client.Client;
import com.sevenbridges.apiclient.client.ClientBuilder;
import com.sevenbridges.apiclient.client.Proxy;
import com.sevenbridges.apiclient.impl.api.ClientTokenApiKey;
import com.sevenbridges.apiclient.impl.config.ClientConfiguration;
import com.sevenbridges.apiclient.impl.config.provider.ChainedConfigProvider;
import com.sevenbridges.apiclient.impl.config.provider.ConfigProvider;
import com.sevenbridges.apiclient.impl.config.provider.DefaultConfigProvider;
import com.sevenbridges.apiclient.impl.config.provider.EmptyConfigProvider;
import com.sevenbridges.apiclient.impl.config.provider.EnvironmentVariableConfigProvider;
import com.sevenbridges.apiclient.impl.config.provider.FileConfigProvider;
import com.sevenbridges.apiclient.impl.io.ClasspathResource;
import com.sevenbridges.apiclient.impl.io.DefaultResourceFactory;
import com.sevenbridges.apiclient.impl.io.Resource;
import com.sevenbridges.apiclient.impl.io.ResourceFactory;
import com.sevenbridges.apiclient.impl.transfer.TransferManagerConfiguration;
import com.sevenbridges.apiclient.impl.transfer.TransferManagerFactory;
import com.sevenbridges.apiclient.lang.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//@formatter:off
/**
 * The default {@link ClientBuilder} implementation.
 *
 * <ul>
 *   <li>classpath:com/sevenbridges/apiclient/config/sevenbridges.properties</li>
 *   <li>classpath:sevenbridges.properties</li>
 *   <li>~/.sevenbridges/credentials</li>
 *   <li>~/.sevenbridges/sevenbridges-java/sevenbridges.properties</li>
 * </ul>
 */
//@formatter:on
public class DefaultClientBuilder implements ClientBuilder {

  private static final Logger log = LoggerFactory.getLogger(DefaultClientBuilder.class);

  private String profile = DEFAULT_PROFILE;
  private ApiKey apiKey = null;
  private String apiEndpoint = null;
  private String authToken = null;
  private Integer connectionTimeout = null;
  private Proxy proxy = null;
  private AuthenticationScheme scheme = null;

  private Integer maxPartRetry = null;
  private Integer maxParallelUploads = null;
  private Integer maxParallelParts = null;

  private ConfigProvider environmentVariables;
  private ConfigProvider fromUserHome;
  private ConfigProvider fromClasspathResource;
  private ConfigProvider defaultConfig;

  private static final String USER_HOME = System.getProperty("user.home") + File.separatorChar;

  private static final String DEFAULT_LOCAL_CONFIG_FILE_LOCATION = ClasspathResource.SCHEME_PREFIX + SEVENBRIDGES_PROPERTIES_FILE_NAME;
  private static final String[] DEFAULT_CREDENTIALS_AND_CONFIG_FILE_LOCATION = {
      USER_HOME + SEVENBRIDGES_ROOT_FOLDER + File.separatorChar + SEVENBRIDGES_CREDENTIALS_FILE_NAME,
      USER_HOME + SEVENBRIDGES_ROOT_FOLDER + File.separatorChar + SEVENBRIDGES_JAVA_CONFIGURATION_FOLDER_NAME + File.separatorChar + SEVENBRIDGES_PROPERTIES_FILE_NAME
  };

  private ClientConfiguration clientConfig = new ClientConfiguration();

  public DefaultClientBuilder() {
    ResourceFactory resourceFactory = new DefaultResourceFactory();
    List<Resource> resources = new ArrayList<>();
    for (String location : DEFAULT_CREDENTIALS_AND_CONFIG_FILE_LOCATION) {
      resources.add(resourceFactory.createResource(location));
    }
    try {
      fromUserHome = new FileConfigProvider(resources);
    } catch (Exception e) {
      log.debug("No configuration resources found in user home", e);
      fromUserHome = new EmptyConfigProvider();
    }
    try {
      fromClasspathResource = new FileConfigProvider(resourceFactory.createResource(DEFAULT_LOCAL_CONFIG_FILE_LOCATION));
    } catch (Exception e) {
      log.debug("No configuration resources found in the classpath", e);
      fromClasspathResource = new EmptyConfigProvider();
    }
    try {
      environmentVariables = new EnvironmentVariableConfigProvider();
    } catch (Exception e) {
      log.debug("Could not get environment variables for config purposes", e);
      environmentVariables = new EmptyConfigProvider();
    }
    try {
      defaultConfig = DefaultConfigProvider.instance();
    } catch (Exception e) {
      log.warn("Could not get default config");
      defaultConfig = new EmptyConfigProvider();
    }
  }

  @Override
  public ClientBuilder setProxy(Proxy proxy) {
    Assert.notNull(proxy, "Proxy argument cannot be null");
    this.proxy = proxy;
    return this;
  }

  @Override
  public ClientBuilder setAuthenticationScheme(AuthenticationScheme authenticationScheme) {
    Assert.notNull(authenticationScheme, "AuthenticationScheme cannot be null");
    this.scheme = authenticationScheme;
    return this;
  }

  @Override
  public ClientBuilder setConnectionTimeout(int timeout) {
    Assert.isTrue(timeout >= 0, "Timeout cannot be a negative number.");
    this.connectionTimeout = timeout;
    return this;
  }

  /**
   * Sets the maximum number of retries for failed part uploads using internal transfer manager for
   * uploads. If a part fails more then maximum part retry times, whole upload will be stopped and
   * aborted. Default value is 5
   *
   * @param partRetry maximum number of part retries for uploads
   * @return the ClientBuilder instance for method chaining
   */
  @Override
  public ClientBuilder setUploadMaximumPartRetry(int partRetry) {
    Assert.isTrue(partRetry >= 0, "Part retry cannot be a negative number.");
    this.maxPartRetry = partRetry;
    return this;
  }

  /**
   * Sets the maximum number of parallel uploads using internal transfer manager for uploads. You
   * can submit any number of uploads for uploading on transfer service, but this number limits the
   * amount of active uploads at any given time. Setting this number optimally depends on the IO
   * capabilities of your system, but also number and size of files that you want to upload.
   * <p>
   * Default value is 4, and is usually more than enough to saturate output bandwidth of around
   * 100Mb/s. Tweak this number carefully
   *
   * @param maximumParallelUploads maximum number of parallel active uploads
   * @return the ClientBuilder instance for method chaining
   */
  @Override
  public ClientBuilder setMaximumParallelUploads(int maximumParallelUploads) {
    Assert.isTrue(maximumParallelUploads >= 0, "Max parallel uploads cannot be a negative number.");
    this.maxParallelUploads = maximumParallelUploads;
    return this;
  }

  /**
   * Sets the maximum number of parts that can be uploaded in parallel for each upload using
   * internal transfer manager service. With this in mind, maximum sum number of uploading parts in
   * any given moment is multiple of this number and 'maximum parallel uploads' number. For example,
   * for maximumParallelParts = 2 and maximumParallelUploads = 4, the maximum number of parallel
   * connections that are uploading data is 4 * 2 = 8
   * <p>
   * Default value is 2, raising this number more than 4 usually gains little benefits even on non
   * IO bound machines. Tweak this number carefully
   *
   * @param maximumParallelParts maximum number of parallel parts per active upload
   * @return the ClientBuilder instance for method chaining
   */
  @Override
  public ClientBuilder setMaximumParallelParts(int maximumParallelParts) {
    Assert.isTrue(maximumParallelParts >= 0, "Max parallel parts cannot be a negative number.");
    this.maxParallelParts = maximumParallelParts;
    return this;
  }

  /**
   * Sets the profile to be used when searching for config in configuration files. If none is
   * provided default config name is 'default'
   *
   * @param profile String name of the profile
   * @return the ClientBilder instance for method chaining
   */
  @Override
  public ClientBuilder setProfile(String profile) {
    Assert.hasText(profile, "Profile argument must not be null or emtpy");
    this.profile = profile.toLowerCase().trim();
    return this;
  }

  /**
   * Sets the base URL of the SevenBridges REST API endpoint to use.  If unspecified, this value
   * defaults to {@code https://api.sbgenomics.com/v2} - the most common use case for SevenBridges's
   * public cloud.
   *
   * @param apiEndpoint the api endpoint URL of the SevenBridges REST API to use.
   * @return the ClientBuilder instance for method chaining
   */
  @Override
  public ClientBuilder setApiEndpoint(String apiEndpoint) {
    Assert.hasText(profile, "ApiEndpoint argument must not be null or emtpy");
    this.apiEndpoint = apiEndpoint;
    return this;
  }

  /**
   * Allows specifying an {@code AuthToken} directly instead of relying on the default
   * location + override/fallback behavior defined in the {@link ClientBuilder documentation
   * above}.
   *
   * @param authToken the authToken to use to authenticate requests to the SevenBridges API server.
   * @return the ClientBuilder instance for method chaining.
   */
  @Override
  public ClientBuilder setAuthToken(String authToken) {
    Assert.hasText(profile, "AuthToken argument must not be null or emtpy");
    this.authToken = authToken;
    return this;
  }

  /**
   * Allows specifying an {@code ApiKey} instance directly instead of relying on the default
   * location + override/fallback behavior defined in the {@link ClientBuilder documentation
   * above}.
   * <p>
   * Consider using an {@link ApiKeyBuilder ApiKeyBuilder} to construct
   * your {@code ApiKey} instance.
   *
   * @param apiKey the ApiKey to use to authenticate requests to the SevenBridges API server.
   * @return the ClientBuilder instance for method chaining.
   * @see ApiKeyBuilder
   */
  @Override
  public ClientBuilder setApiKey(ApiKey apiKey) {
    Assert.notNull(apiKey, "ApiKey argument must not be null");
    this.apiKey = apiKey;
    return this;
  }

  @Override
  public Client build() {
    ChainedConfigProvider providersChain = new ChainedConfigProvider();

    String locationMessage = "Locations : Client Builder -> classpath::sevenbridges.properties > Environment Variable > $HOME/.sevenbridges/ > defaultConfig";
    providersChain
        .appendProvider(fromClasspathResource)
        .appendProvider(environmentVariables)
        .appendProvider(fromUserHome)
        .appendProvider(defaultConfig);

    // auth token building
    log.debug("Building Authentication, searching predefined locations: {}", locationMessage);
    if (this.authToken == null && this.apiEndpoint == null) {
      this.authToken = providersChain.getPropertyForProfileOrDefault(profile, CLIENT_AUTH_TOKEN_PROPERTY_NAME);
    }
    this.apiKey = this.apiKey != null ? this.apiKey : this.authToken == null ? null : new ClientTokenApiKey(this.authToken);
    Assert.state(this.apiKey != null,
        "No AuthToken has been set. It is required to properly build the Client. See 'setAuthToken(String)'.");

    // api endpoint building
    log.debug("Building api endpoint, searching predefined locations: {}", locationMessage);
    if (this.apiEndpoint == null) {
      this.apiEndpoint = providersChain.getPropertyForProfileOrDefault(profile, CLIENT_API_ENDPOINT_PROPERTY_NAME);
    }
    Assert.state(this.apiEndpoint != null,
        "No api endpoint has been set. It is required to properly build the Client. See 'setApiEndpoint(String)'.");

    // connection timeout building
    log.debug("Building connection timeout, searching predefined locations: {}", locationMessage);
    if (this.connectionTimeout == null) {
      String connTimeout = providersChain.getPropertyForProfileOrDefault(profile, CLIENT_CONNECTION_TIMEOUT_PROPERTY_NAME);
      try {
        this.connectionTimeout = Integer.parseInt(connTimeout);
      } catch (NumberFormatException e) {
        log.warn("Error while parsing provided value for key ConnectionTimeout, value '{}'", connTimeout, e);
      }
    }
    Assert.state(this.connectionTimeout != null,
        "No connection timeout has been set. It is required to properly build the Client. See 'setConnectionTimeout(int)'.");

    // auth scheme building
    log.debug("Building authentication schema, searching predefined locations: {}", locationMessage);
    if (this.scheme == null) {
      String authScheme = providersChain.getPropertyForProfileOrDefault(profile, CLIENT_AUTHENTICATION_SCHEME_PROPERTY_NAME);
      this.scheme = AuthenticationScheme.valueOf(authScheme);
    }

    // transfer manager config
    if (this.maxPartRetry == null) {
      String partRetry = providersChain.getPropertyForProfile(profile, CLIENT_MAXIMUM_UPLOAD_PART_RETRY);
      try {
        this.maxPartRetry = Integer.parseInt(partRetry);
      } catch (NumberFormatException e) {
        log.warn("Error while parsing provided value for key 'max_part_retry', value '{}'", partRetry, e);
        this.maxPartRetry = -1;
      }
    }
    if (this.maxParallelUploads == null) {
      String parsed = providersChain.getPropertyForProfile(profile, CLIENT_MAXIMUM_PARALLEL_UPLOADS);
      try {
        this.maxParallelUploads = Integer.parseInt(parsed);
      } catch (NumberFormatException e) {
        log.warn("Error while parsing provided value for key 'max_parallel_uploads', value '{}'", parsed, e);
        this.maxParallelUploads = -1;
      }
    }
    if (this.maxParallelParts == null) {
      String parsed = providersChain.getPropertyForProfile(profile, CLIENT_MAXIMUM_PARALLEL_PARTS_PER_UPLOAD);
      try {
        this.maxParallelParts = Integer.parseInt(parsed);
      } catch (NumberFormatException e) {
        log.warn("Error while parsing provided value for key 'max_parallel_parts', value '{}'", parsed, e);
        this.maxParallelParts = -1;
      }
    }
    TransferManagerFactory.setConfiguration(new TransferManagerConfiguration(this.maxParallelUploads, this.maxPartRetry, this.maxParallelParts));

    // use proxy overrides if they're set
    log.debug("Building proxy, searching predefined locations: {}", locationMessage);
    if (this.proxy == null) {
      String host = providersChain.getPropertyForProfileOrDefault(profile, CLIENT_PROXY_HOST_PROPERTY_NAME);
      String port = providersChain.getPropertyForProfileOrDefault(profile, CLIENT_PROXY_PORT_PROPERTY_NAME);
      Integer portNumber = null;
      try {
        if (port != null) {
          portNumber = Integer.parseInt(port);
        }
      } catch (NumberFormatException e) {
        log.warn("Found some proxy port provided, but is not a valid int - provided value : '{}'", port);
      }
      String username = providersChain.getPropertyForProfileOrDefault(profile, CLIENT_PROXY_USERNAME_PROPERTY_NAME);
      String password = providersChain.getPropertyForProfileOrDefault(profile, CLIENT_PROXY_PASSWORD_PROPERTY_NAME);

      if (portNumber != null && portNumber > 0 && host != null && username == null && password == null) {
        this.proxy = new Proxy(host, portNumber);
      } else if (portNumber != null && portNumber > 0 && host != null && username != null && password != null) {
        this.proxy = new Proxy(host, portNumber, username, password);
      }
    }

    return new DefaultClient(this.apiKey, this.apiEndpoint, this.proxy, this.scheme, this.connectionTimeout);
  }
}
