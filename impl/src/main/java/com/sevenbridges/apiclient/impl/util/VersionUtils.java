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
package com.sevenbridges.apiclient.impl.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for accessing versioning information.
 */
public final class VersionUtils {

  private VersionUtils() { /* No instance methods */ }

  private static final Logger logger = Logger.getLogger(VersionUtils.class.getSimpleName());

  /** The version info resource file. */
  private static final String VERSION_INFO_RESOURCE = "/com/sevenbridges/apiclient/version.properties";

  private static final String userAgentTemplate = "{name}/{version} {os.name}/{os.version} {java.vm.name}/{java.vm.version}/{java.version}{language}";

  /** Application name info. */
  private static volatile String name;

  /** Application version info. */
  private static volatile String version;

  /** User Agent info. */
  private static volatile String userAgent;

  /**
   * Returns the current artifact for the application in which this class is running. Version
   * information is obtained from from the version.properties file which the Java application build
   * process generates.
   *
   * @return The current artifact for the application, if known, otherwise returns a string
   * indicating that the version information is not available.
   */
  public static String getName() {
    if (name == null) {
      synchronized (VersionUtils.class) {
        if (name == null) {
          initializeVersion();
        }
      }
    }
    return name;
  }

  /**
   * Returns the current version for the application in which this class is running. Version
   * information is obtained from from the version.properties file which the Java application build
   * process generates.
   *
   * @return The current version for the application, if known, otherwise returns a string
   * indicating that the version information is not available.
   */
  public static String getVersion() {
    if (version == null) {
      synchronized (VersionUtils.class) {
        if (version == null) {
          initializeVersion();
        }
      }
    }
    return version;
  }

  /**
   * @return Returns the User Agent string to be used when communicating with the web servers.  The
   * User Agent encapsulates application, Java, OS and region information.
   */
  public static String getUserAgent() {
    if (userAgent == null) {
      synchronized (VersionUtils.class) {
        if (userAgent == null) {
          initializeUserAgent();
        }
      }
    }
    return userAgent;
  }

  /**
   * Loads the version.properties file from the Java application and stores the information so that
   * the file doesn't have to be read the next time the data is needed.
   */
  private static void initializeVersion() {
    InputStream inputStream = VersionUtils.class.getResourceAsStream(VERSION_INFO_RESOURCE);
    Properties versionInfoProperties = new Properties();
    try {
      if (inputStream == null) {
        throw new Exception(VERSION_INFO_RESOURCE + " not found on classpath");
      }
      versionInfoProperties.load(inputStream);
      name = versionInfoProperties.getProperty("name");
      version = versionInfoProperties.getProperty("version");
    } catch (Exception e) {
      logger.log(Level.INFO, "Unable to load version information for the running application: {0}", e.getMessage());
      name = "sevenbridges-java";
      version = "unknown-version";
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (Exception ignore) {
          // ignore
        }
      }
    }
  }

  /**
   * Initializes the user agent string by loading a template from {@code InternalConfig} and filling
   * in the detected version/platform info.
   */
  private static void initializeUserAgent() {
    userAgent = userAgent();
  }

  static String userAgent() {

    String ua = userAgentTemplate;

    ua = ua
        .replace("{name}", getName())
        .replace("{version}", getVersion())
        .replace("{os.name}", replaceSpaces(System.getProperty("os.name")))
        .replace("{os.version}", replaceSpaces(System.getProperty("os.version")))
        .replace("{java.vm.name}", replaceSpaces(System.getProperty("java.vm.name")))
        .replace("{java.vm.version}", replaceSpaces(System.getProperty("java.vm.version")))
        .replace("{java.version}", replaceSpaces(System.getProperty("java.version")));

    String language = System.getProperty("user.language");

    String languageAndRegion = "";
    if (language != null) {
      languageAndRegion = " " + replaceSpaces(language);
    }
    ua = ua.replace("{language}", languageAndRegion);

    return ua;
  }

  /**
   * Replace any spaces in the input with underscores.
   *
   * @param input the input
   * @return the input with spaces replaced by underscores
   */
  private static String replaceSpaces(final String input) {
    return input.replace(' ', '_');
  }

}
