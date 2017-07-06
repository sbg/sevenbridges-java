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

import com.sevenbridges.apiclient.file.File;
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractInstanceResource;
import com.sevenbridges.apiclient.impl.resource.BooleanProperty;
import com.sevenbridges.apiclient.impl.resource.DateProperty;
import com.sevenbridges.apiclient.impl.resource.ListProperty;
import com.sevenbridges.apiclient.impl.resource.LongProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.resource.ResourceException;
import com.sevenbridges.apiclient.upload.PartUpload;
import com.sevenbridges.apiclient.upload.Upload;
import com.sevenbridges.apiclient.upload.UploadedPart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultUpload extends AbstractInstanceResource implements Upload {

  // SIMPLE PROPERTIES:
  static final StringProperty UPLOAD_ID = new StringProperty("upload_id");
  static final StringProperty PROJECT = new StringProperty("project");
  static final StringProperty NAME = new StringProperty("name");
  static final StringProperty MD5 = new StringProperty("md5");
  static final LongProperty SIZE = new LongProperty("size");
  static final LongProperty PART_SIZE = new LongProperty("part_size");
  static final DateProperty INITIATED_ON = new DateProperty("initiated");
  static final BooleanProperty PARALLEL_UPLOADS = new BooleanProperty("parallel_uploads");
  static final LongProperty UPLOADED_PARTS_COUNT = new LongProperty("uploaded_parts_count");
  static final ListProperty PARTS = new ListProperty("parts");

  static final String[] HREF_REFERENCES = new String[]{
      "/upload",
      "/upload/multipart"
  };

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      UPLOAD_ID, PROJECT, NAME, MD5, SIZE, PART_SIZE, INITIATED_ON, PARALLEL_UPLOADS, UPLOADED_PARTS_COUNT, PARTS
  );

  private static final int PROPERTIES_COUNT = PROPERTY_DESCRIPTORS.size();

  public DefaultUpload(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultUpload(InternalDataStore dataStore, Map<String, Object> properties) {
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
  public String getUploadId() {
    return getString(UPLOAD_ID);
  }

  @Override
  public String getProjectId() {
    return getString(PROJECT);
  }

  @Override
  public Upload setProjectId(String projectId) {
    setProperty(PROJECT, projectId);
    return this;
  }

  @Override
  public String getName() {
    return getString(NAME);
  }

  @Override
  public Upload setName(String name) {
    setProperty(NAME, name);
    return this;
  }

  @Override
  public String getMD5() {
    return getString(MD5);
  }

  @Override
  public Upload setMD5(String md5) {
    setProperty(MD5, md5);
    return this;
  }

  @Override
  public long getSize() {
    return getLong(SIZE);
  }

  @Override
  public Upload setSize(long size) {
    setProperty(SIZE, size);
    return this;
  }

  @Override
  public long getPartSize() {
    return getLong(PART_SIZE);
  }

  @Override
  public Upload setPartSize(long partSize) {
    setProperty(PART_SIZE, partSize);
    return this;
  }

  @Override
  public Date getInitiatedOn() {
    return getDate(INITIATED_ON);
  }

  @Override
  public boolean isParallelUploads() {
    return getBoolean(PARALLEL_UPLOADS);
  }

  @Override
  public long getUploadedPartsCount() {
    return getLong(UPLOADED_PARTS_COUNT);
  }

  @Override
  public List<UploadedPart> getUploadedParts() {
    List<Map<String, Object>> parts = getList(PARTS);
    if (parts == null) {
      DefaultUpload uploadWithParts = (DefaultUpload) getDataStore().getResource(getHref(), Upload.class, Collections.<String, Object>singletonMap("list_parts", true));
      this.setProperties(uploadWithParts.getInternalProperties());
    }
    parts = getList(PARTS);
    if (parts == null) {
      return Collections.emptyList();
    } else {
      List<UploadedPart> retVal = new ArrayList<>(parts.size());
      for (Map<String, Object> part : parts) {
        UploadedPart partInstance = getDataStore().instantiate(UploadedPart.class, part);
        retVal.add(partInstance);
      }
      return retVal;
    }
  }

  ////////////////////////////////////////////////////////////////////////
  // RESOURCE ACTIONS
  ////////////////////////////////////////////////////////////////////////

  @Override
  public void abortUpload() throws ResourceException {
    getDataStore().delete(this);
  }

  @Override
  public PartUpload getPartUpload(int partNumber) throws ResourceException {
    return getDataStore().getResource(this.getHref() + "/part/" + partNumber, PartUpload.class);
  }

  @Override
  public void reportUploadedPart(int partNumber, Map<String, Object> uploadResponse) {
    Map<String, Object> requestProps = new HashMap<>(2);
    requestProps.put("part_number", partNumber);
    requestProps.put("response", uploadResponse);
    getDataStore().resourceAction("/part", this, this.getClass(), null, requestProps);
  }

  @Override
  public File completeUpload() throws ResourceException {
    return getDataStore().resourceAction("/complete", this, File.class, null, null);
  }
}
