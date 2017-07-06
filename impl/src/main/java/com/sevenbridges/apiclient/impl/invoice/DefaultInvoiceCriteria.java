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

import com.sevenbridges.apiclient.billing.BillingGroup;
import com.sevenbridges.apiclient.impl.query.DefaultCriteria;
import com.sevenbridges.apiclient.impl.query.EqualsExpression;
import com.sevenbridges.apiclient.invoice.InvoiceCriteria;
import com.sevenbridges.apiclient.invoice.InvoiceOptions;
import com.sevenbridges.apiclient.lang.Assert;

public class DefaultInvoiceCriteria extends DefaultCriteria<InvoiceCriteria, InvoiceOptions> implements InvoiceCriteria {

  public DefaultInvoiceCriteria() {
    super(new DefaultInvoiceOptions());
  }

  public DefaultInvoiceCriteria(InvoiceOptions options) {
    super(options);
  }

  @Override
  public InvoiceCriteria forBillingGroup(String billingGroupId) {
    Assert.notNull(billingGroupId, "BillingGroup argument cannot be null.");
    return add(new EqualsExpression("billing_group", billingGroupId));
  }

  @Override
  public InvoiceCriteria forBillingGroup(BillingGroup billingGroup) {
    Assert.notNull(billingGroup, "BillingGroup argument cannot be null.");
    return add(new EqualsExpression("billing_group", billingGroup.getId()));
  }

}
