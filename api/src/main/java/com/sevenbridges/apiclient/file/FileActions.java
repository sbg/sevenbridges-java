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
package com.sevenbridges.apiclient.file;

import com.sevenbridges.apiclient.project.Project;

/**
 * The {@code FileActions} interface represents common user actions (behaviors) that can be executed
 * on a {@link File} instance.
 */
interface FileActions {

  /**
   * Returns the {@link Project} instance that contains current file.
   *
   * @return a parent {@link Project} parent for this file
   */
  Project getProject();

  /**
   * Performs the copy action of current file to the destination project with the same name as
   * source file instance.
   *
   * @param destinationProject Destination project
   * @return a new {@link File} instance, created by copying action
   */
  File copy(Project destinationProject);

  /**
   * Performs a copy action on current file to the destination project with a new name, provided
   * with the input parameter {@code newName}.
   *
   * @param destinationProject Destination project
   * @param newName            String new name of the file in the destination project
   * @return a new {@link File} instance, created by copying action
   */
  File copy(Project destinationProject, String newName);

  /**
   * The {@link DownloadInfo} instance, which contains the signed url from which the file can be
   * downloaded.
   *
   * @return a new {@link DownloadInfo} instance
   */
  DownloadInfo getDownloadInfo();

}
