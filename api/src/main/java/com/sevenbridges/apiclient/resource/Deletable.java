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
package com.sevenbridges.apiclient.resource;

/**
 * Interface to be implemented by {@link Resource Resources} that allows the resource instance to be
 * completely deleted on call of {@link Deletable#delete()} function.
 */
public interface Deletable {

  /**
   * Permanently deletes current resource on the Platform if the current user have sufficient
   * permissions to perform such action. For example to delete a {@link
   * com.sevenbridges.apiclient.file.File} in a {@link com.sevenbridges.apiclient.project.Project},
   * user that is a member of a project must have write permissions in this project.
   */
  void delete();

}
