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
package com.sevenbridges.apiclient.impl.resource;

import com.sevenbridges.apiclient.impl.ds.Enlistment;
import com.sevenbridges.apiclient.impl.ds.InternalDataStore;
import com.sevenbridges.apiclient.impl.http.QueryString;
import com.sevenbridges.apiclient.impl.http.QueryStringFactory;
import com.sevenbridges.apiclient.lang.Assert;
import com.sevenbridges.apiclient.lang.Strings;
import com.sevenbridges.apiclient.resource.CollectionResource;
import com.sevenbridges.apiclient.resource.Resource;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractResource implements Resource {

  private static final Logger log = LoggerFactory.getLogger(AbstractResource.class);

  private static final DateFormat DATE_FORMAT = new ISO8601DateFormat();

  public static final String HREF_PROP_NAME = "href";

  protected Map<String, Object> properties;            //Protected by read/write lock
  protected final Map<String, Object> dirtyProperties; //Protected by read/write lock
  protected final Set<String> deletedPropertyNames;    //Protected by read/write lock
  private final InternalDataStore dataStore;
  protected final Lock readLock;
  protected final Lock writeLock;

  private volatile boolean materialized;
  protected volatile boolean dirty;

  protected final ReferenceFactory referenceFactory;

  protected AbstractResource(InternalDataStore dataStore) {
    this(dataStore, null);
  }

  protected AbstractResource(InternalDataStore dataStore, Map<String, Object> properties) {
    this.referenceFactory = new ReferenceFactory();
    ReadWriteLock rwl = new ReentrantReadWriteLock();
    this.readLock = rwl.readLock();
    this.writeLock = rwl.writeLock();
    this.dataStore = dataStore;
    this.dirtyProperties = new LinkedHashMap<>();
    this.deletedPropertyNames = new HashSet<>();
    if (properties instanceof Enlistment) {
      this.properties = properties;
    } else {
      this.properties = new LinkedHashMap<>();
    }
    setProperties(properties);
  }

  @Override
  public void reload() {
    if (this.properties == null || this.properties.get(HREF_PROP_NAME) == null) {
      throw new RuntimeException("Resource must have HREF param to be able to reload");
    }
    this.dataStore.reload((String) this.properties.get(HREF_PROP_NAME), this.getClass(), this);
  }

  /**
   * Returns {@code true} if the specified data map represents a materialized resource data set,
   * {@code false} otherwise.
   *
   * @param props the data properties to test
   * @return {@code true} if the specified data map represents a materialized resource data set,
   * {@code false} otherwise.
   */
  public static boolean isMaterialized(Map<String, ?> props) {
    return props != null && props.get(HREF_PROP_NAME) != null && props.size() > 1;
  }

  protected static Map<String, Property> createPropertyDescriptorMap(Property... props) {
    Map<String, Property> m = new LinkedHashMap<>();
    for (Property prop : props) {
      m.put(prop.getName(), prop);
    }
    return m;
  }

  public abstract Map<String, Property> getPropertyDescriptors();

  public abstract int getPropertiesCount();

  public final void setProperties(Map<String, Object> properties) {
    writeLock.lock();
    try {
      this.dirtyProperties.clear();
      this.dirty = false;
      if (properties != null && !properties.isEmpty()) {
        if (this.properties instanceof Enlistment && this.properties != properties) {
          this.properties.clear();
          this.properties.putAll(properties);
        } else {
          this.properties = properties;
        }
        // Don't consider this resource materialized if it is only a reference.
        // A reference is any object that has only one 'href' property.
        // A reference can also contain few other properties.
        this.materialized = !(this.properties.containsKey(HREF_PROP_NAME) && this.properties.size() < getPropertiesCount());
      } else {
        this.materialized = false;
      }
    } finally {
      writeLock.unlock();
    }
  }

  public void refreshProperties(Map<String, Object> properties) {
    writeLock.lock();
    try {
      for (String propertyName : properties.keySet()) {
        this.dirtyProperties.remove(propertyName);
      }
      if (this.dirtyProperties.size() == 0) {
        this.dirty = false;
      }
      this.properties.putAll(properties);
    } finally {
      writeLock.unlock();
    }
  }

  public Map<String, Object> getInternalProperties() {
    readLock.lock();
    try {
      return new HashMap<>(this.properties);
    } finally {
      readLock.unlock();
    }
  }

  public String getHref() {
    return getStringProperty(HREF_PROP_NAME);
  }

  protected final InternalDataStore getDataStore() {
    return this.dataStore;
  }

  protected final boolean isMaterialized() {
    return this.materialized;
  }

  /**
   * Returns {@code true} if the resource's properties have been modified in anyway since the
   * resource instance was created.
   *
   * @return {@code true} {@code true} if the resource's properties have been modified in anyway
   * since the resource instance was created
   */
  public final boolean isDirty() {
    return this.dirty;
  }

  /**
   * Returns {@code true} if the resource doesn't yet have an assigned 'href' property, {@code
   * false} otherwise.
   *
   * @return {@code true} if the resource doesn't yet have an assigned 'href' property, {@code
   * false} otherwise.
   */
  protected final boolean isNew() {
    // we can't call getHref() in here, otherwise we'll have an infinite loop:
    Object prop = readProperty(HREF_PROP_NAME);
    if (prop == null) {
      return true;
    }
    String href = String.valueOf(prop);
    return !Strings.hasText(href);
  }

  protected void materialize() {
    AbstractResource resource = dataStore.getResource(getHref(), getClass());
    writeLock.lock();
    try {
      if (this.properties != resource.properties) {
        if (!(this.properties instanceof Enlistment)) {
          this.properties = resource.properties;
        } else {
          this.properties.clear();
          this.properties.putAll(resource.properties);
        }
      }

      //retain dirty properties:
      this.properties.putAll(this.dirtyProperties);

      this.materialized = true;
    } finally {
      writeLock.unlock();
    }
  }

  public Set<String> getPropertyNames() {
    readLock.lock();
    try {
      Set<String> keys = this.properties.keySet();
      return new LinkedHashSet<>(keys);
    } finally {
      readLock.unlock();
    }
  }

  public Set<String> getUpdatedPropertyNames() {
    readLock.lock();
    try {
      Set<String> keys = this.dirtyProperties.keySet();
      return new LinkedHashSet<>(keys);
    } finally {
      readLock.unlock();
    }
  }

  public Set<String> getUpdatedSubResourceNames() {
    readLock.lock();
    try {
      Set<String> keys = new LinkedHashSet<>();
      for (Map.Entry<String, Object> propertyEntry : this.dirtyProperties.entrySet()) {
        if (getPropertyDescriptors().get(propertyEntry.getKey()) instanceof SubResourceProperty) {
          keys.add(propertyEntry.getKey());
        }
      }
      return keys;
    } finally {
      readLock.unlock();
    }
  }

  protected Set<String> getDeletedPropertyNames() {
    readLock.lock();
    try {
      return new LinkedHashSet<>(this.deletedPropertyNames);
    } finally {
      readLock.unlock();
    }
  }

  public Object getProperty(String name) {
    if (!HREF_PROP_NAME.equals(name)) {
      // not the href/id, must be a property that requires materialization:
      if (!isNew() && !isMaterialized()) {

        // only materialize if the property hasn't been set previously (no need to execute a server
        // request since we have the most recent value already):
        boolean present = false;
        readLock.lock();
        try {
          present = this.dirtyProperties.containsKey(name) || this.properties.containsKey(name);
        } finally {
          readLock.unlock();
        }

        if (!present) {
          // exhausted present properties - we require a server call:
          materialize();
        }
      }
    }

    return readProperty(name);
  }

  private Object readProperty(String name) {
    readLock.lock();
    try {
      if (this.deletedPropertyNames.contains(name)) {
        return null;
      }
      Object value = this.dirtyProperties.get(name);
      if (value == null) {
        value = this.properties.get(name);
      }
      return value;
    } finally {
      readLock.unlock();
    }
  }

  protected void setProperty(Property property, Object value) {
    setProperty(property.getName(), value, true);
  }

  protected void setProperty(String name, Object value) {
    setProperty(name, value, true);
  }

  protected Object setProperty(String name, Object value, final boolean dirty) {
    writeLock.lock();
    Object previous;
    try {
      if (dirty) {
        previous = this.dirtyProperties.put(name, value);
        if (previous == null) {
          previous = this.properties.get(name);
        }
        this.dirty = true;
        if (this.deletedPropertyNames.contains(name)) {
          this.deletedPropertyNames.remove(name);
        }
      } else {
        previous = this.dirtyProperties.remove(name);
        if (previous == null) {
          previous = this.properties.put(name, value);
        }
        if (this.dirtyProperties.size() > 0) {
          this.dirty = false;
        }
        if (this.deletedPropertyNames.contains(name)) {
          this.deletedPropertyNames.remove(name);
        }
      }

    } finally {
      writeLock.unlock();
    }
    return previous;
  }

  protected void setMapPropertyEntry(Property property, String key, Object value) {
    if (!Map.class.isAssignableFrom(property.getType())) {
      throw new IllegalArgumentException("Property '" + property.getName() + "' is not a map property");
    }
    setMapPropertyEntry(property.getName(), key, value, true);
  }

  protected void setMapPropertyEntry(String name, String key, Object value) {
    setMapPropertyEntry(name, key, value, true);
  }

  @SuppressWarnings("unchecked")
  protected Object setMapPropertyEntry(String name, String key, Object value, final boolean dirty) {
    writeLock.lock();
    Object previous = null;
    try {
      if (dirty) {
        Object previousMap = this.dirtyProperties.get(name);
        if (previousMap != null) { // there was a value
          if (!Map.class.isAssignableFrom(previousMap.getClass())) {
            throw new IllegalArgumentException("Property '" + name + "' is not a map property");
          } else {
            previous = ((Map<String, Object>) previousMap).put(key, value);
            if (previous == null) {
              previous = value;
            }
          }
        } else { // there was no dirty property
          // getting mapProperty value
          Object propertyMap = this.properties.get(name);
          if (propertyMap != null) {
            if (!Map.class.isAssignableFrom(propertyMap.getClass())) {
              throw new IllegalArgumentException("Property '" + name + "' is not a map property");
            }
            previous = ((Map<String, Object>) propertyMap).get(key);
          }
          Map<String, Object> dirtyMapProp = new LinkedHashMap<>(1);
          dirtyMapProp.put(key, value);
          this.dirtyProperties.put(name, dirtyMapProp);
        }
        this.dirty = true;

      } else { //not dirty
        Object previousMap = this.dirtyProperties.get(name);
        if (previousMap != null) {
          // check if map, if not throw exception
          if (!Map.class.isAssignableFrom(previousMap.getClass())) {
            throw new IllegalArgumentException("Property '" + name + "' is not a map property");
          }
          // remove value from map, if map is empty, remove it from dirtyProps
          previous = ((Map<String, Object>) previousMap).remove(key);
          if (((Map<String, Object>) previousMap).isEmpty()) {
            this.dirtyProperties.remove(name);
          }
        }
        // check if property is map
        previousMap = this.properties.get(name);
        if (previousMap == null) {
          Map<String, Object> mapProp = new LinkedHashMap<>(1);
          mapProp.put(key, value);
          this.properties.put(name, mapProp);
        } else { //there is a property with that name, check if map
          if (!Map.class.isAssignableFrom(previousMap.getClass())) {
            throw new IllegalArgumentException("Property '" + name + "' is not a map property");
          } // it is a map, so new entry put inside
          Object nonDirtyPrevious = ((Map<String, Object>) previousMap).put(key, value);
          if (previous == null) {
            previous = nonDirtyPrevious;
          }
        }

        if (this.dirtyProperties.size() > 0) {
          this.dirty = false;
        }
      }

    } finally {
      writeLock.unlock();
    }
    return previous;
  }

  @SuppressWarnings("unchecked")
  protected Object getMapPropertyEntry(MapProperty property, String entryKey) {
    Object mapProperty = getProperty(property.getName());
    if (mapProperty == null) {
      return null;
    }
    if (!Map.class.isAssignableFrom(mapProperty.getClass())) {
      throw new IllegalArgumentException("Property '" + property.getName() + "' is not a map property");
    }
    readLock.lock();
    Object entryVal = null;
    try {
      entryVal = ((Map<String, Object>) mapProperty).get(entryKey);
    } finally {
      readLock.unlock();
    }
    return entryVal;
  }

  protected String getString(StringProperty property) {
    return getStringProperty(property.getName());
  }

  protected String getStringProperty(String key) {
    Object value = getProperty(key);
    if (value == null) {
      return null;
    }
    return String.valueOf(value);
  }

  protected Date getDate(DateProperty property) {
    return getDateProperty(property.getName());
  }

  protected Date getDateProperty(String key) {
    Object value = getProperty(key);
    if (value == null) {
      return null;
    }
    try {
      return DATE_FORMAT.parse(String.valueOf(value));
    } catch (ParseException e) {
      if (log.isErrorEnabled()) {
        log.error("Unable to parse string '{}' into an date value.  Defaulting to null.", e);
      }
    }
    return null;
  }

  protected int getInt(IntegerProperty property) {
    return getIntProperty(property.getName());
  }

  protected int getIntProperty(String key) {
    Object value = getProperty(key);
    if (value != null) {
      if (value instanceof String) {
        return parseInt((String) value);
      } else if (value instanceof Number) {
        return ((Number) value).intValue();
      }
    }
    return -1;
  }

  protected long getLong(LongProperty property) {
    return getLongProperty(property.getName());
  }

  protected long getLongProperty(String key) {
    Object value = getProperty(key);
    if (value != null) {
      if (value instanceof String) {
        return parseLong((String) value);
      } else if (value instanceof Number) {
        return ((Number) value).longValue();
      }
    }
    return -1;
  }

  protected boolean getBoolean(BooleanProperty property) {
    return getBooleanProperty(property.getName());
  }

  /**
   * Returns an actual boolean value instead of a possible null Boolean value since desired usage is
   * to have either a true or false.
   */
  protected boolean getBooleanProperty(String key) {
    Object value = getProperty(key);
    if (value != null) {
      if (value instanceof Boolean) {
        return (Boolean) value;
      } else if (value instanceof String) {
        return Boolean.valueOf((String) value);
      }
    }
    return Boolean.FALSE;
  }

  @SuppressWarnings("unchecked")
  protected <T> List<T> getList(ListProperty property) {
    return getListProperty(property.getName());
  }

  /**
   * Returns the {@link List} property identified by {@code key}.
   */
  protected List getListProperty(String key) {
    Object value = getProperty(key);
    if (value != null) {
      if (value instanceof List) {
        return (List) value;
      }
      String msg = "'" + key + "' property value type does not match the specified type. Specified type: List. " +
          "Existing type: " + value.getClass().getName();
      msg += (isPrintableProperty(key) ? ".  Value: " + value : ".");
      throw new IllegalArgumentException(msg);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  protected <T> Set<T> getSet(SetProperty property) {
    return getSetProperty(property.getName());
  }

  /**
   * Returns the {@link Set} property identified by {@code key}.
   */
  @SuppressWarnings("unchecked")
  protected Set getSetProperty(String key) {
    Object value = getProperty(key);
    if (value != null) {
      if (value instanceof Set) {
        return (Set) value;
      } else if (value instanceof List) {
        return new HashSet((List) value);
      }
      String msg = "'" + key + "' property value type does not match the specified type. Specified type: Set. " +
          "Existing type: " + value.getClass().getName();
      msg += (isPrintableProperty(key) ? ".  Value: " + value : ".");
      throw new IllegalArgumentException(msg);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  protected <K, V> Map<K, V> getMap(MapProperty property) {
    return getMapProperty(property.getName());
  }

  @SuppressWarnings("unchecked")
  protected <K, V> Map<K, V> getMap(SubResourceProperty property) {
    return getMapProperty(property.getName());
  }

  public Map getMapProperty(String key) {
    Object value = getProperty(key);
    if (value != null) {
      if (value instanceof Map) {
        return (Map) value;
      }
      String msg = "'" + key + "' property value type does not match the specified type. Specified type: Map. " +
          "Existing type: " + value.getClass().getName();
      msg += (isPrintableProperty(key) ? ".  Value: " + value : ".");
      throw new IllegalArgumentException(msg);
    }
    return null;
  }

  protected String getHrefQueryProperty(String key) {
    String href = getStringProperty(HREF_PROP_NAME);
    QueryString qs = QueryStringFactory.createQueryString(href);
    return qs.getFirst(key);
  }

  private int parseInt(String value) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      if (log.isErrorEnabled()) {
        String msg = "Unable to parse string '{}' into an integer value.  Defaulting to -1";
        log.error(msg, e);
      }
    }
    return -1;
  }

  private long parseLong(String value) {
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException e) {
      if (log.isErrorEnabled()) {
        String msg = "Unable to parse string '{}' into an long value.  Defaulting to -1";
        log.error(msg, e);
      }
    }
    return -1;
  }

  @SuppressWarnings("unchecked")
  protected <T extends Resource> T getResourceProperty(ResourceReference<T> property) {
    String key = property.getName();
    Class<T> clazz = property.getType();

    Object value = getProperty(key);
    if (value == null) {
      return null;
    }
    if (clazz.isInstance(value)) {
      return (T) value;
    }
    if (value instanceof Map && !((Map) value).isEmpty()) {
      T resource = dataStore.instantiate(clazz, (Map<String, Object>) value);

      // replace the existing link object (map with an href) with the newly constructed Resource instance.
      // Don't dirty the instance - we're just swapping out a property that already exists for the materialized version.
      // let's not materialize internal collection resources, so they are always retrieved from the backend
      if (!CollectionResource.class.isAssignableFrom(clazz)) {
        setProperty(key, resource, false);
      }
      return resource;
    }

    String msg = "'" + key + "' property value type does not match the specified type.  Specified type: " +
        clazz.getName() + ".  Existing type: " + value.getClass().getName();
    msg += (isPrintableProperty(key) ? ".  Value: " + value : ".");
    throw new IllegalArgumentException(msg);
  }

  protected <T extends Resource> void setResourceProperty(ResourceReference<T> property, Resource value) {
    Assert.notNull(property, "Property argument cannot be null.");
    String name = property.getName();
    Map<String, String> reference = this.referenceFactory.createReference(name, value);
    setProperty(name, reference);
  }

  public String toString() {
    readLock.lock();
    try {
      StringBuilder sb = new StringBuilder();
      for (Map.Entry<String, Object> entry : this.properties.entrySet()) {
        if (sb.length() > 0) {
          sb.append(", ");
        }
        String key = entry.getKey();
        //prevent printing of any sensitive values:
        if (isPrintableProperty(key)) {
          sb.append(key).append(": ").append(String.valueOf(entry.getValue()));
        }
      }
      return sb.toString();
    } finally {
      readLock.unlock();
    }
  }

  /**
   * Returns {@code true} if the internal property is safe to print in toString(), {@code false}
   * otherwise.
   *
   * @param name The name of the property to check for safe printing
   * @return {@code true} if the internal property is safe to print in toString(), {@code false}
   * otherwise.
   */
  protected boolean isPrintableProperty(String name) {
    return true;
  }

  @Override
  public int hashCode() {
    readLock.lock();
    try {
      return this.properties.isEmpty() ? 0 : this.properties.hashCode();
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (this == o) {
      return true;
    }
    if (!o.getClass().equals(getClass())) {
      return false;
    }
    AbstractResource other = (AbstractResource) o;
    readLock.lock();
    try {
      other.readLock.lock();
      try {
        return this.properties.equals(other.properties);
      } finally {
        other.readLock.unlock();
      }
    } finally {
      readLock.unlock();
    }
  }
}
