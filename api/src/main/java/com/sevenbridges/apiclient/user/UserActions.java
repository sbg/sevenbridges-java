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

import com.sevenbridges.apiclient.app.App;
import com.sevenbridges.apiclient.app.AppCriteria;
import com.sevenbridges.apiclient.app.AppList;
import com.sevenbridges.apiclient.billing.BillingGroupCriteria;
import com.sevenbridges.apiclient.billing.BillingGroupList;
import com.sevenbridges.apiclient.file.File;
import com.sevenbridges.apiclient.file.FileCriteria;
import com.sevenbridges.apiclient.file.FileList;
import com.sevenbridges.apiclient.invoice.InvoiceCriteria;
import com.sevenbridges.apiclient.invoice.InvoiceList;
import com.sevenbridges.apiclient.project.CreateProjectRequest;
import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.project.ProjectCriteria;
import com.sevenbridges.apiclient.project.ProjectList;
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
import com.sevenbridges.apiclient.volume.CreateVolumeRequest;
import com.sevenbridges.apiclient.volume.ExportJob;
import com.sevenbridges.apiclient.volume.ImportJob;
import com.sevenbridges.apiclient.volume.Volume;
import com.sevenbridges.apiclient.volume.VolumeList;
import com.sevenbridges.apiclient.volume.VolumeRequestFactory;

import java.util.Map;

/**
 * The {@code UserActions} interface represents common user actions (behaviors) that can be executed
 * on a {@link User} instance <em>or</em> a {@link com.sevenbridges.apiclient.client.Client Client}
 * instance acting on behalf of its {@link com.sevenbridges.apiclient.client.Client#getCurrentUser()
 * current user}.
 */
public interface UserActions {

  ////////////////////////////////////////////////////////////////////////
  // BILLING GROUP ACTIONS
  ////////////////////////////////////////////////////////////////////////

  /**
   * Gets a collection of billing groups that the current user has access to. This call will return
   * a collection resource that you can iterate through to get all of the elements of the
   * collection. Initially only one page of the collection (default limit is 25) is fetched.
   *
   * @return {@link BillingGroupList} collection of {@link com.sevenbridges.apiclient.billing.BillingGroup}
   * represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  BillingGroupList getBillingGroups();

  /**
   * Gets a collection of billing groups that the current user has access to. This call will return
   * a collection resource that you can iterate through to get all of the elements of the
   * collection. Initially only one page of the collection (default limit is 25) is fetched.
   * <p>
   * You can specify custom query params to introduce criteria by which the billing groups will be
   * queried.
   *
   * @param queryParams Map of custom query params
   * @return {@link BillingGroupList} collection of {@link com.sevenbridges.apiclient.billing.BillingGroup}
   * represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  BillingGroupList getBillingGroups(Map<String, Object> queryParams);

  /**
   * Gets a collection of billing groups that the current user has access to. This call will return
   * a collection resource that you can iterate through to get all of the elements of the
   * collection. Initially only one page of the collection (default limit is 25) is fetched.
   * <p>
   * You can specify custom {@link BillingGroupCriteria} to introduce rules by which the billing
   * groups will be queried.
   *
   * @param criteria custom criteria to fetch billing groups by.
   * @return {@link BillingGroupList} collection of {@link com.sevenbridges.apiclient.billing.BillingGroup}
   * represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  BillingGroupList getBillingGroups(BillingGroupCriteria criteria);

  ////////////////////////////////////////////////////////////////////////
  // INVOICE ACTIONS
  ////////////////////////////////////////////////////////////////////////

  /**
   * Gets a collection of invoices for the current user. This call will return a collection resource
   * that you can iterate through to get all of the elements of the collection.
   * Initially only one page of the collection (default limit is 25) is fetched.
   *
   * @return {@link InvoiceList} collection of {@link com.sevenbridges.apiclient.invoice.Invoice}
   * represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  InvoiceList getInvoices();

  /**
   * Gets a collection of invoices for the current user. This call will return a collection resource
   * that you can iterate through to get all of the elements of the collection. Initially only one
   * page of the collection (default limit is 25) is fetched.
   * <p>
   * You can specify custom {@link InvoiceCriteria} to introduce rules by which the invoices will be
   * queried.
   *
   * @param criteria custom criteria to fetch invoices by.
   * @return {@link InvoiceList} collection of {@link com.sevenbridges.apiclient.invoice.Invoice}
   * represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  InvoiceList getInvoices(InvoiceCriteria criteria);

  ////////////////////////////////////////////////////////////////////////
  // PROJECT ACTIONS
  ////////////////////////////////////////////////////////////////////////

  /**
   * Creates a new {@link Project} resource based on the locally created project instance.
   *
   * @param project locally created project instance to save on Platform
   * @return Created Project resource
   */
  Project createProject(Project project);

