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
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.ContentEncoderChannel;
import org.apache.http.nio.FileContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;

abstract class BaseZeroCopyChannelRangeRequestProducer implements HttpAsyncRequestProducer {

  private final URI requestURI;
  private final FileChannel fileChannel;
  private final long position;
  private final long count;
  private final ContentType contentType;
  private final StripedLongAdder bytesTransferredAdder;
  private final int partNumber;

  private long idx = -1;
  private long cnt = -1;
  private long transferredCnt = -1;

  BaseZeroCopyChannelRangeRequestProducer(
      final URI requestURI,
      final FileChannel fileChannel,
      final long position,
      final long count,
      final ContentType contentType,
      final StripedLongAdder bytesTransferredAdder,
      final int partNumber) {
    super();
    Args.notNull(requestURI, "Request URI");
    Args.notNull(fileChannel, "File channel");
    Args.notNegative(position, "Position");
    Args.notNegative(count, "Count");
    Args.notNegative(partNumber, "Part number");
    Args.notNull(bytesTransferredAdder, "Bytes transferred adder");
    this.partNumber = partNumber;
    this.bytesTransferredAdder = bytesTransferredAdder;
    this.contentType = contentType;
    this.count = count;
    this.position = position;
    this.fileChannel = fileChannel;
    this.requestURI = requestURI;
  }

  protected abstract HttpEntityEnclosingRequest createRequest(final URI requestURI, final HttpEntity entity);

  public HttpRequest generateRequest() throws IOException, HttpException {
    final BasicHttpEntity entity = new BasicHttpEntity();
    entity.setChunked(false);
    entity.setContentLength(this.count);
    if (this.contentType != null) {
      entity.setContentType(this.contentType.toString());
    }
    this.idx = position;
    this.cnt = count;
    this.transferredCnt = 0;
    return createRequest(this.requestURI, entity);
  }

  public synchronized HttpHost getTarget() {
    return URIUtils.extractHost(this.requestURI);
  }

  @Override
  public void produceContent(
      final ContentEncoder encoder, final IOControl ioctrl) throws IOException {
    final long transferred;
    if (encoder instanceof FileContentEncoder) {
      transferred = ((FileContentEncoder) encoder).transfer(
          this.fileChannel, this.idx, this.cnt);
    } else {
      transferred = this.fileChannel.transferTo(
          this.idx, this.cnt, new ContentEncoderChannel(encoder));
    }
    if (transferred > 0) {
      this.idx += transferred;
      this.cnt -= transferred;
      this.transferredCnt += transferred;
      bytesTransferredAdder.set(this.partNumber, this.transferredCnt);
    }

    if (this.cnt == 0) {
      encoder.complete();
    }
    if (this.idx >= this.fileChannel.size()) {
      encoder.complete();
    }
  }

  public void requestCompleted(final HttpContext context) {
  }

  public void failed(final Exception ex) {
  }

  public boolean isRepeatable() {
    return true;
  }

  public synchronized void resetRequest() throws IOException {
  }

  public synchronized void close() throws IOException {
    LoggerFactory.getLogger(BaseZeroCopyChannelRangeRequestProducer.class).debug("RequestProducer closing");
  }
}
