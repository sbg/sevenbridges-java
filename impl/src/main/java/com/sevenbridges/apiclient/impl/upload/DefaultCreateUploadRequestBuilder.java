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
package com.sevenbridges.apiclient.impl.upload;

import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.upload.CreateUploadRequest;
import com.sevenbridges.apiclient.upload.CreateUploadRequestBuilder;
import com.sevenbridges.apiclient.upload.Upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DefaultCreateUploadRequestBuilder implements CreateUploadRequestBuilder {

  private final InternalDataStore dataStore;
  private String name;
  private String project;
  private RandomAccessFile file;
  private boolean overwrite = false;
  private long partSize = -1L;
  private String md5 = null;

  public DefaultCreateUploadRequestBuilder(InternalDataStore dataStore) {
    this.dataStore = dataStore;
  }

  @Override
  public CreateUploadRequestBuilder setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public CreateUploadRequestBuilder setProject(String projectId) {
    this.project = projectId;
    return this;
  }

  @Override
  public CreateUploadRequestBuilder setProject(Project project) {
    this.project = project.getId();
    return this;
  }

  @Override
  public CreateUploadRequestBuilder setOverwrite(boolean overwrite) {
    this.overwrite = overwrite;
    return this;
  }

  @Override
  public CreateUploadRequestBuilder setFile(File file) {
    if (file == null) {
      throw new IllegalArgumentException("File to upload can not be null");
    }
    if (!file.exists()) {
      throw new IllegalArgumentException("Provided file '" + file.getName() + "' doesn't exist");
    }
    if (!file.isFile()) {
      throw new IllegalArgumentException("Provided file descriptor '" + file.getName() + "' is not a file");
    }
    try {
      this.file = new RandomAccessFile(file, "r");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(e);
    }
    return this;
  }

  @Override
  public CreateUploadRequestBuilder setFile(String filePath) {
    return setFile(new File(filePath));
  }

  @Override
  public CreateUploadRequestBuilder setPartSize(long partSize) {
    this.partSize = partSize;
    return this;
  }

  /**
   * Sets the MD5 hash digest of the file to be uploaded. This value is not validated, only stored,
   * and is not a required param for upload.
   *
   * @param digest String md5 digest of the file to upload
   * @return builder instance with md5 digest set
   */
  @Override
  public CreateUploadRequestBuilder setMD5(String digest) {
    this.md5 = digest;
    return null;
  }

  @Override
  public CreateUploadRequest build() {
    Assert.notNull(name, "Field 'name' is required");
    Assert.notNull(project, "Field 'project' is required");
    Assert.notNull(file, "File to upload must be provided");

    Upload upload = dataStore.instantiate(Upload.class);
    upload.setName(name);
    upload.setProjectId(project);
    try {
      long fileSize = file.length();
      upload.setSize(fileSize);
    } catch (IOException e) {
      throw new IllegalArgumentException("Error while determining provided file size", e);
    }

    if (partSize > 0) {
      upload.setPartSize(partSize);
    }

    if (md5 != null) {
      upload.setMD5(md5);
    }

    return new DefaultCreateUploadRequest(upload, file, overwrite);
  }
}
