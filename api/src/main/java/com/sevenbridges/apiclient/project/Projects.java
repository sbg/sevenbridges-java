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
package com.sevenbridges.apiclient.project;

import com.sevenbridges.apiclient.lang.Classes;

import java.lang.reflect.Constructor;

/**
 * Static utility/helper methods for working with {@link Project} resources.
 * <p>
 * Most methods are <a href="http://en.wikipedia.org/wiki/Factory_method_pattern">factory
 * method</a>s used for forming Application-specific <a href="http://en.wikipedia.org/wiki/Fluent_interface">fluent
 * DSL</a> queries.
 */
public final class Projects {

  //prevent instantiation
  private Projects() {
  }

  @SuppressWarnings("unchecked")
  private static final Class<CreateProjectRequestBuilder> BUILDER_CLASS =
      Classes.forName("com.sevenbridges.apiclient.impl.project.DefaultCreateProjectRequestBuilder");

  /**
   * Returns a new {@link ProjectOptions} instance, used to customize how one or more {@link
   * Project}s are retrieved.
   *
   * @return a new {@link ProjectOptions} instance, used to customize how one or more {@link
   * Project}s are retrieved.
   */
  public static ProjectOptions options() {
    return (ProjectOptions) Classes.newInstance("com.sevenbridges.apiclient.impl.project.DefaultProjectOptions");
  }

  /**
   * Returns a new {@link ProjectCriteria} instance, used to customize how one or more {@link
   * Project}s are retrieved.
   *
   * @return a new {@link ProjectCriteria} instance, used to customize how one or more {@link
   * Project}s are retrieved.
   */
  public static ProjectCriteria criteria() {
    return (ProjectCriteria) Classes.newInstance("com.sevenbridges.apiclient.impl.project.DefaultProjectCriteria");
  }

  /**
   * Creates a new {@link CreateProjectRequestBuilder CreateProjectRequestBuilder} instance
   * reflecting the specified {@link Project} instance.  The builder can be used to customize any
   * creation request options as necessary.
   *
   * @param project the project to create a new record for in Seven Bridges
   * @return a new {@link CreateProjectRequestBuilder} instance reflecting the specified {@link
   * Project} instance.
   */
  public static CreateProjectRequestBuilder newCreateRequestFor(Project project) {
    Constructor ctor = Classes.getConstructor(BUILDER_CLASS, Project.class);
    return (CreateProjectRequestBuilder) Classes.instantiate(ctor, project);
  }

}
