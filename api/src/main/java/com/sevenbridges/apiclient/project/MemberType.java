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
package com.sevenbridges.apiclient.project;

/**
 * Project {@link Member}s can be differentiated by their type.
 * <p>
 * MemberType enumeration contains valid values for Member type.
 */
public enum MemberType {
  USER("USER"),
  ORG("ORG"),
  TEAM("TEAM");

  public final String name;

  MemberType(String name) {
    this.name = name.toUpperCase();
  }

  public String getName() {
    return this.name;
  }

  public String toString() {
    return this.name;
  }

  public static MemberType getByValue(String value) {
    for (MemberType type : MemberType.values()) {
      if (type.getName().equals(value.toUpperCase())) {
        return type;
      }
    }
    return null;
  }
}