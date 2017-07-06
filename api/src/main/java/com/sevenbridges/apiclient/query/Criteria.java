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
package com.sevenbridges.apiclient.query;

/**
 * A Criteria instance represents one or more {@link Criterion} (conditions) that are used to
 * customize query results.
 */
public interface Criteria<T extends Criteria<T>> {

  /**
   * Sets the query's pagination offset: the index in the overall result set of matching resources
   * that should be considered the first item to include in the response 'page'.  All subsequent
   * resources in the response page will immediately follow this index.
   * <p>
   * For example, a paged query with an offset of 50 will return a page of results where the page
   * contains resource at index 50, then the resource at index 51, then at 52, 53, etc.
   * <p>
   * If unspecified, this number defaults to {@code 0}, indicating that the results should start at
   * the first result in the overall set (i.e the first 'page').  The maximum number of elements
   * returned in a page is specified by the {@link #limitTo(int)} method.
   *
   * @param offset the query's pagination offset: the index in the overall result set of matching
   *               resources that should be considered the first item to include in the response
   *               'page'.
   * @return the criteria instance for method chaining
   * @see #limitTo(int)
   */
  T offsetBy(int offset);

  /**
   * Sets the query's page size limit: the maximum number of results to include in a single page. If
   * unspecified, this number defaults to {@code 25}.  The minimum number allowed is {@code 1}, the
   * maximum is {@code 100}.
   *
   * @param limit the query's page size limit: the maximum number of results to include in a single
   *              page.
   * @return the criteria instance for method chaining
   * @see #offsetBy(int)
   */
  T limitTo(int limit);

  /**
   * Returns {@code true} if this instance does not yet reflect any criteria conditions or orderBy
   * statements, {@code false} otherwise.
   *
   * @return {@code true} if this instance does not yet reflect any criteria conditions or orderBy
   * statements, {@code false} otherwise.
   */
  boolean isEmpty();
}
