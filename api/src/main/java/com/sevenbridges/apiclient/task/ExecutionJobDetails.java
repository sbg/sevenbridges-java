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

import java.util.Date;
import java.util.Map;

/**
 * Information about the particular job execution, a part of a task execution.
 */
public interface ExecutionJobDetails {

  /**
   * String name of the command line job that executed.
   *
   * @return String name
   */
  String getName();

  /**
   * Date start time of the job.
   *
   * @return Date start time
   */
  Date getStartTime();

  /**
   * Date end time of the task, if it is finished.
   *
   * @return Date end time
   */
  Date getEndTime();

  /**
   * String command line that started this job.
   *
   * @return String that started it all
   */
  String getCommandLine();

  /**
   * Current status of this job instance.
   *
   * @return String status
   */
  String getStatus();

  /**
   * Boolean retried flag is true if task retried for some reason.
   *
   * @return Boolean retried status.
   */
  Boolean getRetried();

  /**
   * String type of instance used for running this job.
   *
   * @return String instance type
   */
  String getInstanceType();

  /**
   * String instance id, identifier of the instance used for running this job.
   *
   * @return String instance id
   */
  String getInstanceId();

  /**
   * String instance cloud provider name that provided instance used for running this job.
   *
   * @return String instance provider
   */
  String getInstanceProvider();

  /**
   * Integer instance attached disk size.
   *
   * @return Integer instance disk size
   */
  Integer getInstanceDiskSize();

  /**
   * String instance attached disk measure unit.
   *
   * @return String instance disk measure unit
   */
  String getInstanceDiskUnit();

  /**
   * String instance attached disk type.
   *
   * @return String instance attached disk type.
   */
  String getInstanceDiskType();

  /**
   * SHA hash checksum of the Docker image.
   *
   * @return String checksum
   */
  String getDockerChecksum();

  /**
   * Map containing links on the Platform that can be used to download the standard error logs for
   * the job.
   *
   * @return Map of log links
   */
  Map<String, String> getLogs();
}
