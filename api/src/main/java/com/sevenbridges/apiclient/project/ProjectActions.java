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

import com.sevenbridges.apiclient.app.App;
import com.sevenbridges.apiclient.app.AppCriteria;
import com.sevenbridges.apiclient.app.AppList;
import com.sevenbridges.apiclient.billing.BillingGroup;
import com.sevenbridges.apiclient.file.FileCriteria;
import com.sevenbridges.apiclient.file.FileList;
import com.sevenbridges.apiclient.task.TaskCriteria;
import com.sevenbridges.apiclient.task.TaskList;

import java.util.Map;

/**
 * The {@code ProjectActions} interface represents common user actions (behaviors) that can be
 * executed on a {@link Project} instance.
 */
interface ProjectActions {

  /**
   * Returns the billing group resource object instead of billing group ID that is fetched in {@link
   * Project#getBillingGroupId()}.
   *
   * @return BillingGroup resource that current project instance belongs to
   */
  BillingGroup getBillingGroup();

  /**
   * Returns a collection of files that belong to the current project instance. This call will
   * return a collection resource that you can iterate through to get all of the elements of the
   * collection. Initially only one page of the collection (default limit is 25) is fetched.
   *
   * @return {@link FileList} collection of files represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  FileList getFiles();

  /**
   * Returns a collection of files that belong to the current project instance. This call will
   * return a collection resource that you can iterate through to get all of the elements of the
   * collection. Initially only one page of the collection (default limit is 25) is fetched.
   * <p>
   * You can specify custom query params to introduce criteria by which the files will be queried,
   * or to specify what fields of the {@link com.sevenbridges.apiclient.file.File} object will be
   * returned.
   *
   * @param queryParams Map of custom query params
   * @return {@link FileList} collection of files represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  FileList getFiles(Map<String, Object> queryParams);

  /**
   * Returns a collection of files that belong to the current project instance. This call will
   * return a collection resource that you can iterate through to get all of the elements of the
   * collection. Initially only one page of the collection (default limit is 25) is fetched.
   * <p>
   * You can specify custom {@link FileCriteria} to introduce rules by which the files will be
   * queried, or to specify what fields of the {@link com.sevenbridges.apiclient.file.File} object
   * will be returned.
   *
   * @param criteria custom criteria to fetch files by.
   * @return {@link FileList} collection of files represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  FileList getFiles(FileCriteria criteria);

  /**
   * Returns a collection of members that belong to the current project instance. This call will
   * return a collection resource that you can iterate through to get all of the elements of the
   * collection. Initially only one page of the collection (default limit is 25) is fetched.
   *
   * @return {@link MemberList} collection of members represented in a resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  MemberList getMembers();

  /**
   * Returns a member of the current project instance by its unique string username.
   *
   * @param memberId String username of the member
   * @return {@link Member} instance with specified username
   */
  Member getMemberById(String memberId);

  /**
   * Adds new member to the project, with specified username and member permissions for this
   * project. Only admins of a project are allowed to perform this action on a project.
   *
   * @param newMember {@link Member} to be added to the project
   * @return Added member and its permissions
   */
  Member addMember(Member newMember);

  /**
   * Removes a specified member from the current project instance. Only admins of a project are
   * allowed to perform this action a a project.
   *
   * @param member {@link Member} to remove from project
   */
  void removeMember(Member member);

  /**
   * Removes a member specified by its unique string username identifier from the current project
   * instance. Only admins of a project are allowed to perform this action a a project.
   *
   * @param username String identifier of the member
   */
  void removeMember(String username);

  /**
   * Returns a collection of tasks that belong to the current project instance. This call will
   * return a collection resource that you can iterate through to get all of the elements of the
   * collection. Initially only one page of the collection (default limit is 25) is fetched.
   *
   * @return {@link TaskList} collection of tasks represented in a resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  TaskList getTasks();

  /**
   * Returns a collection of tasks that belong to the current project instance. This call will
   * return a collection resource that you can iterate through to get all of the elements of the
   * collection. Initially only one page of the collection (default limit is 25) is fetched.
   * <p>
   * You can specify custom {@link TaskCriteria} to introduce rules by which the files will be
   * queried.
   *
   * @param criteria custom criteria to fetch tasks by.
   * @return {@link TaskList} collection of tasks represented in a resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  TaskList getTasks(TaskCriteria criteria);

  /**
   * Returns a collection of apps that belong to the current project instance. This call will return
   * a collection resource that you can iterate through to get all of the elements of the
   * collection. Initially only one page of the collection (default limit is 25) is fetched.
   *
   * @return {@link AppList} collection of apps represented in a resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  AppList getApps();

  /**
   * Returns a collection of apps that belong to the current project instance. This call will return
   * a collection resource that you can iterate through to get all of the elements of the
   * collection. Initially only one page of the collection (default limit is 25) is fetched.
   * <p>
   * You can specify custom query params to introduce criteria by which the apps will be queried, or
   * to specify what fields of the {@link com.sevenbridges.apiclient.app.App} object will be
   * returned.
   *
   * @param queryParams Map of custom query params
   * @return {@link AppList} collection of apps represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  AppList getApps(Map<String, Object> queryParams);

  /**
   * Returns a collection of apps that belong to the current project instance. This call will return
   * a collection resource that you can iterate through to get all of the elements of the
   * collection. Initially only one page of the collection (default limit is 25) is fetched.
   * <p>
   * You can specify custom {@link AppCriteria} to introduce rules by which the apps will be
   * queried.
   *
   * @param criteria custom criteria to fetch apps by.
   * @return {@link AppList} collection of apps represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  AppList getApps(AppCriteria criteria);

  /**
   * Installs the new app with a given app name, and with specified raw CWL json into project.
   * If successful, this call will create app with a revision number 0.
   *
   * @param appName String name of the new app
   * @param raw     CWL json of the app
   * @return created {@link App} object
   */
  App installApp(String appName, Map<String, Object> raw);

  /**
   * Installs the new app with a given app name, given revision number, and with specified
   * raw CWL json into project.
   *
   * @param appName  String name of the new app
   * @param revision Int revision number of the new app
   * @param raw      CWL json of the app
   * @return created {@link App} object
   */
  App createRevision(String appName, int revision, Map<String, Object> raw);

}
