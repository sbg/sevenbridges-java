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

/**
 * Storage related information in a specific {@link File} instance.
 */
public interface FileStorage {

  /**
   * Type of storage for the current file instance. 'PLATFORM' type means that the file is hosted
   * on the Seven Bridges Platform, on Seven Bridges internal storage. 'VOLUME' type means that the
   * file is imported from or exported to certain user volume.
   *
   * @return the {@link StorageType} type of storage
   */
  StorageType getType();

  /**
   * If the file is of type 'VOLUME', this field contains the unique identifier of a {@link
   * com.sevenbridges.apiclient.volume.Volume} on a Platform.
   *
   * @return String volume id
   */
  String getVolume();

  /**
   * Returns the location of storage, the string coordinate of a cloud storage.
   *
   * @return String location
   */
  String getLocation();

}
