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

import com.sevenbridges.apiclient.invoice.Invoice;
import com.sevenbridges.apiclient.invoice.InvoiceList;

/**
 * The {@code BillingGroupActions} interface represents common user actions (behaviors) that can be
 * executed on a {@link BillingGroup} instance.
 */
interface BillingGroupActions {

  /**
   * Performs a breakdown action on the current billing group instance. This calls returns the
   * billing group's pricing breakdown, that is per project spending for the billing group. And each
   * project breakdown contains information about per task breakdown, that is pricing for each task
   * run in project.
   *
   * @return the {@link BillingGroupBreakdown} pricing breakdown
   */
  BillingGroupBreakdown breakdown();

  /**
   * This call returns a list of invoices for the current billing group instance, with information
   * about each invoice, including the billing period covered and whether or not the invoice is
   * pending.
   *
   * @return a paginated list of all of the {@link BillingGroup}'s {@link Invoice} resources
   */
  InvoiceList getInvoices();

}
