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

import com.sevenbridges.apiclient.billing.ProjectBreakdown;
import com.sevenbridges.apiclient.billing.TaskBreakdown;
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractResource;
import com.sevenbridges.apiclient.impl.resource.ListProperty;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.price.Price;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultProjectBreakdown extends AbstractResource implements ProjectBreakdown {

  // simple properties
  static final MapProperty ANALYSIS_SPENDING = new MapProperty("analysis_spending");
  static final ListProperty TASK_BREAKDOWN = new ListProperty("task_breakdown");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      ANALYSIS_SPENDING, TASK_BREAKDOWN
  );

  public DefaultProjectBreakdown(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultProjectBreakdown(InternalDataStore dataStore, Map<String, Object> properties) {
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
  public Price getAnalysisSpending() {
    return new AnalysisSpending(ANALYSIS_SPENDING);
  }

  @Override
  public List<TaskBreakdown> getTasksBreakdown() {
    List<Map<String, Object>> taskBreakdownProps = getList(TASK_BREAKDOWN);
    List<TaskBreakdown> taskBreakdownList = new ArrayList<>(taskBreakdownProps.size());
    for (Map<String, Object> prop : taskBreakdownProps) {
      taskBreakdownList.add(getDataStore().instantiate(TaskBreakdown.class, prop));
    }
    return taskBreakdownList;
  }

  class AnalysisSpending implements Price {

    public static final String CURRENCY = "currency";
    public static final String AMOUNT = "amount";
    private final MapProperty analysisSpending;

    private AnalysisSpending(MapProperty analysisSpending) {
      this.analysisSpending = analysisSpending;
    }

    @Override
    public String getCurrency() {
      return (String) DefaultProjectBreakdown.this.getMapPropertyEntry(analysisSpending, CURRENCY);
    }

    @Override
    public String getAmount() {
      return String.valueOf(DefaultProjectBreakdown.this.getMapPropertyEntry(analysisSpending, AMOUNT));
    }

    @Override
    public String toString() {
      return getAmount() + getCurrency();
    }
  }

}
