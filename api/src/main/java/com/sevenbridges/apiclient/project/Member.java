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

import java.util.Map;

/**
 * Resource representing a Member instance. A Member is a user who is part of a {@link Project}.
 */
public interface Member extends Resource, Saveable, Updatable, Deletable {

  /**
   * Username field, unique string identifier of user and project member on the Platform.
   *
   * @return String 'username' property of the current instance
   */
  String getUsername();

  /**
   * Sets the property username of the current Member instance locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param username String username to be set
   * @return Current Member instance
   */
  Member setUsername(String username);

  //@formatter:off
  /**
   * Permissions field, map containing permissions of the current user. Map has fixed key values:
   * <ul>
   *   <li>
   *     <b>read</b>: Member can view file names, metadata, and workflows. They cannot view file
   *     contents. All members of a project have read permissions by default, and it cannot be
   *     unset.
   *   </li>
   *   <li>
   *     <b>write</b>: Member can add, modify, and remove files and workflows in a project.
   *     Set value to true to assign the user write permission, and false to remove it.
   *   </li>
   *   <li>
   *     <b>copy</b>: Member can view file content, copy, and download files from a project.
   *     Set value to true to assign the user copy permission, and false to remove it.
   *   </li>
   *   <li>
   *     <b>execute</b>: User can execute workflows and abort tasks in a project.
   *     Set value to true to assign the user execute permission, and false to remove it.
   *   </li>
   *   <li>
   *     <b>admin</b>: User can modify another user's permissions on a project, add or remove people
   *     from the project and manage funding sources. They also have all of the above permissions.
   *     Set value to true to assign the user admin permission, and false to remove it
   *   </li>
   * </ul>
   *
   * @return Map of the members permissions
   */
  //@formatter:on
  Map<String, Boolean> getPermissions();

  /**
   * Sets the permissions for the current member instance locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param permissions map of permissions to be set
   * @return Current Member instance
   */
  Member setPermissions(Map<String, Boolean> permissions);

}
