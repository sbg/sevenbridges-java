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
package com.sevenbridges.apiclient.impl.client;

import com.sevenbridges.apiclient.app.App;
import com.sevenbridges.apiclient.app.AppCriteria;
import com.sevenbridges.apiclient.app.AppList;
import com.sevenbridges.apiclient.billing.BillingGroupCriteria;
import com.sevenbridges.apiclient.billing.BillingGroupList;
import com.sevenbridges.apiclient.client.ApiKey;
import com.sevenbridges.apiclient.client.AuthenticationScheme;
import com.sevenbridges.apiclient.client.Client;
import com.sevenbridges.apiclient.client.Proxy;
import com.sevenbridges.apiclient.ds.DataStore;
import com.sevenbridges.apiclient.file.File;
import com.sevenbridges.apiclient.file.FileCriteria;
import com.sevenbridges.apiclient.file.FileList;
import com.sevenbridges.apiclient.impl.ds.DefaultDataStore;
import com.sevenbridges.apiclient.impl.http.RequestExecutor;
import com.sevenbridges.apiclient.invoice.InvoiceCriteria;
import com.sevenbridges.apiclient.invoice.InvoiceList;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.lang.Classes;
import com.sevenbridges.apiclient.project.CreateProjectRequest;
import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.project.ProjectCriteria;
import com.sevenbridges.apiclient.project.ProjectList;
import com.sevenbridges.apiclient.query.Options;
import com.sevenbridges.apiclient.resource.Resource;
import com.sevenbridges.apiclient.resource.ResourceException;
import com.sevenbridges.apiclient.task.CreateTaskRequest;
import com.sevenbridges.apiclient.task.Task;
import com.sevenbridges.apiclient.task.TaskCriteria;
import com.sevenbridges.apiclient.task.TaskList;
import com.sevenbridges.apiclient.task.TaskRequestFactory;
import com.sevenbridges.apiclient.transfer.ProgressListener;
import com.sevenbridges.apiclient.transfer.UploadContext;
import com.sevenbridges.apiclient.upload.CreateUploadRequest;
import com.sevenbridges.apiclient.upload.CreateUploadRequestBuilder;
import com.sevenbridges.apiclient.upload.Upload;
import com.sevenbridges.apiclient.upload.UploadList;
import com.sevenbridges.apiclient.user.User;
import com.sevenbridges.apiclient.user.UserOptions;
import com.sevenbridges.apiclient.volume.CreateVolumeRequest;
import com.sevenbridges.apiclient.volume.ExportJob;
import com.sevenbridges.apiclient.volume.ImportJob;
import com.sevenbridges.apiclient.volume.Volume;
import com.sevenbridges.apiclient.volume.VolumeList;
import com.sevenbridges.apiclient.volume.VolumeRequestFactory;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * The default {@link Client} implementation.
 * <p>
 * <h3>DataStore API</h3>
 * <p>
 * This class implements the {@link DataStore} interface, but this implementation merely acts as a
 * wrapper to the underlying 'real' {@code DataStore} instance. This is a convenience mechanism to
 * eliminate the constant need to call {@code client.getDataStore()} every time one needs to
 * instantiate or look up a Resource.
 */
public class DefaultClient implements Client {

  private final DataStore dataStore;

  private String currentUserHref;

  /**
   * Instantiates a new Client instance that will communicate with the SevenBridges REST API.  See
   * the class-level JavaDoc for a usage example.
   *
   * @param apiKey               the SevenBridges account API Key that will be used to authenticate
   *                             the client with SevenBridges's API server
   * @param baseUrl              the SevenBridges base URL
   * @param proxy                the HTTP proxy to be used when communicating with the SevenBridges
   *                             API server (can be null)
   * @param authenticationScheme the HTTP authentication scheme to be used when communicating with
   *                             the SevenBridges API server (can be null)
   */
  public DefaultClient(ApiKey apiKey, String baseUrl, Proxy proxy, AuthenticationScheme authenticationScheme, int connectionTimeout) {
    Assert.notNull(apiKey, "apiKey argument cannot be null.");
    Assert.isTrue(connectionTimeout >= 0, "connectionTimeout cannot be a negative number.");
    RequestExecutor requestExecutor = createRequestExecutor(apiKey, proxy, authenticationScheme, connectionTimeout);
    this.dataStore = createDataStore(requestExecutor, baseUrl, apiKey);
  }

  protected DataStore createDataStore(RequestExecutor requestExecutor, String baseUrl, ApiKey apiKey) {
    return new DefaultDataStore(requestExecutor, baseUrl, apiKey);
  }

  @Override
  public DataStore getDataStore() {
    return this.dataStore;
  }

  @Override
  public User getCurrentUser() {
    String href = currentUserHref;
    if (href == null) {
      href = "/user";
    }
    User current = this.dataStore.getResource(href, User.class);
    this.currentUserHref = current.getHref();
    return current;
  }

