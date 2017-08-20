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

import com.sevenbridges.apiclient.price.Price;
import com.sevenbridges.apiclient.resource.Deletable;
import com.sevenbridges.apiclient.resource.Resource;
import com.sevenbridges.apiclient.resource.Saveable;
import com.sevenbridges.apiclient.resource.Updatable;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Resource representing a Task instance.
 */
public interface Task extends Resource, Updatable, Saveable, Deletable, TaskActions {

  /**
   * ID field, unique identifier of task on the Platform.
   *
   * @return String 'id' property of the current instance
   */
  String getId();

  /**
   * Name field, non unique name of the task.
   *
   * @return String 'name' property of the current instance
   */
  String getName();

  /**
   * Status field, current status of the task represented by the TaskStatus enum.
   *
   * @return String 'status; property of the current instance
   */
  TaskStatus getStatus();

  /**
   * Description field, optional text describing task.
   *
   * @return String 'description' property of the current instance
   */
  String getDescription();

  /**
   * ProjectId field, string unique identifier of a {@link com.sevenbridges.apiclient.project.Project}
   * in which this task is run.
   *
   * @return String 'projectId' property of the current instance
   */
  String getProjectId();

  /**
   * AppId field, string unique identifier of a {@link com.sevenbridges.apiclient.app.App} which
   * specifies the workflow for this task run.
   *
   * @return String 'appId' property of the current instance
   */
  String getAppId();

  /**
   * Type field, string type of the task.
   *
   * @return String 'type' property of the current instance
   */
  String getType();

  /**
   * Created by field, String unique username of the user that created the task.
   *
   * @return String 'createdBy' property of the current instance
   */
  String getCreatedBy();

  /**
   * Executed by field, String unique username of the user that started execution of the task. Can
   * differ from the 'createdBy' field if the task was created as a draft task and run after by
   * another user.
   *
   * @return String 'executedBy' property of the current instance
   */
  String getExecutedBy();

  /**
   * StartTime field, Date time of the task start.
   *
   * @return Date 'startTime' property of the current instance
   */
  Date getStartTime();

  /**
   * Batch field, boolean flag indicating if the task is a batch task.
   *
   * @return Boolean 'batch' property of the current instance
   */
  Boolean isBatch();

  /**
   * BatchBy field, information about a way that a task is batched.
   *
   * @return {@link BatchBy} 'batchBy' property of the current instance
   */
  BatchBy getBatchBy();

  /**
   * BatchGroup field, information about a batch group of a task if the task is batched.
   *
   * @return {@link BatchGroup} 'batchGroup' property of the current instance
   */
  BatchGroup getBatchGroup();

  /**
   * Price field, information about the current price of a task run.
   *
   * @return {@link Price} 'price' property of the current instance
   */
  Price getPrice();

  /**
   * Parent field, String unique identifier of the {@link Task} that is parent of the current task,
   * if any.
   *
   * @return String 'parent' property of the current instance
   */
  String getParent();

  /**
   * EndTime field, Date time of the task finish, if the task is finished.
   *
   * @return Date 'endTime' property of the current instance
   */
  Date getEndTime();

  /**
   * ExecutionStatus field, details of the execution of the current task.
   *
   * @return {@link ExecutionStatus} 'executionStatus' property of the current instance
   */
  ExecutionStatus getExecutionStatus();

  /**
   * Inputs field, map containing current inputs of a task. Keys of the map are input names, and
   * values differ in relation of the type of input (could be simple value type, or map, or list of
   * maps).
   *
   * @return Map 'inputs' property of the current instance
   */
  Map<String, Object> getInputs();

  /**
   * Output field, map containing information about current outputs of a task if any. Structure of
   * map differs in relation of the type of app.
   *
   * @return Map 'outputs' property of the current instance
   */
  Map<String, Object> getOutputs();

  /**
   * Errors field, list of errors found during the validation of the task. If there are any errors
   * found during the task validation, the task can not be run until all errors are fixed. Map
   * contains information about the type of error and hints how that error can be fixed.
   *
   * @return List 'errors' property of the current instance
   */
  List<Map<String, Object>> getErrors();

  /**
   * Warnings field, list of warnings found during the validation of the task, similar to the errors
   * field, but you can run tasks with warnings.  Map contains information about the type of warning
   * and hints how that warning can be fixed.
   *
   * @return List 'warnings' property of the current instance
   */
  List<Map<String, Object>> getWarnings();

  /**
   * CreatedTime field, Date time when the task was created.
   *
   * @return Date 'createdTime' property of the current instance
   */
  Date getCreatedTime();

}
