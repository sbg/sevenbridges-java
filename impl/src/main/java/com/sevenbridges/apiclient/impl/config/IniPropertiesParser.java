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

import com.sevenbridges.apiclient.impl.io.Resource;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.lang.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IniPropertiesParser implements ProfilePropertiesParser {

  private static final Logger log = LoggerFactory.getLogger(IniPropertiesParser.class);

  private static final String DEFAULT_CHARSET_NAME = "UTF-8";
  private static final String COMMENT_POUND = "#";
  private static final String COMMENT_SEMICOLON = ";";
  private static final char ESCAPE_TOKEN = '\\';

  private Pattern profileSection = Pattern.compile("\\s*\\[([^]]*)\\]\\s*");

  /**
   * Parses the specified {@code .properties}-formatted String and returns a map of the parsed
   * properties or an empty map if no properties were found.
   *
   * @param source the String to parse
   * @return a map of the parsed properties or an empty map if no properties were found.
   */
  @Override
  public Map<String, Map<String, String>> parse(String source) {
    Scanner scanner = new Scanner(source);
    return parse(scanner);
  }

  /**
   * Parses the specified {@code .properties} resource and returns a map of the parsed properties or
   * an empty map if no properties were found.
   *
   * @param resource the resource to parse.
   * @return a map of the parsed properties or an empty map if no properties were found.
   * @throws IOException if unable to obtain the resource's {@link Resource#getInputStream()
   *                     inputStream}.
   */
  @Override
  public Map<String, Map<String, String>> parse(Resource resource) throws IOException {
    InputStream is = resource.getInputStream();
    if (is == null) {
      throw new FileNotFoundException("Resource '" + resource.toString() + "' not found");
    }
    Scanner scanner = new Scanner(is, DEFAULT_CHARSET_NAME);
    return parse(scanner);
  }

  /**
   * Parses the scanned content according to {@code .properties} formatting rules and returns a map
   * of the parsed properties or an empty map if no properties were found.
   *
   * @param scanner the Scanner to use to parse the content
   * @return a map of the parsed properties or an empty map if no properties were found.
   */
  @Override
  public Map<String, Map<String, String>> parse(Scanner scanner) {
    Assert.notNull(scanner, "Scanner argument cannot be null.");

    Map<String, Map<String, String>> profilesMap = new LinkedHashMap<>();
    Map<String, String> props = new LinkedHashMap<>();
    String currentProfile = null;

    StringBuilder lineBuffer = new StringBuilder();

    while (scanner.hasNextLine()) {

      String rawLine = scanner.nextLine();
      String line = Strings.clean(rawLine);

      if (line == null || line.startsWith(COMMENT_POUND) || line.startsWith(COMMENT_SEMICOLON)) {
        //skip empty lines and comments:
        continue;
      }

      if (isContinued(line)) {
        //strip off the last continuation backslash:
        line = line.substring(0, line.length() - 1);
        lineBuffer.append(line);
        continue;
      } else {
        lineBuffer.append(line);
      }

      line = lineBuffer.toString();
      lineBuffer = new StringBuilder();

      //check if it is profile section
      Matcher m = profileSection.matcher(line);
      if (m.matches()) {
        if (!props.isEmpty()) {
          if (currentProfile == null) {
            currentProfile = "default";
          }
          profilesMap.put(currentProfile, props);
          props = new LinkedHashMap<>();
        }
        currentProfile = m.group(1).trim().toLowerCase();
      } else {
        // we expect key / value pair here
        String[] kvPair = splitKeyValue(line);
        props.put(kvPair[0], kvPair[1]);
      }
    }

    if (!props.isEmpty()) {
      if (currentProfile == null) {
        currentProfile = "default";
      }
      profilesMap.put(currentProfile, props);
    }

    return profilesMap;
  }

  protected static boolean isContinued(String line) {
    if (!Strings.hasText(line)) {
      return false;
    }
    int length = line.length();
    //find the number of backslashes at the end of the line.  If an even number, the backslashes are
    // considered escaped.  If an odd number, the line is considered continued on the next line
    int backslashCount = 0;
    for (int i = length - 1; i > 0; i--) {
      if (line.charAt(i) == ESCAPE_TOKEN) {
        backslashCount++;
      } else {
        break;
      }
    }
    return backslashCount % 2 != 0;
  }

  private static boolean isKeyValueSeparatorChar(char c) {
    return Character.isWhitespace(c) || c == ':' || c == '=';
  }

  private static boolean isCharEscaped(CharSequence s, int index) {
    return index > 0 && s.charAt(index - 1) == ESCAPE_TOKEN;
  }

  //Protected to access in a test case
  protected static String[] splitKeyValue(String keyValueLine) {
    String line = Strings.clean(keyValueLine);
    if (line == null) {
      return null;
    }
    StringBuilder keyBuffer = new StringBuilder();
    StringBuilder valueBuffer = new StringBuilder();

    boolean buildingKey = true; //we'll build the value next:

    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);

      if (buildingKey) {
        if (isKeyValueSeparatorChar(c) && !isCharEscaped(line, i)) {
          buildingKey = false;//now start building the value
        } else {
          keyBuffer.append(c);
        }
      } else {
        if (valueBuffer.length() == 0 && isKeyValueSeparatorChar(c) && !isCharEscaped(line, i)) {
          //swallow the separator chars before we start building the value
        } else {
          valueBuffer.append(c);
        }
      }
    }

    String key = Strings.clean(keyBuffer.toString());
    String value = Strings.clean(valueBuffer.toString());

    if (key == null) {
      String msg = String.format("Line argument must contain a key. None was found but was expected. - Line: '%s'",
          keyValueLine);
      throw new IllegalArgumentException(msg);
    }

    log.trace("Discovered key/value pair: {} = {}", key, value);

    return new String[]{key, value};
  }

}
