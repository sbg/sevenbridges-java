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

import com.sevenbridges.apiclient.app.App;
import com.sevenbridges.apiclient.project.Project;

/**
 * The {@code TaskActions} interface represents common user actions (behaviors) that can be executed
 * on a {@link Task} instance.
 */
interface TaskActions {

  /**
   * Returns the project resource instance of the Platform project that the task is created in.
   *
   * @return {@link Project} of the current task instance
   */
  Project getProject();

  /**
   * Returns the app resource instance of the Platform app that the task is running.
   *
   * @return {@link App} of the current task instance
   */
  App getApp();

  /**
   * Runs the task if the task is in a DRAFT state {@link TaskStatus}.
   *
   * @return current {@link Task} instance
   */
  Task run();

  /**
   * Aborts a task if the task is in a RUNNING state {@link TaskStatus}.
   *
   * @return current {@link Task} instance
   */
  Task abort();

  /**
   * Returns full details of the current task execution.
   *
   * @return {@link ExecutionDetails} of the current Task instance
   */
  ExecutionDetails getExecutionDetails();
}
