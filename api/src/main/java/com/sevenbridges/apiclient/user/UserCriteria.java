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
package com.sevenbridges.apiclient.user;

import com.sevenbridges.apiclient.query.Criteria;

/**
 * A {@link User}-specific {@link Criteria} class which enables a User-specific <a
 * href="http://en.wikipedia.org/wiki/Fluent_interface">fluent</a> query DSL. UserCriteria
 * instances can be constructed by using the {@link Users} utility class, for example:
 * <pre>
 * Users.options()
 *   .withProjects()
 * </pre>
 */
public interface UserCriteria extends Criteria<UserCriteria>, UserOptions<UserCriteria> {
}