  /**
   * Creates a new {@link Project} resource from the CreateProjectRequest.
   *
   * @param request request to create project resource
   * @return Created Project resource
   */
  Project createProject(CreateProjectRequest request);

  /**
   * Gets a collection of projects that the current user is a member of. This call will return a
   * collection resource that you can iterate through to get all of the elements of the collection.
   * Initially only one page of the collection (default limit is 25) is fetched.
   *
   * @return {@link ProjectList} collection of {@link Project} represented in resource collection
   * object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  ProjectList getProjects();

  /**
   * Gets a collection of projects that the current user is a member of owned by the specified user.
   * This call will return a collection resource that you can iterate through to get all of the
   * elements of the collection. Initially only one page of the collection (default limit is 25) is
   * fetched.
   *
   * @param ownerUsername String username identifier of the user
   * @return {@link ProjectList} collection of {@link Project} represented in resource collection
   * object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  ProjectList getProjects(String ownerUsername);

  /**
   * Gets a collection of projects that the current user is a member of. This call will return a
   * collection resource that you can iterate through to get all of the elements of the collection.
   * Initially only one page of the collection (default limit is 25) is fetched.
   * <p>
   * You can specify custom query params to introduce criteria by which the projects will be
   * queried.
   *
   * @param queryParams Map of custom query params
   * @return {@link ProjectList} collection of {@link Project}s represented in resource collection
   * object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  ProjectList getProjects(Map<String, Object> queryParams);

  /**
   * Gets a collection of projects that the current user is a member of. This call will return a
   * collection resource that you can iterate through to get all of the elements of the collection.
   * Initially only one page of the collection (default limit is 25) is fetched.
   * <p>
   * You can specify custom {@link ProjectCriteria} to introduce rules by which the projects will be
   * queried.
   *
   * @param criteria custom criteria to fetch projects by.
   * @return {@link ProjectList} collection of {@link Project}s represented in resource collection
   * object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  ProjectList getProjects(ProjectCriteria criteria);

  /**
   * Gets a project resource by the specified project id, string unique identifier of the {@link
   * Project} on the Platform.
   *
   * @param projectId String ID of the project to get
   * @return found Project instance
   */
  Project getProjectById(String projectId);

  ////////////////////////////////////////////////////////////////////////
  // FILE ACTIONS
  ////////////////////////////////////////////////////////////////////////

  /**
   * Gets a {@link File} resource by the specified file id, String unique identifier of the file on
   * the Platform.
   *
   * @param fileId String ID of the file to get
   * @return found File instance if any
   */
  File getFileById(String fileId);

  /**
   * Gets a collection of files from the special public project on the Platform. This public files
   * represent the collection of useful files that are accessible for each Platform user.
   * <p>
   * This call will return a collection resource that you can iterate through to get all of the
   * elements of the collection. Initially only one page of the collection (default limit is 25) is
   * fetched.
   *
   * @return {@link FileList} collection of {@link File}s represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  FileList getPublicFiles();

  /**
   * Gets a collection of files from the special public project on the Platform. This public files
   * represent the collection of useful files that are accessible for each Platform user.
   * <p>
   * This call will return a collection resource that you can iterate through to get all of the
   * elements of the collection. Initially only one page of the collection (default limit is 25) is
   * fetched.
   * <p>
   * You can specify custom {@link FileCriteria} to introduce rules by which the files will be
   * queried.
   *
   * @param criteria custom criteria to fetch files by.
   * @return {@link FileList} collection of {@link File}s represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  FileList getPublicFiles(FileCriteria criteria);

  ////////////////////////////////////////////////////////////////////////
  // APP ACTIONS
  ////////////////////////////////////////////////////////////////////////

  /**
   * Gets a collection of apps from the special public project on the Platform. This public apps
   * represent the collection of usable apps that are accessible for each Platform user.
   * <p>
   * This call will return a collection resource that you can iterate through to get all of the
   * elements of the collection. Initially only one page of the collection (default limit is 25) is
   * fetched.
   *
   * @return {@link AppList} collection of {@link App}s represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  AppList getPublicApps();

  /**
   * Gets a collection of apps that the current has access to. This call will return a collection
   * resource that you can iterate through to get all of the elements of the collection. Initially
   * only one page of the collection (default limit is 25) is fetched.
   * <p>
   * You can specify custom {@link AppCriteria} to introduce rules by which the apps will be
   * queried.
   *
   * @param criteria custom criteria to fetch apps by.
   * @return {@link AppList} collection of {@link App}s represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  AppList getApps(AppCriteria criteria);

  /**
   * Gets a {@link App} resource by the specified app id, String unique identifier of the app on
   * the Platform. If the app ID doesn't contain app version, the latest one will be fetched.
   *
   * @param appId String app id
   * @return App resource instance
   */
  App getAppById(String appId);

