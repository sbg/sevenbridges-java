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
package com.sevenbridges.apiclient.impl.file;

import com.sevenbridges.apiclient.file.FileCriteria;
import com.sevenbridges.apiclient.file.FileOptions;
import com.sevenbridges.apiclient.impl.query.DefaultCriteria;
import com.sevenbridges.apiclient.impl.query.EqualsExpression;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.task.Task;

public class DefaultFileCriteria extends DefaultCriteria<FileCriteria, FileOptions> implements FileCriteria {

  public DefaultFileCriteria() {
    super(new DefaultFileOptions());
  }

  public DefaultFileCriteria(FileOptions options) {
    super(options);
  }

  @Override
  public FileCriteria forProject(String projectId) {
    Assert.notNull(projectId, "projectId argument cannot be null.");
    return add(new EqualsExpression("project", projectId));
  }

  @Override
  public FileCriteria forProject(Project project) {
    Assert.notNull(project, "project argument cannot be null.");
    Assert.notNull(project.getId(), "projectId argument cannot be null.");
    return forProject(project.getId());
  }

  @Override
  public FileCriteria withName(String name) {
    Assert.notNull(name, "name argument cannot be null.");
    return add(new EqualsExpression("name", name));
  }

  @Override
  public FileCriteria withTag(String tag) {
    Assert.notNull(tag, "tag argument cannot be null.");
    return add(new EqualsExpression("tag", tag));
  }

  @Override
  public FileCriteria withTaskOrigin(Task taskOrigin) {
    Assert.notNull(taskOrigin, "Origin task argument cannot be null.");
    return withTaskOrigin(taskOrigin.getId());
  }

  @Override
  public FileCriteria withTaskOrigin(String taskId) {
    Assert.notNull(taskId, "Origin task ID argument cannot be null.");
    return add(new EqualsExpression("origin.task", taskId));
  }

  @Override
  public FileCriteria withMetadata(String key, String value) {
    Assert.notNull(key, "Metadata 'key' argument cannot be null");
    Assert.notNull(key, "Metadata 'value' argument cannot be null");
    key = key.toLowerCase().replace(' ', '_'); // just some basic validation
    return add(new EqualsExpression(String.format("metadata.%s", key), value));
  }

  @Override
  public FileCriteria withAllFields() {
    return add(new EqualsExpression("fields", "_all"));
  }

}
