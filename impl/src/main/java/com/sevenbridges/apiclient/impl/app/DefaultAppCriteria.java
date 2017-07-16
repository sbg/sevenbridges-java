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
package com.sevenbridges.apiclient.impl.app;

import com.sevenbridges.apiclient.app.AppCriteria;
import com.sevenbridges.apiclient.app.AppOptions;
import com.sevenbridges.apiclient.impl.query.DefaultCriteria;
import com.sevenbridges.apiclient.impl.query.EqualsExpression;
import com.sevenbridges.apiclient.impl.query.ListExpression;
import com.sevenbridges.apiclient.impl.query.Operator;
import com.sevenbridges.apiclient.impl.util.StringUtils;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.project.Project;

import java.util.List;

public class DefaultAppCriteria extends DefaultCriteria<AppCriteria, AppOptions> implements AppCriteria {

  public DefaultAppCriteria() {
    super(new DefaultAppOptions());
  }

  public DefaultAppCriteria(AppOptions options) {
    super(options);
  }

  @Override
  public AppCriteria forProject(String projectId) {
    Assert.hasText(projectId, "'Project Id' argument cannot be null.");
    return add(new EqualsExpression("project", projectId));
  }

  @Override
  public AppCriteria forProject(Project project) {
    Assert.notNull(project, "'Project' argument cannot be null.");
    Assert.hasText(project.getId(), "'Project Id' property of a project must be set");
    return add(new EqualsExpression("project", project.getId()));
  }

  @Override
  public AppCriteria forProjectOwner(String username) {
    Assert.hasText(username, "'Username' argument cannot be null.");
    return add(new EqualsExpression("project_owner", username));
  }

  @Override
  public AppCriteria forAppIds(List<String> appIds) {
    Assert.notNull(appIds, "'App Ids' argument cannot be null.");
    Assert.notEmpty(appIds, "'App Ids' argument cannot be empty.");
    return add(new ListExpression("id", appIds, Operator.EQUALS));
  }

  @Override public AppCriteria forQuery(List<String> queryTerms) {
    Assert.notNull(queryTerms, "'Query terms argument cannot be null.");
    Assert.notEmpty(queryTerms, "'Query terms argument cannot be empty.");
    String queryString = StringUtils.join(queryTerms, " ");
    return add(new EqualsExpression("q", queryString));
  }

  @Override
  public AppCriteria isPublic() {
    remove("visibility");
    return add(new EqualsExpression("visibility", "PUBLIC"));
  }

  @Override
  public AppCriteria isPrivate() {
    remove("visibility");
    return add(new EqualsExpression("visibility", "PRIVATE"));
  }
}
