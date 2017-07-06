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

import java.util.List;

/**
 * Price breakdown by project in a specific {@link BillingGroupBreakdown}.
 */
public interface ProjectBreakdown extends Resource {

  /**
   * Spending for a single project in the billing group breakdown, see {@link
   * BillingGroupActions#breakdown()}.
   *
   * @return a {@link Price} that represents spending for the project
   */
  Price getAnalysisSpending();

  /**
   * Per task breakdown of the pricing for the current project.
   *
   * @return a list of {@link TaskBreakdown} for the current project
   */
  List<TaskBreakdown> getTasksBreakdown();

}
