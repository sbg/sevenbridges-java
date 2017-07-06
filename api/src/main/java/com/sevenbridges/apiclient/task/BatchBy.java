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

import java.util.List;

/**
 * Batch by property of a {@link Task}.
 */
public interface BatchBy {

  /**
   * String type of batching (i.e. "ITEM" or "CRITERIA").
   *
   * @return String type of BatchBy
   */
  String getType();

  /**
   * Criteria list of strings to batch the task by.
   *
   * @return batching criteria list
   */
  List<String> getCriteria();

}