  ////////////////////////////////////////////////////////////////////////
  // VOLUME ACTIONS
  ////////////////////////////////////////////////////////////////////////

  /**
   * Gets a collection of volumes that the current user has access to. This call will return a
   * collection resource that you can iterate through to get all of the elements of the collection.
   * Initially only one page of the collection (default limit is 25) is fetched.
   *
   * @return {@link VolumeList} collection of {@link Volume} represented in resource collection
   * object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  VolumeList getVolumes();

  /**
   * Gets a collection of volumes that the current user has access to that are created by specified
   * user. This call will return a collection resource that you can iterate through to get all of
   * the elements of the collection. Initially only one page of the collection (default limit is 25)
   * is fetched.
   *
   * @param username String username of the user to fetch volumes for
   * @return {@link VolumeList} collection of {@link Volume} represented in resource collection
   * object
   */
  VolumeList getVolumesForUser(String username);

  /**
   * Gets a {@link Volume} resource by the specified volume id, String unique identifier of the
   * volume on the Platform.
   *
   * @param volumeId String volume id
   * @return Volume resource instance
   */
  Volume getVolumeById(String volumeId);

  /**
   * Gets the {@link VolumeRequestFactory} instance used for creating builders used to create
   * {@link CreateVolumeRequest}.
   *
   * @return VolumeRequestFactory of volume request builders
   * @see com.sevenbridges.apiclient.volume.CreateS3VolumeRequestBuilder
   * @see com.sevenbridges.apiclient.volume.CreateGcsVolumeRequestBuilder
   */
  VolumeRequestFactory getVolumeRequestFactory();

  /**
   * Invokes creation of the new volume specified with {@link CreateVolumeRequest} request.
   *
   * @param request CreateVolumeRequest containing information about volume to be created
   * @return created {@link Volume} instance
   */
  Volume createVolume(CreateVolumeRequest request);

  ////////////////////////////////////////////////////////////////////////
  // IMPORT ACTIONS
  ////////////////////////////////////////////////////////////////////////

  /**
   * Starts a new volume import job with specified params.
   *
   * @param volumeId  String unique ID of the volume to import from
   * @param location  String location on the volume to import
   * @param projectId String project ID to import volume location to
   * @param name      String name of the file created by this import job
   * @param overwrite Boolean flag specifying should the import overwrite files with same name
   * @return {@link ImportJob} created instance of import job
   */
  ImportJob startImport(String volumeId, String location,
                        String projectId, String name, Boolean overwrite);

  /**
   * Starts a new volume import job with specified params.
   *
   * @param volume    Volume resource to import from
   * @param location  String location on the volume to import
   * @param project   Project resource to import volume location to
   * @param name      String name of the file created by this import job
   * @param overwrite Boolean flag specifying should the import overwrite files with same name
   * @return {@link ImportJob} created instance of import job
   */
  ImportJob startImport(Volume volume, String location,
                        Project project, String name, Boolean overwrite);

  ////////////////////////////////////////////////////////////////////////
  // EXPORT ACTIONS
  ////////////////////////////////////////////////////////////////////////

  /**
   * Starts a new volume export job with specified params.
   *
   * @param fileId    String unique ID of the file to export
   * @param volumeId  String unique ID of the volume to export to
   * @param location  String location on the volume to export file to
   * @param overwrite Boolean flag specifying should the export overwrite
   * @return {@link ExportJob} created instance of export job
   */
  ExportJob startExport(String fileId,
                        String volumeId, String location, Boolean overwrite);

