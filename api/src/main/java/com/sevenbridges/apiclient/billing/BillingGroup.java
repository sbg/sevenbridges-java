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
package com.sevenbridges.apiclient.billing;

import com.sevenbridges.apiclient.price.Price;
import com.sevenbridges.apiclient.resource.Deletable;
import com.sevenbridges.apiclient.resource.Resource;
import com.sevenbridges.apiclient.resource.Saveable;

/**
 * Resource representing a BillingGroup instance.
 */
public interface BillingGroup extends Resource, Saveable, Deletable, BillingGroupActions {

  /**
   * ID field, the unique identifier of a billing group on the Platform.
   *
   * @return String 'id' property of the current instance
   */
  String getId();

  /**
   * Owner field, the username ID of the user who is the owner of a billing group.
   *
   * @return String 'owner' property of the current instance
   */
  String getOwner();

  /**
   * Name field, a non-unique name of a billing group.
   *
   * @return String 'name' property of the current instance
   */
  String getName();

  /**
   * Type field, indicating the type of a billing group.
   *
   * @return String 'type' property of the current instance
   */
  String getType();

  /**
   * Pending field, the flag indicating that a billing group is in a pending state.
   *
   * @return Boolean 'pending' property of the current instance
   */
  boolean isPending();

  /**
   * Disabled field, the flag indicating that a billing group is disabled.
   *
   * @return Boolean 'disabled' property of the current instance
   */
  boolean isDisabled();

  /**
   * Sets the disabled field of the current instance locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param disabled the flag indicating that a billing group is disabled
   * @return current BillingGroup instance
   */
  BillingGroup setDisabled(boolean disabled);

  /**
   * Balance field, indicates the total balance of a billing group.
   *
   * @return Price 'balance' property of the current instance
   */
  Price getBalance();

}
