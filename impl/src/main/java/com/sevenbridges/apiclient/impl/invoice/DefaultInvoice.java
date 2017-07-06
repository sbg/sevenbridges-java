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
package com.sevenbridges.apiclient.impl.invoice;

import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractResource;
import com.sevenbridges.apiclient.impl.resource.BooleanProperty;
import com.sevenbridges.apiclient.impl.resource.DateProperty;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.invoice.Invoice;
import com.sevenbridges.apiclient.price.Price;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class DefaultInvoice extends AbstractResource implements Invoice {

  private static String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  // SIMPLE PROPERTIES
  static final StringProperty ID = new StringProperty("id");
  static final BooleanProperty PENDING = new BooleanProperty("pending");
  static final DateProperty APPROVAL_DATE = new DateProperty("approval_date");
  static final MapProperty INVOICE_PERIOD = new MapProperty("invoice_period");
  static final MapProperty ANALYSIS_COSTS = new MapProperty("analysis_costs");
  static final MapProperty STORAGE_COSTS = new MapProperty("storage_costs");
  static final MapProperty TOTAL_COSTS = new MapProperty("total");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      ID, PENDING, APPROVAL_DATE, INVOICE_PERIOD, ANALYSIS_COSTS, STORAGE_COSTS, TOTAL_COSTS
  );

  public DefaultInvoice(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultInvoice(InternalDataStore dataStore, Map<String, Object> properties) {
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
  public String getId() {
    return getString(ID);
  }

  @Override
  public Boolean isPending() {
    return getBoolean(PENDING);
  }

  @Override
  public Date getApprovalDate() {
    return getDate(APPROVAL_DATE);
  }

  @Override
  public Price getAnalysisCosts() {
    return new InvoicePrice(ANALYSIS_COSTS);
  }

  @Override
  public Price getStorageCosts() {
    return new InvoicePrice(STORAGE_COSTS);
  }

  @Override
  public Price getTotalCosts() {
    return new InvoicePrice(TOTAL_COSTS);
  }

  @Override
  public Date getFrom() {
    String dateString = (String) getMapPropertyEntry(INVOICE_PERIOD, "from");
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
      dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
      return dateString == null ? null : dateFormat.parse(dateString);
    } catch (ParseException e) {
      return null;
    }
  }

  @Override
  public Date getTo() {
    String dateString = (String) getMapPropertyEntry(INVOICE_PERIOD, "to");
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
      dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
      return dateString == null ? null : dateFormat.parse(dateString);
    } catch (ParseException e) {
      return null;
    }
  }

  private class InvoicePrice implements Price {

    private final MapProperty price;

    private InvoicePrice(MapProperty price) {
      this.price = price;
    }


    @Override
    public String getCurrency() {
      return (String) DefaultInvoice.this.getMapPropertyEntry(price, "currency");
    }

    @Override
    public String getAmount() {
      return String.valueOf(DefaultInvoice.this.getMapPropertyEntry(price, "currency"));
    }

    @Override
    public String toString() {
      return getAmount() + getCurrency();
    }
  }
}
