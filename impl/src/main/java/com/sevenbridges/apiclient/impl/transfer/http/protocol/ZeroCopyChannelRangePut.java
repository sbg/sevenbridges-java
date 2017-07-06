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
package com.sevenbridges.apiclient.impl.transfer.http.protocol;

import com.sevenbridges.apiclient.impl.transfer.atomic.StripedLongAdder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;

import java.net.URI;
import java.nio.channels.FileChannel;

public class ZeroCopyChannelRangePut extends BaseZeroCopyChannelRangeRequestProducer {

  public ZeroCopyChannelRangePut(final URI requestURI,
                                 final FileChannel fileChannel,
                                 final long position,
                                 final long count,
                                 final ContentType contentType,
                                 final StripedLongAdder bytesTransferredAdder,
                                 final int partNumber) {
    super(requestURI, fileChannel, position, count, contentType, bytesTransferredAdder, partNumber);
  }

  public ZeroCopyChannelRangePut(final String requestURI,
                                 final FileChannel fileChannel,
                                 final long position,
                                 final long count,
                                 final ContentType contentType,
                                 final StripedLongAdder bytesTransferredAdder,
                                 final int partNumber) {
    super(URI.create(requestURI), fileChannel, position, count, contentType, bytesTransferredAdder, partNumber);
  }

  @Override
  protected HttpEntityEnclosingRequest createRequest(final URI requestURI, final HttpEntity entity) {
    final HttpPut httpput = new HttpPut(requestURI);
    httpput.setEntity(entity);
    return httpput;
  }
}
