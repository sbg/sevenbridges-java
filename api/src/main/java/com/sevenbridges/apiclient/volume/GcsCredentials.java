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

import com.sevenbridges.apiclient.resource.Saveable;

/**
 * Property of the {@link GcsVolumeService} containing credentials information.
 */
public interface GcsCredentials {

  /**
   * Sets the Google Cloud Storage credentials locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param clientEmail String client email
   * @param privateKey  String private key
   * @return GcsCredentials instance
   */
  GcsCredentials setCredentials(String clientEmail, String privateKey);

  /**
   * Gets the client email of the current credentials.
   *
   * @return String client email
   */
  String getClientEmail();

  /**
   * Gets the private key of the current credentials.
   *
   * @return String pricate key
   */
  String getPrivateKey();

}
