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
package com.sevenbridges.apiclient.task;

import com.sevenbridges.apiclient.lang.Assert;

/**
 * Named constants for common values for the {@link Task} state.
 */
public enum TaskStatus {

  /** The task is in a draft state. */
  DRAFT(false),

  /** The task is being created. */
  CREATING(false),

  /** The task is queued for execution. */
  QUEUED(false),

  /** The task is currently running. */
  RUNNING(false),

  /** The task has successfully completed. */
  COMPLETED(true),

  /** The task is being aborted. */
  ABORTING(false),

  /** The task has been explicitly aborted. */
  ABORTED(true),

  /** The task has failed. */
  FAILED(true);

  private final boolean finished;

  TaskStatus(boolean finished) {
    this.finished = finished;
  }

  /**
   * @return {@code true} if the {@link Task} is finished.
   */
  public final boolean isFinished() {
    return finished;
  }

  public static TaskStatus fromValue(String value) {
    Assert.hasText(value, "Value cannot be null or empty!");
    switch (value.toUpperCase()) {
      case "DRAFT": {
        return DRAFT;
      }
      case "CREATING": {
        return CREATING;
      }
      case "QUEUED": {
        return QUEUED;
      }
      case "RUNNING": {
        return RUNNING;
      }
      case "COMPLETED": {
        return COMPLETED;
      }
      case "ABORTING": {
        return ABORTING;
      }
      case "ABORTED": {
        return ABORTED;
      }
      case "FAILED": {
        return FAILED;
      }
      default: {
        throw new IllegalArgumentException("Cannot create enum 'TaskStatus' from value - " + value);
      }
    }
  }

}
