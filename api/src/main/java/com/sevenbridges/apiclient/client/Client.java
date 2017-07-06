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
package com.sevenbridges.apiclient.client;

import com.sevenbridges.apiclient.ds.DataStore;
import com.sevenbridges.apiclient.user.User;
import com.sevenbridges.apiclient.user.UserActions;
import com.sevenbridges.apiclient.user.UserOptions;

/**
 * The {@code Client} is the main entry point to the Seven Bridges Java client library.  A JVM
 * project wishing to communicate with the Seven Bridges REST API service must build a {@code
 * Client} instance.  After obtaining a {@code Client instance}, the REST API may be used by making
 * simple Java calls on objects returned from the Client (or any children objects obtained
 * therein).
 * <p>
 * For example:
 * <pre>
 * String path = System.getProperty("user.home") + "/.sbg/<a href="http://docs.sevenbridges.com/docs/credentials">credentials</a>";
 * Client client = {@link Clients Clients}.{@link Clients#builder() builder()}
 *     .{@link ClientBuilder#setApiKey(ApiKey) setApiKey}({@link ApiKeys ApiKeys}.builder()
 *         .setFileLocation(path)
 *         .build())
 *     .build();
 *
 * ProjectsList projects = client.getProjects();
 *
 * System.out.println("My Projects: ");
 *
 * for (Project project : projects) {
 *     System.out.println(project);
 * }
 * </pre>
 *
 * @see <a href="http://docs.sevenbridges.com/docs/get-your-authentication-token">Get your
 * authentication token</a>
 * @see UserActions
 * @see DataStore
 */
public interface Client extends DataStore, UserActions {

  /**
   * Returns the internal {@link DataStore} of the client.  It is typically not necessary to invoke
   * this method as the Client implements the {@link DataStore} API and will delegate to this
   * instance automatically.
   *
   * @return the client's internal {@link DataStore}.
   */
  DataStore getDataStore();

  /**
   * Returns the sole {@link User} resource associated with this client.
   *
   * @return the sole {@link User} resource associated with this client.
   */
  User getCurrentUser();

  /**
   * Returns the {@link User} resource associated to this client, customized by the specified {@link
   * UserOptions}.
   *
   * @param userOptions The {@link UserOptions} to use to customize the retrieved User resource.
   * @return the {@link User} resource customized by the specified {@link UserOptions}
   */
  User getCurrentUser(UserOptions userOptions);

}
