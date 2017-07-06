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
package com.sevenbridges.apiclient.user;

import com.sevenbridges.apiclient.resource.Resource;
import com.sevenbridges.apiclient.resource.Saveable;

/**
 * Resource representing an User instance.
 */
public interface User extends Resource, Saveable, UserActions {

  /**
   * Username field, unique identifier of user on the Platform.
   *
   * @return String 'username' property of the current instance
   */
  String getUsername();

  /**
   * Sets the property 'username' of the user locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param username new username of the user
   * @return current {@link User} instance
   */
  User setUsername(String username);

  /**
   * Email field, string email of the user.
   *
   * @return String 'email' property of the current instance
   */
  String getEmail();

  /**
   * Sets the property 'email' of the user locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param email new email of the user
   * @return current user instance
   */
  User setEmail(String email);

  /**
   * First Name field, string first name of the user.
   *
   * @return String 'firstName' property of the current instance
   */
  String getFirstName();

  /**
   * Sets the property 'firstName' of the user locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param firstName String first name of the user
   * @return current user instance
   */
  User setFirstName(String firstName);

  /**
   * Last Name field, string last name of the user.
   *
   * @return String 'lastName' property of the current instance
   */
  String getLastName();

  /**
   * Sets the property 'lastName' of the user locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param lastName String last name of the user
   * @return current user instance
   */
  User setLastName(String lastName);

  /**
   * Address field, string address of the user.
   *
   * @return String 'address' property of the current instance
   */
  String getAddress();

  /**
   * Sets the property 'address' of the user locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance
   *
   * @param address String address of the user
   * @return current user instance
   */
  User setAddress(String address);

  /**
   * City field, string city of the user.
   *
   * @return String 'city' property of the current instance
   */
  String getCity();

  /**
   * Sets the property 'city' of the user locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param city String city of the user
   * @return current user instance
   */
  User setCity(String city);

  /**
   * State field, string state of the user.
   *
   * @return String 'state' property of the current instance
   */
  String getState();

  /**
   * Sets the property 'state' of the user locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param state String 'state' of the user
   * @return current user instance
   */
  User setState(String state);

  /**
   * Country property, string country of the user.
   *
   * @return String 'country' property of the current instance
   */
  String getCountry();

  /**
   * Sets the property 'country' of the user locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param country String country of the user
   * @return current user instance
   */
  User setCountry(String country);

  /**
   * ZipCode property, String zip code of the user.
   *
   * @return String 'zipCode' property of the current instance
   */
  String getZipCode();

  /**
   * Sets the property 'zipCode' of the user locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param zipCode string zip code of the user
   * @return current user instance
   */
  User setZipCode(String zipCode);

  /**
   * Phone property, string phone of the user.
   *
   * @return String 'phone' property of the current instance
   */
  String getPhone();

  /**
   * Sets the property 'phone' of the user locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param phone String phone code of the user
   * @return current user instance
   */
  User setPhone(String phone);

  /**
   * Affiliation property, String affiliation of the user.
   *
   * @return String 'affiliation' property of the current instance
   */
  String getAffiliation();

  /**
   * Sets the property 'affiliation' of the user locally.
   * <p>
   * To permanently change the resource on the Platform invoke {@link Saveable#save()} on current
   * instance.
   *
   * @param affiliation string affiliation of the user
   * @return current user instance
   */
  User setAffiliation(String affiliation);

}
