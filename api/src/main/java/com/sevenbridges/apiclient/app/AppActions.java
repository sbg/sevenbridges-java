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

import com.sevenbridges.apiclient.project.Project;

/**
 * The {@code AppActions} interface represents common user actions (behaviors) that can be executed
 * on a {@link App} instance.
 */
interface AppActions {

  /**
   * Performs a copy action on the current app instance. The app will be copied to the destination
   * project with a new ID (the ID of an app contains {@link Project} ID) but the same name as
   * source app, with a revision number of 0.
   *
   * @param destination Destination project to copy an App to
   * @return a new {@link App} instance, created by the copy action
   */
  App copy(Project destination);

  /**
   * Performs a copy action on the current app instance. It will be copied to the destination
   * project with a new ID (the ID of an app contains the {@link Project} ID) and be given new name
   * differentiating it from the source app, with a revision number of 0.
   *
   * @param newName     String new name of the copied app
   * @param destination Destination project to copy an App to
   * @return a new {@link App} instance, created by the copy action
   */
  App copy(Project destination, String newName);

  /**
   * Performs a synchronization action on the current app instance. If the current app instance is a
   * copy of another app it will collect all changes from the parent app and apply it to the
   * instance.
   *
   * @return a new {@link App} instance, created by the synchronize action.
   */
  App synchronize();
}
