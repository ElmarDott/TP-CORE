# Introduction

This is a brief introduction for the functionality of the TP-CORE library. An Open Source an free usage project, also for commercial purpose under Apache License 2. The artifact contains typical basic functions for Java applications. The module is elaborated as library and packed as JAR file. Many complex functionality is wrapped by a simple API, to have an easy to use toolbox for application development. It's possible to use this artifact in JavaEE and JavaSE (Desktop) applications. TP-CORE will not include any other dependency from the together platform universe.

The usage of this library is strict defined by the implicit API and layer architecture. All functions are described in the Java Docs Comments of the Interfaces, located in the business package.


    <dependency>
        <groupId>io.github.together.modules</groupId>
        <artifactId>core</artifactId>
        <version>${version}</version>
    </dependency>

All released artifacts are deployed to Maven Central. [Here](https://search.maven.org/#search%7Cga%7C1%7Cio.github.together) you will find an overview of available versions and components. Each deployed Release is tagged in the SCM.

# Kick Start: Build & modify the last revision from the SCM
To run an own build in a local development environment, its necessary to follow some simple prerequisite:

- checkout source repository [TP-CORE](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/)
- checkout source repository [TP-CM](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CM)
- have an [PostgreSQL](https://hub.docker.com/_/postgres) DBMS installation (check the readme.md for details)

The together platform use the Maven filter technology for token replacement. The specific property files **[database](https://github.com/ElmarDott/TP-CORE/blob/master/src/main/filters/database.properties)** and **[mail](https://github.com/ElmarDott/TP-CORE/blob/master/src/main/filters/mail.properties)** in the /src/main/filter directory holds configuration parameters which are changeable during the build. The necessity of a change is only for configure an own workspace to run your own builds. The in the JAR included configuration files can be used as templates to integrate functionality in your own project.

# Exceptions

The module contains a set of custom Exceptions, which defined for the implemented services. The following list gives an overview which Exception is available:

* [DAOException](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/exceptions/DAOException.java)
* [JsonProcessingException](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/exceptions/JsonProcessingException.java)
* [MisconfigurationException](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/exceptions/MisconfigurationException.java)
* [TimeOutException](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/exceptions/TimeOutException.java)
* [UnsupportedVersionException](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/exceptions/UnsupportedVersionException.java)

# Utils
org.europa.together.utils.**[Constraints](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/utils/Constraints.java)**:  is a collection of helpful system wide constants.

org.europa.together.utils.**[Validator](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/utils/Validator.java)**: is an simple helper based on regular expressions (RegEx). The most checks are defined as RegEx pattern which can be used in the validate() method.

org.europa.together.utils.**[StringUtils](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/utils/StringUtils.java)**: contains a collection of some useful methods e. g. Hash, UUID, Converters and a File Write.

org.europa.together.utils.**[FileUtils](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/utils/FileUtils.java)**: provides some simple file operations like read, write and append.

# Architectural overview

All TP applications following an internal layer architecture, were each layer is represented by a own package. The Access between the layers is from TOP to DOWN. That means the lowest prioritized Layer, the Domain Layer can accessed from all layers above. itself has the no other layers then himself. Technically it means that Domain Objects has no import from other layers. The image below demonstrate the used layer architecture.

![Layer Architecture](https://github.com/ElmarDott/TP-CORE/wiki/images/LayerArchitecture.png)



