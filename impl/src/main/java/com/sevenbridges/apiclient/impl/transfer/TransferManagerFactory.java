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
package com.sevenbridges.apiclient.impl.transfer;

import com.sevenbridges.apiclient.impl.transfer.http.HttpAsyncClientPool;

public class TransferManagerFactory {

  private static TransferManager TRANSFER_MANAGER = null;
  private static TransferManagerConfiguration CONF = null;

  public static TransferManager getTransferManager() {
    if (TRANSFER_MANAGER == null) {
      if (CONF != null) {
        TRANSFER_MANAGER = new TransferManager(HttpAsyncClientPool.getClient(), CONF);
      } else {
        TRANSFER_MANAGER = new TransferManager(HttpAsyncClientPool.getClient());
      }
    }
    return TRANSFER_MANAGER;
  }

  public static void stopTransferManager() {
    if (TRANSFER_MANAGER != null) {
      TRANSFER_MANAGER.stopService();
      TRANSFER_MANAGER = null;
    }
  }

  public static void setConfiguration(TransferManagerConfiguration configuration) {
    TransferManagerFactory.CONF = configuration;
  }
}
