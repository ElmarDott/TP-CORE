# QrCodeGenerator

@**since**: 1.0 > @**api-version**: 1.2> **Dependencies**: XZing

Files:

* **API Interface** org.europa.together.business.[QrCodeGenerator](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/QrCodeGenerator.java)
* **Implementation** org.europa.together.application.[ZxingGenerator](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/ZxingGenerator.java)

---

Quick Response Codes (QR Codes) allows a direct communication between different devices. For example you are be able to generate an QR Code with address information as an simple image. Afterwards you can take a picture with you mobile device of this code. Then the QR Code reader application of your mobile device extract the information from the code and store the address in the contact list of your mobile device. As you can see this technique is very helpful for a fast information exchange between devices.

- vCard
- CalendarEvent
- Url
- GeoLocation

**Sample:**

```java
QrCodeGenerator generator = new ZxingGenerator();
generator.setup("path/to/file/image.png", 100);
String qrCode = null;

//URL
qrCode = generator.generateDataForUrl("http://www.sample.org");

//Event
qrCode = generator.generateDataForCalendarEvent("Appointment",
             ZonedDateTime.of(2017, 1, 1, 1, 0, 0, 0, ZoneId.of("UTC")),
             ZonedDateTime.of(2017, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC"))
         );

//GeoLocation
qrCode = generator.generateDataForGeoLocation("40.71872", "-73.98905");

//vCard
Map<String, String> vCard = new HashMap<>();
vCard.put("gender", "Mr.");
vCard.put("name", "Doe");
vCard.put("surname", "John");
vCard.put("title", "MSc");
vCard.put("organization", "org.europa");
vCard.put("role", "Developer");
vCard.put("home-street", "Poniente 15");
vCard.put("home-city", "Oaxaca");
vCard.put("home-zipcode", "12345");
vCard.put("home-state", "Oaxaca");
vCard.put("home-country", "Mexico");
vCard.put("home-phone", "(55) 22365014");
vCard.put("home-mobile", "1245 44548954");
vCard.put("work-street", "Poniente 15");
vCard.put("work-city", "Oaxaca");
vCard.put("work-zipcode", "12345");
vCard.put("work-state", "Oaxaca");
vCard.put("work-country", "Mexico");
vCard.put("work-phone", "(234) 12456457801");
vCard.put("work-mobile", "145 78457 54");
vCard.put("e-mail", "john.doe@sample.org");
vCard.put("homepage", "http://www.sample.org");
qrCode = generator.generateDataForvCard(vCard);

//render String to the image
generator.encode(qrCode);

//read content from QrCodeImage
File image = new File("/path/to/grapic/image.png");
String output = generator.decode(image);
```