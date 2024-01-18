# ConfigurationDAO

@**since**: 1.0 > @**api-version**: 1.2 > **Dependencies**: none

Files:

* **API Interface** org.europa.together.business.[ConfigurationDAO](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/ConfigurationDAO.java)
* **DomainObject** org.europa.together.domain.[ConfigurationDO](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/domain/ConfigurationDO.java)
* **Implementation** org.europa.together.application.[ConfigurationHbmDAO](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/ConfigurationHbmDAO.java)
* **Service** org.europa.together.service.[ConfigurationService](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/service/ConfigurationService.java)

---

The application configuration is a central registry, who allows to storage changeable settings for functionality of an application. As persistence layer is used a simple database table. To secure the data integrity, all entries will be managed by the application. Configuration keys in the database are hashed by SHA256. This strategy try to protect the entries against a manual manipulation. The mapping between readable configuration keys and the hashes is defined *only* in the Configuration Domain Object.

The configuration registry is an general implementation. The specialization of functionality is done in the concrete service. The ConfigurationDAO and it’s corresponding ConfigurationService will be the fundamental implementation for each other Service to access the configuration registry.

The implementation of the ConfigurationDAO supports four different views of an configuration entry:

* single key value entry
* configuration set to combine all entries of a service
* module set with all entries of the module e. g. ACL
* full get all entries of the whole application e. g. import / export

Key functions of the ConfigurationDAO are:
* CRUD functions of the DAO
* get a single Configuration Object by Key
* get all Configuration Objects of a Set
* get all DEPRECATED entries of the Registry
* get the full history of an entry
* restore a single entry to his default value
* update a list of configuration entries
* get a value of an entry by his key

In compliance of the given architecture have the ConfigurationService an higher level than the DAO Object. That means also, it is not possible to use service objects in the DAO Layer to mix concerns between functionality. The access from other functionality like E-Mail to the registry happen in this example in the MailService. There is possible to use other services and even the full access to the DAO objects. For this reason the implementation of import, export, restore default values and filters for deprecated and mandatory are placed inside the service.

The database table of the ConfigurationDO is APP_CONFIG. The constant &lt;DomainObject&gt;.TABLE_NAME contains the information about the table name for this Domain Object.

![ConfiguratioDO](https://github.com/ElmarDott/TP-CORE/wiki/images/ConfigurationDO.png)

In the case a service change his API and an entry will be obsolete in the next version, it can declared as deprecated. The flag mandatory mark an entry as necessary. This information is specially for GUI form descriptions. The versioning for configuration entries follows the version of the module which define them.

**The Configuration Application is used for the [CORE-06] SMTP E Mail Client implementation for the database configuration.**

**Sample:**

```java
@Autowired
private ConfigurationDAO configurationDAO;

@Autowired
private ConfigurationService configService;

// the combination of: ModuleName, ConfigurationSet & version
// in the ConfigurationDO have to be unique.
String module = "Module_A";
String version = "1.0";
String configSet = "Set_1";

// get a list of all mandantory configurattion entries of a configSet for a module
List<ConfigurationDO> mandatoryEntries =
      configService.filterMandatoryFieldsOfConfigSet(module, version, configSet);

// create a configuration entry
ConfigurationDO configDO = new ConfigurationDO();
configDO.setUuid("QWERTZ");
configDO.setKey("key");
configDO.setValue("no value");
configDO.setDefaultValue("DEFAULT");
configDO.setConfigurationSet(configSet);
configDO.setModulName(module);
configDO.setVersion(version);

// persist configuration entry
configurationDAO.create(configDO);

// find is always bind to the primarey key - the ConfigurationDO use UUID as PK
ConfigurationDO configDO = configurationDAO.find("QWERTZ");
configurationDAO.update("QWERTZ", configDO);
configurationDAO.delete("QWERTZ");

// quick access read a value by a given key.
String value = configurationDAO.getValueByKey(configDO.getKey(), module, version);

// Service Example - reset ale entries of a module to thier default entries
configService.resetModuleToDefault(module);
```