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
package com.sevenbridges.apiclient.impl.project;

import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractInstanceResource;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.impl.resource.SubResourceProperty;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.project.Member;
import com.sevenbridges.apiclient.project.Members;

import java.util.Map;

public class DefaultMember extends AbstractInstanceResource implements Member {

  // SIMPLE PROPERTIES:
  static final StringProperty USERNAME = new StringProperty("username");
  static final SubResourceProperty PERMISSIONS = new SubResourceProperty("permissions");

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      USERNAME, PERMISSIONS
  );

  public DefaultMember(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultMember(InternalDataStore dataStore, Map<String, Object> properties) {
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
  public void delete() {
    getDataStore().delete(this);
  }

  ////////////////////////////////////////////////////////////////////////
  // SIMPLE PROPERTIES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public String getUsername() {
    return getString(USERNAME);
  }

  @Override
  public Member setUsername(String username) {
    Assert.hasText(username, "Username property cannot be null or empty");
    setProperty(USERNAME, username);
    return this;
  }

  @Override
  public Map<String, Boolean> getPermissions() {
    return getMap(PERMISSIONS);
  }

  @Override
  public Member setPermissions(Map<String, Boolean> permissions) {
    Assert.notNull(permissions, "Permissions object cannot be null");
    Map<String, Boolean> validatedPermissions;
    if (this.isNew()) {
      validatedPermissions = Members.validateNewPermissions(permissions);
    } else {
      validatedPermissions = Members.validatePatchPermissions(permissions);
    }
    setProperty(PERMISSIONS, validatedPermissions);
    return this;
  }

}
