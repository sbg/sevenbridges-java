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
package com.sevenbridges.apiclient.project;

import com.sevenbridges.apiclient.resource.Deletable;
import com.sevenbridges.apiclient.resource.Resource;
import com.sevenbridges.apiclient.resource.Saveable;
import com.sevenbridges.apiclient.resource.Updatable;

import java.util.Set;

/**
 * Resource representing a Project instance.
 */
public interface Project extends Resource, Saveable, Updatable, Deletable, ProjectActions {

  /**
   * ID field, unique identifier of project on the Platform. Project ID consists of username of the
   * creator of the project and "slugified" initial name of the project. If the name of the project
   * changes, the ID will stay the same.
   *
   * @return String 'id' property of the current instance
   */
  String getId();

  /**
   * Name field, name of the project, non unique string.
   *
   * @return String 'name' property of the current instance
   */
  String getName();

  /**
   * Sets the property name of the project locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param name String new name of the project
   * @return current Project instance
   */
  Project setName(String name);

  /**
   * Type field, currently it is either v2 (rabix) or v1 (legacy) project. This will probably change
   * in the future.
   *
   * @return String 'type' property of the current instance
   */
  String getType();

  /**
   * Sets the type of the project locally, if omitted while creating default will be new, 'v2'
   * project.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param type String type of project to be set
   * @return current Project instance
   */
  Project setType(String type);

  /**
   * Tags field, project tags represent some internal Seven Bridges state of the project and they
   * can be changed only by superusers. This field will usually be empty for every normal project.
   *
   * @return Set 'tags' property of the current instance
   */
  Set<String> getTags();

  /**
   * Sets the tags of the current project instance locally. Unless current user is a super user this
   * change will not be saved on the Platform resource project on save.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param tags Set of String tags to be set
   * @return Current Project instance
   */
  Project setTags(Set<String> tags);

  /**
   * Description field, text description of the project. This is plain text information about a
   * project.
   *
   * @return String 'description' property of the current instance
   */
  String getDescription();

  /**
   * Sets the property description of the current project instance locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param description String new description of the project
   * @return Current project instance
   */
  Project setDescription(String description);

  /**
   * BillingGroupId field, unique string identifier of the {@link com.sevenbridges.apiclient.billing.BillingGroup}
   * that the project belongs to.
   *
   * @return String 'billingGroupID' property of the current instance.
   */
  String getBillingGroupId();

  /**
   * Sets the property billingGroupId of the current project instance locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param billingGroupId String ID of the billing group
   * @return Current Project instance
   */
  Project setBillingGroupId(String billingGroupId);

  /**
   * Locked field, boolean flag indicating is the current project instance locked.
   *
   * @return Boolean 'locked' property of the current instance.
   */
  Boolean getLocked();

  /**
   * Sets the property 'locked' of the current project locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param locked Boolean flag to be set
   * @return Current Project instance
   */
  Project setLocked(Boolean locked);

}
