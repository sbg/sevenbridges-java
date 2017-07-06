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
package com.sevenbridges.apiclient.impl.task;

import com.sevenbridges.apiclient.app.App;
import com.sevenbridges.apiclient.file.File;
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.task.CreateTaskRequest;
import com.sevenbridges.apiclient.task.CreateTaskRequestBuilder;
import com.sevenbridges.apiclient.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultCreateTaskRequestBuilder implements CreateTaskRequestBuilder {

  private final InternalDataStore dataStore;
  private Map<String, Object> properties;
  private Map<String, Object> inputs;
  private boolean runNow = false;

  DefaultCreateTaskRequestBuilder(InternalDataStore dataStore) {
    this.dataStore = dataStore;
    properties = new LinkedHashMap<>(7);
    inputs = new HashMap<>(5);
  }

  @Override
  public CreateTaskRequestBuilder setName(String name) {
    properties.put("name", name);
    return this;
  }

  @Override
  public CreateTaskRequestBuilder setDescription(String description) {
    properties.put("description", description);
    return this;
  }

  @Override
  public CreateTaskRequestBuilder setProject(String projectId) {
    properties.put("project", projectId);
    return this;
  }

  @Override
  public CreateTaskRequestBuilder setProject(Project project) {
    properties.put("project", project.getId());
    return this;
  }

  @Override
  public CreateTaskRequestBuilder setApp(String appId) {
    properties.put("app", appId);
    return this;
  }

  @Override
  public CreateTaskRequestBuilder setApp(App app) {
    properties.put("app", app.getId());
    return this;
  }

  @Override
  public CreateTaskRequestBuilder setBatchInput(String batchInput) {
    properties.put("batch_input", batchInput);
    return this;
  }

  @Override
  public CreateTaskRequestBuilder addInput(String inputName, int value) {
    return addInput(inputName, String.valueOf(value));
  }

  @Override
  public CreateTaskRequestBuilder addInput(String inputName, float value) {
    return addInput(inputName, String.valueOf(value));
  }

  @Override
  public CreateTaskRequestBuilder addInput(String inputName, boolean value) {
    return addInput(inputName, String.valueOf(value));
  }

  @Override
  public CreateTaskRequestBuilder addInput(String inputName, String value) {
    return addInputCustom(inputName, value);
  }

  @Override
  public CreateTaskRequestBuilder addInputCustom(String inputName, Object value) {
    inputs.put(inputName, value);
    return this;
  }

  @Override
  public CreateTaskRequestBuilder addInput(String inputName, File file) {
    Map<String, String> fileDict = new LinkedHashMap<>(3);
    fileDict.put("class", "File");
    fileDict.put("path", file.getId());
    fileDict.put("name", file.getName());
    return addInput(inputName, fileDict);
  }

  @Override
  public CreateTaskRequestBuilder addInputAsSingletonList(String inputName, File file) {
    Map<String, String> fileDict = new LinkedHashMap<>(3);
    fileDict.put("class", "File");
    fileDict.put("path", file.getId());
    fileDict.put("name", file.getName());
    List<Map<String, String>> singletonList = new ArrayList<>(1);
    singletonList.add(fileDict);
    inputs.put(inputName, singletonList);
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public CreateTaskRequestBuilder addInput(String inputName, Map<String, String> fileDict) {
    if (!fileDict.containsKey("class") ||
        !fileDict.containsKey("path") ||
        !fileDict.containsKey("name")) {
      throw new IllegalArgumentException("Input file dict must contain keys 'class', 'path' and 'name'");
    }
    Object currentInput = inputs.get(inputName);
    if (currentInput != null) {
      if (currentInput instanceof Map) {
        List<Map<String, String>> newList = new ArrayList<>();
        newList.add((Map<String, String>) currentInput);
        newList.add(fileDict);
        inputs.put(inputName, newList);
      } else if (currentInput instanceof List) {
        ((List<Map<String, String>>) currentInput).add(fileDict);
      } else {
        inputs.put(inputName, fileDict);
      }
    } else {
      inputs.put(inputName, fileDict);
    }
    return this;
  }

  @Override
  public CreateTaskRequestBuilder addInputAsSingletonList(String inputName, Map<String, String> fileDict) {
    if (!fileDict.containsKey("class") ||
        !fileDict.containsKey("path") ||
        !fileDict.containsKey("name")) {
      throw new IllegalArgumentException("Input file dict must contain keys 'class', 'path' and 'name'");
    }
    List<Map<String, String>> singletonList = new ArrayList<>(1);
    singletonList.add(fileDict);
    inputs.put(inputName, singletonList);
    return this;
  }

  @Override
  public CreateTaskRequestBuilder addInput(String inputName, File[] files) {
    List<Map<String, String>> fileDictList = new ArrayList<>(files.length);
    for (File file : files) {
      Map<String, String> fileDict = new LinkedHashMap<>(3);
      fileDict.put("class", "File");
      fileDict.put("path", file.getId());
      fileDict.put("name", file.getName());
      fileDictList.add(fileDict);
    }
    return addInput(inputName, fileDictList);
  }

  @SuppressWarnings("unchecked")
  @Override
  public CreateTaskRequestBuilder addInput(String inputName, List<Map<String, String>> fileDictList) {
    // validate first
    for (Map<String, String> fileDict : fileDictList) {
      if (!fileDict.containsKey("class") ||
          !fileDict.containsKey("path") ||
          !fileDict.containsKey("name")) {
        throw new IllegalArgumentException("Input file dict must contain keys 'class', 'path' and 'name'");
      }
    }
    // then put or merge list
    Object currentInput = inputs.get(inputName);
    if (currentInput != null) {
      if (currentInput instanceof Map) {
        List<Map<String, String>> newList = new ArrayList<>();
        newList.add((Map<String, String>) currentInput);
        newList.addAll(fileDictList);
        inputs.put(inputName, newList);
      } else if (currentInput instanceof List) {
        ((List<Map<String, String>>) currentInput).addAll(fileDictList);
      } else {
        inputs.put(inputName, new ArrayList<>(fileDictList));
      }
    } else {
      inputs.put(inputName, new ArrayList<>(fileDictList));
    }
    return this;
  }

  @Override
  public CreateTaskRequestBuilder addBatchBy(String type, List<String> criteria) {
    if (criteria == null && "CRITERIA".equals(type)) {
      throw new IllegalArgumentException("Batch_by type 'CRITERIA' must have filled list of criteria");
    }
    Map<String, Object> batchBy = new HashMap<>(2);
    batchBy.put("type", type);
    if (criteria != null) {
      batchBy.put("criteria", criteria);
    }
    properties.put("batch_by", batchBy);
    return this;
  }

  @Override
  public CreateTaskRequestBuilder runNow(boolean runNow) {
    this.runNow = runNow;
    return this;
  }

  @Override
  public CreateTaskRequest build() {
    Assert.notNull(properties.get("name"), "Field 'name' is required");
    Assert.notNull(properties.get("project"), "Field 'project' is required");
    Assert.notNull(properties.get("app"), "Field 'app' is required");

    if (inputs.size() > 0) {
      properties.put("inputs", inputs);
    }
    Task newTask = dataStore.instantiate(Task.class, properties, false);
    return new DefaultCreateTaskRequest(newTask, runNow);
  }
}
