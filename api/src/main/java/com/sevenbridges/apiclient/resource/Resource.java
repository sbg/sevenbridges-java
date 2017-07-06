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
package com.sevenbridges.apiclient.resource;

/**
 * Resource is a first class citizen of the Seven Bridges API, representing logical structure with
 * its unique ID HREF string url that it can by fetched by.
 */
public interface Resource {

  /**
   * Unique string identifier of a resource, link that you can get current instance of resource on.
   *
   * @return String href property of resource
   */
  String getHref();

  /**
   * Reload is an action of a resource instance that has its 'href' field set. Invoking reload will
   * throw away any local, unsaved modifications on this resource instance, and will fetch the
   * latest version of a resource from the Seven Bridges Platform if it still exists.
   * <p>
   * Getting properties of the resource will always get locally stored properties if any (sometimes
   * resource is lazily loaded, with just the some of the properties until you invoke get on some
   * missing property). If you want to have most up to date properties you MUST invoke the {@code
   * #reload()} action.
   * <p>
   * For example, if you want to poll and wait for the status of an {@link
   * com.sevenbridges.apiclient.volume.ExportJob} to change to finished, you should reload resource
   * on each iteration of wait. For example:
   * <pre>
   *   while (!isFinished(exportJob.getState()) {
   *     Thread.sleep(5_000);
   *     exportJob.reload()
   *   }
   * </pre>
   * <p>
   * Omitting the reload call in the above example will always get the locally stored 'state'
   * property, and this while loop will became infinite until interrupted.
   */
  void reload();

}
