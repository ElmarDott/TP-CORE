<img src="https://enrebaja.files.wordpress.com/2018/04/logo_250x250.png" style="float:left; height:50%; width:50%;" />

# together Platform :: CORE

[![License Apache 2](https://img.shields.io/github/license/ElmarDott/TP-CORE)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/badge/Maven%20Central-2.0.2-green.svg)](https://mvnrepository.com/artifact/io.github.together.modules/core)
[![Javadocs](https://www.javadoc.io/badge/io.github.together.modules/core.svg)](https://www.javadoc.io/doc/io.github.together.modules/core)
[![Build Status](https://travis-ci.org/ElmarDott/TP-CORE.svg?branch=master)](https://travis-ci.org/ElmarDott/TP-CORE)
[![Coverage Status](https://coveralls.io/repos/github/ElmarDott/TP-CORE/badge.svg?branch=master)](https://coveralls.io/github/ElmarDott/TP-CORE)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f00b311bb51247c1ac215b699b52e5ed)](https://app.codacy.com/app/ElmarDott/TP-CORE?utm_source=github.com&utm_medium=referral&utm_content=ElmarDott/TP-CORE&utm_campaign=Badge_Grade_Dashboard)

The TP-CORE Artifact contains typical basic functions for Java Applications. The Module is elaborated as library and packed as JAR file. It is possible to use this Artifact in Java EE and Java SE (Desktop) Applications. The implementation of these library has the goal, to create an useful and compact toolbox. Free of charge for any kind of usage, commercial and private,

## Getting Started
Please check out the [Wiki](https://github.com/ElmarDott/TP-CORE/wiki/home) for futher information.

Components - Release: 2.0
 * [CORE-01] [Application Logger](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0001%5D-Application-Logger)
 * [CORE-02] [generic Data Access Object](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0002%5D-generic-Data-Access-Object---DAO)
 * [CORE-03] [Velocity Template Engine](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0003%5D-Velocity-Template-Engine)
 * [CORE-04] [Property Reader](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0004%5D-Property-Reader)
 * [CORE-05] [Application Configuration](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0005%5D-Application-Configuration)
 * [CORE-06] [SMTP E-Mail Client](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0006%5D-SMTP-E-Mail-Client)
 * [CORE-07] [QR Code Reader / Writer](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0007%5D-QR-Code-Reader---Writer)
 * [CORE-08] [Database Actions](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0008%5D-Database-Actions)
 * [CORE-09] [Tree Walker](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0009%5D-Tree-Walker)
 * [CORE-10] [XML Tools](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0010%5D-XML-Tools)
 * [CORE-11] [PDF Renderer](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0011%5D-PDF-Renderer)
 * [CORE-12] [Image Processor](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0012%5D-Image-Processor)
 * [CORE-13] [Feature Toggle](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0013%5D-Feature-Toggle)
 * [CORE-14] [Crypto Tools](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0014%5D-Crypto-Tools)

Basic concepts of this project are: KISS (Keep it simple, stupid), COC (Convention over configurations) and DRY (Don't repeat yourself). Also we following the programming paradigms of: Test Driven Development (TDD), Behavioral Driven Development (BDD) and Domain Driven Development (DDD).

### Prerequisites

The CORE Module is build with NetBeans 12.4, Maven 3.8.1 and Java 11 SE (openJDK). The implementation is also designed to run in Java EE 9 (e.g. Tomcat) environments. The most important dependencies are Hibernate 5.4, Spring 5.3 and JUnit 5. As Database Server (DBMS) we recommend PostgeSQL DBMS 11.

We decided to use docker for an easy database setup. After on your system docker is running you are be able to setup the database by the following steps:

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

  URI/>  172.17.0.1:5432   User: postgres PWD: n/a
  DOC/>  https://docs.docker.com/samples/library/postgres/

  * docker start postgres
  * docker stop postgres

To create user and schemata (also for testing), you are be able to use TP-CM/dbms/src/sql/initial_postgresql.sql script. If you need a short introduction about docker, you can check our tutorial on [YouTube](https://www.youtube.com/channel/UCBdJ0zh8xnMrQ-xQ4Gymy2Q).

### Build

To build the Project you will need the parent-pom from the TP-CM project (build-workflow). The project configurations are available in src/main/filter/ directory.

In the case there is no DBMS available, all test cases which depend on Database access will skipped.

### Installing
All released Artifacts will be available on Maven Central for usage. To fit with the hosting restriction on Sonatype Open Source Project Repository Hosting, it was necessary to change the POM GAV. As Result the Java packages do not fit with the pom GAV. So you are be able to use the artifact in your project as dependency with the following entry:

Please check the Release Notes for published Artifact Versions.
**Maven**

```
<dependency>
    <groupId>io.github.together.modules</groupId>
    <artifactId>core</artifactId>
    <version>2.1.0</version>
</dependency>
```

## Authors

* **Elmar Dott** - *Concept, Architecture, Development* - [enRebaja](https://enRebaja.wordpress.com)

## License

This project is licensed under the Apache 2.0 license.

## Contributors

Feel free to send a request by e-mail in the case you want to contribute the project. Everyone is welcome, even beginners in programming. We also appreciate help by optimizing our documentation and creating tutorials.

Mistakes happen. But we only able to fix them, when we you inform us you find a bug. Do not hesitate to send a report in the way you feel common. We try to give as much as possible fast & direct support.

In the case you like this project, let me know it and rate it with a star.

## Release Notes

|Version | Comment
|--------|----------------------------------------------------------------------
| 3.0.0  | in progress
|        | - refactor: test cases
|        | - remove jodatime
|        | - remove flexjson
|        | - Refactor: MailClient & MailService
|        | - bugfix: TreeWalker.addNode()
|        | - extend: Validator.IP4_ADDRESS
|        | - refactor: Validator.VERSION_NUMBER to SEMANTIC_VERSION_NUMBER
|        | - remove ITextRenderer for OpenPdfRenderer
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
