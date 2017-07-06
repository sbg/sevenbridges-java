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
package com.sevenbridges.apiclient.billing;

import com.sevenbridges.apiclient.price.Price;
import com.sevenbridges.apiclient.resource.Resource;

import java.util.Date;

/**
 * Price breakdown by task in a specific {@link BillingGroupBreakdown}.
 */
public interface TaskBreakdown extends Resource {

  /**
   * Username of the project member who executed the task.
   *
   * @return String username of a Platform user
   */
  String getRunnerUsername();

  /**
   * Start time of the task.
   *
   * @return Date start time
   */
  Date getTimeStarted();

  /**
   * End time of the task.
   *
   * @return Date end time
   */
  Date getTimeFinished();

  /**
   * Total price of the current task.
   *
   * @return a {@link Price} that represents spending for the task
   */
  Price getTaskCost();

}
