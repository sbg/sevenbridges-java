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
 * Detailed execution status of a task.
 */
public interface ExecutionStatus {

  /**
   * Integer number of finished jobs inside this task.
   *
   * @return Integer steps completed
   */
  Integer getStepsCompleted();

  /**
   * Integer total number of steps that needs to finish inside this task.
   *
   * @return Integer steps total
   */
  Integer getStepsTotal();

  /**
   * String message information about task execution.
   *
   * @return String message
   */
  String getMessage();

  /**
   * Integer number of queued jobs.
   *
   * @return int queued
   */
  Integer getQueued();

  /**
   * Integer number of running jobs.
   *
   * @return int running
   */
  Integer getRunning();

  /**
   * Integer number of completed jobs.
   *
   * @return int completed
   */
  Integer getCompleted();

  /**
   * Integer number of failed jobs.
   *
   * @return int failed
   */
  Integer getFailed();

  /**
   * Integer number of failed jobs.
   *
   * @return int aborted
   */
  Integer getAborted();

  /**
   * Boolean indicating whether the platform instance limit has bean reached.
   *
   * @return Boolean system limit
   */
  Boolean getSystemLimit();

  /**
   * Boolean indicating whether the account instance limit has bean reached.
   *
   * @return Boolean account limit
   */
  Boolean getAccountLimit();

  /**
   * Boolean indicating whether the task is in initializing state.
   *
   * @return Boolean instance init
   */
  Boolean getInstanceInit();

  /**
   * Long amount of time that task has spent in the Queued state.
   *
   * @return Long queued duration
   */
  Integer getQueuedDuration();

  /**
   * Long amount of time that task has spent in the Running state.
   *
   * @return Long running duration
   */
  Integer getRunningDuration();

  /**
   * Long amount of current execution time.
   *
   * @return Long current execution duration.
   */
  Integer getExecutionDuration();

  /**
   * Long total duration.
   *
   * @return Long total task duration.
   */
  Integer getDuration();

}
