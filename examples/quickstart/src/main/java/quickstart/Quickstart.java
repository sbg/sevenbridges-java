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
package quickstart;

import com.sevenbridges.apiclient.billing.BillingGroup;
import com.sevenbridges.apiclient.billing.BillingGroupList;
import com.sevenbridges.apiclient.client.Client;
import com.sevenbridges.apiclient.client.ClientBuilder;
import com.sevenbridges.apiclient.client.Clients;
import com.sevenbridges.apiclient.file.File;
import com.sevenbridges.apiclient.file.FileList;
import com.sevenbridges.apiclient.file.Files;
import com.sevenbridges.apiclient.project.Project;
import com.sevenbridges.apiclient.project.ProjectList;
import com.sevenbridges.apiclient.user.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * This class demonstrates the code found in the SevenBridges Java library QuickStart Guide.
 */
public class Quickstart {

  private static final Logger log = LoggerFactory.getLogger(Quickstart.class);

  public static void main(String[] args) {

    // Instantiate a builder for your Client. If needed, settings like Proxy can be defined here.
    ClientBuilder builder = Clients.builder();

    // No need to define anything else; build the Client instance. The ApiKey information will be
    // automatically sought in pre-defined locations.
    Client client = builder.build();

    // Obtain your current user
    User user = client.getCurrentUser();
    log.info("Current User: {}, {}", user.getHref(), user.getUsername());

    // Retrieve your billing groups
    BillingGroupList billingGroups = user.getBillingGroups();

    BillingGroup billingGroup = billingGroups.iterator().next();
    log.info("BillingGroup: {}, {}", billingGroup.getHref(), billingGroup.getName());

    // Retrieve your projects
    ProjectList projects = user.getProjects();

    Project project = projects.iterator().next();
    log.info("Project: {}, {}", project.getHref(), project.getId());

    // Retrieve and list project files (with page set to 10)

    FileList files = project.getFiles(Files.criteria().limitTo(10));

    Iterator<File> fileIterator = files.iterator();
    File file;
    while (fileIterator.hasNext()) {
      file = fileIterator.next();
      log.info("\t{}", file.getName());
    }

    // Create a Project

    //Create the project object
    Project p = client.instantiate(Project.class);
    // Set the project properties
    p.setName("Java Quickstart Project");
    p.setBillingGroupId(billingGroup.getId());
    p.setDescription("Project from Java");

    //Create project only if not created before
    boolean pExists = false;
    Iterator<Project> projectIterator = projects.iterator();
    while (projectIterator.hasNext()) {
      project = projectIterator.next();
      if ("Java Quickstart Project".equalsIgnoreCase(project.getName())) {
        pExists = true;
        p = project;
        break;
      }
    }

    if (!pExists) {
      // Create the project using the existing Project object
      user.createProject(p);

      log.info("Created project");
    }

    //Print project details
    log.info("Id: " + project.getId());
    log.info("Name: " + project.getName());

  }
}
