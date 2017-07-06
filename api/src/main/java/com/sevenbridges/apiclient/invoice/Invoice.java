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

import com.sevenbridges.apiclient.price.Price;
import com.sevenbridges.apiclient.resource.Resource;

import java.util.Date;

/**
 * Resource representing an Invoice instance.
 */
public interface Invoice extends Resource {

  /**
   * ID field, unique identifier of an invoice on the Platform.
   *
   * @return String 'id' property of the current instance
   */
  String getId();

  /**
   * Pending field, the boolean flag that indicates the current invoice instance is pending.
   *
   * @return Boolean 'pending' property of the current instance
   */
  Boolean isPending();

  /**
   * Approval date field, the time of the approval of the current invoice instance.
   *
   * @return Date 'approvalDate' property of the current instance
   */
  Date getApprovalDate();

  /**
   * AnalysisCosts price field, the cost of running tasks associated with the current invoice
   * instance.
   *
   * @return Price 'analysisCosts' property of the current instance
   */
  Price getAnalysisCosts();

  /**
   * StorageCosts price field, the cost of file storage associated with the current invoice
   * instance.
   *
   * @return Price 'storageCosts' property of the current instance
   */
  Price getStorageCosts();

  /**
   * TotalCosts price field, the summed cost of analysis and storage costs associated with the
   * current invoice instance.
   *
   * @return Price 'totalCosts' property of the current instance
   */
  Price getTotalCosts();

  /**
   * From date field, the start of the current invoice period.
   *
   * @return Date 'from' property of the current instance
   */
  Date getFrom();

  /**
   * To date field, the end of the current invoice period.
   *
   * @return Date 'to' property of the current instance
   */
  Date getTo();

}
