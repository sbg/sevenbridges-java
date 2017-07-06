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

import com.sevenbridges.apiclient.impl.http.CanonicalUri;
import com.sevenbridges.apiclient.impl.http.HttpHeaders;
import com.sevenbridges.apiclient.resource.Resource;

import java.util.Map;

public class DefaultResourceDataRequest extends DefaultResourceMessage implements ResourceDataRequest {

  public DefaultResourceDataRequest(ResourceAction action, CanonicalUri uri, Class<? extends Resource> resourceClass, Map<String, Object> data) {
    super(action, uri, resourceClass, data);
  }

  public DefaultResourceDataRequest(ResourceAction action, CanonicalUri uri, Class<? extends Resource> resourceClass, Map<String, Object> data, HttpHeaders customHeaders) {
    super(action, uri, resourceClass, data, customHeaders);
  }
}
