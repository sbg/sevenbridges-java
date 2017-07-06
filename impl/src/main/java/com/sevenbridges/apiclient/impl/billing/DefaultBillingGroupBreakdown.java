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

import com.sevenbridges.apiclient.billing.BillingGroupBreakdown;
import com.sevenbridges.apiclient.billing.ProjectBreakdown;
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractResource;
import com.sevenbridges.apiclient.impl.resource.ListProperty;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.price.Price;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultBillingGroupBreakdown extends AbstractResource implements BillingGroupBreakdown {

  //SIMPLE PROPERTIES
  static final MapProperty TOTAL_SPENDING = new MapProperty("total_spending");
  static final ListProperty PROJECT_BREAKDOWN = new ListProperty("project_breakdown");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      TOTAL_SPENDING, PROJECT_BREAKDOWN
  );

  public DefaultBillingGroupBreakdown(InternalDataStore dataStore, Map<String, Object> properties) {
    super(dataStore, properties);
    //Maybe i should here iterate through map and create resource object from given Map<String, Object> data
  }

  public DefaultBillingGroupBreakdown(InternalDataStore dataStore) {
    super(dataStore);
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
  public Price getTotalSpending() {
    return new TotalSpending(TOTAL_SPENDING);
  }

  @Override
  public List<ProjectBreakdown> getProjectsBreakdown() {
    List<Map<String, Object>> projectBreakdownProps = getList(PROJECT_BREAKDOWN);
    List<ProjectBreakdown> projectBreakdownList = new ArrayList<>(projectBreakdownProps.size());
    for (Map<String, Object> prop : projectBreakdownProps) {
      projectBreakdownList.add(getDataStore().instantiate(ProjectBreakdown.class, prop));
    }
    return projectBreakdownList;
  }

  class TotalSpending implements Price {

    private static final String CURRENCY = "currency";
    private static final String AMOUNT = "amount";

    private final MapProperty totalSpending;

    private TotalSpending(MapProperty totalSpending) {
      this.totalSpending = totalSpending;
    }

    @Override
    public String getCurrency() {
      return (String) DefaultBillingGroupBreakdown.this.getMapPropertyEntry(totalSpending, CURRENCY);
    }

    @Override
    public String getAmount() {
      return String.valueOf(DefaultBillingGroupBreakdown.this.getMapPropertyEntry(totalSpending, AMOUNT));
    }

    @Override
    public String toString() {
      return getAmount() + getCurrency();
    }
  }

}
