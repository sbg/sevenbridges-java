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

import com.sevenbridges.apiclient.resource.Resource;
import com.sevenbridges.apiclient.resource.Saveable;

import java.util.Map;

/**
 * Resource representing a {@link Volume} Import Job instance.
 */
public interface ImportJob extends Resource, Saveable {

  /**
   * ID field, unique identifier of import job on the Platform.
   *
   * @return String 'id' property of the current instance
   */
  String getId();

  /**
   * State field, current state of the volume import.
   *
   * @return {@link VolumeJobState} 'state' property of the current instance
   */
  VolumeJobState getState();

  /**
   * Overwrite field, boolean flag indicating whether the file should be overwritten in the
   * destination.
   *
   * @return Boolean 'overwrite' property of the current instance
   */
  Boolean getOverwrite();

  /**
   * SourceVolumeId field, String unique identifier of the registered {@link Volume} from where the
   * file is being imported.
   *
   * @return String 'sourceVolumeId' property of the current instance
   */
  String getSourceVolumeId();

  /**
   * SourceLocation field, String location on the volume from where the {@link
   * com.sevenbridges.apiclient.file.File} is being imported.
   *
   * @return String 'sourceLocation' property of the current instance
   */
  String getSourceLocation();

  /**
   * DestinationProjectId field, String unique identifier of the {@link
   * com.sevenbridges.apiclient.project.Project} on the Seven Bridges Platform where the file form
   * volume is being imported by this import job.
   *
   * @return String 'destinationProjectId' property of the current instance
   */
  String getDestinationProjectId();

  /**
   * DestinationName field, String name of the {@link com.sevenbridges.apiclient.file.File} that is
   * being imported by this import job.
   *
   * @return String 'destinationName' property of the current instance
   */
  String getDestinationName();

  /**
   * Error field, map of error information for this import if any error occurred (i.e. if Import is
   * in "FAILED" state).
   *
   * @return Map 'error' property of the current instance
   */
  Map<String, String> getError();

}
