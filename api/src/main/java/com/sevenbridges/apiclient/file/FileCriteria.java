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
package com.sevenbridges.apiclient.file;

import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.query.Criteria;
import com.sevenbridges.apiclient.task.Task;

/**
 * An {@link File}-specific {@link Criteria} class which enables a File-specific <a
 * href="http://en.wikipedia.org/wiki/Fluent_interface">fluent</a> query DSL.
 */
public interface FileCriteria extends Criteria<FileCriteria>, FileOptions<FileCriteria> {

  /**
   * Adds a specific criterion to the current {@link FileCriteria} instance. Only {@link File}s that
   * are included in the {@link Project} specified by its project ID can meet this criteria.
   *
   * @param projectId String ID of a project
   * @return FileCriteria with added specified criterion
   */
  FileCriteria forProject(String projectId);

  /**
   * Adds a specific criterion to the current {@link FileCriteria} instance. Only {@link File}s that
   * are included in the specified {@link Project} can meet this criteria.
   *
   * @param project Project instance
   * @return FileCriteria with added specified criterion
   */
  FileCriteria forProject(Project project);

  /**
   * Adds a specific criterion to the current {@link FileCriteria} instance. Only {@link File}s with
   * specified names can meet this criteria.
   * <p>
   * This criterion is not exclusive, you can specify multiple names.
   *
   * @param name String name of the file
   * @return FileCriteria with added specified criterion
   */
  FileCriteria withName(String name);

  /**
   * Adds a specific criterion to the current {@link FileCriteria} instance. Only {@link File}s
   * tagged with specified tag can meet this criteria.
   * <p>
   * This criterion is not exclusive, you can specify multiple tags.
   *
   * @param tag String tag, label of the file
   * @return FileCriteria with added specified criterion
   */
  FileCriteria withTag(String tag);

  /**
   * Adds a specific criterion to the current {@link FileCriteria} instance. Only {@link File}s
   * originated from the specified {@link Task} can meet this criteria.
   * <p>
   * This criterion is not exclusive, you can specify multiple task origins.
   *
   * @param taskOrigin Task instance, origin of the files
   * @return FileCriteria with added specified criterion
   */
  FileCriteria withTaskOrigin(Task taskOrigin);

  /**
   * Adds a specific criterion to the current {@link FileCriteria} instance. Only {@link File}s
   * originated from the {@link Task} specified by its task ID can meet this criteria.
   * <p>
   * This criterion is not exclusive, you can specify multiple task origins.
   *
   * @param taskId String ID of the task
   * @return FileCriteria with added specified criterion
   */
  FileCriteria withTaskOrigin(String taskId);

  /**
   * Adds a specific criterion to the current {@link FileCriteria} instance. Only {@link File}s with
   * specified metadata can meet this criteria.
   * <p>
   * This criterion is not exclusive, you can specify multiple metadata key/value pairs.
   *
   * @param key   String key identifier of the metadata pair
   * @param value String value of the metadata pair
   * @return FileCriteria with added specified criterion
   */
  FileCriteria withMetadata(String key, String value);

  /**
   * Indicates that query results with current {@link FileCriteria} should return full information
   * about the queried {@link File}s, instead of reduced number of fields. Useful when you know that
   * you need to have full information about a number of files, so this is convenient method of
   * specifying it, instead of providing your own query params, or getting full file information one
   * file at a time.
   *
   * @return FileCriteria with added specified criterion
   */
  FileCriteria withAllFields();

}
