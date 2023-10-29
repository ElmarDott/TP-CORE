# FeatureToggle

**@since**: 3.0 > **@api-version**: 1.0 > **Dependencies**: FF4j

Files:

* **API Interface** org.europa.together.business.[FeatureFlags](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/FeatureFlags.java)
* **Implementation** org.europa.together.application.[FeatureFlagsFF4j](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/FeatureFlagsFF4j.java)
* **SQL Import** org.europa.together.sql.[ff-configuration.sql](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/resources-filtered/org/europa/together/sql/ff-configuration.sql)

---

**Feature Toggle** also known as **Feature Flag** or **Feature Flip** is a wrapper class for the FF4j library.

The Feature Flag use as Feature Store a database, which got connected by the JdbcActions. The configuration of the Feature Store is processed from the ConfigurationDAO. The configuration parameters (Version 1) are:

- **ff.activation** (boolean) - default: false -> activate / deactivate Feature Flags
- **ff.audit** (boolean) - default: false -> enable auditing if a feature don't exist
- **ff.autocreate** (boolean) - default: false -> autocreate a feature if it's not exist

For the creation of the Feature Store database tables includes the FF4j library several SQL script which are available in the classpath. The following scripts can be used: schema-ddl.sql (create), schema-drop.sql & schema-ddl-db2.sql.

After the connection to the feature store is established all basic functions: add, remove, update, activate, deactivate, get & check (the status if a feature is activated) are usable.

**Sample**

```java
// setup configuration
DatabaseActions jdbcActions = new JdbcActions();
jdbcActions.connect('jdbc.properties');
jdbcActions.executeSqlFromClasspath('ff-configuration.sql');

// connect to the FeatureStore database
FeatureFlags featureFlags = new FeatureFlagsFF4j();
FF4j ff4j = featureFlags.getFeatureStore("jdbc.properties");

// creat database tables for the feature store
ff4j.createSchema();

// create a feature
Feature myFeature = new Feature("TEST");
myFeature.setDescription("Temporary test feature.");
myFeature.setGroup("none");
myFeature.setEnable(true);

// add the feature to the feature store
featureFlags.addFeature(myFeature);

// check the status of a feature
if (featureFlags.check("TEST")) {
	// do some stuff because the feature is activated
}

// remove feature from the feature store
featureFlags.removeFeature("TEST");
```

