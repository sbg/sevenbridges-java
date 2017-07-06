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
package com.sevenbridges.apiclient.impl.task;

import com.sevenbridges.apiclient.impl.query.DefaultCriteria;
import com.sevenbridges.apiclient.impl.query.EqualsExpression;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.task.Task;
import com.sevenbridges.apiclient.task.TaskCriteria;
import com.sevenbridges.apiclient.task.TaskOptions;
import com.sevenbridges.apiclient.task.TaskStatus;

public class DefaultTaskCriteria extends DefaultCriteria<TaskCriteria, TaskOptions> implements TaskCriteria {

  public DefaultTaskCriteria() {
    super(new DefaultTaskOptions());
  }

  public DefaultTaskCriteria(TaskOptions options) {
    super(options);
  }

  @Override
  public TaskCriteria forProject(String projectId) {
    Assert.notNull(projectId, "'Project Id' argument cannot be null.");
    return add(new EqualsExpression("project", projectId));
  }

  @Override
  public TaskCriteria forProject(Project project) {
    Assert.notNull(project, "'Project' argument cannot be null.");
    Assert.hasText(project.getId(), "'Project Id' property of a project must be set");
    return add(new EqualsExpression("project", project.getId()));
  }

  @Override
  public TaskCriteria withStatus(TaskStatus status) {
    Assert.notNull(status, "'Task status' argument cannot be null.");
    return add(new EqualsExpression("status", status.name()));
  }

  @Override
  public TaskCriteria withBatch(boolean batch) {
    return add(new EqualsExpression("batch", batch));
  }

  @Override
  public TaskCriteria withParentTask(String taskId) {
    Assert.notNull(taskId, "'Task Id' argument cannot be null.");
    return add(new EqualsExpression("parent", taskId));
  }

  @Override
  public TaskCriteria withParentTask(Task parentTask) {
    Assert.notNull(parentTask, "'Parent task' argument cannot be null.");
    Assert.hasText(parentTask.getId(), "'Task Id' property of a task must be set");
    return add(new EqualsExpression("parent", parentTask.getId()));
  }
}
