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

import com.sevenbridges.apiclient.app.App;
import com.sevenbridges.apiclient.app.AppCriteria;
import com.sevenbridges.apiclient.app.AppList;
import com.sevenbridges.apiclient.app.Apps;
import com.sevenbridges.apiclient.billing.BillingGroup;
import com.sevenbridges.apiclient.file.File;
import com.sevenbridges.apiclient.file.FileCriteria;
import com.sevenbridges.apiclient.file.FileList;
import com.sevenbridges.apiclient.file.Files;
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.resource.AbstractInstanceResource;
import com.sevenbridges.apiclient.impl.resource.CollectionReference;
import com.sevenbridges.apiclient.impl.resource.MapProperty;
import com.sevenbridges.apiclient.impl.resource.Property;
import com.sevenbridges.apiclient.impl.resource.SetProperty;
import com.sevenbridges.apiclient.impl.resource.StringProperty;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.project.Member;
import com.sevenbridges.apiclient.project.MemberList;
import com.sevenbridges.apiclient.project.Members;
import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.query.Criteria;
import com.sevenbridges.apiclient.task.TaskCriteria;
import com.sevenbridges.apiclient.task.TaskList;
import com.sevenbridges.apiclient.task.Tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultProject extends AbstractInstanceResource implements Project {

  // SIMPLE PROPERTIES:
  static final StringProperty ID = new StringProperty("id");
  static final StringProperty NAME = new StringProperty("name");
  static final StringProperty TYPE = new StringProperty("type");
  @SuppressWarnings("unchecked")
  static final SetProperty<String> TAGS = new SetProperty("tags", String.class);
  static final StringProperty DESCRIPTION = new StringProperty("description");
  static final StringProperty BILLING_GROUP = new StringProperty("billing_group");
  static final MapProperty SETTINGS = new MapProperty("settings");

  // COLLECTION RESOURCE REFERENCES:
  static final CollectionReference<FileList, File> FILES =
      new CollectionReference<>("files", FileList.class, File.class);

  static final CollectionReference<MemberList, Member> MEMBERS =
      new CollectionReference<>("members", MemberList.class, Member.class);

  static final CollectionReference<AppList, App> APPS =
      new CollectionReference<>("apps", AppList.class, App.class);

  static final int H_FILES = 0;
  static final int H_MEMBERS = 1;
  static final int H_APPS = 2;
  static final int H_RAW = 3;
  static final int H_TASKS = 4;
  static final int H_ACTION_COPY = 5;

  static final String[] HREF_REFERENCES = new String[]{
      "/files",
      "/members",
      "/apps",
      "/raw",
      "/tasks",
      "/action/files/copy"
  };

  private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
      ID, NAME, TYPE, TAGS, DESCRIPTION, SETTINGS,
      BILLING_GROUP,
      FILES, MEMBERS, APPS
  );

  private static final int PROPERTIES_COUNT = PROPERTY_DESCRIPTORS.size() - 3;

  public DefaultProject(InternalDataStore dataStore) {
    super(dataStore);
  }

  public DefaultProject(InternalDataStore dataStore, Map<String, Object> properties) {
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
  public Project setName(String name) {
    Assert.hasText(name, "Name cannot be null or empty");
    setProperty(NAME, name);
    return this;
  }

  @Override
  public String getType() {
    return getString(TYPE);
  }

  @Override
  public Project setType(String type) {
    Assert.hasText(type, "Type cannot be null or empty");
    setProperty(TYPE, type);
    return this;
  }

  @Override
  public Set<String> getTags() {
    return getSet(TAGS);
  }

  @Override
  public Project setTags(Set<String> tags) {
    Assert.notNull(tags, "Tags object cannot be null");
    setProperty(TAGS, tags);
    return this;
  }

  @Override
  public String getDescription() {
    return getString(DESCRIPTION);
  }

  @Override
  public Project setDescription(String description) {
    setProperty(DESCRIPTION, description);
    return this;
  }

  @Override
  public String getBillingGroupId() {
    return getString(BILLING_GROUP);
  }

  @Override
  public Project setBillingGroupId(String billingGroupId) {
    Assert.hasText(billingGroupId, "BillingGroupId cannot be null or empty");
    setProperty(BILLING_GROUP, billingGroupId);
    return this;
  }

  @Override
  public Boolean getLocked() {
    return (Boolean) getMapPropertyEntry(SETTINGS, "locked");
  }

  @Override
  public Project setLocked(Boolean locked) {
    Assert.notNull(locked, "Locked object cannot be null");
    setMapPropertyEntry(SETTINGS, "locked", locked);
    return this;
  }

  ////////////////////////////////////////////////////////////////////////
  // INSTANCE RESOURCE REFERENCES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public BillingGroup getBillingGroup() {
    final String billingGroupId = getBillingGroupId();
    if (billingGroupId == null) {
      return null;
    }
    final String[] href = new String[]{"/billing/groups/" + billingGroupId};
    return getDataStore().getResource(href[0], BillingGroup.class);
  }

  ////////////////////////////////////////////////////////////////////////
  // COLLECTION RESOURCE REFERENCES
  ////////////////////////////////////////////////////////////////////////

  @Override
  public FileList getFiles() {
    final String projectId = getId();
    if (projectId == null) {
      return null;
    }
    return getDataStore().getResource(HREF_REFERENCES[H_FILES], FileList.class,
        (Criteria<FileCriteria>) Files.criteria().forProject(projectId));
  }

  @Override
  public FileList getFiles(Map<String, Object> queryParams) {
    final String projectId = getId();
    if (projectId == null) {
      return null;
    }
    queryParams.put("project", projectId);
    return getDataStore().getResource(HREF_REFERENCES[H_FILES], FileList.class, queryParams);
  }

  @Override
  public FileList getFiles(FileCriteria criteria) {
    final String projectId = getId();
    if (projectId == null) {
      return null;
    }
    criteria.forProject(projectId);
    return getDataStore().getResource(HREF_REFERENCES[H_FILES], FileList.class,
        (Criteria<FileCriteria>) criteria);
  }

  @Override
  public MemberList getMembers() {
    return getDataStore().getResource(getHref() + HREF_REFERENCES[H_MEMBERS], MemberList.class);
  }

  @Override
  public Member getMemberById(String memberId) {
    Assert.hasText(memberId, "Member id must not be null");
    return getDataStore().getResource(
        getHref() + HREF_REFERENCES[H_MEMBERS] + "/" + memberId,
        Member.class
    );
  }

  @Override
  public AppList getApps() {
    final String projectId = getId();
    if (projectId == null) {
      return null;
    }
    return getDataStore().getResource(HREF_REFERENCES[H_APPS], AppList.class,
        (Criteria<AppCriteria>) Apps.criteria().forProject(projectId));
  }

  @Override
  public AppList getApps(Map<String, Object> queryParams) {
    final String projectId = getId();
    if (projectId == null) {
      return null;
    }
    queryParams.put("project", projectId);
    return getDataStore().getResource(HREF_REFERENCES[H_APPS], AppList.class, queryParams);
  }

  @Override
  public AppList getApps(AppCriteria criteria) {
    final String projectId = getId();
    if (projectId == null) {
      return null;
    }
    criteria.forProject(projectId);
    return getDataStore().getResource(HREF_REFERENCES[H_APPS], AppList.class,
        (Criteria<AppCriteria>) criteria);
  }

  ////////////////////////////////////////////////////////////////////////
  // RESOURCE ACTIONS
  ////////////////////////////////////////////////////////////////////////

  @Override
  public Member addMember(Member newMember) {
    Assert.notNull(newMember, "NewMember object cannot be null");
    Assert.hasText(newMember.getUsername(), "New member must have a username");
    if (newMember.getPermissions() == null) {
      newMember.setPermissions(Members.getDefaultPermissions());
    }
    return getDataStore().create(getHref() + "/members", newMember);
  }

  @Override
  public void removeMember(Member member) {
    Assert.notNull(member, "Can not remove 'null' member");
    Assert.hasText(member.getUsername(), "Username property of member must be filled");
    removeMember(member.getUsername());
  }

  @Override
  public void removeMember(String username) {
    Assert.hasText(username, "Username cannot be null or empty");
    Member memberToRemove = getDataStore()
        .getResource(getHref() + "/members/" + username, Member.class);
    getDataStore().delete(memberToRemove);
  }

  @Override
  public TaskList getTasks() {
    return getDataStore().getResource(
        HREF_REFERENCES[H_TASKS],
        TaskList.class,
        (Criteria<TaskCriteria>) Tasks.criteria().forProject(this)
    );
  }

  @Override
  public TaskList getTasks(TaskCriteria criteria) {
    if (criteria == null) {
      criteria = Tasks.criteria();
    }
    return getDataStore().getResource(
        HREF_REFERENCES[H_TASKS],
        TaskList.class,
        (Criteria<TaskCriteria>) criteria.forProject(this)
    );
  }

  @Override
  public App installApp(String appName, Map<String, Object> raw) {
    Assert.hasText(appName, "New app must have a name");
    Map<String, Object> appProperties = new HashMap<>(1);
    appProperties.put("href", String.format("%s/%s/%s",
        HREF_REFERENCES[H_APPS], this.getId(), appName));
    App app = getDataStore().instantiate(App.class, appProperties);
    String actionHref = HREF_REFERENCES[H_RAW];
    getDataStore().resourceAction(actionHref, app, App.class, new HashMap<String, Object>(), raw);
    return getDataStore().getResource((String)appProperties.get("href"), App.class);
  }

  @Override
  public App createRevision(String appName, int revision, Map<String, Object> raw) {
    Assert.hasText(appName, "App revision must have a name");

    Map<String, Object> appProperties = new HashMap<>(1);
    appProperties.put("href", String.format("%s/%s/%s/%s",
        HREF_REFERENCES[H_APPS], this.getId(), appName, revision));
    App app = getDataStore().instantiate(App.class, appProperties);
    String actionHref = HREF_REFERENCES[H_RAW];
    getDataStore().resourceAction(actionHref, app, App.class, new HashMap<String, Object>(), raw);
    return getDataStore().getResource((String)appProperties.get("href"), App.class);
  }

}
