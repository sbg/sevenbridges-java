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

import com.sevenbridges.apiclient.resource.Resource;

import java.util.Date;
import java.util.List;

/**
 * Resource representing execution details of the specific task. Information breaks down into the
 * task's distinct jobs.
 * <p>
 * A job is a single subprocess carried out in a task. The information returned by this call is
 * broadly similar to that which can be found in the task stats and logs provided on the Platform.
 */
public interface ExecutionDetails extends Resource {

  /**
   * Start time of the whole task.
   *
   * @return Date start time
   */
  Date getStartTime();

  /**
   * End time of the whole task.
   *
   * @return Date end time
   */
  Date getEndTime();

  /**
   * Current status of the whole task.
   *
   * @return String task status
   */
  String getStatus();

  /**
   * String message with further information about current task execution.
   *
   * @return String message
   */
  String getMessage();

  /**
   * List of per job execution details {@link ExecutionJobDetails}.
   *
   * @return List of execution job details
   */
  List<ExecutionJobDetails> getJobs();
}
