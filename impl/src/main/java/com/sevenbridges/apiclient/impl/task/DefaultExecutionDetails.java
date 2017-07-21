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

import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractInstanceResource;
import com.sevenbridges.apiclient.impl.resource.DateProperty;
import com.sevenbridges.apiclient.impl.resource.ListProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.task.ExecutionDetails;
import com.sevenbridges.apiclient.task.ExecutionJobDetails;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DefaultExecutionDetails extends AbstractInstanceResource implements ExecutionDetails {

  static final DateProperty START_TIME = new DateProperty("start_time");
  static final DateProperty END_TIME = new DateProperty("end_time");
  static final StringProperty STATUS = new StringProperty("status");
  static final StringProperty MESSAGE = new StringProperty("message");
  static final ListProperty JOBS = new ListProperty("jobs");

  List<ExecutionJobDetails> jobsList = null;


  public DefaultExecutionDetails(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultExecutionDetails(InternalDataStore dataStore, Map<String, Object> properties) {
    super(dataStore, properties);
  }

  @Override
  public Map<String, Property> getPropertyDescriptors() {
    return null;
  }

  @Override
  public int getPropertiesCount() {
    return 0;
  }

  @Override
  public Date getStartTime() {
    return getDate(START_TIME);
  }

  @Override
  public Date getEndTime() {
    return getDate(END_TIME);
  }

  @Override
  public String getStatus() {
    return getString(STATUS);
  }

  @Override
  public String getMessage() {
    return getString(MESSAGE);
  }

  @Override
  public List<ExecutionJobDetails> getJobs() {
    if (jobsList == null) {
      jobsList = initJobsList();
    }
    return jobsList;
  }

  @SuppressWarnings("unchecked")
  private List<ExecutionJobDetails> initJobsList() {
    List<Map<String, Object>> jobsMapList = getList(JOBS);
    if (jobsMapList == null) {
      return Collections.emptyList();
    }

    List<ExecutionJobDetails> jobList = new ArrayList<>(jobsMapList.size());
    DateFormat dateFormat = new ISO8601DateFormat();
    for (Map<String, Object> jobMap : jobsMapList) {
      String name = (String) jobMap.get("name");
      Date startTime = null;
      Date endTime = null;
      try {
        startTime = dateFormat.parse((String) jobMap.get("start_time"));
        endTime = dateFormat.parse((String) jobMap.get("end_time"));
      } catch (ParseException e) {
        // do nothing, leave them null
      }
      String commandLine = (String) jobMap.get("command_line");
      Map<String, Object> instanceMap = (Map<String, Object>) jobMap.get("instance");
      String instanceId = null;
      String instanceType = null;
      String instanceProvider = null;
      Integer instanceDiskSize = null;
      String instanceDiskUnit = null;
      String instanceDiskType = null;
      if (instanceMap != null) {
        instanceId = (String) instanceMap.get("id");
        instanceType = (String) instanceMap.get("type");
        instanceProvider = (String) instanceMap.get("provider");
        Map<String, Object> diskMap = (Map<String, Object>) instanceMap.get("disk");
        instanceDiskSize = (Integer) diskMap.get("size");
        instanceDiskUnit = (String) diskMap.get("unit");
        instanceDiskType = (String) diskMap.get("type");
      }
      String status = (String) jobMap.get("status");
      Boolean retried = (Boolean) jobMap.get("retried");
      Map<String, String> dockerMap = (Map<String, String>) jobMap.get("docker");
      String dockerChecksum = null;
      if (dockerMap != null) {
        dockerChecksum = dockerMap.get("checksum");
      }
      Map<String, String> logs = (Map<String, String>) jobMap.get("logs");
      if (logs != null) {
        logs = Collections.unmodifiableMap(logs);
      }
      DefaultExecutionJobDetails job = new DefaultExecutionJobDetails(
          name,
          startTime,
          endTime,
          commandLine,
          status,
          retried,
          instanceType,
          instanceId,
          instanceProvider,
          instanceDiskSize,
          instanceDiskUnit,
          instanceDiskType,
          dockerChecksum,
          logs
      );
      jobList.add(job);
    }
    return jobList;
  }

  private static class DefaultExecutionJobDetails implements ExecutionJobDetails {

    private final String name;
    private final Date startTime;
    private final Date endTime;
    private final String commandLine;
    private final String status;
    private final Boolean retried;
    private final String instanceType;
    private final String instanceId;
    private final String instanceProvider;
    private final String dockerChecksum;
    private final Map<String, String> logs;
    private final Integer instanceDiskSize;
    private final String instanceDiskUnit;
    private final String instanceDiskType;

    DefaultExecutionJobDetails(final String name,
                               final Date startTime,
                               final Date endTime,
                               final String commandLine,
                               final String status,
                               final Boolean retried,
                               final String instanceType,
                               final String instanceId,
                               final String instanceProvider,
                               final Integer instanceDiskSize,
                               final String instanceDiskUnit,
                               final String instanceDiskType,
                               final String dockerChecksum,
                               final Map<String, String> logs) {
      this.name = name;
      this.startTime = startTime;
      this.endTime = endTime;
      this.commandLine = commandLine;
      this.status = status;
      this.retried = retried;
      this.instanceType = instanceType;
      this.instanceId = instanceId;
      this.instanceProvider = instanceProvider;
      this.instanceDiskSize = instanceDiskSize;
      this.instanceDiskUnit = instanceDiskUnit;
      this.instanceDiskType = instanceDiskType;
      this.dockerChecksum = dockerChecksum;
      this.logs = logs;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public Date getStartTime() {
      return startTime;
    }

    @Override
    public Date getEndTime() {
      return endTime;
    }

    @Override
    public String getCommandLine() {
      return commandLine;
    }

    @Override
    public String getStatus() {
      return status;
    }

    @Override
    public Boolean getRetried() {
      return retried;
    }

    @Override
    public String getInstanceType() {
      return instanceType;
    }

    @Override
    public String getInstanceId() {
      return instanceId;
    }

    @Override
    public String getInstanceProvider() {
      return instanceProvider;
    }

    @Override
    public Integer getInstanceDiskSize() {
      return instanceDiskSize;
    }

    @Override
    public String getInstanceDiskUnit() {
      return instanceDiskUnit;
    }

    @Override
    public String getInstanceDiskType() {
      return instanceDiskType;
    }

    @Override
    public String getDockerChecksum() {
      return dockerChecksum;
    }

    @Override
    public Map<String, String> getLogs() {
      return logs;
    }
  }

}
