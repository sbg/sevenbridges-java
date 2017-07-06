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
package com.sevenbridges.apiclient.app;

import com.sevenbridges.apiclient.resource.Resource;

import java.util.Map;

/**
 * Resource representing an App instance.
 */
public interface App extends Resource, AppActions {

  /**
   * ID field, unique identifier of the app on the Platform.
   * <p>
   * Consists of PROJECT_ID/APP_NAME/APP_REVISION
   *
   * @return String 'id' property of the current instance
   */
  String getId();

  /**
   * Name field, non-unique string name of the app.
   *
   * @return String 'name' property of the current instance
   */
  String getName();

  /**
   * Project field, unique project ID for the project to which app belongs.
   *
   * @return String 'project' property of the current instance
   */
  String getProject();

  /**
   * Revision number, non-negative integer describing the current revision of the app.
   *
   * @return Integer 'revision' property of the current instance
   */
  Integer getRevision();

  /**
   * Raw field, represents the full CWL description in its raw JSON form, serialized as Map.
   *
   * @return Map serialized json of raw CWL
   */
  Map<String, Object> getRaw();

}
