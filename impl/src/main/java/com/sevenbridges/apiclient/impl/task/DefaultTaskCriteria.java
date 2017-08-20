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

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.sevenbridges.apiclient.impl.query.DefaultCriteria;
import com.sevenbridges.apiclient.impl.query.EqualsExpression;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.task.Task;
import com.sevenbridges.apiclient.task.TaskCriteria;
import com.sevenbridges.apiclient.task.TaskOptions;
import com.sevenbridges.apiclient.task.TaskStatus;

import java.text.DateFormat;
import java.util.Date;

public class DefaultTaskCriteria extends DefaultCriteria<TaskCriteria, TaskOptions> implements TaskCriteria {

  private static final DateFormat DATE_FORMAT = new ISO8601DateFormat();


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

  @Override
  public TaskCriteria createdFrom(Date createdFrom) {
    Assert.notNull(createdFrom, "'Created from' argument cannot be null.");
    String date = DATE_FORMAT.format(createdFrom);
    return add(new EqualsExpression("created_from", date));
  }

  @Override
  public TaskCriteria createdTo(Date createdTo) {
    Assert.notNull(createdTo, "'Created to' argument cannot be null.");
    String date = DATE_FORMAT.format(createdTo);
    return add(new EqualsExpression("created_to", date));
  }

  @Override
  public TaskCriteria startedFrom(Date startedFrom) {
    Assert.notNull(startedFrom, "'Started from' argument cannot be null.");
    String date = DATE_FORMAT.format(startedFrom);
    return add(new EqualsExpression("started_from", date));
  }

  @Override
  public TaskCriteria startedTo(Date startedTo) {
    Assert.notNull(startedTo, "'Started to' argument cannot be null.");
    String date = DATE_FORMAT.format(startedTo);
    return add(new EqualsExpression("started_to", date));
  }

  @Override
  public TaskCriteria endedFrom(Date endedFrom) {
    Assert.notNull(endedFrom, "'Ended from' argument cannot be null");
    String date = DATE_FORMAT.format(endedFrom);
    return add(new EqualsExpression("ended_from", date));
  }

  @Override
  public TaskCriteria endedTo(Date endedTo) {
    Assert.notNull(endedTo, "'Ended to' argument cannot be null");
    String date = DATE_FORMAT.format(endedTo);
    return add(new EqualsExpression("ended_to", date));
  }
}
