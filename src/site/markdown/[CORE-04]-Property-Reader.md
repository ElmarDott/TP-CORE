# PropertyReader

@**since**: 1.0 > @**api-version**: 1.2 > **Dependencies**: none

Files:

* **API Interface** org.europa.together.business.[PropertyReader](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/PropertyReader.java)
* **Implementation** org.europa.together.application.[PropertyFileReader](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/PropertyFileReader.java)

---

The Property Reader reads ASCII text files in the key=value format. The extension for those files should be property. Values can converted into Boolean, String, Integer, Float and Double data types, this allows a type secure usage inside JAVA Applications.

The property files can be loaded from an external File or from the classpath. In the case its necessary to load the properties from a database or Web Service, its possible to read the specific data in a new Map<String, String> and add the List with addList(Map properties).

The implementation of the property list is a HashMap. That means when an new list get loaded, the whole list get updated (extended) and existing entries will overwritten with the new value. The method clear() allows to empty the whole list.

The Implementation provides no sorting strategy for the property list.

**Sample:**

```java
PropertyReader propertyReader = new PropertyFileReader();

propertyReader.clear();
propertyReader
    .appendPropertiesFromFile("dir/file.properties");

if(propertyReader.count() > 0) {

    boolean test = propertyReader
        .getPropertyAsBoolean("test.type.boolean");
    int number = propertyReader
        .getPropertyAsInt("test.type.int");
    String text = propertyReader
        .getPropertyAsString("test.type.string");
    float digit = propertyReader
        .getPropertyAsFloat("test.type.float");
    double bigNumber = propertyReader
        .getPropertyAsDouble("test.type.double");
}

// do nothing if the property already exist
propertyReader.addProperty("key", "value");

// create a new entry in the cas the property don't exist
propertyReader.updateProperty("key", "new value");
propertyReader.removeProperty("key");

//export
Map<String, String> properties = propertyReader.getPropertyList();

```