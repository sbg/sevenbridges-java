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

import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.query.Criteria;

/**
 * An {@link Task}-specific {@link Criteria} class which enables a Task-specific <a
 * href="http://en.wikipedia.org/wiki/Fluent_interface">fluent</a> query DSL.
 */
public interface TaskCriteria extends Criteria<TaskCriteria>, TaskOptions<TaskCriteria> {

  /**
   * Adds a specific criterion to the current {@link TaskCriteria} instance. Only {@link Task}s that
   * are included in the {@link Project} specified with project ID can meet this criteria.
   *
   * @param projectId String ID of a project
   * @return TaskCriteria with added specified criterion
   */
  TaskCriteria forProject(String projectId);

  /**
   * Adds a specific criterion to the current {@link TaskCriteria} instance. Only {@link Task}s that
   * are included in the specified {@link Project} can meet this criteria.
   *
   * @param project Project instance
   * @return TaskCriteria with added specified criterion
   */
  TaskCriteria forProject(Project project);

  /**
   * Adds a specific criterion to the current {@link TaskCriteria} instance. Only {@link Task}s in
   * the specified {@link TaskStatus} can meet this criteria.
   *
   * @param status TaskStatus
   * @return TaskCriteria with added specified criterion
   */
  TaskCriteria withStatus(TaskStatus status);

  /**
   * Adds a specific criterion to the current {@link TaskCriteria} instance. Only {@link Task}s with
   * batch property matching the specified batch parameter can meet this criteria.
   *
   * @param batch boolean flag
   * @return TaskCriteria with added specified criterion
   */
  TaskCriteria withBatch(boolean batch);

  /**
   * Adds a specific criterion to the current {@link TaskCriteria} instance. Only {@link Task}s with
   * parent task that match the provided task ID can meet this criteria.
   *
   * @param taskId String unique task identifier
   * @return TaskCriteria with added specified criterion
   */
  TaskCriteria withParentTask(String taskId);

  /**
   * Adds a specific criterion to the current {@link TaskCriteria} instance. Only {@link Task}s with
   * parent task that match the provided task can meet this criteria.
   *
   * @param parentTask Task resource instance
   * @return TaskCriteria with added specified criterion
   */
  TaskCriteria withParentTask(Task parentTask);

}
