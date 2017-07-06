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
package com.sevenbridges.apiclient.volume;

/**
 * The {@code VolumeActions} interface represents common user actions (behaviors) that can be
 * executed for {@link Volume} resources.
 */
interface VolumeActions {

  /**
   * Gets the collection of imports for the current volume.
   *
   * @return {@link ImportJobList} collection of imports
   */
  ImportJobList getImports();

  /**
   * Gets the collection of exports for the current volume.
   *
   * @return {@link ExportJobList} collection of imports
   */
  ExportJobList getExports();

}
