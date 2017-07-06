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
package com.sevenbridges.apiclient.transfer;

/**
 * Local upload state.
 */
public enum UploadState {

  CREATED,
  RUNNING,
  PAUSING,
  PAUSED,
  QUEUED,
  FINISHED,
  ABORTED;

  public static UploadState fromValue(String value) {
    value = value.toUpperCase();
    switch (value) {
      case "CREATED": {
        return UploadState.CREATED;
      }
      case "QUEUED": {
        return UploadState.QUEUED;
      }
      case "RUNNING": {
        return UploadState.RUNNING;
      }
      case "PAUSING": {
        return UploadState.PAUSING;
      }
      case "PAUSED": {
        return UploadState.PAUSED;
      }
      case "FINISHED": {
        return UploadState.FINISHED;
      }
      case "ABORTED": {
        return UploadState.ABORTED;
      }
      default: {
        throw new IllegalArgumentException("Can not create UploadState from value: " + value);
      }
    }
  }

}
