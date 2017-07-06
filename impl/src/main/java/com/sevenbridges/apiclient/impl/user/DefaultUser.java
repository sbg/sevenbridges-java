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
package com.sevenbridges.apiclient.impl.user;

import com.sevenbridges.apiclient.app.App;
import com.sevenbridges.apiclient.app.AppCriteria;
import com.sevenbridges.apiclient.app.AppList;
import com.sevenbridges.apiclient.billing.BillingGroupCriteria;
import com.sevenbridges.apiclient.billing.BillingGroupList;
import com.sevenbridges.apiclient.file.File;
import com.sevenbridges.apiclient.file.FileCriteria;
import com.sevenbridges.apiclient.file.FileList;
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.project.DefaultCreateProjectRequest;
import com.sevenbridges.apiclient.impl.resource.AbstractInstanceResource;
import com.sevenbridges.apiclient.impl.resource.CollectionReference;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.impl.task.DefaultTaskRequestFactory;
import com.sevenbridges.apiclient.impl.transfer.NoopProgressListener;
import com.sevenbridges.apiclient.impl.transfer.TransferManagerFactory;
import com.sevenbridges.apiclient.impl.upload.DefaultCreateUploadRequestBuilder;
import com.sevenbridges.apiclient.impl.volume.DefaultVolumeRequestFactory;
import com.sevenbridges.apiclient.invoice.InvoiceCriteria;
import com.sevenbridges.apiclient.invoice.InvoiceList;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.project.CreateProjectRequest;
import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.project.ProjectCriteria;
import com.sevenbridges.apiclient.project.ProjectList;
import com.sevenbridges.apiclient.project.Projects;
import com.sevenbridges.apiclient.query.Criteria;
import com.sevenbridges.apiclient.resource.ResourceException;
import com.sevenbridges.apiclient.task.CreateTaskRequest;
import com.sevenbridges.apiclient.task.Task;
import com.sevenbridges.apiclient.task.TaskCriteria;
import com.sevenbridges.apiclient.task.TaskList;
import com.sevenbridges.apiclient.task.TaskRequestFactory;
import com.sevenbridges.apiclient.transfer.ProgressListener;
import com.sevenbridges.apiclient.transfer.UploadContext;
import com.sevenbridges.apiclient.transfer.UploadState;
import com.sevenbridges.apiclient.upload.CreateUploadRequest;
import com.sevenbridges.apiclient.upload.CreateUploadRequestBuilder;
import com.sevenbridges.apiclient.upload.Upload;
import com.sevenbridges.apiclient.upload.UploadList;
import com.sevenbridges.apiclient.user.User;
import com.sevenbridges.apiclient.volume.CreateVolumeRequest;
import com.sevenbridges.apiclient.volume.ExportJob;
import com.sevenbridges.apiclient.volume.ImportJob;
import com.sevenbridges.apiclient.volume.Volume;
import com.sevenbridges.apiclient.volume.VolumeList;
import com.sevenbridges.apiclient.volume.VolumeRequestFactory;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultUser extends AbstractInstanceResource implements User {

  // SIMPLE PROPERTIES:
  static final StringProperty USERNAME = new StringProperty("username");
  static final StringProperty EMAIL = new StringProperty("email");
  static final StringProperty FIRST_NAME = new StringProperty("first_name");
  static final StringProperty LAST_NAME = new StringProperty("last_name");
  static final StringProperty ADDRESS = new StringProperty("address");
  static final StringProperty CITY = new StringProperty("city");
  static final StringProperty STATE = new StringProperty("state");
  static final StringProperty COUNTRY = new StringProperty("country");
  static final StringProperty ZIP_CODE = new StringProperty("zip_code");
  static final StringProperty PHONE = new StringProperty("phone");
  static final StringProperty AFFILIATION = new StringProperty("affiliation");

  // COLLECTION RESOURCE REFERENCES:
  static final CollectionReference<ProjectList, Project> PROJECTS =
      new CollectionReference<>("projects", ProjectList.class, Project.class);

  static final CollectionReference<TaskList, Task> TASKS =
      new CollectionReference<>("tasks", TaskList.class, Task.class);

  static final int H_BILLING_GROUPS = 0;
  static final int H_PROJECTS = 1;
  static final int H_APPS = 2;
  static final int H_VOLUMES = 3;
  static final int H_IMPORTS = 4;
  static final int H_EXPORTS = 5;
  static final int H_TASKS = 6;
  static final int H_UPLOADS = 7;
  static final int H_INVOICES = 8;
  static final int H_FILES = 9;

  static final String[] HREF_REFERENCES = new String[]{
      "/billing/groups",
      "/projects",
      "/apps",
      "/storage/volumes",
      "/storage/imports",
      "/storage/exports",
      "/tasks",
      "/upload/multipart",
      "/billing/invoices",
      "/files"
  };

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      USERNAME, EMAIL, FIRST_NAME, LAST_NAME, ADDRESS, CITY, STATE, COUNTRY, ZIP_CODE, PHONE, AFFILIATION,
      PROJECTS, TASKS
  );

  private static final int PROPERTIES_COUNT = PROPERTY_DESCRIPTORS.size() - 2;

  public DefaultUser(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultUser(InternalDataStore dataStore, Map<String, Object> properties) {
    super(dataStore, properties);
  }

  @Override
  public Map<String, Property> getPropertyDescriptors() {
    return PROPERTY_DESCRIPTORS;
  }

  @Override
  public int getPropertiesCount() {
    return PROPERTIES_COUNT;
  }

  ////////////////////////////////////////////////////////////////////////
  // SIMPLE PROPERTIES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public String getUsername() {
    return getString(USERNAME);
  }

  @Override
  public User setUsername(String username) {
    Assert.hasText(username, "Username cannot be null or empty");
    setProperty(USERNAME, username);
    return this;
  }

  @Override
  public String getEmail() {
    return getString(EMAIL);
  }

  @Override
  public User setEmail(String email) {
    Assert.hasText(email, "Email cannot be null or empty");
    setProperty(EMAIL, email);
    return this;
  }

  @Override
  public String getFirstName() {
    return getString(FIRST_NAME);
  }

  @Override
  public User setFirstName(String firstName) {
    Assert.hasText(firstName, "FirstName cannot be null or empty");
    setProperty(FIRST_NAME, firstName);
    return this;
  }

  @Override
  public String getLastName() {
    return getString(LAST_NAME);
  }

  @Override
  public User setLastName(String lastName) {
    Assert.hasText(lastName, "LastName cannot be null or empty");
    setProperty(LAST_NAME, lastName);
    return this;
  }

  @Override
  public String getAddress() {
    return getString(ADDRESS);
  }

  @Override
  public User setAddress(String address) {
    Assert.hasText(address, "Address cannot be null or empty");
    setProperty(ADDRESS, address);
    return this;
  }

  @Override
  public String getCity() {
    return getString(CITY);
  }

  @Override
  public User setCity(String city) {
    Assert.hasText(city, "City cannot be null or empty");
    setProperty(CITY, city);
    return this;
  }

  @Override
  public String getState() {
    return getString(STATE);
  }

  @Override
  public User setState(String state) {
    Assert.hasText(state, "State cannot be null or empty");
    setProperty(STATE, state);
    return this;
  }

  @Override
  public String getCountry() {
    return getString(COUNTRY);
  }

  @Override
  public User setCountry(String country) {
    Assert.hasText(country, "Country cannot be null or empty");
    setProperty(COUNTRY, country);
    return this;
  }

  @Override
  public String getZipCode() {
    return getString(ZIP_CODE);
  }

  @Override
  public User setZipCode(String zipCode) {
    Assert.hasText(zipCode, "ZipCode cannot be null or empty");
    setProperty(ZIP_CODE, zipCode);
    return this;
  }

  @Override
  public String getPhone() {
    return getString(PHONE);
  }

  @Override
  public User setPhone(String phone) {
    Assert.hasText(phone, "Phone cannot be null or empty");
    setProperty(PHONE, phone);
    return this;
  }

  @Override
  public String getAffiliation() {
    return getString(AFFILIATION);
  }

  @Override
  public User setAffiliation(String affiliation) {
    Assert.hasText(affiliation, "Affiliation cannot be null or empty");
    setProperty(AFFILIATION, affiliation);
    return this;
  }

  ////////////////////////////////////////////////////////////////////////
  // COLLECTION RESOURCE REFERENCES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public BillingGroupList getBillingGroups() {
    return getDataStore().getResource(HREF_REFERENCES[H_BILLING_GROUPS], BillingGroupList.class);
  }

  @Override
  public BillingGroupList getBillingGroups(Map<String, Object> queryParams) {
    return getDataStore().getResource(
        HREF_REFERENCES[H_BILLING_GROUPS],
        BillingGroupList.class,
        queryParams
    );
  }

  @Override
  public BillingGroupList getBillingGroups(BillingGroupCriteria criteria) {
    return getDataStore().getResource(
        HREF_REFERENCES[H_BILLING_GROUPS],
        BillingGroupList.class,
        (Criteria<BillingGroupCriteria>) criteria
    );
  }

  @Override
  public InvoiceList getInvoices() {
    return getDataStore().getResource(HREF_REFERENCES[H_INVOICES], InvoiceList.class);
  }

  @Override
  public InvoiceList getInvoices(InvoiceCriteria criteria) {
    return getDataStore().getResource(
        HREF_REFERENCES[H_INVOICES],
        InvoiceList.class,
        (Criteria<InvoiceCriteria>) criteria
    );
  }

  @Override
  public Project createProject(Project project) throws ResourceException {
    Assert.notNull(project, "Project object cannot be null");
    CreateProjectRequest request = Projects.newCreateRequestFor(project).build();
    return createProject(request);
  }

  @Override
  public Project createProject(CreateProjectRequest cpr) throws ResourceException {
    Assert.notNull(cpr, "CreateProjectRequest object cannot be null");
    Assert.isInstanceOf(DefaultCreateProjectRequest.class, cpr);
    DefaultCreateProjectRequest request = (DefaultCreateProjectRequest) cpr;

    final Project project = request.getProject();

    return getDataStore().create(HREF_REFERENCES[H_PROJECTS], project);
  }

  @Override
  public ProjectList getProjects() {
    return getDataStore().getResource(HREF_REFERENCES[H_PROJECTS], ProjectList.class);
  }

  @Override
  public ProjectList getProjects(String ownerUsername) {
    Assert.hasText(ownerUsername, "OwnerUsername cannot be null or empty");
    return getDataStore().getResource(
        HREF_REFERENCES[H_PROJECTS] + "/" + ownerUsername,
        ProjectList.class
    );
  }

  @Override
  public ProjectList getProjects(Map<String, Object> queryParams) {
    return getDataStore().getResource(
        HREF_REFERENCES[H_PROJECTS],
        ProjectList.class,
        queryParams
    );
  }

  @Override
  public ProjectList getProjects(ProjectCriteria criteria) {
    return getDataStore().getResource(
        HREF_REFERENCES[H_PROJECTS],
        ProjectList.class,
        (Criteria<ProjectCriteria>) criteria
    );
  }

  @Override
  public Project getProjectById(String projectId) {
    Assert.hasText(projectId, "ProjectId cannot be null or empty");
    return getDataStore().getResource(
        HREF_REFERENCES[H_PROJECTS] + "/" + projectId,
        Project.class
    );
  }

  @Override
  public File getFileById(String fileId) {
    Assert.hasText(fileId, "FileId cannot be null or empty");
    return getDataStore().getResource(
        HREF_REFERENCES[H_FILES] + "/" + fileId,
        File.class
    );
  }

  @Override
  public FileList getPublicFiles() {
    return this.getProjectById("admin/sbg-public-data").getFiles();
  }

  @Override
  public FileList getPublicFiles(FileCriteria criteria) {
    return this.getProjectById("admin/sbg-public-data").getFiles(criteria);
  }

  @Override
  public TaskList getTasks() {
    return getDataStore().getResource(HREF_REFERENCES[H_TASKS], TaskList.class);
  }

  @Override
  public TaskList getTasks(TaskCriteria criteria) {
    return getDataStore().getResource(
        HREF_REFERENCES[H_TASKS],
        TaskList.class,
        (Criteria<TaskCriteria>) criteria
    );
  }

  @Override
  public TaskList getTasks(Map<String, Object> queryParams) {
    return getDataStore().getResource(
        HREF_REFERENCES[H_TASKS],
        TaskList.class,
        queryParams
    );
  }

  /**
   * Gets a {@link Task} resource by the specified task id, String unique identifier of the task on
   * the platform.
   *
   * @param taskId String task id
   * @return Task resource instance
   */
  @Override
  public Task getTaskById(String taskId) {
    return getDataStore().getResource(
        HREF_REFERENCES[H_TASKS] + "/" + taskId,
        Task.class
    );
  }

  @Override
  public AppList getPublicApps() {
    Map<String, Object> queryParams = new HashMap<>(1);
    queryParams.put("visibility", "public");
    return getDataStore().getResource(HREF_REFERENCES[H_APPS], AppList.class, queryParams);
  }

  @Override
  public AppList getApps(AppCriteria criteria) {
    return getDataStore().getResource(
        HREF_REFERENCES[H_APPS],
        AppList.class,
        (Criteria<AppCriteria>) criteria
    );
  }

  @Override
  public App getAppById(String appId) {
    Assert.hasText(appId, "AppId cannot be null or empty");
    return getDataStore().getResource(
        HREF_REFERENCES[H_APPS] + "/" + appId,
        App.class
    );
  }

  ////////////////////////////////////////////////////////////////////////
  // RESOURCE ACTIONS
  ////////////////////////////////////////////////////////////////////////

  @Override
  public VolumeList getVolumes() {
    return getDataStore().getResource(HREF_REFERENCES[H_VOLUMES], VolumeList.class);
  }

  @Override
  public VolumeList getVolumesForUser(String username) {
    Assert.hasText(username, "Username cannot be null or empty");
    return getDataStore().getResource(HREF_REFERENCES[H_VOLUMES] + "/" + username, VolumeList.class);
  }

  @Override
  public Volume getVolumeById(String volumeId) {
    Assert.hasText(volumeId, "VolumeId cannot be null or empty");
    return getDataStore().getResource(HREF_REFERENCES[H_VOLUMES] + "/" + volumeId, Volume.class);
  }

  @Override
  public VolumeRequestFactory getVolumeRequestFactory() {
    return new DefaultVolumeRequestFactory(getDataStore());
  }

  @Override
  public Volume createVolume(CreateVolumeRequest request) throws ResourceException {
    Assert.notNull(request, "CreateVolumeRequest object cannot be null");
    Volume volume = request.getVolume();
    return getDataStore().create(HREF_REFERENCES[H_VOLUMES], volume);
  }

  @Override
  public ImportJob startImport(String volumeId, String location, String projectId, String name, Boolean overwrite) {
    Assert.hasText(volumeId, "VolumeId cannot be null or empty");
    Assert.hasText(location, "Location cannot be null or empty");
    Assert.hasText(projectId, "ProjectId cannot be null or empty");
    Assert.hasText(name, "Name cannot be null or empty");

    Map<String, Object> source = new HashMap<>(2);
    source.put("volume", volumeId);
    source.put("location", location);

    Map<String, Object> destination = new HashMap<>(2);
    destination.put("project", projectId);
    destination.put("name", name);

    Map<String, Object> properties = new HashMap<>(3);
    properties.put("overwrite", overwrite == null ? Boolean.FALSE : overwrite);
    properties.put("source", source);
    properties.put("destination", destination);

    ImportJob importJob = getDataStore().instantiate(ImportJob.class, properties);

    return getDataStore().create(HREF_REFERENCES[H_IMPORTS], importJob);
  }

  @Override
  public ImportJob startImport(Volume volume, String location, Project project, String name, Boolean overwrite) {
    Assert.notNull(volume, "Volume object cannot be null");
    Assert.notNull(project, "Project object cannot be null");
    return startImport(volume.getVolumeId(), location, project.getId(), name, overwrite);
  }

  @Override
  public ExportJob startExport(String fileId, String volumeId, String location, Boolean overwrite) {
    Assert.hasText(fileId, "FileId cannot be null or empty");
    Assert.hasText(volumeId, "VolumeId cannot be null or empty");
    Assert.hasText(location, "LocationId cannot be null or empty");

    Map<String, Object> source = new HashMap<>(2);
    source.put("file", fileId);

    Map<String, Object> destination = new HashMap<>(2);
    destination.put("volume", volumeId);
    destination.put("location", location);

    Map<String, Object> properties = new HashMap<>(3);
    properties.put("source", source);
    properties.put("destination", destination);

    Map<String, Object> qp = new HashMap<>(1);
    qp.put("overwrite", overwrite == null ? Boolean.FALSE : overwrite);

    ExportJob exportJob = getDataStore().instantiate(ExportJob.class, properties);

    return getDataStore().create(HREF_REFERENCES[H_EXPORTS], exportJob, ExportJob.class, qp);
  }

  @Override
  public ExportJob startExport(String fileId, String volumeId, String location, Boolean overwrite, Map<String, String> properties) {
    Assert.hasText(fileId, "FileId cannot be null or empty");
    Assert.hasText(volumeId, "VolumeId cannot be null or empty");
    Assert.hasText(location,"Location cannot be null or empty");

    Map<String, Object> source = new HashMap<>(2);
    source.put("file", fileId);

    Map<String, Object> destination = new HashMap<>(2);
    destination.put("volume", volumeId);
    destination.put("location", location);

    Map<String, Object> resourceProperties = new HashMap<>(3);
    resourceProperties.put("source", source);
    resourceProperties.put("destination", destination);
    resourceProperties.put("properties", properties);

    Map<String, Object> qp = new HashMap<>(1);
    if (overwrite == null) {
      overwrite = false;
    }
    qp.put("overwrite", overwrite);

    ExportJob exportJob = getDataStore().instantiate(ExportJob.class, resourceProperties);

    return getDataStore().create(HREF_REFERENCES[H_EXPORTS], exportJob, ExportJob.class, qp);
  }

  @Override
  public ExportJob startExport(File file, Volume volume, String location, Boolean overwrite) {
    Assert.notNull(file, "File object cannot be null");
    Assert.notNull(volume, "Volume object cannot be null");
    return startExport(file.getId(), volume.getVolumeId(), location, overwrite);
  }

  @Override
  public ExportJob startExport(File file, Volume volume, String location, Boolean overwrite, Map<String, String> properties) {
    Assert.notNull(file, "File object cannot be null");
    Assert.notNull(volume, "Volume object cannot be null");
    Assert.hasText(location, "Location cannot be null or empty");
    return startExport(file.getId(), volume.getVolumeId(), location, overwrite, properties);
  }

  @Override
  public TaskRequestFactory getTaskRequestFactory() {
    return new DefaultTaskRequestFactory(getDataStore());
  }

  @Override
  public Task createTask(CreateTaskRequest createTaskRequest) {
    Assert.notNull(createTaskRequest, "CreateTaskRequest object cannot be null");
    if (createTaskRequest.isRunNow()) {
      Map<String, Object> qp = new HashMap<>(1);
      qp.put("action", "RUN");
      return getDataStore().create(HREF_REFERENCES[H_TASKS], createTaskRequest.getTask(), Task.class, qp);
    } else {
      return getDataStore().create(HREF_REFERENCES[H_TASKS], createTaskRequest.getTask(), Task.class);
    }
  }

  @Override
  public CreateUploadRequestBuilder getUploadRequestBuilder() {
    return new DefaultCreateUploadRequestBuilder(getDataStore());
  }

  @Override
  public UploadList getUploads() {
    return getDataStore().getResource(
        HREF_REFERENCES[H_UPLOADS],
        UploadList.class
    );
  }

  @Override
  public Upload getUploadById(String uploadId) {
    Assert.hasText(uploadId, "UploadId cannot be null or empty");
    return getDataStore().getResource(
        HREF_REFERENCES[H_UPLOADS] + "/" + uploadId,
        Upload.class,
        Collections.<String, Object>singletonMap("list_parts", true)
    );
  }

  @Override
  public Upload initUpload(CreateUploadRequest uploadRequest) {
    Assert.notNull(uploadRequest, "UploadRequest object cannot be null");
    Map<String, Object> qp = new HashMap<>(1);
    qp.put("overwrite", uploadRequest.getOverwrite());
    return getDataStore().create(HREF_REFERENCES[H_UPLOADS], uploadRequest.getUpload(), Upload.class, qp);
  }

  @Override
  public UploadContext submitUpload(CreateUploadRequest uploadRequest) {
    Assert.notNull(uploadRequest, "UploadRequest object cannot be null");
    return submitUpload(uploadRequest, NoopProgressListener.getInstance());
  }

  @Override
  public UploadContext submitUpload(CreateUploadRequest uploadRequest, ProgressListener listener) {
    Assert.notNull(uploadRequest, "UploadRequest object cannot be null");
    if (listener == null) {
      listener = NoopProgressListener.getInstance();
    }
    Map<String, Object> qp = new HashMap<>(1);
    qp.put("overwrite", uploadRequest.getOverwrite());
    Upload upload = getDataStore().create(HREF_REFERENCES[H_UPLOADS], uploadRequest.getUpload(), Upload.class, qp);
    return TransferManagerFactory.getTransferManager().upload(
        upload,
        uploadRequest.getFile(),
        listener == null ? NoopProgressListener.getInstance() : listener);
  }

  @Override
  public UploadContext resumeUpload(UploadContext pausedContext, java.io.File file) {
    Assert.notNull(pausedContext, "PausedContext object cannot be null");
    Assert.notNull(file, "File object cannot be null");
    return resumeUpload(pausedContext, file, NoopProgressListener.getInstance());
  }

  @Override
  public UploadContext resumeUpload(UploadContext pausedContext, java.io.File file, ProgressListener listener) {
    Assert.notNull(pausedContext, "PausedContext object cannot be null");
    Assert.notNull(file, "File object cannot be null");
    if (!UploadState.PAUSED.equals(pausedContext.getState())) {
      throw new IllegalArgumentException("Cannot resume non PAUSED upload");
    }
    return resumeUpload(
        this.getUploadById(pausedContext.getUploadId()),
        file,
        listener == null ? NoopProgressListener.getInstance() : listener);
  }

  @Override
  public UploadContext resumeUpload(Upload pausedUpload, java.io.File file) {
    Assert.notNull(pausedUpload, "PausedUpload object cannot be null");
    Assert.notNull(file, "File object cannot be null");
    return resumeUpload(pausedUpload, file, NoopProgressListener.getInstance());
  }

  @Override
  public UploadContext resumeUpload(Upload pausedUpload, java.io.File file, ProgressListener listener) {
    Assert.notNull(pausedUpload, "Upload object must not be null");
    Assert.notNull(file, "File object must not be null");
    RandomAccessFile rac;
    try {
      rac = new RandomAccessFile(file, "r");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Provided file not found", e);
    }
    return TransferManagerFactory.getTransferManager().upload(
        pausedUpload,
        rac,
        listener == null ? NoopProgressListener.getInstance() : listener);
  }

  @Override
  public void shutdownTransferService() {
    TransferManagerFactory.stopTransferManager();
  }

}
