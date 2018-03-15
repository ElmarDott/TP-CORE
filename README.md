# together Platform :: CORE

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

### Prerequisites

The CORE Module is build with NetBeans 8.2, Maven 3.5.0 and Java 8 SE. The
implementation is also designed to run in Java EE 7 (e.g. Tomcat) environments.
The most important dependencies are Hibernate 4.3, Spring 5 and JUnit 5. As
Database Server (DBMS) we recommend PostgeSQL.

### Build

To build the Project you will need the parent-pom from the TP-KM project. The
project configurations are available in src/main/filter/ directory.

In the case there is no DBMS available, all test cases which depend on Database
access will skipped.

### Installing

Maven:
```
<dependency>
    <groupId>org.europa.together.modules</groupId>
    <artifactId>core</artifactId>
    <version>1.0</version>
</dependency>
```

## Authors

* **Elmar Dott** - *Concept, Architecture, Development* - [enRebaja](https://enRebaja.wordpress.com)

## License

This project is licensed under the General Public License GPL-3.0

## Contributors

Feel free to send a request by e-mail to contribute the project. In the case you
like this project, let me know it an rate it with a star.