  /**
   * Starts a new volume export job with specified params.
   *
   * @param fileId     String unique ID of the file to export
   * @param volumeId   String unique ID of the volume to export to
   * @param location   String location on the volume to export file to
   * @param overwrite  Boolean flag specifying should the export overwrite
   * @param properties Map of custom params configuring this export
   * @return {@link ExportJob} created instance of export job
   */
  ExportJob startExport(String fileId,
                        String volumeId, String location, Boolean overwrite,
                        Map<String, String> properties);

  /**
   * Starts a new volume export job with specified params.
   *
   * @param file      {@link File} resource to be exported
   * @param volume    {@link Volume} resource to export the file to
   * @param location  String location on the volume to export file to
   * @param overwrite Boolean flag specifying should the export overwrite
   * @return {@link ExportJob} created instance of export job
   */
  ExportJob startExport(File file,
                        Volume volume, String location, Boolean overwrite);

  /**
   * Starts a new volume export job with specified params.
   *
   * @param file       {@link File} resource to be exported
   * @param volume     {@link Volume} resource to export the file to
   * @param location   String location on the volume to export file to
   * @param overwrite  Boolean flag specifying should the export overwrite
   * @param properties Map of custom params configuring this export
   * @return {@link ExportJob} created instance of export job
   */
  ExportJob startExport(File file,
                        Volume volume, String location, Boolean overwrite,
                        Map<String, String> properties);

  ////////////////////////////////////////////////////////////////////////
  // TASK ACTIONS
  ////////////////////////////////////////////////////////////////////////

  /**
   * Gets the {@link TaskRequestFactory} instance used for creating builders used to create
   * {@link CreateTaskRequest}.
   *
   * @return TaskRequestFactory of tasks request builders
   * @see com.sevenbridges.apiclient.task.CreateTaskRequestBuilder
   */
  TaskRequestFactory getTaskRequestFactory();

  /**
   * Creates a new {@link Task} from request created by the {@link com.sevenbridges.apiclient.task.CreateTaskRequestBuilder}.
   *
   * @param createTaskRequest request to create a task from
   * @return new {@link Task} resource instance
   */
  Task createTask(CreateTaskRequest createTaskRequest);

  /**
   * Gets a collection of tasks that the current user has access to. This call will return a
   * collection resource that you can iterate through to get all of the elements of the collection.
   * Initially only one page of the collection (default limit is 25) is fetched.
   *
   * @return {@link TaskList} collection of {@link Task} represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  TaskList getTasks();

  /**
   * Gets a collection of tasks that the current user has access to. This call will return a
   * collection resource that you can iterate through to get all of the elements of the collection.
   * Initially only one page of the collection (default limit is 25) is fetched.
   * <p>
   * You can specify custom query params to introduce criteria by which the tasks will be queried.
   *
   * @param queryParams Map of custom query params
   * @return {@link TaskList} collection of {@link Task}s represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  TaskList getTasks(Map<String, Object> queryParams);

  /**
   * Gets a {@link Task} resource by the specified task id, String unique identifier of the task on
   * the Platform.
   *
   * @param taskId String task id
   * @return Task resource instance
   */
  Task getTaskById(String taskId);

  /**
   * Gets a collection of tasks that the current user has access to. This call will return a
   * collection resource that you can iterate through to get all of the elements of the collection.
   * Initially only one page of the collection (default limit is 25) is fetched.
   * <p>
   * You can specify custom {@link TaskCriteria} to introduce rules by which the tasks will be
   * queried.
   *
   * @param criteria custom criteria to fetch tasks by.
   * @return {@link TaskList} collection of {@link Task}s represented in resource collection object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  TaskList getTasks(TaskCriteria criteria);

  ////////////////////////////////////////////////////////////////////////
  // UPLOAD ACTIONS
  ////////////////////////////////////////////////////////////////////////

  /**
   * Gets the {@link CreateUploadRequestBuilder} builder used for creating {@link
   * CreateUploadRequest} requests for uploads.
   *
   * @return new instance of CreateUploadRequestBuilder builder
   */
  CreateUploadRequestBuilder getUploadRequestBuilder();

  /**
   * Gets a collection of uploads that the current user started. This call will return a
   * collection resource that you can iterate through to get all of the elements of the collection.
   * Initially only one page of the collection (default limit is 25) is fetched.
   *
   * @return {@link UploadList} collection of {@link Upload} represented in resource collection
   * object
   * @see com.sevenbridges.apiclient.resource.CollectionResource
   */
  UploadList getUploads();

  /**
   * Gets a {@link Upload} resource by the specified upload id, String unique identifier of the
   * upload on the Platform.
   *
   * @param uploadId String upload id
   * @return Upload resource instance
   */
  Upload getUploadById(String uploadId);

