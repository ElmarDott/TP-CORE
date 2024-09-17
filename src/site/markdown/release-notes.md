# Release Notes

|Version | Comment
|-----------|-------------------------------------------------------------------
| __3.2.0__ | in progress
|           | - increase test coverage & remove vulernabilitoes
|           | - extend ImgSclrProcessor.copyImage
|           | - add Validator.luhnNumberChecksum() to verify credidt and gift cards.
|           | - optimized site / documentation
| __3.1.0__  | published 01/2024
|           | - update Spring 6.0 & HBM 6.1 (javax -> jakarta)
|           | - add EQUAL pagination filter for: String, Bool, Int, Float, Date
|           | - remove jGiven BDD acceptance tests to TP-TestSuite
|           | - include postgres 14 testcontainers & optimize test excecution
|           | - extend Validator.IP4_ADDRESS with localhost
|           | - extend SringUtils.base64Url encode/decode
|           | - fix ConfigurationService & MailClientService
|           | - Add Functionality: JWT & JWS
| __3.0.0__ | published 10/2022
|           | - implement FeatureFlags based FF4j
|           | - extend Validator.IP4_ADDRESS
|           | - extend Validator.isIsbn10() + isIsbn13()
|           | - extend GenericDAO.listAll() with pagination
|           | - remove jodatime
|           | - replace flexjson for Jackson
|           | - replace ITextRenderer for OpenPdfRenderer
|           | - replace JavaMail for JakartaMail
|           | - refactor MailClient & MailService
|           | - refactor Validator.VERSION_NUMBER to SEMANTIC_VERSION_NUMBER
|           | - refactor test cases
|           | - refactor log output
|           | - bugfix TreeWalker.addNode()
|           | - bugfix Change TreeNode.value to generic object (typesafe)
| __2.2.0__ | published 10/2021 - EOL
|           | - add OpenPDF Renderer (iText 5 will removed in Version 3.0)
| __2.1.0__ | published 12/2020
|           | - extend StringUtils.createDateFromString()
|           | - extend TreeWalker.validate()
|           | - extend CryptoTools.generateKeyPair()
|           | - extend Constraints with TimeZone entry
|           | - separate UnitTests from IntegrationTests
|           | - JSON lib replace flexjson for Jackson data-bind
|           | - bugfix: optimize Spring DI
|           | - replace JodaTime for java.time Java 8 compatibility (QRCodeGenerator & Validator)
|           | - replace 3CPO Connection Pool with Commons DBCP2 for Java 8 compatibility
| __2.0.2__ | published 01/2020
|           | - move internal implementation classes to package internal
|           | - bugfix: add Java Modul name to MANIFEST.MF
|           | - bugfix; cleanup test case imports
|           | - bugfix; ConfigurationDO rename MODULE_VERSION to SERVICE_VERSION
| __2.0.1__ | published 12/2019
|           | - bugfix; DAO visibilities
| __2.0.0__ | published 11/2019
|           | - change license to Apache License 2.0
|           | - Change bean validation to version 2.0
|           | - API changes: renaming impl classes
|           | - GenericDAO CRUD operations remove final
|           | - Add Functionality: CryptoTool
|           | - Add Functionality: Feature Toggle
|           | - Extend Application Configuration by Version() data class
|           | - provide JDBC connection object in DatabaseActions
|           | - refactor pom
|           | - fix TreeNode hashCode() & equals()
|           | - add TreeNode.copy()
| __1.1.0__ | published 10/2018 - EOL
|           | - optimize overall Test Coverage
|           | - Fix (medium) :: MailClient.loadConfigFromDatabase() {MODUL_VERSION}
|           | - Add Functionality: XmlTools:transformXslt()
|           | - Add Functionality: XmlTools:shrinkXml()
|           | - REFRACTOR XmlToolsImpl
|           | - Add Functionality: PdFRenderer.removePage()
|           | - Add Functionality: Logger.setLogLevel()
|           | - Add Functionality: DatabaseActions.getMetaData
|           | - Migrate DAO to Hibernate 5.3 (JPA 2.1)
|           | - Add swagger support
| __1.0.2__ | published 04/2018
|           | - include PGP signing for all
| __1.0.1__ | Rejected : not published
|           | - change Maven POM GAV for Open Source Project Repository Hosting
| __1.0.0__ | Rejected : not published

