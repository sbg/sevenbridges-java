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

import com.sevenbridges.apiclient.lang.Classes;

/**
 * Static utility/helper methods for working with {@link Invoice} resources.
 * <p>
 * Most methods are <a href="http://en.wikipedia.org/wiki/Factory_method_pattern">factory
 * method</a>s used for forming Application-specific <a href="http://en.wikipedia.org/wiki/Fluent_interface">fluent
 * DSL</a> queries.
 */
public final class Invoices {

  //prevent instantiation
  private Invoices() {
  }

  /**
   * Returns a new {@link InvoiceOptions} instance, used to customize how one or more {@link
   * Invoice}s are retrieved.
   *
   * @return a new {@link InvoiceOptions} instance, used to customize how one or more {@link
   * Invoice}s are retrieved.
   */
  public static InvoiceOptions options() {
    return (InvoiceOptions) Classes.newInstance("com.sevenbridges.apiclient.impl.invoice.DefaultInvoiceOptions");
  }

  /**
   * Returns a new {@link InvoiceCriteria} instance, used to customize how one or more {@link
   * Invoice}s are retrieved.
   *
   * @return a new {@link InvoiceCriteria} instance, used to customize how one or more {@link
   * Invoice}s are retrieved.
   */
  public static InvoiceCriteria criteria() {
    return (InvoiceCriteria) Classes.newInstance("com.sevenbridges.apiclient.impl.invoice.DefaultInvoiceCriteria");
  }

}
