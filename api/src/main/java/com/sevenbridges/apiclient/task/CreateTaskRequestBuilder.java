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

import com.sevenbridges.apiclient.app.App;
import com.sevenbridges.apiclient.file.File;
import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.user.UserActions;

import java.util.List;
import java.util.Map;

/**
 * A Builder to construct {@link CreateTaskRequest}s.
 * <p>
 * Builder can be instantiated from a task factory {@link UserActions#getTaskRequestFactory()}.
 *
 * @see UserActions#getTaskRequestFactory()
 */
public interface CreateTaskRequestBuilder {

  /**
   * Sets the name of the future task. Name of the task is not unique.
   *
   * @param name String name of the future task
   * @return builder instance with name set
   */
  CreateTaskRequestBuilder setName(String name);

  /**
   * String description of the future task, this field is optional.
   *
   * @param description String description of the future task
   * @return builder instance with description set
   */
  CreateTaskRequestBuilder setDescription(String description);

  /**
   * String project ID of the {@link Project} to run task in.
   *
   * @param projectId String project id, unique identifier of a project
   * @return builder instance with project set
   */
  CreateTaskRequestBuilder setProject(String projectId);

  /**
   * {@link Project} instance to run project in.
   *
   * @param project Project resource instance
   * @return builder instance with project set
   */
  CreateTaskRequestBuilder setProject(Project project);

  /**
   * String app ID of the {@link App} that specify workflow of this future task.
   *
   * @param appId String app id, unique identifier of a app
   * @return builder instance with app set
   */
  CreateTaskRequestBuilder setApp(String appId);

  /**
   * App instance that specify workflow of this future task.
   *
   * @param app App resource instance
   * @return builder instance with app set
   */
  CreateTaskRequestBuilder setApp(App app);

  /**
   * The ID of the input on which you wish to batch. You would typically batch on the input
   * consisting of a list of files. If this parameter is omitted, the default batching criteria
   * defined for the app will be used.
   *
   * @param batchInput String batch_input value
   * @return builder instance with batchInput set
   */
  CreateTaskRequestBuilder setBatchInput(String batchInput);

  /**
   * This specifies the criteria on which to batch the future task. Type specifies type of batching
   * (i.e. "ITEM" or "CRITERIA"), and criteria list to batch the task by.
   *
   * @param type     String type of batch by
   * @param criteria List of criteria to batch by
   * @return builder instance with batchBy set
   * @see <a href="http://docs.sevenbridges.com/docs/perform-batch-analysis">More about batching
   * tasks</a>
   */
  CreateTaskRequestBuilder addBatchBy(String type, List<String> criteria);

  /**
   * Adds an integer input with specified value and input name. If there already exists an input
   * with this input name this integer input will remove the old input and place the current ones
   * value.
   *
   * @param inputName String name of the input
   * @param value     value of the input
   * @return builder instance with input set
   * @see <a href="http://docs.sevenbridges.com/reference#section-inputs">More about defining task
   * inputs</a>
   */
  CreateTaskRequestBuilder addInput(String inputName, int value);

  /**
   * Adds a float input with specified value and input name. If there already exists an input with
   * this input name this float input will remove the old input and place the current ones value.
   *
   * @param inputName String name of the input
   * @param value     value of the input
   * @return builder instance with input set
   * @see <a href="http://docs.sevenbridges.com/reference#section-inputs">More about defining task
   * inputs</a>
   */
  CreateTaskRequestBuilder addInput(String inputName, float value);

  /**
   * Adds a boolean input with specified value and input name. If there already exists an input with
   * this input name this boolean input will remove the old input and place the current ones value.
   *
   * @param inputName String name of the input
   * @param value     value of the input
   * @return builder instance with input set
   * @see <a href="http://docs.sevenbridges.com/reference#section-inputs">More about defining task
   * inputs</a>
   */
  CreateTaskRequestBuilder addInput(String inputName, boolean value);

  /**
   * Adds a string input with specified value and input name. If there already exists an input with
   * this input name this string input will remove the old input and place the current ones value.
   *
   * @param inputName String name of the input
   * @param value     value of the input
   * @return builder instance with input set
   * @see <a href="http://docs.sevenbridges.com/reference#section-inputs">More about defining task
   * inputs</a>
   */
  CreateTaskRequestBuilder addInput(String inputName, String value);

  /**
   * Adds a file dict input value object from a specified {@link File} resource. From this file a
   * file dict map will be created and set as value for specified input. If the specified input
   * already contains non file dict value, that input value will be overwritten with this file. If
   * the specified input already contains a file, or a file list, this file will be added to the
   * list, or in a case of singular file, existing file, and new file will form a list of files that
   * will serve as an input for specified input name.
   *
   * @param inputName String name of the input
   * @param file      File resource value of the input
   * @return builder instance with input set
   * @see <a href="http://docs.sevenbridges.com/reference#section-inputs">More about defining task
   * inputs</a>
   */
  CreateTaskRequestBuilder addInput(String inputName, File file);

