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
package com.sevenbridges.apiclient.project;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Static utility/helper methods for working with {@link Member} resources.
 * <p>
 * Most methods are <a href="http://en.wikipedia.org/wiki/Factory_method_pattern">factory
 * method</a>s used for forming Application-specific <a href="http://en.wikipedia.org/wiki/Fluent_interface">fluent
 * DSL</a> queries.
 */
public final class Members {

  //prevent instantiation
  private Members() {
  }

  /*
   * Permission types, can be used as keys to the permission Map
   */
  public static final String PERMISSION_WRITE = "write";
  public static final String PERMISSION_READ = "read";
  public static final String PERMISSION_COPY = "copy";
  public static final String PERMISSION_EXECUTE = "execute";
  public static final String PERMISSION_ADMIN = "admin";

  /**
   * Default permissions of a member in a project.
   */
  static final Map<String, Boolean> DEFAULT_PERMISSIONS;

  static {
    Map<String, Boolean> permissions = new HashMap<>(5);
    permissions.put(PERMISSION_READ, true);
    permissions.put(PERMISSION_WRITE, false);
    permissions.put(PERMISSION_COPY, false);
    permissions.put(PERMISSION_EXECUTE, false);
    permissions.put(PERMISSION_ADMIN, false);
    DEFAULT_PERMISSIONS = Collections.unmodifiableMap(permissions);
  }

  public static Map<String, Boolean> getDefaultPermissions() {
    return new HashMap<>(DEFAULT_PERMISSIONS);
  }

  /**
   * Validates given map of permissions.
   *
   * @param permissionsMap entry permission map
   * @return validated permission map
   */
  public static Map<String, Boolean> validateNewPermissions(Map<String, Boolean> permissionsMap) {
    Map<String, Boolean> defPermissions = getDefaultPermissions();
    if (permissionsMap.containsKey(PERMISSION_READ)) {
      defPermissions.put(PERMISSION_READ, permissionsMap.get(PERMISSION_READ));
    }
    if (permissionsMap.containsKey(PERMISSION_WRITE)) {
      defPermissions.put(PERMISSION_WRITE, permissionsMap.get(PERMISSION_WRITE));
    }
    if (permissionsMap.containsKey(PERMISSION_COPY)) {
      defPermissions.put(PERMISSION_COPY, permissionsMap.get(PERMISSION_COPY));
    }
    if (permissionsMap.containsKey(PERMISSION_EXECUTE)) {
      defPermissions.put(PERMISSION_EXECUTE, permissionsMap.get(PERMISSION_EXECUTE));
    }
    if (permissionsMap.containsKey(PERMISSION_ADMIN)) {
      defPermissions.put(PERMISSION_ADMIN, permissionsMap.get(PERMISSION_ADMIN));
    }
    return defPermissions;
  }

  /**
   * Validates given map of permissions for patch purposes. Only specified permissions will be
   * included in validated patch permissions map
   *
   * @param permissionsMap entry permission map
   * @return validated permission map
   */
  public static Map<String, Boolean> validatePatchPermissions(Map<String, Boolean> permissionsMap) {
    Map<String, Boolean> defPermissions = getDefaultPermissions();
    if (permissionsMap.containsKey(PERMISSION_READ)) {
      defPermissions.put(PERMISSION_READ, permissionsMap.get(PERMISSION_READ));
    } else {
      defPermissions.remove(PERMISSION_READ);
    }
    if (permissionsMap.containsKey(PERMISSION_WRITE)) {
      defPermissions.put(PERMISSION_WRITE, permissionsMap.get(PERMISSION_WRITE));
    } else {
      defPermissions.remove(PERMISSION_WRITE);
    }
    if (permissionsMap.containsKey(PERMISSION_COPY)) {
      defPermissions.put(PERMISSION_COPY, permissionsMap.get(PERMISSION_COPY));
    } else {
      defPermissions.remove(PERMISSION_COPY);
    }
    if (permissionsMap.containsKey(PERMISSION_EXECUTE)) {
      defPermissions.put(PERMISSION_EXECUTE, permissionsMap.get(PERMISSION_EXECUTE));
    } else {
      defPermissions.remove(PERMISSION_EXECUTE);
    }
    if (permissionsMap.containsKey(PERMISSION_ADMIN)) {
      defPermissions.put(PERMISSION_ADMIN, permissionsMap.get(PERMISSION_ADMIN));
    } else {
      defPermissions.remove(PERMISSION_ADMIN);
    }
    return defPermissions;
  }

}
