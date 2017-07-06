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

import com.sevenbridges.apiclient.impl.transfer.model.UploadPartContext;
import com.sevenbridges.apiclient.upload.UploadedPart;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

class PartsProducer {

  private final LinkedBlockingQueue<UploadPartContext> newPartsBuffer;
  private final LinkedBlockingQueue<UploadPartContext> finishedPartsBuffer;

  private volatile boolean failed = false;
  private volatile UploadPartContext failedPart = null;

  PartsProducer(int numberOfParts) {
    this(numberOfParts, Collections.<UploadedPart>emptyList());
  }

  PartsProducer(int numberOfParts, List<UploadedPart> finishedParts) {
    if (finishedParts == null) {
      finishedParts = Collections.emptyList();
    }
    int numberOfUnfinishedParts = numberOfParts - finishedParts.size();

    this.newPartsBuffer = new LinkedBlockingQueue<>(numberOfUnfinishedParts);
    this.finishedPartsBuffer = new LinkedBlockingQueue<>(numberOfUnfinishedParts);

    Set<Integer> finishedPartsSet = new HashSet<>(finishedParts.size());
    for (UploadedPart finishedPart : finishedParts) {
      finishedPartsSet.add(finishedPart.getPartNumber());
    }

    for (int i = 1; i <= numberOfParts; i++) {
      if (!finishedPartsSet.contains(i)) {
        this.putNew(UploadPartContext.buildNewContext(i));
      }
    }
  }

  UploadPartContext takeNewOrFinished() throws InterruptedException {
    if (isUploadFailed()) {
      return failedPart;
    }
    UploadPartContext newContext = newPartsBuffer.poll();
    if (newContext != null) {
      return newContext;
    } else {
      return takeFinished();
    }
  }

  UploadPartContext takeFinished() throws InterruptedException {
    if (isUploadFailed()) {
      return failedPart;
    }
    return finishedPartsBuffer.take();
  }

  List<UploadPartContext> flushFinishedAndGetSuccessful() throws InterruptedException {
    List<UploadPartContext> finished = new LinkedList<>();
    finishedPartsBuffer.drainTo(finished);
    Iterator<UploadPartContext> iterator = finished.iterator();
    while (iterator.hasNext()) {
      UploadPartContext next = iterator.next();
      if (next.isFailed()) {
        iterator.remove();
      }
    }
    return finished;
  }

  boolean isUploadFailed() {
    return failed;
  }

  boolean putNew(UploadPartContext partContext) {
    if (partContext.isNew()) {
      try {
        this.newPartsBuffer.put(partContext);
        return true;
      } catch (InterruptedException e) {
        // should never happen, buffer is big enough
        return false;
      }
    }
    return false;
  }

  boolean putFinished(UploadPartContext partContext) {
    if (partContext.isFailedBeyondRetry()) {
      if (!failed) {
        failed = true;
        failedPart = partContext;
      }
      return true;
    } else if (partContext.isFailed() || partContext.isSucceeded()) {
      try {
        this.finishedPartsBuffer.put(partContext);
        return true;
      } catch (InterruptedException e) {
        // should never happen, buffer is big enough
        return false;
      }
    }
    return false;
  }


}
