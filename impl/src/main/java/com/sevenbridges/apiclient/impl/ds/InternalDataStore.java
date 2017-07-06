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
package com.sevenbridges.apiclient.impl.ds;

import com.sevenbridges.apiclient.ds.DataStore;
import com.sevenbridges.apiclient.impl.http.HttpHeaders;
import com.sevenbridges.apiclient.query.Criteria;
import com.sevenbridges.apiclient.query.Options;
import com.sevenbridges.apiclient.resource.Resource;
import com.sevenbridges.apiclient.resource.Saveable;

import java.util.Map;

/**
 * Internal DataStore used for implementation purposes only.  Not intended to be called by
 * SevenBridges Java library end users!
 * <p>
 * <b>WARNING: This API CAN CHANGE AT ANY TIME, WITHOUT NOTICE.  DO NOT DEPEND ON IT.</b>
 */
public interface InternalDataStore extends DataStore {

  <T extends Resource> T instantiate(Class<T> clazz, Map<String, Object> properties);

  /**
   * Instantiates and returns a new instance of the specified Resource type. The instance is merely
   * instantiated and is not saved/synchronized with the server in any way. This operation allows
   * the {@code href} to be a fragment, where the {@code baseUrl} can be missing and will be added
   * automatically.
   *
   * @param clazz        the Resource class to instantiate.
   * @param <T>          the Resource sub-type
   * @param properties   the properties the instantiated resource will have
   * @param hrefFragment when {@code true}, the baseUrl will be appended to the value found in the
   *                     href key of the properties map. If {@code false} the href will not be
   *                     altered and will be kept as-is.
   * @return a resource instance corresponding to the specified clazz.
   */
  <T extends Resource> T instantiate(Class<T> clazz, Map<String, Object> properties, boolean hrefFragment);

  <T extends Resource> T create(String parentHref, T resource);

  <T extends Resource> T create(String parentHref, T resource, Options options);

  <T extends Resource, R extends Resource> R create(String parentHref, T resource, Class<? extends R> returnType);

  <T extends Resource, R extends Resource> R create(String parentHref, T resource, Class<? extends R> returnType, HttpHeaders customHeaders);

  <T extends Resource, R extends Resource> R create(String parentHref, T resource, Class<? extends R> returnType, Options options);

  <T extends Resource, R extends Resource> R create(String parentHref, T resource, Class<? extends R> returnType, Map<String, Object> queryParams);

  <T extends Resource & Saveable> void save(T resource);

  <T extends Resource & Saveable> void save(T resource, Options options);

  <T extends Resource & Saveable, R extends Resource> R save(T resource, Class<? extends R> returnType);

  <T extends Resource & Saveable> void update(T resource);

  <T extends Resource & Saveable> void update(T resource, Options options);

  <T extends Resource & Saveable> void update(T resource, boolean force);

  <T extends Resource & Saveable> void update(T resource, boolean force, Options options);

  <T extends Resource> void delete(T resource);

  <T extends Resource> void deleteResourceProperty(T resource, String propertyName);

  <T extends Resource> T getResource(String href, Class<T> clazz, Map<String, Object> queryParameters);

  <T extends Resource> T getResource(String href, Class<T> clazz, Criteria criteria);

  <T extends Resource, R extends T> R getResource(String href, Class<T> parent, String childIdProperty, Map<String, Class<? extends R>> stringClassMap);

  <T extends Resource, R extends Resource> R resourceAction(String actionHref, T resource, Class<? extends R> returnType, Map<String, Object> queryParams);

  <T extends Resource, R extends Resource> R resourceAction(String actionHref, T resource, Class<? extends R> returnType, Map<String, Object> queryParams, Map<String, Object> bodyParams);

  void reload(String resourceHref, Class<? extends Resource> resourceType, Resource resource);
}
