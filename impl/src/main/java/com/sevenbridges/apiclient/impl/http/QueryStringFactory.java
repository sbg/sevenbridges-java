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

import com.sevenbridges.apiclient.impl.http.support.DefaultCanonicalUri;
import com.sevenbridges.apiclient.impl.query.DefaultCriteria;
import com.sevenbridges.apiclient.impl.query.DefaultOptions;
import com.sevenbridges.apiclient.impl.query.Expansion;
import com.sevenbridges.apiclient.impl.query.ListExpression;
import com.sevenbridges.apiclient.impl.query.SimpleExpression;
import com.sevenbridges.apiclient.lang.Collections;
import com.sevenbridges.apiclient.lang.Strings;
import com.sevenbridges.apiclient.query.Criterion;

import java.util.List;
import java.util.Map;

public class QueryStringFactory {

  public static QueryString createQueryString(String href) {

    QueryString query = new QueryString();

    CanonicalUri uri = DefaultCanonicalUri.create(href, null);
    if (uri.hasQuery()) {
      // Query params embedded directly in the href, if any, take precedence.
      // Overwrite any from the criteria:
      query.putAll(uri.getQuery());
    }

    return query;
  }

  public QueryString createQueryString(String href, DefaultCriteria criteria) {

    QueryString query = createQueryString(criteria);

    CanonicalUri uri = DefaultCanonicalUri.create(href, null);
    if (uri.hasQuery()) {
      // Query params embedded directly in the href, if any, take precedence.
      // Overwrite any from the criteria:
      query.putAll(uri.getQuery());
    }

    return query;
  }

  @SuppressWarnings("unchecked")
  public QueryString createQueryString(DefaultCriteria criteria) {
    QueryString qs = new QueryString();

    if (criteria == null || criteria.isEmpty()) {
      return qs;
    }

    List<Criterion> criterionList = criteria.getCriterionEntries();
    List<Expansion> expansionList = criteria.getExpansions();

    if (!Collections.isEmpty(criterionList)) {
      addCriterionEntries(qs, criterionList);
    }

    Integer offset = criteria.getOffset();
    if (offset != null) {
      String value = String.valueOf(offset);
      qs.set("offset", value);
    }

    Integer limit = criteria.getLimit();
    if (limit != null) {
      String value = String.valueOf(limit);
      qs.set("limit", value);
    }

    applyExpansions(qs, expansionList);

    return qs;
  }

  public QueryString createQueryString(String href, DefaultOptions defaultOptions) {

    QueryString query = createQueryString(defaultOptions);

    CanonicalUri uri = DefaultCanonicalUri.create(href, null);
    if (uri.hasQuery()) {
      //Query params embedded directly in the href, if any, take precedence. Overwrite any from the criteria:
      query.putAll(uri.getQuery());
    }

    return query;
  }

  @SuppressWarnings("unchecked")
  public QueryString createQueryString(DefaultOptions options) {
    QueryString qs = new QueryString();
    List<Expansion> expansions = options.getExpansions();
    applyExpansions(qs, expansions);
    return qs;
  }

  private void applyExpansions(QueryString qs, List<Expansion> expansions) {
    if (!Collections.isEmpty(expansions)) {
      String expand = Strings.collectionToCommaDelimitedString(expansions);
      qs.set("expand", expand);
    }
  }

  public QueryString createQueryString(Map<String, ?> params) {
    if (params instanceof QueryString) {
      return (QueryString) params;
    }
    if (params == null) {
      return new QueryString();
    }
    return new QueryString(params);
  }

  private void addCriterionEntries(QueryString qs, List<Criterion> entries) {

    for (Criterion c : entries) {
      //NOTE: add new expressions here
      if (c instanceof SimpleExpression) {
        SimpleExpression se = (SimpleExpression) c;
        String queryParamName = se.getPropertyName();
        Object value = se.getValue();
        String queryParamValue = String.valueOf(value);
        qs.add(queryParamName, queryParamValue);
      } else if (c instanceof ListExpression) {
        ListExpression le = (ListExpression) c;
        String queryParamName = le.getPropertyName();
        List<?> values = le.getValue();
        for (Object v : values) {
          qs.add(queryParamName, String.valueOf(v));
        }
      } else {
        throw new IllegalArgumentException("Unexpected Criterion type: " + c);
      }
    }
  }
}
