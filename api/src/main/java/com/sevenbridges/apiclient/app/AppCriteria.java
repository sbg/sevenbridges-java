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
package com.sevenbridges.apiclient.app;

import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.query.Criteria;

import java.util.List;

/**
 * An {@link App}-specific {@link Criteria} class which enables an App-specific <a
 * href="http://en.wikipedia.org/wiki/Fluent_interface">fluent</a> query DSL.
 */
public interface AppCriteria extends Criteria<AppCriteria>, AppOptions<AppCriteria> {

  /**
   * Adds a specific criterion to the current {@link AppCriteria} instance. Only {@link App}s that
   * are included in the {@link Project} specified by its project ID can meet this criteria.
   *
   * @param projectId String ID of a project
   * @return AppCriteria with added specified criterion
   */
  AppCriteria forProject(String projectId);

  /**
   * Adds a specific criterion to the current {@link AppCriteria} instance. Only {@link App}s that
   * are included in the specified {@link Project} can meet this criteria.
   *
   * @param project Project instance
   * @return AppCriteria with added specified criterion
   */
  AppCriteria forProject(Project project);

  /**
   * Adds a specific criterion to the current {@link AppCriteria} instance. Only {@link App}s that
   * are in projects created by the specified user can meet this criteria.
   *
   * @param username String identification of a Platform user
   * @return AppCriteria with added specified criterion
   */
  AppCriteria forProjectOwner(String username);

  /**
   * Adds a specific criterion to the current {@link AppCriteria} instance. Only {@link App}s with
   * ids that are the same as requested can meet this criteria.
   *
   * @param appIds App identifiers.
   * @return AppCriteria with added specified criterion
   */
  AppCriteria forAppIds(List<String> appIds);

  /**
   * Adds a specific criterion to the current {@link AppCriteria} instance. Only {@link App}s with
   * attributes (name, label, toolkit, toolkit version, category, tagline, description) that
   * match the query terms will satisfy this criterion.
   *
   * @param queryTerms Query terms.
   * @return AppCriteria with added specified criterion.
   */
  AppCriteria forQuery(List<String> queryTerms);

  /**
   * Adds a specific criterion to the current {@link AppCriteria} instance. Only {@link App}s that
   * are publicly accessible can meet this criteria.
   *
   * @return AppCriteria with added specified criterion
   */
  AppCriteria isPublic();

  /**
   * Adds a specific criterion to the current {@link AppCriteria} instance. Only {@link App}s that
   * are private can meet this criteria.
   *
   * @return AppCriteria with added specified criterion
   */
  AppCriteria isPrivate();


}
