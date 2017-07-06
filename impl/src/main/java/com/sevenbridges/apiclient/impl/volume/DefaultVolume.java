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
package com.sevenbridges.apiclient.impl.volume;

import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractInstanceResource;
import com.sevenbridges.apiclient.impl.resource.BooleanProperty;
import com.sevenbridges.apiclient.impl.resource.CollectionReference;
import com.sevenbridges.apiclient.impl.resource.DateProperty;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.volume.AccessMode;
import com.sevenbridges.apiclient.volume.ExportJob;
import com.sevenbridges.apiclient.volume.ExportJobList;
import com.sevenbridges.apiclient.volume.GcsCredentials;
import com.sevenbridges.apiclient.volume.GcsVolumeService;
import com.sevenbridges.apiclient.volume.ImportJob;
import com.sevenbridges.apiclient.volume.ImportJobList;
import com.sevenbridges.apiclient.volume.S3Credentials;
import com.sevenbridges.apiclient.volume.S3VolumeService;
import com.sevenbridges.apiclient.volume.Volume;
import com.sevenbridges.apiclient.volume.VolumeService;
import com.sevenbridges.apiclient.volume.VolumeType;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultVolume extends AbstractInstanceResource implements Volume {

  static final StringProperty ID = new StringProperty("id");
  static final StringProperty NAME = new StringProperty("name");
  static final StringProperty DESCRIPTION = new StringProperty("description");
  static final StringProperty ACCESS_MODE = new StringProperty("access_mode");
  static final DateProperty CREATED_ON = new DateProperty("created_on");
  static final DateProperty MODIFIED_ON = new DateProperty("modified_on");
  static final MapProperty SERVICE = new MapProperty("service");
  static final BooleanProperty ACTIVE = new BooleanProperty("active");

  // COLLECTION RESOURCE REFERENCES:
  static final CollectionReference<ImportJobList, ImportJob> IMPORT_JOBS =
      new CollectionReference<>("import_jobs", ImportJobList.class, ImportJob.class);

  static final CollectionReference<ExportJobList, ExportJob> EXPORT_JOBS =
      new CollectionReference<>("export_jobs", ExportJobList.class, ExportJob.class);

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      ID, NAME, DESCRIPTION, ACCESS_MODE, CREATED_ON, MODIFIED_ON, SERVICE, ACTIVE,
      IMPORT_JOBS, EXPORT_JOBS
  );

  static final int H_IMPORTS = 0;
  static final int H_EXPORTS = 1;

  static final String[] HREF_REFERENCES = new String[]{
      "/storage/imports",
      "/storage/exports"
  };

  public DefaultVolume(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultVolume(InternalDataStore dataStore, Map<String, Object> properties) {
    super(dataStore, properties);
  }

  @Override
  public Map<String, Property> getPropertyDescriptors() {
    return PROPERTY_DESCRIPTORS;
  }

  @Override
  public int getPropertiesCount() {
    return PROPERTY_DESCRIPTORS.size() - 2;
  }

  ////////////////////////////////////////////////////////////////////////
  // SIMPLE PROPERTIES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public String getVolumeId() {
    return getString(ID);
  }

  @Override
  public String getName() {
    return getString(NAME);
  }

  @Override
  public Volume setName(String name) {
    Assert.hasText(name, "Name cannot be null or empty");
    setProperty(NAME, name);
    return this;
  }

  @Override
  public String getDescription() {
    return getString(DESCRIPTION);
  }

  @Override
  public Volume setDescription(String description) {
    setProperty(DESCRIPTION, description);
    return this;
  }

  @Override
  public AccessMode getAccessMode() {
    return AccessMode.valueOf(getString(ACCESS_MODE));
  }

  @Override
  public Volume setAccessMode(AccessMode accessMode) {
    Assert.notNull(accessMode, "AccessMode object cannot be null");
    setProperty(ACCESS_MODE, accessMode.name());
    return this;
  }

  @Override
  public Boolean isActive() {
    return getBoolean(ACTIVE);
  }

  @Override
  public Volume setActive(boolean active) {
    setProperty(ACTIVE, active);
    return this;
  }

  @Override
  public VolumeService getService() {
    String type = (String) getMapPropertyEntry(SERVICE, "type");
    if (VolumeType.S3.name().equals(type)) {
      return new DefaultS3VolumeService();
    } else if (VolumeType.GCS.name().equals(type)) {
      return new DefaultGcsVolumeService();
    }
    return null;
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
  // COLLECTION RESOURCE REFERENCES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public ImportJobList getImports() {
    final String volumeId = getVolumeId();
    if (volumeId == null) {
      return null;
    }
    Map<String, Object> queryParam = new HashMap<>(1);
    queryParam.put("volume", volumeId);
    return getDataStore().getResource(HREF_REFERENCES[H_IMPORTS], ImportJobList.class, queryParam);
  }

  @Override
  public ExportJobList getExports() {
    final String volumeId = getVolumeId();
    if (volumeId == null) {
      return null;
    }
    Map<String, Object> queryParam = new HashMap<>(1);
    queryParam.put("volume", volumeId);
    return getDataStore().getResource(HREF_REFERENCES[H_EXPORTS], ExportJobList.class, queryParam);
  }

  ////////////////////////////////////////////////////////////////////////
  // RESOURCE ACTIONS
  ////////////////////////////////////////////////////////////////////////

  @Override
  public void delete() {
    getDataStore().delete(this);
  }

  ////////////////////////////////////////////////////////////////////////
  // INNER CLASSES
  ////////////////////////////////////////////////////////////////////////

  @SuppressWarnings("unchecked")
  class DefaultS3VolumeService implements S3VolumeService {

    private final MapProperty serviceProperty;

    static final String TYPE = "type";
    static final String PREFIX = "prefix";
    static final String BUCKET = "bucket";
    static final String ROOT_URL = "root_url";
    static final String SSE_ALGORITHM = "sse_algorithm";
    static final String AWS_CANNED_ACL = "aws_canned_acl";
    static final String PROPERTIES = "properties";

    DefaultS3VolumeService() {
      this.serviceProperty = DefaultVolume.SERVICE;
    }

    @Override
    public S3Credentials getCredentials() {
      return new DefaultS3Credentials(serviceProperty);
    }

    @Override
    public String getType() {
      return VolumeType.S3.name();
    }

    @Override
    public S3VolumeService setType(VolumeType type) {
      Assert.notNull(type, "Type object cannot be null");
      DefaultVolume.this.setMapPropertyEntry(serviceProperty, TYPE, type.name());
      return this;
    }

    @Override
    public String getPrefix() {
      return (String) DefaultVolume.this.getMapPropertyEntry(serviceProperty, PREFIX);
    }

    @Override
    public S3VolumeService setPrefix(String prefix) {
      Assert.hasText(prefix, "Prefix cannot be null or empty");
      DefaultVolume.this.setMapPropertyEntry(serviceProperty, PREFIX, prefix);
      return this;
    }

    @Override
    public String getBucket() {
      return (String) DefaultVolume.this.getMapPropertyEntry(serviceProperty, BUCKET);
    }

    @Override
    public S3VolumeService setBucket(String bucket) {
      Assert.hasText(bucket, "Bucket cannot be null or empty");
      DefaultVolume.this.setMapPropertyEntry(serviceProperty, BUCKET, bucket);
      return this;
    }

    @Override
    public String getRootUrl() {
      return (String) DefaultVolume.this.getMapPropertyEntry(serviceProperty, ROOT_URL);
    }

    @Override
    public S3VolumeService setRootUrl(String rootUrl) {
      Assert.hasText(rootUrl, "RootUrl cannot be null or empty");
      DefaultVolume.this.setMapPropertyEntry(serviceProperty, ROOT_URL, rootUrl);
      return this;
    }

    @Override
    public String getSseAlgorithm() {
      Map<String, String> properties = (Map<String, String>) DefaultVolume.this.getMapPropertyEntry(serviceProperty, PROPERTIES);
      if (properties == null) {
        return null;
      }
      return properties.get(SSE_ALGORITHM);
    }

    @Override
    public S3VolumeService setSseAlgorithm(String sseAlgorithm) {
      Map<String, String> properties = (Map<String, String>) DefaultVolume.this.getMapPropertyEntry(serviceProperty, PROPERTIES);
      if (properties == null) {
        properties = new HashMap<>(2);
        if (sseAlgorithm != null) {
          properties.put(SSE_ALGORITHM, sseAlgorithm);
        }
      } else {
        if (sseAlgorithm != null) {
          properties.put(SSE_ALGORITHM, sseAlgorithm);
        } else {
          properties.remove(SSE_ALGORITHM);
        }
      }
      DefaultVolume.this.setMapPropertyEntry(serviceProperty, PROPERTIES, properties);
      return this;
    }

    @Override
    public String getAwsCannedAcl() {
      Map<String, String> properties = (Map<String, String>) DefaultVolume.this.getMapPropertyEntry(serviceProperty, PROPERTIES);
      if (properties == null) {
        return null;
      }
      return properties.get(AWS_CANNED_ACL);
    }

    @Override
    public S3VolumeService setAwsCannedAcl(String awsCannedAcl) {
      Map<String, String> properties = (Map<String, String>) DefaultVolume.this.getMapPropertyEntry(serviceProperty, PROPERTIES);
      if (properties == null) {
        properties = new HashMap<>(2);
        if (awsCannedAcl != null) {
          properties.put(AWS_CANNED_ACL, awsCannedAcl);
        }
      } else {
        if (awsCannedAcl != null) {
          properties.put(AWS_CANNED_ACL, awsCannedAcl);
        } else {
          properties.remove(AWS_CANNED_ACL);
        }
      }
      DefaultVolume.this.setMapPropertyEntry(serviceProperty, PROPERTIES, properties);
      return this;
    }

  }

  class DefaultS3Credentials implements S3Credentials {

    private final MapProperty serviceProperty;

    static final String CREDENTIALS = "credentials";
    static final String ACCESS_KEY = "access_key_id";
    static final String SECRET_KEY = "secret_access_key";

    private DefaultS3Credentials(MapProperty serviceProperty) {
      this.serviceProperty = serviceProperty;
    }

    @Override
    public S3Credentials setCredentials(String accessKey, String secretKey) {
      Assert.hasText(accessKey, "accessKey cannot be null or empty");
      Assert.hasText(secretKey, "secretKey cannot be null or empty");
      Map<String, String> credentials = new LinkedHashMap<>(2);
      credentials.put(ACCESS_KEY, accessKey);
      credentials.put(SECRET_KEY, secretKey);
      setMapPropertyEntry(serviceProperty, CREDENTIALS, credentials);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getAccessKey() {
      Map<String, String> credentials = (Map<String, String>) DefaultVolume.this.getMapPropertyEntry(serviceProperty, CREDENTIALS);
      return credentials.get(ACCESS_KEY);
    }


    @Override
    @SuppressWarnings("unchecked")
    public String getSecretKey() {
      Map<String, String> credentials = (Map<String, String>) DefaultVolume.this.getMapPropertyEntry(serviceProperty, CREDENTIALS);
      return credentials.get(SECRET_KEY);
    }
  }

  class DefaultGcsVolumeService implements GcsVolumeService {

    static final String TYPE = "type";
    static final String PREFIX = "prefix";
    static final String BUCKET = "bucket";
    static final String ROOT_URL = "root_url";
    static final String PROPERTIES = "properties";

    private final MapProperty serviceProperty;

    DefaultGcsVolumeService() {
      this.serviceProperty = DefaultVolume.SERVICE;
    }

    @Override
    public GcsCredentials getCredentials() {
      return null;
    }

    @Override
    public String getType() {
      return VolumeType.GCS.name();
    }

    @Override
    public VolumeService setType(VolumeType type) {
      Assert.notNull(type, "Type object cannot be null");
      DefaultVolume.this.setMapPropertyEntry(serviceProperty, TYPE, type.name());
      return this;
    }

    @Override
    public String getPrefix() {
      return (String) DefaultVolume.this.getMapPropertyEntry(serviceProperty, PREFIX);
    }

    @Override
    public VolumeService setPrefix(String prefix) {
      Assert.hasText(prefix, "Prefix cannot be null or empty");
      DefaultVolume.this.setMapPropertyEntry(serviceProperty, PREFIX, prefix);
      return this;
    }

    @Override
    public String getBucket() {
      return (String) DefaultVolume.this.getMapPropertyEntry(serviceProperty, BUCKET);
    }

    @Override
    public VolumeService setBucket(String bucket) {
      Assert.hasText(bucket, "Bucket cannot be null or empty");
      DefaultVolume.this.setMapPropertyEntry(serviceProperty, BUCKET, bucket);
      return this;
    }

    @Override
    public String getRootUrl() {
      return (String) DefaultVolume.this.getMapPropertyEntry(serviceProperty, ROOT_URL);
    }

    @Override
    public VolumeService setRootUrl(String rootUrl) {
      Assert.hasText(rootUrl, "rootUrl cannot be null or empty");
      DefaultVolume.this.setMapPropertyEntry(serviceProperty, ROOT_URL, rootUrl);
      return this;
    }
  }

  class DefaultGcsCredentials implements GcsCredentials {

    static final String CLIENT_MAIL = "client_email";
    static final String PRIVATE_KEY = "private_key";
    static final String CREDENTIALS = "credentials";

    private final MapProperty serviceProperty;

    DefaultGcsCredentials(MapProperty serviceProperty) {
      this.serviceProperty = serviceProperty;
    }

    @Override
    public GcsCredentials setCredentials(String clientEmail, String privateKey) {
      Assert.hasText(clientEmail, "ClientEmail cannot be null or empty");
      Assert.hasText(privateKey, "PrivateKey cannot be null or empty");
      Map<String, String> credentials = new LinkedHashMap<>(2);
      credentials.put(CLIENT_MAIL, clientEmail);
      credentials.put(PRIVATE_KEY, privateKey);
      setMapPropertyEntry(serviceProperty, CREDENTIALS, credentials);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getClientEmail() {
      Map<String, String> credentials = (Map<String, String>) DefaultVolume.this.getMapPropertyEntry(serviceProperty, CREDENTIALS);
      return credentials.get(CLIENT_MAIL);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getPrivateKey() {
      Map<String, String> credentials = (Map<String, String>) DefaultVolume.this.getMapPropertyEntry(serviceProperty, CREDENTIALS);
      return credentials.get(PRIVATE_KEY);
    }

  }
}