  /**
   * This action initializes upload on the Platform and returns the created {@link Upload} resource.
   * This action is intended to be used for manually manged uploads, if you want to delagate
   * managing of an upload to client library use the {@link #submitUpload(CreateUploadRequest)}
   * call.
   *
   * @param uploadRequest {@link CreateUploadRequest} request created by the {@link
   *                      CreateUploadRequestBuilder}
   * @return created Upload resource instance
   */
  Upload initUpload(CreateUploadRequest uploadRequest);

  /**
   * This action delegates {@link CreateUploadRequest} to the internally managed TransferManager
   * that will upload the file specified in the request. This call returns {@link UploadContext}
   * object that you can use to control the upload.
   *
   * @param uploadRequest {@link CreateUploadRequest} request created by the {@link
   *                      CreateUploadRequestBuilder}
   * @return {@link UploadContext} of the created {@link Upload}
   */
  UploadContext submitUpload(CreateUploadRequest uploadRequest);

  /**
   * This action delegates {@link CreateUploadRequest} to the internally managed TransferManager
   * that will upload the file specified in the request. This call returns {@link UploadContext}
   * object that you can use to control the upload.
   * <p>
   * You can also provide your implementation of {@link ProgressListener} that listens to events of
   * the upload.
   *
   * @param uploadRequest {@link CreateUploadRequest} request created by the {@link
   *                      CreateUploadRequestBuilder}
   * @param listener      Custom upload listener to catch upload events with
   * @return {@link UploadContext} of the created {@link Upload}
   */
  UploadContext submitUpload(CreateUploadRequest uploadRequest, ProgressListener listener);

  /**
   * This action resumes the already started upload, that is paused for some reason (by calling the
   * {@link UploadContext#pauseTransfer()} action or by shutting down the whole client). This upload
   * will continue to be managed by the internal transfer manager. Provided file MUST be the same
   * as the file that started the upload. No additional validation on that will be issued.
   *
   * @param pausedContext paused UploadContext to be resumed
   * @param file          File to be resumed uploading
   * @return new {@link UploadContext} instance to mange the upload with
   */
  UploadContext resumeUpload(UploadContext pausedContext, java.io.File file);

  /**
   * This action resumes the already started upload, that is paused for some reason (by calling the
   * {@link UploadContext#pauseTransfer()} action or by shutting down the whole client). This upload
   * will continue to be managed by the internal transfer manager. Provided file MUST be the same as
   * the file that started the upload. No additional validation on that will be issued.
   * <p>
   * You can also provide your implementation of {@link ProgressListener} that listens to events of
   * the upload.
   *
   * @param pausedContext paused UploadContext to be resumed
   * @param file          File to be resumed uploading
   * @param listener      Custom upload listener to catch upload events with
   * @return new {@link UploadContext} instance to mange the upload with
   */
  UploadContext resumeUpload(UploadContext pausedContext, java.io.File file, ProgressListener listener);

  /**
   * This action resumes the already started upload, that is paused for some reason (by calling the
   * {@link UploadContext#pauseTransfer()} action or by shutting down the whole client). This upload
   * will be managed by the internal transfer manager. Provided file MUST be the same
   * as the file that started the upload. No additional validation on that will be issued.
   *
   * @param pausedUpload paused Upload to be resumed
   * @param file         File to be resumed uploading
   * @return new {@link UploadContext} instance to mange the upload with
   */
  UploadContext resumeUpload(Upload pausedUpload, java.io.File file);

  /**
   * This action resumes the already started upload, that is paused for some reason (by calling the
   * {@link UploadContext#pauseTransfer()} action or by shutting down the whole client). This upload
   * will be managed by the internal transfer manager. Provided file MUST be the same as the file
   * that started the upload. No additional validation on that will be issued.
   * <p>
   * You can also provide your implementation of {@link ProgressListener} that listens to events of
   * the upload.
   *
   * @param pausedUpload paused Upload to be resumed
   * @param file         File to be resumed uploading
   * @param listener     Custom upload listener to catch upload events with
   * @return new {@link UploadContext} instance to mange the upload with
   */
  UploadContext resumeUpload(Upload pausedUpload, java.io.File file, ProgressListener listener);

  /**
   * Gracefully shuts down the transfer service if any upload was started during the lifecycle of
   * client. It is good practice to call this function before quiting application that is using this
   * library.
   */
  void shutdownTransferService();

}
