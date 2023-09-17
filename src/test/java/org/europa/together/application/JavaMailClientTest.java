package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class JavaMailClientTest {

    private static final Logger LOGGER = new LogbackLogger(JavaMailClientTest.class);
    private static DatabaseActions CONNECTION = new JdbcActions();
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes";
    private static final String SQL_FILE
            = "org/europa/together/sql/email-config-test.sql";
    private static GreenMail SMTP_SERVER;

    @Autowired
    private MailClient mailer;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        boolean check = true;
        String out = "executed";
        FF4jProcessor feature = new FF4jProcessor();

        boolean toggle = feature.deactivateUnitTests(MailClient.FEATURE_ID);
        boolean socket = CONNECTION.connect("default");
        if (!toggle || !socket) {
            out = "skiped.";
            check = false;
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out + "\n", LogLevel.TRACE);
        Assumptions.assumeTrue(check);

        //SMTP Test Server
        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        SMTP_SERVER = new GreenMail(ServerSetupTest.SMTPS);
        SMTP_SERVER.start();
        SMTP_SERVER.setUser("john.doe@localhost", "JohnDoe", "s3cr3t");
    }

    @AfterAll
    static void tearDown() {
        try {
            SMTP_SERVER.stop();
            CONNECTION.executeQuery("TRUNCATE TABLE app_config;");

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        LOGGER.log("### TEST SUITE TERMINATED.\n", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(JavaMailClient.class, hasValidBeanConstructor());
    }

    @Test
    void testInitialConfiguration() {
        LOGGER.log("TEST CASE: initialConfiguration", LogLevel.DEBUG);

        MailClient client = new JavaMailClient();
        assertEquals(-1, client.getBulkMailLimiter());
        assertEquals(-1, client.getWaitTime());
        assertEquals(10, client.getConfiguration().size());
    }

    @Test
    void testMimeType() {
        LOGGER.log("TEST CASE: mimeType", LogLevel.DEBUG);

        assertEquals("plain", mailer.getMimeType());
        mailer.setMimeTypeToHTML();
        assertEquals("html", mailer.getMimeType());
        mailer.setMimeTypeToPlain();
        assertEquals("plain", mailer.getMimeType());
    }

    @Test
    void testSetContent() {
        LOGGER.log("TEST CASE: setContent", LogLevel.DEBUG);

        String content = "<h1>E Mail>/h1><p>this is the content of the email.";
        mailer.setContent(content);
        assertEquals(content, mailer.getContent());
        mailer.setContent("");
    }

    @Test
    void testSubject() {
        LOGGER.log("TEST CASE: subject", LogLevel.DEBUG);

        String subject = "E-MAil Topic";
        mailer.setSubject(subject);
        assertEquals(subject, mailer.getSubject());
        mailer.setSubject("");
    }

    @Test
    void testAddRecipent() {
        LOGGER.log("TEST CASE: addRecipent", LogLevel.DEBUG);

        assertEquals(0, mailer.getRecipentList().size());

        List<String> success = new ArrayList<>();
        success.add("success@domain.test");
        success.add("no_mail@domain.eu");
        success.add("success1@domain.test");
        success.add("success@domain-test.org");

        for (String entry : success) {
            assertTrue(mailer.addRecipent(entry));
        }
        assertEquals(4, mailer.getRecipentList().size());
        mailer.clearRecipents();
    }

    @Test
    void testAddRecipentDetectDoubleEntries() {
        LOGGER.log("TEST CASE: addRecipentDetectDoubleEntries", LogLevel.DEBUG);

        assertEquals(0, mailer.getRecipentList().size());

        List<String> success = new ArrayList<>();
        success.add("success@domain.test");
        success.add("success@domain.test");
        success.add("no_mail@domain.eu");
        success.add("no_mail@domain.eu");

        for (String entry : success) {
            mailer.addRecipent(entry);
        }
        assertEquals(2, mailer.getRecipentList().size());
        mailer.clearRecipents();
    }

    @Test
    void testAddRecipentValidationFail() {
        LOGGER.log("TEST CASE: addRecipentValidationFail", LogLevel.DEBUG);

        assertEquals(0, mailer.getRecipentList().size());

        List<String> failure = new ArrayList<>();
        failure.add("@domain");
        failure.add("@domain");
        failure.add("no_mail.domain");
        failure.add("success@domain");
        failure.add("success@domain");
        failure.add("no_mail.domain");

        for (String entry : failure) {
            assertFalse(mailer.addRecipent(entry));
        }
        assertEquals(0, mailer.getRecipentList().size());
    }

    @Test
    void testAddRecipientList() {
        LOGGER.log("TEST CASE: addRecipientList", LogLevel.DEBUG);

        List<String> recipients = new ArrayList<>();
        recipients.add("fail");
        recipients.add("test@sample.com");
        recipients.add("test_01@sample.com");
        recipients.add("test_02@sample.com");
        recipients.add("test_03@sample.com");
        recipients.add("test@sample.com");

        mailer.addRecipientList(recipients);
        assertEquals(4, mailer.getRecipentList().size());

        mailer.clearRecipents();
    }

    @Test
    void testAttachmentSize() {
        LOGGER.log("TEST CASE: attachmentSize", LogLevel.DEBUG);

        assertEquals(0, mailer.getAttachmentSize());
        mailer.setAttachmentSize(100);
        assertEquals(100, mailer.getAttachmentSize());
        mailer.setAttachmentSize(0);
        assertEquals(0, mailer.getAttachmentSize());
    }

    @Test
    void testAddAttachmentLimitSize() {
        LOGGER.log("TEST CASE: addAttachmentLimitSize", LogLevel.DEBUG);

        mailer.setAttachmentSize(100);
        assertFalse(mailer.addAttachment(DIRECTORY + "/Attachment.pdf"));
        assertTrue(mailer.addAttachment(DIRECTORY + "/TestFile"));

        mailer.setAttachmentSize(0);
        assertTrue(mailer.addAttachment(DIRECTORY + "/Attachment.pdf"));
    }

    @Test
    void testAddEmptyAttachment() {
        LOGGER.log("TEST CASE: addEmptyAttachment", LogLevel.DEBUG);
        assertEquals(0, mailer.getAttachmentList().size());

        assertFalse(mailer.addAttachment(DIRECTORY + "/empty.attachment"));
        assertEquals(0, mailer.getAttachmentList().size());
        mailer.clearAttachments();
        assertEquals(0, mailer.getAttachmentList().size());
    }

    @Test
    void testAddAttachment() {
        LOGGER.log("TEST CASE: addAttachment", LogLevel.DEBUG);

        assertEquals(0, mailer.getAttachmentList().size());

        assertTrue(mailer.addAttachment(DIRECTORY + "/Attachment.pdf"));
        assertEquals(1, mailer.getAttachmentList().size());
        mailer.clearAttachments();
        assertEquals(0, mailer.getAttachmentList().size());
        mailer.clearAttachments();
    }

    @Test
    void testaddAttachmentList() {
        LOGGER.log("TEST CASE: addAttachmentList", LogLevel.DEBUG);

        List<String> attachments = new ArrayList<>();
        attachments.add(DIRECTORY + "/Attachment.pdf");
        attachments.add(DIRECTORY + "/TestFile");
        mailer.addAttachmentList(attachments);
        assertEquals(2, mailer.getAttachmentList().size());
        mailer.clearAttachments();
    }

    @Test
    void testAddAttachmentSizeLimit() {
        LOGGER.log("TEST CASE: addAttachmentSizeLimit", LogLevel.DEBUG);

        mailer.setAttachmentSize(500);
        assertFalse(mailer.addAttachment(DIRECTORY + "/Attachment.pdf"));
        assertEquals(0, mailer.getAttachmentList().size());
        mailer.setAttachmentSize(0);
        assertEquals(0, mailer.getAttachmentSize());
    }

    @Test
    void testAddAttachmentFail() {
        LOGGER.log("TEST CASE: addAttachmentFail", LogLevel.DEBUG);

        mailer.clearAttachments();
        assertFalse(mailer.addAttachment(DIRECTORY + "/File.pdf"));
        assertEquals(0, mailer.getAttachmentList().size());
    }

    @Test
    void testLoadConfigurationFromClasspath() {
        LOGGER.log("TEST CASE: loadConfigurationFromClasspath", LogLevel.DEBUG);

        mailer.clearConfiguration();
        assertFalse(mailer.loadConfigurationFromProperties("mailer.properties"));
        assertTrue(mailer.loadConfigurationFromProperties("org/europa/together/properties/mail-test-classpath.properties"));
        assertEquals("localhost", mailer.getConfiguration().get("mailer.host"));
    }

    @Test
    void testLoadConfigurationFromFileSystem() {
        LOGGER.log("TEST CASE: loadConfigurationFromFileSystem", LogLevel.DEBUG);

        mailer.clearConfiguration();
        assertFalse(mailer.loadConfigurationFromProperties("mailer.properties"));
        assertTrue(mailer.loadConfigurationFromProperties(DIRECTORY
                + "/org/europa/together/properties/mail-test-filesystem.properties"));
        assertEquals("www.sample.com", mailer.getConfiguration().get("mailer.host"));
    }

    @Test
    void testFailSMTPConnection() throws Exception {
        LOGGER.log("TEST CASE: failSMTPConnection", LogLevel.DEBUG);

        mailer.clearRecipents();
        mailer.clearAttachments();

        mailer.loadConfigurationFromProperties("org/europa/together/properties/mail-test-classpath.properties");
        assertNotNull(mailer.composeMail(new InternetAddress("otto@sample.org")));
    }

    @Test
    void testConnectSMTPServer() throws Exception {
        LOGGER.log("TEST CASE: connectSMTPServer", LogLevel.DEBUG);

        mailer.clearRecipents();
        mailer.clearAttachments();

        mailer.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");
        MimeMessage message = mailer.composeMail(new InternetAddress("otto@sample.org"));
        assertEquals("text/plain", message.getContentType());
    }

    @Test
    void testComposeTextMail() throws Exception {
        LOGGER.log("TEST CASE: composeTextMail", LogLevel.DEBUG);

        mailer.clearRecipents();
        mailer.clearAttachments();

        mailer.loadConfigurationFromProperties("org/europa/together/properties/mail-test-classpath.properties");

        mailer.addRecipent("otto@sample.org");
        mailer.addAttachment(DIRECTORY + "/Attachment.pdf");
        mailer.setSubject("TESTMAIL");
        mailer.setContent("<h1>TEST</h1><br/><p>This ist the testmail content.</p>");
        mailer.setMimeTypeToHTML();

        MimeMessage message = mailer.composeMail(mailer.getRecipentList().get(0));
        assertEquals("text/plain", message.getContentType());
        assertEquals("TESTMAIL", message.getSubject());
        assertEquals("noreply@sample.org", message.getFrom()[0].toString());
        assertEquals(new InternetAddress("noreply@sample.org"), message.getSender());
        assertEquals("otto@sample.org", message.getAllRecipients()[0].toString());
    }

    @Test
    void testPopulateDatabaseConfiguration() {
        LOGGER.log("TEST CASE: populateDatabaseConfiguration", LogLevel.DEBUG);

        mailer.populateDbConfiguration(SQL_FILE, true);
        assertTrue(mailer.loadConfigurationFromDatabase());
        assertEquals("smtp.sample.org", mailer.getConfiguration().get("mailer.host"));
        //Clean up
        CONNECTION.executeQuery("TRUNCATE TABLE app_config;");
    }

    @Test
    void testFailPopulateDatabaseConfiguration() {
        LOGGER.log("TEST CASE: failPopulateDatabaseConfiguration", LogLevel.DEBUG);

        mailer.populateDbConfiguration(null);
        assertFalse(mailer.loadConfigurationFromDatabase());
        //Clean up
        CONNECTION.executeQuery("TRUNCATE TABLE app_config;");
    }

    @Test
    void testLoadConfigurationFromDatabase() {
        LOGGER.log("TEST CASE: loadConfigurationFromDatabase", LogLevel.DEBUG);

        try {
            //DBMS Table setup
            mailer.populateDbConfiguration(SQL_FILE, true);
            assertTrue(mailer.loadConfigurationFromDatabase());

            assertEquals("smtp.sample.org", mailer.getConfiguration().get("mailer.host"));
            assertEquals("465", mailer.getConfiguration().get("mailer.port"));
            assertEquals("noreply@sample.org", mailer.getConfiguration().get("mailer.sender"));
            assertEquals("JohnDoe", mailer.getConfiguration().get("mailer.user"));
            assertEquals("s3cr3t", mailer.getConfiguration().get("mailer.password"));
            assertEquals("true", mailer.getConfiguration().get("mailer.ssl"));
            assertEquals("true", mailer.getConfiguration().get("mailer.tls"));
            assertEquals("false", mailer.getConfiguration().get("mailer.debug"));
            assertEquals("1", mailer.getConfiguration().get("mailer.count"));
            assertEquals("0", mailer.getConfiguration().get("mailer.wait"));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }

        CONNECTION.executeQuery("TRUNCATE TABLE app_config;");
    }
}
