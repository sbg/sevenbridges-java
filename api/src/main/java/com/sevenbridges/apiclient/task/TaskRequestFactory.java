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
package com.sevenbridges.apiclient.task;

/**
 * Factory providing builders for the tasks. Builders {@link CreateTaskRequestBuilder} are used for
 * creating new task instances.
 * <p>
 * You can get TaskRequestFactory instance with {@link com.sevenbridges.apiclient.user.UserActions#getTaskRequestFactory()}
 * call.
 */
public interface TaskRequestFactory<TRB extends CreateTaskRequestBuilder> {

  /**
   * Creates a new {@link CreateTaskRequestBuilder} that can be used for creating new tasks.
   *
   * @return new CreateTaskRequestBuilder instance
   */
  TRB createTaskBuilder();

}
