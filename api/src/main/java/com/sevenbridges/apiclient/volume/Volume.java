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

import com.sevenbridges.apiclient.resource.Auditable;
import com.sevenbridges.apiclient.resource.Deletable;
import com.sevenbridges.apiclient.resource.Resource;
import com.sevenbridges.apiclient.resource.Saveable;

/**
 * A volume resource holds specific information needed to work with cloud storage providers (e.g,
 * Amazon AWS and Google Cloud Platform).
 */
public interface Volume extends Resource, Auditable, Saveable, Deletable, VolumeActions {

  /**
   * VolumeId field, unique identifier of volume on the Platform.
   *
   * @return String 'volumeId' property of the current instance
   */
  String getVolumeId();

  /**
   * Name field, string name of the volume.
   *
   * @return String 'name' of the current instance
   */
  String getName();

  /**
   * Sets the name of the volume locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param name String new name of the volume
   * @return Current Volume instance
   */
  Volume setName(String name);

  /**
   * Description field, string optional description of the volume if any.
   *
   * @return String 'description' of the current instance
   */
  String getDescription();

  /**
   * Sets the description of the volume locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param description String new description of the volume
   * @return Current Volume instance
   */
  Volume setDescription(String description);

  /**
   * AccessMode field, {@link AccessMode} of the volume.
   *
   * @return AccessMode 'accessMode' property of the current instance
   */
  AccessMode getAccessMode();

  /**
   * Sets the accessMode of the volume locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param accessMode new access mode of the volume
   * @return Current Volume instance
   */
  Volume setAccessMode(AccessMode accessMode);

  /**
   * Active field, boolean flag indicating is volume active.
   *
   * @return Boolean flag indicator is volume active
   */
  Boolean isActive();

  /**
   * Sets the active flag of the volume locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param active boolean active value of the Volume
   * @return current Volume instance
   */
  Volume setActive(boolean active);

  /**
   * Service field, {@link VolumeService} service of the volume.
   *
   * @return VolumeService 'service' property of the current instance
   */
  VolumeService getService();

}
