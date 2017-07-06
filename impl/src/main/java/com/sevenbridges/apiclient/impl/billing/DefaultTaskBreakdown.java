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
package com.sevenbridges.apiclient.impl.billing;

import com.sevenbridges.apiclient.billing.TaskBreakdown;
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractResource;
import com.sevenbridges.apiclient.impl.resource.DateProperty;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.price.Price;

import java.util.Date;
import java.util.Map;

public class DefaultTaskBreakdown extends AbstractResource implements TaskBreakdown {

  // simple properties
  static final StringProperty RUNNER_USERNAME = new StringProperty("runner_username");
  static final DateProperty TIME_STARTED = new DateProperty("time_started");
  static final DateProperty TIME_FINISHED = new DateProperty("time_finished");
  static final MapProperty TASK_COST = new MapProperty("task_cost");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      RUNNER_USERNAME, TIME_STARTED, TIME_FINISHED, TASK_COST
  );

  public DefaultTaskBreakdown(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultTaskBreakdown(InternalDataStore dataStore, Map<String, Object> properties) {
    super(dataStore, properties);
  }

  @Override
  public Map<String, Property> getPropertyDescriptors() {
    return PROPERTY_DESCRIPTORS;
  }

  @Override
  public int getPropertiesCount() {
    return PROPERTY_DESCRIPTORS.size();
  }

  @Override
  public String getRunnerUsername() {
    return getString(RUNNER_USERNAME);
  }

  @Override
  public Date getTimeStarted() {
    return getDate(TIME_STARTED);
  }

  @Override
  public Date getTimeFinished() {
    return getDate(TIME_FINISHED);
  }

  @Override
  public Price getTaskCost() {
    return new TaskCost(TASK_COST);
  }

  private class TaskCost implements Price {

    private static final String CURRENCY = "currency";
    private static final String AMOUNT = "amount";
    private final MapProperty taskCost;

    private TaskCost(MapProperty taskCost) {
      this.taskCost = taskCost;
    }

    @Override
    public String getCurrency() {
      return (String) DefaultTaskBreakdown.this.getMapPropertyEntry(taskCost, CURRENCY);
    }

    @Override
    public String getAmount() {
      return String.valueOf(DefaultTaskBreakdown.this.getMapPropertyEntry(taskCost, AMOUNT));
    }

    @Override
    public String toString() {
      return getAmount() + getCurrency();
    }
  }

}
