# MailClient

@**since**: 1.0 > @**api-version**: 2.0 > **Dependencies**: Java Mail

Files:

* **API Interface** org.europa.together.business.[MailClient](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/MailClient.java)
* **Domain Object** org.europa.together.domain.[Mail][https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/domain/Mail.java]
* **Implementation** org.europa.together.application.[JavaMailClient](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/JavaMailClient.java)
* **Service** org.europa.together.service.[MailClientService](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/service/MailClientService.java)
* **Configuration** org.europa.together.configuration.[mail.properties](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/resources-filtered/org/europa/together/configuration/mail.properties)
* **SQL Import** org.europa.together.sql.[mail-configuration.sql](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/resources-filtered/org/europa/together/sql/mail-configuration.sql)

---

The implementation of a simple SMTP E-Mail Client (simple mail transfer protocol) is an important basic functionality, to allow an application to communicate  with their user. Typical usage could be a newsletter, inform the administrator about abnormalities and so on.

To send e-mails, you will need as first to configure your SMTP Server which get connected by the client. The SMTP server is responsible to send the E-Mail. This module does not provide an own SMTP server. It need to connect by an existing SMTP service like GMail or self hosted. The configuration parameters (Version: 1) are:
* **mailer.host** e. g.: smtp.gmail.com
* **mailer.port** e. g.: 465
* **mailer.sender** e. g.: your.email@gmail.com
* **mailer.user** e. g.: your gmail account
* **mailer.password** e. g.: your gmail password
* **mailer.ssl** e. g.: true
* **mailer.tls** e. g.: true
* **mailer.count** default: 1
* **mailer.wait** default: 0 (milliseconds)

The example shows a configuration of an Google Mail SMTP server. The optional parameters mailer.count and mailer.wait are important for mass (bulk) e-mail sending. Usually public providers like Google Mail have an Spam protection. That means, in a defined time (e. g. a day) you can not send unlimited e-mails. Typical limitations are 100 mails in a short time and then you have to wait a specified time until you are able to continue. If you run your own SMTP server you are out of this restrictions. In this context the parameter mailer.count set the amount of E-Mails you can sent at once.  With the parameter mailer.wait you define the time interval in milliseconds the application interrupt the sending queue. Both parameters comes together. The default values are -1 and represent the the deactivation of this feature.

In a second step is necessary to compose the mail. The mail class in the domain package set: mime type, subject, content, recipient(s) and attachment(s).

There are several ways how to inject the configuration parameters into the application. The most easy option is to use the mail.properties file. This file can be placed inside the classpath or stored somewhere in your file system. Another possibility is to use the Application Configuration (Feature 06 - ConfigurationDAO), which stores the information in a database table.

The core implementation of the mail functionality is done by the JavaMailClient (Internet Message Format RFC 2822 8)  located in the *application* package and will used by the MailClientService in the *orchestration* package. If you choose to send bulk mails, the options CC and BCC are not used. Every recipient receive it's own e-mail whiteout the risk to see other recipients in the list.

**Sample:**

```java
@Autowired
private MailClientService mailClientService

@Autowired
private TemplateRenderer renderer;

// compose e-mail content by velocity renderer
Map<String, String> model = new HashMap<>();
model.put("property_key", "value");

String resourcePath = "org/europa/together/velocity";
String template = "template.vm";
String content = renderer.generateContentByClasspathResource(
                resourcePath, template, model);

// compose the e-mail
Mail mail = new Mail();
mail.setMimeTypeToHTML();
mail.setSubject("TESTMAIL");
mail.setMessage(content);
mail.addRecipent("info@sample.org");
mail.addAttachment("path/to/my/attachmet/file");

// if the property file dont't exist the configuration is feched by database
mailClientService.setExternalConfigurationFile("path/to/mail/config.properties");

// send single e-mail
mailClientService.sendEmail(mail);

// send the same e-mail to differrent recipients
mailClientService.sendBulkMail(mail);

// manipulate the DB configuration
Map<String, String> configuration
    = mailClientService.loadConfiguration();
configuration.put("mailer.sender", "test@sample.org");
mailClientService.updateDatabaseConfiguration(configuration);

```
