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
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractInstanceResource;
import com.sevenbridges.apiclient.impl.resource.BooleanProperty;
import com.sevenbridges.apiclient.impl.resource.DateProperty;
import com.sevenbridges.apiclient.impl.resource.ListProperty;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.price.Price;
import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.task.BatchBy;
import com.sevenbridges.apiclient.task.BatchGroup;
import com.sevenbridges.apiclient.task.ExecutionDetails;
import com.sevenbridges.apiclient.task.ExecutionStatus;
import com.sevenbridges.apiclient.task.Task;
import com.sevenbridges.apiclient.task.TaskStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultTask extends AbstractInstanceResource implements Task {

  // SIMPLE PROPERTIES:
  static final StringProperty ID = new StringProperty("id");
  static final StringProperty NAME = new StringProperty("name");
  static final StringProperty STATUS = new StringProperty("status");
  static final StringProperty DESCRIPTION = new StringProperty("description");
  static final StringProperty PROJECT_ID = new StringProperty("project");
  static final StringProperty APP_ID = new StringProperty("app");
  static final StringProperty TYPE = new StringProperty("type");
  static final StringProperty CREATED_BY = new StringProperty("created_by");
  static final StringProperty EXECUTED_BY = new StringProperty("executed_by");
  static final DateProperty START_TIME = new DateProperty("start_time");
  static final DateProperty END_TIME = new DateProperty("end_time");
  static final DateProperty CREATED_TIME = new DateProperty("created_time");
  static final BooleanProperty BATCH = new BooleanProperty("batch");
  static final MapProperty BATCH_BY = new MapProperty("batch_by");
  static final MapProperty BATCH_GROUP = new MapProperty("batch_group");
  static final StringProperty PARENT = new StringProperty("parent");
  static final MapProperty EXECUTION_STATUS = new MapProperty("execution_status");
  static final MapProperty INPUTS = new MapProperty("inputs");
  static final MapProperty OUTPUTS = new MapProperty("outputs");
  static final ListProperty ERRORS = new ListProperty("errors");
  static final ListProperty WARNINGS = new ListProperty("warnings");
  static final MapProperty PRICE = new MapProperty("price");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      ID, NAME, STATUS, DESCRIPTION, PROJECT_ID, APP_ID, TYPE, CREATED_BY, EXECUTED_BY,
      START_TIME, END_TIME, BATCH, BATCH_BY, BATCH_GROUP, PARENT, EXECUTION_STATUS, INPUTS, OUTPUTS,
      ERRORS, WARNINGS
  );

  static final int H_ACTION_RUN = 0;
  static final int H_ACTION_ABORT = 1;
  static final int H_PROJECTS = 2;
  static final int H_APPS = 3;
  static final int H_EXECUTION_DETAILS = 4;

  static final String[] HREF_REFERENCES = new String[]{
      "/actions/run",
      "/actions/abort",
      "/projects",
      "/apps",
      "/execution_details"
  };

  public DefaultTask(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultTask(InternalDataStore dataStore, Map<String, Object> properties) {
    super(dataStore, properties);
  }

  @Override
  public Map<String, Property> getPropertyDescriptors() {
    return PROPERTY_DESCRIPTORS;
  }

  @Override
  public int getPropertiesCount() {
    return PROPERTY_DESCRIPTORS.size();
  }

  @Override
  public String getId() {
    return getString(ID);
  }

  @Override
  public String getName() {
    return getString(NAME);
  }

  @Override
  public TaskStatus getStatus() {
    String stringValue = getString(STATUS);
    return stringValue == null ? null : TaskStatus.fromValue(stringValue);
  }

  @Override
  public String getDescription() {
    return getString(DESCRIPTION);
  }

  @Override
  public String getProjectId() {
    return getString(PROJECT_ID);
  }

  @Override
  public String getAppId() {
    return getString(APP_ID);
  }

  @Override
  public String getType() {
    return getString(TYPE);
  }

  @Override
  public String getCreatedBy() {
    return getString(CREATED_BY);
  }

  @Override
  public String getExecutedBy() {
    return getString(EXECUTED_BY);
  }

  @Override
  public Date getStartTime() {
    return getDate(START_TIME);
  }

  @Override
  public Boolean isBatch() {
    return getBoolean(BATCH);
  }

  @Override
  public BatchBy getBatchBy() {
    return new DefaultBatchBy();
  }

  @Override
  public BatchGroup getBatchGroup() {
    return new DefaultBatchGroup();
  }

  @Override
  public Price getPrice() {
    return new DefaultPrice();
  }

  @Override
  public String getParent() {
    return getString(PARENT);
  }

  @Override
  public Date getEndTime() {
    return getDate(END_TIME);
  }

  @Override
  public ExecutionStatus getExecutionStatus() {
    return new DefaultExecutionStatus();
  }

  @Override
  public Map<String, Object> getInputs() {
    return getMap(INPUTS);
  }

  @Override
  public Map<String, Object> getOutputs() {
    return getMap(OUTPUTS);
  }

  @Override
  public List<Map<String, Object>> getErrors() {
    return getList(ERRORS);
  }

  @Override
  public List<Map<String, Object>> getWarnings() {
    return getList(WARNINGS);
  }

  @Override
  public Date getCreatedTime() {
    return getDate(CREATED_TIME);
  }

  @Override
  public void delete() {
    getDataStore().delete(this);
  }

  ////////////////////////////////////////////////////////////////////////
  // RESOURCE ACTIONS
  ////////////////////////////////////////////////////////////////////////

  @Override
  public Project getProject() {
    if (getProjectId() == null) {
      return null;
    } else {
      return getDataStore().getResource(
          HREF_REFERENCES[H_PROJECTS] + "/" + getProjectId(),
          Project.class
      );
    }
  }

  @Override
  public App getApp() {
    if (getAppId() == null) {
      return null;
    } else {
      return getDataStore().getResource(
          HREF_REFERENCES[H_APPS] + "/" + getAppId(),
          App.class
      );
    }
  }

  @Override
  public Task run() {
    Map<String, Object> qp = new HashMap<>(2);
    Boolean batch = this.isBatch();
    qp.put("batch", batch == null ? Boolean.FALSE : batch);
    return getDataStore().resourceAction(HREF_REFERENCES[H_ACTION_RUN], this, this.getClass(), qp);
  }

  @Override
  public Task abort() {
    return getDataStore().resourceAction(HREF_REFERENCES[H_ACTION_ABORT], this, this.getClass(), null);
  }

  @Override
  public ExecutionDetails getExecutionDetails() {
    return getDataStore().getResource(this.getHref() +  HREF_REFERENCES[H_EXECUTION_DETAILS], ExecutionDetails.class);
  }

  ////////////////////////////////////////////////////////////////////////
  // INNER CLASSES
  ////////////////////////////////////////////////////////////////////////

  class DefaultPrice implements Price {

    static final String CURRENCY = "currency";
    static final String AMOUNT = "amount";

    DefaultPrice() {
    }

    @Override
    public String getCurrency() {
      return (String) DefaultTask.this.getMapPropertyEntry(PRICE, CURRENCY);
    }

    @Override
    public String getAmount() {
      return String.valueOf(DefaultTask.this.getMapPropertyEntry(PRICE, AMOUNT));
    }

    @Override
    public String toString() {
      return getAmount() + getCurrency();
    }
  }

  class DefaultBatchBy implements BatchBy {

    static final String TYPE = "type";
    static final String CRITERIA = "criteria";

    DefaultBatchBy() {
    }

    @Override
    public String getType() {
      return (String) DefaultTask.this.getMapPropertyEntry(BATCH_BY, TYPE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getCriteria() {
      return (List<String>) DefaultTask.this.getMapPropertyEntry(BATCH_BY, CRITERIA);
    }
  }

  class DefaultBatchGroup implements BatchGroup {

    static final String VALUE = "value";
    static final String FIELDS = "fields";

    @Override
    public String getValue() {
      return (String) DefaultTask.this.getMapPropertyEntry(BATCH_GROUP, VALUE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> getFields() {
      return (Map<String, String>) DefaultTask.this.getMapPropertyEntry(BATCH_GROUP, FIELDS);
    }
  }

  class DefaultExecutionStatus implements ExecutionStatus {

    static final String STEPS_COMPLETED = "steps_completed";
    static final String STEPS_TOTAL = "steps_total";
    static final String MESSAGE = "message";
    static final String QUEUED = "queued";
    static final String RUNNING = "running";
    static final String COMPLETED = "completed";
    static final String FAILED = "failed";
    static final String ABORTED = "aborted";
    static final String SYSTEM_LIMIT = "system_limit";
    static final String ACCOUNT_LIMIT = "account_limit";
    static final String INSTANCE_INIT = "instance_init";
    static final String QUEUED_DURATION = "queued_duration";
    static final String RUNNING_DURATION = "running_duration";
    static final String EXECUTION_DURATION = "execution_duration";
    static final String DURATION = "duration";

    @Override
    public Integer getStepsCompleted() {
      return (Integer) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, STEPS_COMPLETED);
    }

    @Override
    public Integer getStepsTotal() {
      return (Integer) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, STEPS_TOTAL);
    }

    @Override
    public String getMessage() {
      return (String) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, MESSAGE);
    }

    @Override
    public Integer getQueued() {
      return (Integer) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, QUEUED);
    }

    @Override
    public Integer getRunning() {
      return (Integer) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, RUNNING);
    }

    @Override
    public Integer getCompleted() {
      return (Integer) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, COMPLETED);
    }

    @Override
    public Integer getFailed() {
      return (Integer) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, FAILED);
    }

    @Override
    public Integer getAborted() {
      return (Integer) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, ABORTED);
    }

    @Override
    public Boolean getSystemLimit() {
      return (Boolean) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, SYSTEM_LIMIT);
    }

    @Override
    public Boolean getAccountLimit() {
      return (Boolean) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, ACCOUNT_LIMIT);
    }

    @Override
    public Boolean getInstanceInit() {
      return (Boolean) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, INSTANCE_INIT);
    }

    @Override
    public Integer getQueuedDuration() {
      return (Integer) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, QUEUED_DURATION);
    }

    @Override
    public Integer getRunningDuration() {
      return (Integer) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, RUNNING_DURATION);
    }

    @Override
    public Integer getExecutionDuration() {
      return (Integer) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, EXECUTION_DURATION);
    }

    @Override
    public Integer getDuration() {
      return (Integer) DefaultTask.this.getMapPropertyEntry(EXECUTION_STATUS, DURATION);
    }
  }
}
