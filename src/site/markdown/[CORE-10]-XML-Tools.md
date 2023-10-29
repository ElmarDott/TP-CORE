# XmlTools

@**since**: 1.0 > @**api-version**: 2.0 > **Dependencies**: xerces

Files:

* **API Interface** org.europa.together.business.[XmlTools](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/XmlTools.java)
* **Implementation** org.europa.together.application.[SaxTools](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/SaxTools.java)
* **Helper** org.europa.together.application.internal.[SaxDocumentHandler](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/internal/SaxDocumentHandler.java)

---

XML Tools is a SAX based implementation to handle XML Files. The main usage is testing if a XML document is well formed and valid against an XML Grammar. The validation supports the Document Type Definition (DTD) and XML Schema (XSD). The validation mechanism distinguish the following ways:

- in the document included DTD
- an external in the document referenced DTD (FilePath)
- an remote in the document referenced DTD (URL)
- in the document included XSD [will fail, because is not XML Standard]
- an external in the document referenced XSD (FilePath)
- an remote in the document referenced XSD (URL)
- multiple in the document referenced XSD (URL)
- mixed documents with DTD and XSD [will fail, because is not XML Standard]

Well formed documents are not inevitably valid. Its only secures that XML documents follow the rules of correctness. A valid XML means that the data structure of the document fit by the given grammar. For a correct parsing it is necessary that the document is well formed. The checks for valid and well formed XML are extremely helpful for storing new XML Documents without failures.

Another section of dealing with XML is data transformation with XSLT. The XML Tools contains a simple functionality who transform XML by a given XSLT.

The third main topic of XML handling are solutions to format documents. Formatting has two different aspects. One is a better human readable structure and the other is a optimized representation for a fast and efficient computing. The function shrink() removes all Comments and whitespace e. g. line breaks to reduce the file size to an minimum. This can save important network resources in the context of Java Message Services (JMS) in SOA applications.

**Samples:**

```java
XmlTools xmlTools = new SaxTools();

// read the content of an XML file
String xmlContent = xmlTools.parseXmlFile(new File("test.xml"));

// create a human readble output
String xmlFormatted = xmlTools.prettyPrintXml();

// compress the content of an XML file
String compressed = xmlTools.shrinkContent(xmlContent);

// test if xml is well formed
boolean test = xmlTools.isWellFormed();

// test if xml is valid
boolean test = xmlTools.isValid();

//add an explicit schema file
xmlTools.setSchemaFile(new File("simple.xsd"));
boolean test = xmlTools.isValid();

//XSLT transfortmation
File xml = new File("xml_datasource.xml");
File xslt = new File("template.xslt");
String transformed = xmlTools.transformXslt(xml, xslt);

```