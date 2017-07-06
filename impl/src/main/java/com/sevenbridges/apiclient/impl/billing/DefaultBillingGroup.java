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

import com.sevenbridges.apiclient.billing.BillingGroup;
import com.sevenbridges.apiclient.billing.BillingGroupBreakdown;
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractInstanceResource;
import com.sevenbridges.apiclient.impl.resource.BooleanProperty;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.invoice.InvoiceCriteria;
import com.sevenbridges.apiclient.invoice.InvoiceList;
import com.sevenbridges.apiclient.invoice.Invoices;
import com.sevenbridges.apiclient.price.Price;
import com.sevenbridges.apiclient.query.Criteria;

import java.util.Map;

public class DefaultBillingGroup extends AbstractInstanceResource implements BillingGroup {

  // SIMPLE PROPERTIES:
  static final StringProperty ID = new StringProperty("id");
  static final StringProperty OWNER = new StringProperty("owner");
  static final StringProperty NAME = new StringProperty("name");
  static final StringProperty TYPE = new StringProperty("type");
  static final BooleanProperty PENDING = new BooleanProperty("pending");
  static final BooleanProperty DISABLED = new BooleanProperty("disabled");
  static final MapProperty BALANCE = new MapProperty("balance");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      ID, OWNER, NAME, TYPE, PENDING, DISABLED, BALANCE
  );

  private static final int PROPERTIES_COUNT = PROPERTY_DESCRIPTORS.size();

  static final int H_INVOICES = 0;

  static final String[] HREF_REFERENCES = new String[]{
      "/billing/invoices"
  };

  public DefaultBillingGroup(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultBillingGroup(InternalDataStore dataStore, Map<String, Object> properties) {
    super(dataStore, properties);
  }

  @Override
  public Map<String, Property> getPropertyDescriptors() {
    return PROPERTY_DESCRIPTORS;
  }

  @Override
  public int getPropertiesCount() {
    return PROPERTIES_COUNT;
  }

  @Override
  public void delete() {
    getDataStore().delete(this);
  }

  ////////////////////////////////////////////////////////////////////////
  // SIMPLE PROPERTIES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public String getId() {
    return getString(ID);
  }

  @Override
  public String getOwner() {
    return getString(OWNER);
  }

  @Override
  public String getName() {
    return getString(NAME);
  }

  @Override
  public String getType() {
    return getString(TYPE);
  }

  @Override
  public boolean isPending() {
    return getBoolean(PENDING);
  }

  @Override
  public boolean isDisabled() {
    return getBoolean(DISABLED);
  }

  @Override
  public BillingGroup setDisabled(boolean disabled) {
    setProperty(DISABLED, disabled);
    return this;
  }

  @Override
  public Price getBalance() {
    return new BillingBalance(BALANCE);
  }

  @Override
  public BillingGroupBreakdown breakdown() {
    return getDataStore().getResource(getHref() + "/breakdown", BillingGroupBreakdown.class);
  }

  @Override
  public InvoiceList getInvoices() {
    return getDataStore().getResource(HREF_REFERENCES[H_INVOICES], InvoiceList.class,
        (Criteria<InvoiceCriteria>) Invoices.criteria().forBillingGroup(getId()));
  }

  private class BillingBalance implements Price {

    private final MapProperty balance;

    private BillingBalance(MapProperty balance) {
      this.balance = balance;
    }

    @Override
    public String getCurrency() {
      return (String) getMapPropertyEntry(balance, "currency");
    }

    @Override
    public String getAmount() {
      return String.valueOf(getMapPropertyEntry(balance, "amount"));
    }

    @Override
    public String toString() {
      return getAmount() + getCurrency();
    }
  }

}
