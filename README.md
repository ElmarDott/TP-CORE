<img src="https://enrebaja.files.wordpress.com/2018/04/logo_250x250.png" style="float:left; height:50%; width:50%;" />

# together Platform :: CORE

[![License GPL-3.0](https://img.shields.io/badge/license-GNU-green.svg)](https://www.gnu.org/licenses/gpl-3.0.en.html)
[![Maven Central](https://img.shields.io/badge/Maven%20Central-1.1.0-green.svg)](https://mvnrepository.com/artifact/io.github.together.modules/core)
[![Javadocs](https://www.javadoc.io/badge/io.github.together.modules/core.svg)](https://www.javadoc.io/doc/io.github.together.modules/core)
[![Build Status](https://travis-ci.org/ElmarDott/TP-CORE.svg?branch=master)](https://travis-ci.org/ElmarDott/TP-CORE)
[![Coverage Status](https://coveralls.io/repos/github/ElmarDott/TP-CORE/badge.svg?branch=master)](https://coveralls.io/github/ElmarDott/TP-CORE)

The CORE Artifact contains typical basic functions for Java Applications. The
Module is elaborated as library and packed as JAR file. It is possible to use
this Artifact in Java EE and Java SE (Desktop) Applications. The implementation
of these library has the goal, to create an useful and compact toolbox.

## Getting Started

Components - Release: 1.0
 * [CORE-0001] [Application Logger](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0001%5D-Application-Logger)
 * [CORE-0002] [generic Data Access Object](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0002%5D-generic-Data-Access-Object---DAO)
 * [CORE-0003] [Velocity Template Engine](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0003%5D-Velocity-Template-Engine)
 * [CORE-0004] [Property Reader](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0004%5D-Property-Reader)
 * [CORE-0005] [Application Configuration](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0005%5D-Application-Configuration)
 * [CORE-0006] [SMTP E-Mail Client](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0006%5D-SMTP-E-Mail-Client)
 * [CORE-0007] [QR Code Reader / Writer](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0007%5D-QR-Code-Reader---Writer)
 * [CORE-0008] [Database Actions](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0008%5D-Database-Actions)
 * [CORE-0009] [Tree Walker](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0009%5D-Tree-Walker)
 * [CORE-0010] [XML Tools](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0010%5D-XML-Tools)
 * [CORE-0011] [PDF Renderer](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0011%5D-PDF-Renderer)
 * [CORE-0012] [Image Processor](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0012%5D-Image-Processor)
 * [CORE-0013] [Feature Toggle](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-0013%5D-Feature-Toggle)

### Prerequisites

The CORE Module is build with NetBeans 8.2, Maven 3.5.3 and Java 8 SE. The
implementation is also designed to run in Java EE 7 (e.g. Tomcat) environments.
The most important dependencies are Hibernate 5.3, Spring 5.1 and JUnit 5. As
Database Server (DBMS) we recommend PostgeSQL.

### Build

To build the Project you will need the parent-pom from the TP-CM project. The
project configurations are available in src/main/filter/ directory.

In the case there is no DBMS available, all test cases which depend on Database
access will skipped.

### Installing
All released Artifacts will be available on Maven Central for usage. To fit with the
hosting restriction on Sonatype Open Source Project Repository Hosting was necessary
to change the POM GAV. As Result the java packages do not fit with the pom GAV. SO you
are be able to use TP-CORE in your project as dependency wit the following entry:

Please check the Release Notes for published Artifact Versions.
**Maven**
```
<dependency>
    <groupId>io.github.together.modules</groupId>
    <artifactId>core</artifactId>
    <version>1.2.0</version>
</dependency>
```

## Authors

* **Elmar Dott** - *Concept, Architecture, Development* - [enRebaja](https://enRebaja.wordpress.com)

## License

This project is licensed under the General Public License GPL-3.0

## Contributors

Feel free to send a request by e-mail to contribute the project. In the case you
like this project, let me know it an rate it with a star.

## Release Notes

|Version | Comment
|--------|----------------------------------------------------------------------
| 1.2    | in process
|        | - Add Functionality: Feature Toggle
|        | - Extend Application Configuration by Version() data class
|        | - provide JDBC connection object in DatabaseActions
|        | - refactor pom
|        | - fix TreeNode hashCode() & equals()
|        | - add TreeNode.copy()
|        | - Add Functionality: (Utils) JavaCryptoTool
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
