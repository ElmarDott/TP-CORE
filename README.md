<img src="https://elmar-dott.com/wp-content/uploads/ElmarDott.com_.jpg" style="float:left; height:50%; width:50%;" />

# Together Platform :: CORE

[![License Apache 2](https://img.shields.io/github/license/ElmarDott/TP-CORE)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/badge/Maven%20Central-2.0.2-green.svg)](https://mvnrepository.com/artifact/io.github.together.modules/core)
[![Javadocs](https://www.javadoc.io/badge/io.github.together.modules/core.svg)](https://www.javadoc.io/doc/io.github.together.modules/core)
[![Build Status](https://travis-ci.org/ElmarDott/TP-CORE.svg?branch=master)](https://travis-ci.org/ElmarDott/TP-CORE)
[![Coverage Status](https://coveralls.io/repos/github/ElmarDott/TP-CORE/badge.svg?branch=master)](https://coveralls.io/github/ElmarDott/TP-CORE)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f00b311bb51247c1ac215b699b52e5ed)](https://app.codacy.com/app/ElmarDott/TP-CORE?utm_source=github.com&utm_medium=referral&utm_content=ElmarDott/TP-CORE&utm_campaign=Badge_Grade_Dashboard)

## Getting Started
The TP-CORE artifact contains typical basic functions for Java Applications. The module is elaborated as library and packed as JAR file. It is possible to use this artifact in Java EE and Java SE (Desktop) Applications. The implementation of these library has the goal, to create an useful and compact toolbox. Free of charge for any kind of usage, commercial and private,

Components - Release: 2.0

 * [CORE-01] [Application Logger](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-01%5D-Logger)
 * [CORE-02] [generic Data Access Object](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-02%5D-generic-Data-Access-Object---DAO)
 * [CORE-03] [Velocity Template Engine](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-03%5D-Template-Renderer)
 * [CORE-04] [Property Reader](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-04%5D-Property-Reader)
 * [CORE-05] [Application Configuration](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-05%5D-ConfigurationDAO)
 * [CORE-06] [SMTP E-Mail Client](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-06%5D-SMTP-E-Mail-Client)
 * [CORE-07] [QR Code Reader / Writer](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-07%5D-QR-Code-Generator)
 * [CORE-08] [Database Actions](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-08%5D-Database-Actions)
 * [CORE-09] [Tree Walker](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-09%5D-Tree-Walker)
 * [CORE-10] [XML Tools](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-10%5D-XML-Tools)
 * [CORE-11] [PDF Renderer](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-11%5D-PDF-Renderer)
 * [CORE-12] [Image Processor](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-12%5D-Image-Processor)
 * [CORE-13] [Feature Toggle](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-13%5D-Feature-Toggle)
 * [CORE-14] [Crypto Tools](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-14%5D-Crypto-Tools)

Please also check out the [Wiki](https://github.com/ElmarDott/TP-CORE/wiki/home) for futher information.

### Prerequisites

The CORE Module is build with NetBeans, Maven and Java SE (openJDK). The most important dependencies are Hibernate, Spring and JUnit 5. As Database Server (DBMS) PostgeSQL DBMS 11 is recommended.

Docker was chosen for an simple and fast database setup. In the case you wish to have a short introduction about docker, you can check my tutorial on [BitCute](https://elmar-dott.com/articles/tutorial/docker-basics/). After on your system docker is running, you are be able to setup the database by the following steps:

  docker network create -d bridge --subnet=172.18.0.0/16 services

  docker run -d --name postgres --restart=no \
  -p 5432:5432 --net services --ip 172.18.0.2 \
  -e POSTGRES_PASSWORD=s3cr3t \
  -e PGPASSWORD=s3cr3t \
  -v /home/user/docker/postgres:/var/lib/postgresql/data \
  postgres:11

  docker run -d --name pgadmin --restart=no \
  -p 8004:80 --net services --ip 172.18.0.98 \
  -e PGADMIN_DEFAULT_EMAIL=myself@sample.com \
  -e PGADMIN_DEFAULT_PASSWORD=s3cr3t \
  --link postgres:11 \
  dpage/pgadmin4:4.29

  **URI**/>  172.17.0.1:5432   User: postgres PWD: n/a
  **DOC**/>  https://docs.docker.com/samples/library/postgres/

  * docker start postgres
  * docker stop postgres

To create default user and schemata (also for testing), you are be able to use [TP-CM/dbms/src/sql/initial_postgresql.sql](https://github.com/ElmarDott/TP-CM/blob/master/dbms/src/sql/initial_postgresql.sql) script.

### Build

TP-CORE uses always the current version of Apache Maven. To build the project by your own you will need the current version from the master branch of the parent-pom from the TP-CM project (build-workflow).

The project configurations are available in src/main/filter/ directory.

In the case no DBMS is available, all test cases which depend on Database access will skipped.

### Installing
All released artifacts are available on Maven Central for free usage. You are be able to use the released artifact in your project as dependency with the following entry:

**Maven**
```
<dependency>
    <groupId>io.github.together.modules</groupId>
    <artifactId>core</artifactId>
    <version>2.2.0</version>
</dependency>
```

## Authors

* **Elmar Dott** - [*Concept, Architecture, Development*](https://elmar-dott.com)

## License

This project is licensed under the Apache 2.0 license.

## Contribution

Feel free open a pull request or to send a feature request by e-mail in the case you want to contribute the project. Everyone is welcome, even beginners in programming. I also appreciate help by optimizing the documentation and creating tutorials.

Mistakes happen. But we only able to fix them, when we you inform us you found a bug. Do not hesitate to send a report in the way you feel common. I try to give as much as possible fast & direct support.

In the case you like this project, let me know it and rate it with a star.

## Release Notes

|Version | Comment
|--------|----------------------------------------------------------------------
| 3.0.0  | in progress
|        | - implement FeatureFlags based FF4j
|        | - extend Validator.IP4_ADDRESS
|        | - extend Validator.isIsbn10() + isIsbn13()
|        | - extend GenericDAO.listAll() with pagination
|        | - remove jodatime
|        | - remove flexjson
|        | - remove ITextRenderer for OpenPdfRenderer
|        | - change javax to jakarta (Jakarta EE 9)
|        | - refactor MailClient & MailService
|        | - refactor Validator.VERSION_NUMBER to SEMANTIC_VERSION_NUMBER
|        | - refactor test cases
|        | - refactor log output
|        | - bugfix TreeWalker.addNode()
|        | - bugfix Change TreeNode.value to generic object (typesafe)
|--------|----------------------------------------------------------------------
| 2.2.0  | published 10/2021
|        | - add OpenPDF Renderer (iText 5 will removed in Version 3.0)
|--------|----------------------------------------------------------------------
| 2.1.0  | published 12/2020
|        | - extend StringUtils.createDateFromString()
|        | - extend TreeWalker.validate()
|        | - extend CryptoTools.generateKeyPair()
|        | - extend Constraints with TimeZone entry
|        | - separate UnitTests from IntegrationTests
|        | - JSON lib replace flexjson for Jackson data-bind
|        | - bugfix: optimize Spring DI
|        | - replace JodaTime for java.time Java 8 compatibility (QRCodeGenerator & Validator)
|        | - replace 3CPO Connection Pool with Commons DBCP2 for Java 8 compatibility
|--------|----------------------------------------------------------------------
| 2.0.2  | published 01/2020
|        | - move internal implementation classes to package internal
|        | - bugfix: add Java Modul name to MANIFEST.MF
|        | - bugfix; cleanup test case imports
|        | - bugfix; ConfigurationDO rename MODULE_VERSION to SERVICE_VERSION
|--------|----------------------------------------------------------------------
| 2.0.1  | published 12/2019
|        | - bugfix; DAO visibilities
|--------|----------------------------------------------------------------------
| 2.0    | published 11/2019
|        | - Change bean validation to version 2.0
|        | - API changes: renaming impl classes
|        | - GenericDAO CRUD operations remove final
|        | - Add Functionality: CryptoTool
|--------|----------------------------------------------------------------------
| 1.2    | in process
|        | - change license to Apache License 2.0
|        | - Add Functionality: Feature Toggle
|        | - Extend Application Configuration by Version() data class
|        | - provide JDBC connection object in DatabaseActions
|        | - refactor pom
|        | - fix TreeNode hashCode() & equals()
|        | - add TreeNode.copy()
|--------|----------------------------------------------------------------------
| 1.1    | published 10/2018
|        | - optimize overall Test Coverage
|        | - Fix (medium) :: MailClient.loadConfigFromDatabase() {MODUL_VERSION}
|        | - Add Functionality: XmlTools:transformXslt()
|        | - Add Functionality: XmlTools:shrinkXml()
|        | - REFRACTOR XmlToolsImpl
|        | - Add Functionality: PdFRenderer.removePage()
|        | - Add Functionality: Logger.setLogLevel()
|        | - Add Functionality: DatabaseActions.getMetaData
|        | - Migrate DAO to Hibernate 5.3 (JPA 2.1)
|        | - Add swagger support
|--------|----------------------------------------------------------------------
| 1.0.2  | published 04/2018
|        | - include PGP signing for all
|--------|----------------------------------------------------------------------
| 1.0.1  | Rejected : not published
|        | - change Maven POM GAV for Open Source Project Repository Hosting
|--------|----------------------------------------------------------------------
| 1.0    | Rejected : not published
|--------|----------------------------------------------------------------------