  /**
   * Adds a file dict input value object from a specified {@link File} resource. From this file a
   * file dict map will be created and set as value for specified input.
   * <p>
   * In this call, singular file dict will be set as a SINGLETON LIST. This is useful when the type
   * of input is defined as List of files, but you want to set only one one file. If you use
   * addInput() call in this case file will be set as singular dict, not an array of dicts, and task
   * validation will fail with wrong type.
   * <p>
   * Created file dict will overwrite any input specified for this inputName if any.
   *
   * @param inputName String name of the input
   * @param file      File resource value of the input
   * @return builder instance with input set
   */
  CreateTaskRequestBuilder addInputAsSingletonList(String inputName, File file);

  /**
   * Adds a specified file dict input map as value for specified inputName. If the specified input
   * already contains non file dict value, that input value will be overwritten with this fileDict.
   * If the specified input already contains a file, or a file list, this file will be added to the
   * list, or in a case of singular file, existing file, and new file will form a list of files that
   * will serve as an input for the specified input name.
   * <p>
   * FileDict map object normally has keys "class", "path" and "name".
   *
   * @param inputName String name of the input
   * @param fileDict  map of strings, information about a file input
   * @return builder instance with input set
   * @see <a href="http://docs.sevenbridges.com/reference#section-inputs">More about defining task
   * inputs</a>
   */
  CreateTaskRequestBuilder addInput(String inputName, Map<String, String> fileDict);

  /**
   * Adds a specified file dict input map as value for specified inputName.
   * <p>
   * In this call, singular file dict will be set as a SINGLETON LIST. This is useful when the type
   * of input is defined as List of files, but you want to set only one one file. If you use
   * addInput() call in this case file will be set as singular dict, not an array of dicts, and task
   * validation will fail with wrong type.
   * <p>
   * Created file dict will overwrite any input specified for this inputName if any.
   * <p>
   * FileDict map object normally has keys "class", "path" and "name".
   *
   * @param inputName String name of the input
   * @param fileDict  map of strings, information about a file input
   * @return builder instance with input set
   */
  CreateTaskRequestBuilder addInputAsSingletonList(String inputName, Map<String, String> fileDict);

  /**
   * Adds an array of specified {@link File}s resources as inputs for a specified inputName. If the
   * specified input already contains non file dict value, that input value will be overwritten with
   * this file list. If the specified input already contains a file, or a file list, files will be
   * merged into one list and be set as input at the specified input name.
   *
   * @param inputName String name of the input
   * @param files     array of Files that will serve as the value of the input
   * @return builder instance with input set
   * @see <a href="http://docs.sevenbridges.com/reference#section-inputs">More about defining task
   * inputs</a>
   */
  CreateTaskRequestBuilder addInput(String inputName, File[] files);

  /**
   * Adds a list of specified file dict objects as inputs for a specified inputName. If the
   * specified input already contains non file dict value, that input value will be overwritten with
   * this file dict list. If the specified input already contains a file, or a file list, files will
   * be merged into one list and be set as input at the specified input name.
   * <p>
   * FileDict map object normally has keys "class", "path" and "name".
   *
   * @param inputName    String name of the input
   * @param fileDictList array of Files that will serve as the value of the input
   * @return builder instance with input set
   * @see <a href="http://docs.sevenbridges.com/reference#section-inputs">More about defining task
   * inputs</a>
   */
  CreateTaskRequestBuilder addInput(String inputName, List<Map<String, String>> fileDictList);

  /**
   * Adds a custom object as a value for the specified input name. Provided object should be
   * serializable or else, a client error will be thrown. This call should not be used generally,
   * but is left here if no other method satisfies your needs for specifying inputs.
   *
   * @param inputName String name of the input
   * @param value     serializable object to serve as the value of the input
   * @return builder instance with input set
   * @see <a href="http://docs.sevenbridges.com/reference#section-inputs">More about defining task
   * inputs</a>
   */
  CreateTaskRequestBuilder addInputCustom(String inputName, Object value);

  /**
   * Optional boolean flag that marks a task for immediate execution if true. If omitted task will
   * just be created in draft state.
   *
   * @param runNow boolean runNow indicator
   * @return builder instance with run now set
   */
  CreateTaskRequestBuilder runNow(boolean runNow);

  /**
   * Creates a new {@code CreateTaskRequest} instance based on the current builder state.
   *
   * @return a new {@code CreateTaskRequest} instance based on the current builder state.
   */
  CreateTaskRequest build();
}
