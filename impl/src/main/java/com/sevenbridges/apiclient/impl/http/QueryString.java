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
package com.sevenbridges.apiclient.impl.http;

import com.sevenbridges.apiclient.impl.util.QueryParamsMultiMap;
import com.sevenbridges.apiclient.impl.util.RequestUtils;
import com.sevenbridges.apiclient.lang.Collections;
import com.sevenbridges.apiclient.lang.Strings;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class QueryString extends QueryParamsMultiMap {

  public QueryString() {
  }

  public QueryString(Map<String, ?> source) {
    super();
    if (!Collections.isEmpty(source)) {
      for (Map.Entry<String, ?> entry : source.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        if (value instanceof Collection) {
          for (Object v : (Collection) value) {
            if (v != null) {
              this.add(key, String.valueOf(v));
            }
          }
        } else {
          String sValue = value != null ? String.valueOf(value) : null;
          set(key, sValue);
        }
      }
    }
  }

  public String toString() {
    return toString(false);
  }

  /**
   * The canonicalized query string is formed by first sorting all the query string parameters, then
   * URI encoding both the key and value and then joining them, in order, separating key value pairs
   * with an '&amp;'.
   *
   * @param canonical whether or not the string should be canonicalized
   * @return the canonical query string
   */
  public String toString(boolean canonical) {
    if (isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();

    for (Map.Entry<String, List<String>> entry : entrySet()) {
      String key = RequestUtils.encodeUrl(entry.getKey(), false, canonical);
      for (String value : entry.getValue()) {
        String encodedValue = RequestUtils.encodeUrl(value, false, canonical);
        if (sb.length() > 0) {
          sb.append('&');
        }
        sb.append(key).append("=").append(encodedValue);
      }
    }

    return sb.toString();
  }

  public static QueryString create(String query) {
    if (!Strings.hasLength(query)) {
      return null;
    }

    QueryString queryString = new QueryString();

    // only returns null if string is null
    String[] tokens = Strings.tokenizeToStringArray(query, "&", false, false);
    for (String token : tokens) {
      applyKeyValuePair(queryString, token);
    }

    return queryString;
  }

  private static void applyKeyValuePair(QueryString qs, String kv) {

    String[] pair = Strings.split(kv, "=");

    if (pair != null) {
      String key = pair[0];
      String value = pair[1] != null ? pair[1] : "";
      qs.add(key, value);
    } else {
      //no equals sign, it's just a key:
      qs.add(kv, null);
    }
  }

}
