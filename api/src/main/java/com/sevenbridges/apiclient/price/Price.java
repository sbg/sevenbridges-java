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
package com.sevenbridges.apiclient.price;

/**
 * Information about pricing on various resources (example: {@link com.sevenbridges.apiclient.billing.BillingGroupBreakdown}).
 */
public interface Price {

  /**
   * Type of currency that is used in this pricing instance.
   *
   * @return String currency
   */
  String getCurrency();

  /**
   * Amount of currency that is used in this pricing instance.
   *
   * @return String amount
   */
  String getAmount();

}
