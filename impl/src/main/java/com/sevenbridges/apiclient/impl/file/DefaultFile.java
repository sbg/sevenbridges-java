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

import com.sevenbridges.apiclient.file.DownloadInfo;
import com.sevenbridges.apiclient.file.File;
import com.sevenbridges.apiclient.file.FileOrigin;
import com.sevenbridges.apiclient.file.FileStorage;
import com.sevenbridges.apiclient.file.StorageType;
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractInstanceResource;
import com.sevenbridges.apiclient.impl.resource.DateProperty;
import com.sevenbridges.apiclient.impl.resource.LongProperty;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.SetProperty;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.project.Project;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultFile extends AbstractInstanceResource implements File {

  // SIMPLE PROPERTIES:
  static final StringProperty ID = new StringProperty("id");
  static final StringProperty NAME = new StringProperty("name");
  static final LongProperty SIZE = new LongProperty("size");
  static final StringProperty PROJECT = new StringProperty("project");
  static final MapProperty METADATA = new MapProperty("metadata");
  static final DateProperty CREATED_ON = new DateProperty("created_on");
  static final DateProperty MODIFIED_ON = new DateProperty("modified_on");
  static final SetProperty<String> TAGS = new SetProperty<>("tags", String.class);
  static final MapProperty STORAGE = new MapProperty("storage");
  static final MapProperty ORIGIN = new MapProperty("origin");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      ID, NAME, SIZE, PROJECT, METADATA,
      CREATED_ON, MODIFIED_ON,
      STORAGE, ORIGIN
  );

  private static final int PROPERTIES_COUNT = PROPERTY_DESCRIPTORS.size();

  static final int H_ACTION_COPY = 0;
  static final int H_PROJECTS = 1;
  static final int H_DOWNLOAD = 2;

  static final String[] HREF_REFERENCES = new String[]{
      "/actions/copy",
      "/projects/",
      "/download_info"
  };

  public DefaultFile(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultFile(InternalDataStore dataStore, Map<String, Object> properties) {
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

  @Override
  public void delete() {
    getDataStore().delete(this);
  }

  ////////////////////////////////////////////////////////////////////////
  // SIMPLE PROPERTIES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public String getId() {
    return getString(ID);
  }

  @Override
  public String getName() {
    return getString(NAME);
  }

  @Override
  public File setName(String name) {
    Assert.hasText(name, "Name property cannot be null or empty");
    setProperty(NAME, name);
    return this;
  }

  @Override
  public long getSize() {
    return getLong(SIZE);
  }

  @Override
  public String getProjectId() {
    return getString(PROJECT);
  }

  @Override
  public Map<String, String> getMetadata() {
    return getMap(METADATA);
  }

  @Override
  public File setMetadata(Map<String, String> metadata) {
    Assert.notNull(metadata, "Metadata object cannot be null");
    setProperty(METADATA, metadata);
    return this;
  }

  @Override
  public File patchMetadata(Map<String, String> metadataPatch) {
    Assert.notNull(metadataPatch, "MetadataPatch object cannot be null");
    Map<String, String> current = getMap(METADATA);
    Map<String, String> patched = new HashMap<>(current.size() + metadataPatch.size());
    patched.putAll(current);
    patched.putAll(metadataPatch);
    setProperty(METADATA, patched);
    return this;
  }

  @Override
  public FileStorage getFileStorage() {
    return new DefaultFileStorage(STORAGE);
  }

  @Override
  public FileOrigin getFileOrigin() {
    return new DefaultFileOrigin(ORIGIN);
  }

  @Override
  public DownloadInfo getDownloadInfo() {
    return getDataStore().getResource(this.getHref() + HREF_REFERENCES[H_DOWNLOAD], DownloadInfo.class);
  }

  @Override
  public Set<String> getTags() {
    Set<String> tagsSet = getSet(TAGS);
    if (tagsSet == null) {
      tagsSet = Collections.emptySet();
    }
    return tagsSet;
  }

  @Override
  public File setTags(Set<String> newTags) {
    Assert.notNull(newTags, "NewTags object cannot be null");
    setProperty(TAGS, newTags);
    return this;
  }

  @Override
  public Date getCreatedOn() {
    return getDate(CREATED_ON);
  }

  @Override
  public Date getModifiedOn() {
    return getDate(MODIFIED_ON);
  }

  ////////////////////////////////////////////////////////////////////////
  // INSTANCE RESOURCE REFERENCES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public Project getProject() {
    final String projectId = getProjectId();
    if (projectId == null) {
      return null;
    }
    return getDataStore().getResource(HREF_REFERENCES[H_PROJECTS] + projectId, Project.class);
  }

  @Override
  public File copy(Project destinationProject) {
    Assert.notNull(destinationProject, "DestinationProject object cannot be null");
    return copy(destinationProject, this.getName());
  }

  @Override
  public File copy(Project destinationProject, String newName) {
    Assert.notNull(destinationProject, "Destination project must not be null");
    Assert.notNull(destinationProject.getId(), "Destination project id must not be null");
    Assert.notNull(newName, "Name of the file must not be null");
    Map<String, Object> body = new HashMap<>(2);
    body.put("name", newName);
    body.put("project", destinationProject.getId());
    Map<String, Object> destinationFileProps = new HashMap<>(1);
    destinationFileProps.put("href", this.getHref());
    File destinationFile = getDataStore().instantiate(File.class, destinationFileProps);
    return getDataStore().resourceAction(HREF_REFERENCES[H_ACTION_COPY], destinationFile, File.class, null, body);
  }

  private class DefaultFileOrigin implements FileOrigin {

    private final MapProperty prop;

    private DefaultFileOrigin(MapProperty originProperty) {
      this.prop = originProperty;
    }

    @Override
    public String getTask() {
      return (String) getMapPropertyEntry(prop, "task");
    }

  }

  private class DefaultFileStorage implements FileStorage {

    private final MapProperty prop;

    private DefaultFileStorage(MapProperty storageProperty) {
      this.prop = storageProperty;
    }

    @Override
    public StorageType getType() {
      return StorageType.fromValue((String) getMapPropertyEntry(prop, "type"));
    }

    @Override
    public String getVolume() {
      return (String) getMapPropertyEntry(prop, "volume");
    }

    @Override
    public String getLocation() {
      return (String) getMapPropertyEntry(prop, "location");
    }
  }

}
