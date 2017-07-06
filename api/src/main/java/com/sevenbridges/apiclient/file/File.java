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

import com.sevenbridges.apiclient.resource.Auditable;
import com.sevenbridges.apiclient.resource.Deletable;
import com.sevenbridges.apiclient.resource.Resource;
import com.sevenbridges.apiclient.resource.Saveable;
import com.sevenbridges.apiclient.resource.Updatable;

import java.util.Map;
import java.util.Set;

/**
 * Resource representing a File instance.
 */
public interface File extends Resource, Saveable, Updatable, Deletable, Auditable, FileActions {

  /**
   * ID field, unique identifier of file on the Platform.
   *
   * @return String 'id' property of the current instance
   */
  String getId();

  /**
   * Name of the file.
   *
   * @return String 'name' property of the current instance
   */
  String getName();

  /**
   * Sets the property name of the file locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param name new name of the file
   * @return current {@link File} instance
   */
  File setName(String name);

  /**
   * Size of the file in bytes.
   *
   * @return long 'size' property of the current instance
   */
  long getSize();

  /**
   * ProjectId of the project current file belongs to.
   *
   * @return String 'projectId' property of the current instance
   */
  String getProjectId();

  /**
   * Map of the metadata attributes of file.
   *
   * @return Map 'metadata' property of the current instance
   */
  Map<String, String> getMetadata();

  /**
   * Sets the metadata property overriding the old value locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param newMetadata new metadata of the file
   * @return current {@link File} instance
   */
  File setMetadata(Map<String, String> newMetadata);

  /**
   * Locally patches the metadata of the current file by setting only the fields provided as input
   * parameters, leaving all other metadata intact.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param metadataPatch patch metadata map
   * @return current {@link File} instance
   */
  File patchMetadata(Map<String, String> metadataPatch);

  /**
   * Storage information of the current file. If {@link StorageType} is VOLUME then information
   * about volume and location is provided.
   *
   * @return {@link FileStorage} property of the current instance
   */
  FileStorage getFileStorage();

  /**
   * Origin of the file, containing task ID of the task it originated from (if any).
   *
   * @return {@link FileOrigin} property of the current instance
   */
  FileOrigin getFileOrigin();

  /**
   * Set of string tags belonging to the file instance.
   *
   * @return Set 'tags' property of the current instance
   */
  Set<String> getTags();

  /**
   * Sets the tags of the current file with provided parameter {@code newTags} locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param newTags Set of String tags that will be set to file
   * @return current {@link File} instance
   */
  File setTags(Set<String> newTags);

}
