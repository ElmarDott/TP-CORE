# TemplateRenderer

@**since**: 1.0 > @**api-version**: 1.0 > **Dependencies**: Apache Velocity

Files:

* **API Interface** org.europa.together.business.[TemplateRenderer](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/TemplateRenderer.java)
* **Implementation** org.europa.together.application.[VelocityRenderer](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/VelocityRenderer.java)

---

The velocity project was chosen for the template engine. The important arguments for this choice was an good documentation, plenty functionality, an active community and a successful usage in many other projects. Also very important is the fact that the usage is very simple.

Template content can be loaded from a classpath resource, from the file system or from a String. Specially the last option to load content from a String gives a high flexibility to handle other Resource types like databases or micro services in a general manner.

Beside the usage of variables, Velocity supports a full scripting language to develop complex templates for a generic usage.

**Sample:**

```java
TemplateRenderer velocityRenderer =
                        new VelocityRenderer();

String resourcePath = "org/europa/together/velocity";
String template = "template.vm";

Map<String, String> model = new HashMap<>();
model.put("property_key", "value");

String content = velocityRenderer
            .generateContentByClasspathResource(
                resourcePath, template, model);
```
**Template:**
```bash
## single line comment
#set( $var = "Velocity" )
Hello $var World?
#if($property_key=="value") : $property_key #endy
```
Output: **Hello Velocity World? value**.

The HashTable demonstrate how simple it is to use variables inside the template. A simple if condition is set in the example, but the processor is much more powerful an support things like loops and so on.

The full processed template is placed inside the String content and can be used to write inside a file or displayed in a HTML page. Other usages can be E-Mail templates to create dynamic mails, send by the [SMTP E-Mail Client](https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-06%5D-SMTP-E-Mail-Client) [TP-06]. To use the full advantage of Velocity templates check the project documentation.
