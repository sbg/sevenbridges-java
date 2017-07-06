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
package com.sevenbridges.apiclient.volume;

import com.sevenbridges.apiclient.lang.Assert;

/**
 * State of the volume job (import or export).
 */
public enum VolumeJobState {

  /** The volume job is in a pending state. */
  PENDING(false),

  /** The volume job is currently running. */
  RUNNING(false),

  /** The volume job has successfully completed. */
  COMPLETED(true),

  /** The volume job has failed. */
  FAILED(true);

  private final boolean finished;

  VolumeJobState(boolean finished) {
    this.finished = finished;
  }

  /**
   * @return {@code true} if the volume job is finished.
   */
  public final boolean isFinished() {
    return finished;
  }

  public static VolumeJobState fromValue(String value) {
    Assert.hasText(value, "Value cannot be null or empty!");
    switch (value.toUpperCase()) {
      case "PENDING": {
        return PENDING;
      }
      case "RUNNING": {
        return RUNNING;
      }
      case "COMPLETED": {
        return COMPLETED;
      }
      case "FAILED": {
        return FAILED;
      }
      default: {
        throw new IllegalArgumentException("Cannot create enum 'VolumeJobState' from value - " + value);
      }
    }
  }
}