  @Override
  public User getCurrentUser(UserOptions userOptions) {
    String href = currentUserHref;
    if (href == null) {
      href = "/user";
    }
    User current = this.dataStore.getResource(href, User.class, userOptions);
    this.currentUserHref = current.getHref();
    return current;
  }

  @Override
  public ApiKey getApiKey() {
    return this.dataStore.getApiKey();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private RequestExecutor createRequestExecutor(ApiKey apiKey, Proxy proxy, AuthenticationScheme authenticationScheme, int connectionTimeout) {

    String className = "com.sevenbridges.apiclient.impl.http.httpclient.HttpClientRequestExecutor";

    Class requestExecutorClass;

    if (Classes.isAvailable(className)) {
      requestExecutorClass = Classes.forName(className);
    } else {
      //we might be able to check for other implementations in the future, but for now, we only
      //support HTTP calls via the HttpClient.  Throw an exception:

      String msg = "Unable to find the '" + className + "' implementation on the classpath.  Please ensure you " +
          "have added the sevenbridges-java-httpclient.jar file to your runtime classpath.";
      throw new RuntimeException(msg);
    }

    Constructor<RequestExecutor> ctor = Classes.getConstructor(requestExecutorClass, ApiKey.class, Proxy.class, AuthenticationScheme.class, Integer.class);

    return Classes.instantiate(ctor, apiKey, proxy, authenticationScheme, connectionTimeout);
  }

  ////////////////////////////////////////////////////////////////////////
  // DataStore methods (delegate to underlying DataStore instance)
  ////////////////////////////////////////////////////////////////////////

  /**
   * Delegates to the internal {@code dataStore} instance. This is a convenience mechanism to
   * eliminate the constant need to call {@code client.getDataStore()} every time one needs to
   * instantiate Resource.
   *
   * @param clazz the Resource class to instantiate.
   * @param <T>   the Resource sub-type
   * @return a new instance of the specified Resource.
   */
  @Override
  public <T extends Resource> T instantiate(Class<T> clazz) {
    return this.dataStore.instantiate(clazz);
  }

  /**
   * Delegates to the internal {@code dataStore} instance. This is a convenience mechanism to
   * eliminate the constant need to call {@code client.getDataStore()} every time one needs to look
   * up a Resource.
   *
   * @param href  the resource URL of the resource to retrieve
   * @param clazz the {@link Resource} sub-interface to instantiate
   * @param <T>   type parameter indicating the returned value is a {@link Resource} instance.
   * @return an instance of the specified class based on the data returned from the specified {@code
   * href} URL.
   */
  @Override
  public <T extends Resource> T getResource(String href, Class<T> clazz) {
    return this.dataStore.getResource(href, clazz);
  }

  /**
   * Delegates to the internal {@code dataStore} instance. This is a convenience mechanism to
   * eliminate the constant need to call {@code client.getDataStore()} every time one needs to look
   * up a Resource.
   *
   * @param href    the URL of the resource to retrieve
   * @param clazz   the {@link Resource} sub-interface to instantiate
   * @param options the {@link Options} sub-interface with the properties to expand
   * @param <T>     type parameter indicating the returned value is a {@link Resource} instance.
   * @return an instance of the specified {@code Class} based on the data returned from the
   * specified {@code href} URL.
   */
  @Override
  public <T extends Resource, O extends Options> T getResource(String href, Class<T> clazz, O options) {
    return this.dataStore.getResource(href, clazz, options);
  }

  ////////////////////////////////////////////////////////////////////////
  // Billing
  ////////////////////////////////////////////////////////////////////////

  @Override
  public BillingGroupList getBillingGroups() {
    return getCurrentUser().getBillingGroups();
  }

  @Override
  public BillingGroupList getBillingGroups(Map<String, Object> queryParams) {
    return getCurrentUser().getBillingGroups(queryParams);
  }

  @Override
  public BillingGroupList getBillingGroups(BillingGroupCriteria criteria) {
    return getCurrentUser().getBillingGroups(criteria);
  }

  @Override
  public InvoiceList getInvoices() {
    return getCurrentUser().getInvoices();
  }

  @Override
  public InvoiceList getInvoices(InvoiceCriteria criteria) {
    return getCurrentUser().getInvoices(criteria);
  }

  ////////////////////////////////////////////////////////////////////////
  // Projects
  ////////////////////////////////////////////////////////////////////////

  @Override
  public Project createProject(Project project) throws ResourceException {
    return getCurrentUser().createProject(project);
  }

  @Override
  public Project createProject(CreateProjectRequest request) throws ResourceException {
    return getCurrentUser().createProject(request);
  }

  @Override
  public ProjectList getProjects() {
    return getCurrentUser().getProjects();
  }

  @Override
  public ProjectList getProjects(String ownerUsername) {
    return getCurrentUser().getProjects(ownerUsername);
  }

  @Override
  public ProjectList getProjects(Map<String, Object> queryParams) {
    return getCurrentUser().getProjects(queryParams);
  }

  @Override
  public ProjectList getProjects(ProjectCriteria criteria) {
    return getCurrentUser().getProjects(criteria);
  }

  @Override
  public Project getProjectById(String projectId) {
    return getCurrentUser().getProjectById(projectId);
  }

  @Override
  public File getFileById(String fileId) {
    return getCurrentUser().getFileById(fileId);
  }

  @Override
  public FileList getPublicFiles() {
    return getCurrentUser().getPublicFiles();
  }

  @Override
  public FileList getPublicFiles(FileCriteria criteria) {
    return getCurrentUser().getPublicFiles(criteria);
  }

  @Override
  public AppList getPublicApps() {
    return getCurrentUser().getPublicApps();
  }

  @Override
  public AppList getApps(AppCriteria criteria) {
    return getCurrentUser().getApps(criteria);
  }

  @Override
  public App getAppById(String appId) {
    return getCurrentUser().getAppById(appId);
  }

  @Override
  public VolumeList getVolumes() {
    return getCurrentUser().getVolumes();
  }

  @Override
  public VolumeList getVolumesForUser(String username) {
    return getCurrentUser().getVolumesForUser(username);
  }

  @Override
  public Volume getVolumeById(String volumeId) {
    return getCurrentUser().getVolumeById(volumeId);
  }

  @Override
  public VolumeRequestFactory getVolumeRequestFactory() {
    return getCurrentUser().getVolumeRequestFactory();
  }

  @Override
  public Volume createVolume(CreateVolumeRequest request) throws ResourceException {
    return getCurrentUser().createVolume(request);
  }

  @Override
  public ImportJob startImport(String volumeId, String location, String projectId, String name, Boolean overwrite) {
    return getCurrentUser().startImport(volumeId, location, projectId, name, overwrite);
  }

  @Override
  public ImportJob startImport(Volume volume, String location, Project project, String name, Boolean overwrite) {
    return getCurrentUser().startImport(volume, location, project, name, overwrite);
  }

  @Override
  public ExportJob startExport(String fileId, String volumeId, String location, Boolean overwrite) {
    return getCurrentUser().startExport(fileId, volumeId, location, overwrite);
  }

  @Override
  public ExportJob startExport(String fileId, String volumeId, String location, Boolean overwrite, Map<String, String> properties) {
    return getCurrentUser().startExport(fileId, volumeId, location, overwrite, properties);
  }

  @Override
  public ExportJob startExport(File file, Volume volume, String location, Boolean overwrite) {
    return getCurrentUser().startExport(file, volume, location, overwrite);
  }

  @Override
  public ExportJob startExport(File file, Volume volume, String location, Boolean overwrite, Map<String, String> properties) {
    return getCurrentUser().startExport(file, volume, location, overwrite, properties);
  }

  @Override
  public TaskRequestFactory getTaskRequestFactory() {
    return getCurrentUser().getTaskRequestFactory();
  }

  @Override
  public Task createTask(CreateTaskRequest createTaskRequest) {
    return getCurrentUser().createTask(createTaskRequest);
  }

  @Override
  public TaskList getTasks() {
    return getCurrentUser().getTasks();
  }

  @Override
  public TaskList getTasks(TaskCriteria criteria) {
    return getCurrentUser().getTasks(criteria);
  }

  @Override
  public TaskList getTasks(Map<String, Object> queryParams) {
    return getCurrentUser().getTasks(queryParams);
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
    return getCurrentUser().getTaskById(taskId);
  }

  @Override
  public CreateUploadRequestBuilder getUploadRequestBuilder() {
    return getCurrentUser().getUploadRequestBuilder();
  }

  @Override
  public UploadList getUploads() {
    return getCurrentUser().getUploads();
  }

  @Override
  public Upload getUploadById(String uploadId) {
    return getCurrentUser().getUploadById(uploadId);
  }

  @Override
  public Upload initUpload(CreateUploadRequest uploadRequest) {
    return getCurrentUser().initUpload(uploadRequest);
  }

  @Override
  public UploadContext submitUpload(CreateUploadRequest uploadRequest) {
    return getCurrentUser().submitUpload(uploadRequest);
  }

  @Override
  public UploadContext submitUpload(CreateUploadRequest uploadRequest, ProgressListener listener) {
    return getCurrentUser().submitUpload(uploadRequest, listener);
  }

  @Override
  public UploadContext resumeUpload(UploadContext pausedContext, java.io.File file) {
    return getCurrentUser().resumeUpload(pausedContext, file);
  }

  @Override
  public UploadContext resumeUpload(UploadContext pausedContext, java.io.File file, ProgressListener listener) {
    return getCurrentUser().resumeUpload(pausedContext, file, listener);
  }

  @Override
  public UploadContext resumeUpload(Upload pausedUpload, java.io.File file) {
    return getCurrentUser().resumeUpload(pausedUpload, file);
  }

  @Override
  public UploadContext resumeUpload(Upload pausedUpload, java.io.File file, ProgressListener listener) {
    return getCurrentUser().resumeUpload(pausedUpload, file, listener);
  }

  @Override
  public void shutdownTransferService() {
    getCurrentUser().shutdownTransferService();
  }

}
