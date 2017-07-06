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
package com.sevenbridges.apiclient.impl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QueryParamsMultiMap extends HashMap<String, List<String>> implements MultiValueMap<String, String> {

  public QueryParamsMultiMap() {
    super();
  }

  public QueryParamsMultiMap(MultiValueMap<String, String> source) {
    super(source.size());
    for (Map.Entry<String, List<String>> e : source.entrySet()) {
      this.put(e.getKey(), new ArrayList<>(e.getValue()));
    }
  }

  @Override
  public String getFirst(String key) {
    List<String> values = get(key);
    if (values != null && values.size() > 0) {
      return values.get(0);
    } else {
      return null;
    }
  }

  @Override
  public void add(String key, String value) {
    List<String> l = getList(key);

    if (value != null) {
      l.add(value);
    }
  }

  @Override
  public void set(String key, String value) {
    List<String> l = getList(key);

    l.clear();
    if (value != null) {
      l.add(value);
    }
  }

  @Override
  public void setAll(Map<String, String> values) {
    for (Map.Entry<String, String> entry : values.entrySet()) {
      set(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public Map<String, String> toSingleValueMap() {
    HashMap<String, String> singleValueMap = new HashMap<>(this.size());
    for (String key : this.keySet()) {
      String value = this.getFirst(key);
      if (value != null) {
        singleValueMap.put(key, value);
      }
    }
    return singleValueMap;
  }

  private List<String> getList(String key) {
    List<String> l = get(key);
    if (l == null) {
      l = new LinkedList<>();
      put(key, l);
    }
    return l;
  }
}
