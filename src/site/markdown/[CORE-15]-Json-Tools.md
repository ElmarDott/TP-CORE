# JsonTools

@**since**: 3.0 > @**api-version**: 1.0 > **Dependencies**:  Jackson

Files:

* **API Interface** org.europa.together.business.[JsonTools](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/JsonTools.java)
* **Implementation** org.europa.together.application.[JacksonJsonTools](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/JacksonJsonTools.java)
* **Exception** org.europa.together.exceptions.[JsonProcessingException](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/exceptions/JsonProcessingException.java)

---

JsonTools is a proxy for the Jackson JSON library, to wrap all necessary functionality to process JavaScript Object Notation (JSON) objects.

The main usage for JsonTools is serialize Java Objects to JSON Strings and back from JSON Strings to Plain Java Data Objects. This functionality is used in the GenericDAO for JSON serialization and deserialization. Also in REST services need the JSON processing to exchange information. To archive this goal the three functions are available:

- ​	serializeAsJsonObject(DomainObject)
- ​	deserializeJsonAsObject(json, DomainObject.class);
- ​	deserializeJsonAsList(json)

JsonTools needed to get instanced as generic, by the transformed DomainObject. DomainObjects are Plain Old Java Objects (POJO) without any business logic. They just storages for structured data.

**Sample**:

```java
// DomainObject
public class DomainObject {

    private String key;
    private int value;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

String jsonObject =
    "{\"key\":\"key\",\"value\":12345}";
String jsonObjectList =
    "{ {\"key\":\"key\",\"value\":12345} }";

DomainObject domainObject = new DomainObject();
        domainObject.setKey("key");
        domainObject.setValue(12345);

JsonTools<DomainObject> jsonTools = new JacksonJsonTools();

String jsonString
    = jsonTools.serializeAsJsonObject(domainObject);
DomainObject object
    = deserializeJsonAsObject(jsonObject, DomainObject.class);
List<DomainObject> objectList
    = jsonTools.deserializeJsonAsList(jsonObjectList);
```

