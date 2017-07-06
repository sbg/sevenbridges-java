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

import java.util.Date;
import java.util.Map;

/**
 * Resource representing a {@link Volume} Export Job instance.
 */
public interface ExportJob extends Resource {

  /**
   * ID field, unique identifier of export job on the Platform.
   *
   * @return String 'id' property of the current instance
   */
  String getId();

  /**
   * State field, current state of the volume export.
   *
   * @return {@link VolumeJobState} 'state' property of the current instance
   */
  VolumeJobState getState();

  /**
   * SourceFileId field, String unique identifier of the {@link com.sevenbridges.apiclient.file.File}
   * on the Seven Bridges Platform that is being exported to volume.
   *
   * @return String 'sourceFileId' property of the current instance
   */
  String getSourceFileId();

  /**
   * DestinationVolumeId field, String unique identifier of the registered {@link Volume} where the
   * file is being exported.
   *
   * @return String 'destinationVolumeId' property of the current instance
   */
  String getDestinationVolumeId();

  /**
   * DestinationLocation field, String location on the volume where the {@link
   * com.sevenbridges.apiclient.file.File} is being exported.
   *
   * @return String 'destinationLocation' property of the current instance
   */
  String getDestinationLocation();

  /**
   * StartedOn field, Date time of the start of the current export.
   *
   * @return Date 'startedOn' property of the current instance
   */
  Date getStartedOn();

  /**
   * Properties field, Map of the storage specific properties that are being used during export, if
   * any.
   *
   * @return Map 'properties' property of the current instance
   */
  Map<String, String> getProperties();

  /**
   * Overwrite field, boolean flag indicating whether the file should be overwritten in the
   * destination.
   *
   * @return Boolean 'overwrite' property of the current instance
   */
  Boolean getOverwrite();

  /**
   * Error field, map of error information for this export if any error occurred (i.e. if Export is
   * in "FAILED" state).
   *
   * @return Map 'error' property of the current instance
   */
  Map<String, String> getError();

}
