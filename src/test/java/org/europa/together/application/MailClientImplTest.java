package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
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
import org.europa.together.utils.SocketTimeout;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao-test.xml"})
public class MailClientImplTest {

    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes";
    private static final String SQL_FILE
            = "org/europa/together/sql/email-config-test.sql";

    private static final Logger LOGGER = new LoggerImpl(MailClientImplTest.class);

    @Autowired
    @Qualifier("mailClientImpl")
    private MailClient mailer;

    private static DatabaseActions actions = new DatabaseActionsImpl(true);
    private static GreenMail greenMail;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        actions.connect("default");
        boolean check = SocketTimeout.timeout(2000, actions.getUri(), actions.getPort());
        LOGGER.log("PERFORM TESTS :: Check DBMS availability -> " + check, LogLevel.TRACE);
        String out;
        if (check) {
            out = "executed.";
        } else {
            out = "skiped.";
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out, LogLevel.TRACE);
        Assumptions.assumeTrue(check);

        //SMTP Test Server
        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        greenMail = new GreenMail(ServerSetupTest.SMTPS);
        greenMail.start();
        greenMail.setUser("john.doe@localhost", "JohnDoe", "s3cr3t");

        actions.executeSqlFromClasspath(SQL_FILE);
    }

    @AfterAll
    static void tearDown() {
        greenMail.stop();
        actions.executeQuery("TRUNCATE TABLE app_config;");
        LOGGER.log("TEST SUITE TERMINATED.", LogLevel.TRACE);
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
        assertThat(MailClientImpl.class, hasValidBeanConstructor());
    }

    @Test
    void testInitialConfiguration() {
        MailClient client = new MailClientImpl();
        assertEquals(-1, client.getBulkMailLimiter());
        assertEquals(-1, client.getWaitTime());
        assertEquals(10, client.getConfiguration().size());
    }

    @Test
    void testMimeType() {
        assertEquals("plain", mailer.getMimeType());
        mailer.setMimeTypeToHTML();
        assertEquals("html", mailer.getMimeType());
        mailer.setMimeTypeToPlain();
        assertEquals("plain", mailer.getMimeType());
    }

    @Test
    void testSetContent() {
        String content = "<h1>E Mail>/h1><p>this is the content of the email.";
        mailer.setContent(content);
        assertEquals(content, mailer.getContent());
        mailer.setContent("");
    }

    @Test
    void testSubject() {
        String subject = "E-MAil Topic";
        mailer.setSubject(subject);
        assertEquals(subject, mailer.getSubject());
        mailer.setSubject("");
    }

    @Test
    void testAddRecipent() {
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
        assertEquals(0, mailer.getAttachmentSize());
        mailer.setAttachmentSize(100);
        assertEquals(100, mailer.getAttachmentSize());
        mailer.setAttachmentSize(0);
        assertEquals(0, mailer.getAttachmentSize());
    }

    @Test
    void testAddAttachmentLimitSize() {
        mailer.setAttachmentSize(100);
        assertFalse(mailer.addAttachment(DIRECTORY + "/Attachment.pdf"));
        assertTrue(mailer.addAttachment(DIRECTORY + "/TestFile"));

        mailer.setAttachmentSize(0);
        assertTrue(mailer.addAttachment(DIRECTORY + "/Attachment.pdf"));
    }

    @Test
    void testAddEmptyAttachment() {
        assertEquals(0, mailer.getAttachmentList().size());

        assertFalse(mailer.addAttachment(DIRECTORY + "/empty.attachment"));
        assertEquals(0, mailer.getAttachmentList().size());
        mailer.clearAttachments();
        assertEquals(0, mailer.getAttachmentList().size());
    }

    @Test
    void testAddAttachment() {
        assertEquals(0, mailer.getAttachmentList().size());

        assertTrue(mailer.addAttachment(DIRECTORY + "/Attachment.pdf"));
        assertEquals(1, mailer.getAttachmentList().size());
        mailer.clearAttachments();
        assertEquals(0, mailer.getAttachmentList().size());
        mailer.clearAttachments();
    }

    @Test
    void addAttachmentList() {
        List<String> attachments = new ArrayList<>();
        attachments.add(DIRECTORY + "/Attachment.pdf");
        attachments.add(DIRECTORY + "/TestFile");
        mailer.addAttachmentList(attachments);
        assertEquals(2, mailer.getAttachmentList().size());
        mailer.clearAttachments();
    }

    @Test
    void testAddAttachmentSizeLimit() {
        mailer.setAttachmentSize(500);
        assertFalse(mailer.addAttachment(DIRECTORY + "/Attachment.pdf"));
        assertEquals(0, mailer.getAttachmentList().size());
        mailer.setAttachmentSize(0);
        assertEquals(0, mailer.getAttachmentSize());
    }

    @Test
    void testAddAttachmentFail() {
        mailer.clearAttachments();
        assertFalse(mailer.addAttachment(DIRECTORY + "/File.pdf"));
        assertEquals(0, mailer.getAttachmentList().size());
    }

    @Test
    void testLoadConfigurationFromClasspath() {
        assertFalse(mailer.loadConfigurationFromProperties("mailer.properties"));
        assertTrue(mailer.loadConfigurationFromProperties("org/europa/together/properties/mail-test-classpath.properties"));
    }

    @Test
    void testLoadConfigurationFromFileSystem() {
        assertFalse(mailer.loadConfigurationFromProperties("mailer.properties"));
        assertTrue(mailer.loadConfigurationFromProperties(DIRECTORY
                + "/org/europa/together/properties/mail-test-filesystem.properties"));
    }

    @Test
    void testFailSMTPConnection() throws Exception {
        mailer.clearRecipents();
        mailer.clearAttachments();

        mailer.loadConfigurationFromProperties("org/europa/together/properties/mail-test-classpath.properties");
        assertNotNull(mailer.composeMail(new InternetAddress("otto@sample.org")));
    }

    @Test
    void testConnectSMTPServer() throws Exception {
        mailer.clearRecipents();
        mailer.clearAttachments();

        mailer.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");
        MimeMessage message = mailer.composeMail(new InternetAddress("otto@sample.org"));
        assertEquals("text/plain", message.getContentType());
    }

    @Test
    void testComposeTextMail() throws Exception {
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
    void testLoadConfigurationFromDatabase() {
        System.out.println("\n\n # \n\n");
        assertTrue(mailer.loadConfigurationFromDatabase());

        assertEquals("smtp.gmail.com", mailer.getConfiguration().get("mailer.host"));
        assertEquals("465", mailer.getConfiguration().get("mailer.port"));
        assertEquals("noreply@sample.org", mailer.getConfiguration().get("mailer.sender"));
        assertEquals("JohnDoe", mailer.getConfiguration().get("mailer.user"));
        assertEquals("s3cr3t", mailer.getConfiguration().get("mailer.password"));
        assertEquals("true", mailer.getConfiguration().get("mailer.ssl"));
        assertEquals("true", mailer.getConfiguration().get("mailer.tls"));
        assertEquals("false", mailer.getConfiguration().get("mailer.debug"));
        assertEquals("1", mailer.getConfiguration().get("mailer.count"));
        assertEquals("0", mailer.getConfiguration().get("mailer.wait"));
    }

}
