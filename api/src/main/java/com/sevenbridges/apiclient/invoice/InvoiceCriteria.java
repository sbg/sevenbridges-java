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
package com.sevenbridges.apiclient.invoice;

import com.sevenbridges.apiclient.billing.BillingGroup;
import com.sevenbridges.apiclient.query.Criteria;

/**
 * An {@link Invoice}-specific {@link Criteria} class which enables a Invoice-specific <a
 * href="http://en.wikipedia.org/wiki/Fluent_interface">fluent</a> query DSL.
 */
public interface InvoiceCriteria extends Criteria<InvoiceCriteria>, InvoiceOptions<InvoiceCriteria> {

  /**
   * Adds a specific criterion to the current {@link InvoiceCriteria} instance. Only {@link
   * Invoice}s that are associated with the {@link BillingGroup} specified with billing group ID can
   * meet this criteria.
   *
   * @param billingGroupId String ID of a billing group
   * @return InvoiceCriteria with added specified criterion
   */
  InvoiceCriteria forBillingGroup(String billingGroupId);

  /**
   * Adds a specific criterion to the current {@link InvoiceCriteria} instance. Only {@link
   * Invoice}s that are associated with the specified {@link BillingGroup} can meet this criteria.
   *
   * @param billingGroup billing group
   * @return InvoiceCriteria with added specified criterion
   */
  InvoiceCriteria forBillingGroup(BillingGroup billingGroup);

}
