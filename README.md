# Seven Bridges Platform Java Library

SevenBridges Java is a Java library that provides an interface to the [Seven Bridges Platform](https://www.sevenbridges.com/) and [Cancer Genomics Cloud](https://www.cancergenomicscloud.org/) public APIs.

The Seven Bridges Platform (SBG) is a cloud-based environment for conducting bioinformatic analyses. It is a central hub for teams to store, analyze, and jointly interpret their bioinformatic data. The Platform co-locates analysis pipelines alongside the largest genomic datasets to optimize processing, allocating storage and compute resources on demand.

The Cancer Genomics Cloud (CGC), powered by Seven Bridges, is also a cloud-based computation environment. It was built as one of three pilot systems funded by the [National Cancer Institute](https://www.cancer.gov/) to explore the paradigm of colocalizing massive genomics datasets, like The Cancer Genomics Atlas (TCGA), alongside secure and scalable computational resources to analyze them. The CGC makes more than a petabyte of multidimensional data available immediately to authorized researchers. You can add your own data to analyze alongside TCGA using predefined analytical workflows or your own tools.

This library allows you to:

- securely log into the SBG and CGC Platforms
- manage projects, tasks, files and users on the Platforms
- upload files synchronously and asynchronously and download them in a robust and reliable manner

If you have feedback about this library, please get in touch and share your thoughts! <java@sevenbridges.com>

# Documentation

SevenBridges offers detailed documentation and support for the Java library, the public API.

[Java library overview and Quickstart](http://docs.sevenbridges.com/docs/api-java-library)

[Public API overview and reference](http://docs.sevenbridges.com/v1.0/reference#the-api)

Please email <java@sevenbridges.com> with any errors or issues with the documentation.

## Usage

Installation

Get started with Seven Bridges Java using Maven add the following lines to the <dependencies> section of your pom.xml file:

```
<dependency>
    <groupId>com.sevenbridges.apiclient</groupId>
    <artifactId>sevenbridges-java-api</artifactId>
    <version>0.5.0</version>
</dependency>
<dependency>
    <groupId>com.sevenbridges.apiclient</groupId>
    <artifactId>sevenbridges-java-httpclient</artifactId>
    <version>0.5.0</version>
    <scope>runtime</scope>
</dependency>
```

To install using Gradle, add this to the dependencies section of your "build.gradle" file:

```
dependencies {
    compile group: 'com.sevenbridges.apiclient', name: 'sevenbridges-java-api', version: '0.5.0'
    runtime group: 'com.sevenbridges.apiclient', name: 'sevenbridges-java-httpclient', version: '0.5.0'
}
```

## Build Instructions

This project requires Maven 3.3.3 and JDK 7+ to build.  Run the following:

    mvn install

# Contributing

Contributions, bug reports and issues are very welcome.

You can make your own contributions by forking the develop branch of this repository, making your changes, and issuing pull request on the develop branch.
Contributors should read the [Seven Bridges Notice to Contributors](https://github.com/sbg/sevenbridges-java/blob/master/CONTRIBUTORS_NOTICE.md) and sign the [Seven Bridges Contributor Agreement](https://secure.na1.echosign.com/public/esignWidget?wid=CBFCIBAA3AAABLblqZhAqt_9rHEqy2MggS0uWRmKHUN2HYi8DWNjkgg5N68iKAhRFTy7k2AOEpRHMMorxc_0*) before submitting a pull request.

## Copyright

Copyright (c) 2016-2017 Seven Bridges Genomics, Inc. All rights reserved.

This project is open-source via the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0).
