# PdfRenderer

@**since**: 2.2 > @**api-version**: 2.0 > **Dependencies**: OpenPdf

Files:

* **API Interface** org.europa.together.business.[PdfRenderer](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/PdfRenderer.java)
* **Domain Object**  org.europa.together.application.internal.[PdfDocument](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/internal/PdfDocument.java)
* **Implementation** org.europa.together.application.[OpenPdfRenderer](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/OpenPdfRenderer.java)
* **Helper**
  * org.europa.together.application.internal.[PdfDocument](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/internalPdfDocument.java)
  * org.europa.together.application.internal.[PdfReplacedElementFactory](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/internal/PdfReplacedElementFactory.java)


---

The implementation of this PDF generator is very basic and the usage is also as simple. It allows to generate an PDF from an HTML String. The result is a document in the European Norm Size A4. With this functionality you are be able to render PDF in your application for billing or other reports.

**Sample:**

```java
String html = "<h1>My First PDF Document</h1 > <p>"
                + StringUtils.generateLoremIpsum(0) + "</p>";

PdfRenderer pdf = new OpenPdfRenderer();
pdf.setTitle("OpenPDF test document");
pdf.setAuthor("Elmar Dott");
pdf.setSubject("JUnit test");
pdf.setKeywords("test, junit5, openPDF");
pdf.renderDocumentFromHtml("test.pdf", html);

// load a existing PDF
PdfReader document = pdf.loadDocument(new File("/document.pdf"));
int size = document.getNumberOfPages();

// manipulate PDF
PdfDocument reduced = pdf.removePage(document, 1, 5);
pdf.writeDocument(reduced, "/reduced.pdf");
```