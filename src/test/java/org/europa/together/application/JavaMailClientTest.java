package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import java.security.Security;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.Mail;
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

    private static GreenMail SMTP_SERVER;
    private static DatabaseActions CONNECTION = new JdbcActions();
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes";
    private static final String SQL_FILE
            = "org/europa/together/sql/email-config-test.sql";

    @Autowired
    private MailClient mailClient;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        boolean check = true;

        boolean socket = CONNECTION.connect("default");
        if (!socket) {
            check = false;
        }

        LOGGER.log("Assumption terminated. TestSuite execution: " + check, LogLevel.TRACE);
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
        assertEquals(10, client.getDebugActiveConfiguration().size());
        assertEquals("mailer.host", client.getDebugActiveConfiguration().get("mailer.host"));
    }

    @Test
    void testCleanConfiguration() {
        MailClient client = new JavaMailClient();
        client.clearConfiguration();
        assertEquals(0, client.getDebugActiveConfiguration().size());
    }

    @Test
    void testFailPopulateDatabaseConfiguration() {
        LOGGER.log("TEST CASE: failPopulateDatabaseConfiguration", LogLevel.DEBUG);

        MailClient client = new JavaMailClient();
        client.populateDbConfiguration(null);
        assertEquals("mailer.host", client.getDebugActiveConfiguration().get("mailer.host"));
    }

    @Test
    void testFailLoadConfigurationFromPropertyFile() {
        LOGGER.log("TEST CASE: failLoadConfigurationFromPropertyFile", LogLevel.DEBUG);

        assertFalse(mailClient.loadConfigurationFromProperties(DIRECTORY + "/not-exist.properties"));
    }

    @Test
    void testFailLoadConfigurationFromDatabase() {
        LOGGER.log("TEST CASE: failLoadConfigurationFromDatabase", LogLevel.DEBUG);

        assertTrue(CONNECTION.executeQuery("TRUNCATE TABLE app_config;"));
        assertFalse(mailClient.loadConfigurationFromDatabase());
    }

    @Test
    void testLoadConfigurationFromDatabase() {
        LOGGER.log("TEST CASE: loadConfigurationFromDatabase", LogLevel.DEBUG);

        mailClient.populateDbConfiguration(SQL_FILE);
        assertTrue(mailClient.loadConfigurationFromDatabase());

        assertEquals("smtp.sample.org", mailClient.getDebugActiveConfiguration().get("mailer.host"));
        assertEquals("465", mailClient.getDebugActiveConfiguration().get("mailer.port"));
        assertEquals("noreply@sample.org", mailClient.getDebugActiveConfiguration().get("mailer.sender"));
        assertEquals("JohnDoe", mailClient.getDebugActiveConfiguration().get("mailer.user"));
        assertEquals("s3cr3t", mailClient.getDebugActiveConfiguration().get("mailer.password"));
        assertEquals("true", mailClient.getDebugActiveConfiguration().get("mailer.ssl"));
        assertEquals("true", mailClient.getDebugActiveConfiguration().get("mailer.tls"));
        assertEquals("false", mailClient.getDebugActiveConfiguration().get("mailer.debug"));
        assertEquals("1", mailClient.getDebugActiveConfiguration().get("mailer.count"));
        assertEquals("0", mailClient.getDebugActiveConfiguration().get("mailer.wait"));
    }

    @Test
    void testLoadConfigurationFromClasspath() {
        LOGGER.log("TEST CASE: loadConfigurationFromClasspath", LogLevel.DEBUG);

        assertTrue(mailClient.loadConfigurationFromProperties("org/europa/together/properties/mail-test-classpath.properties"));
        assertEquals("localhost", mailClient.getDebugActiveConfiguration().get("mailer.host"));
    }

    @Test
    void testLoadConfigurationFromFileSystem() {
        LOGGER.log("TEST CASE: loadConfigurationFromFileSystem", LogLevel.DEBUG);

        assertTrue(mailClient.loadConfigurationFromProperties(DIRECTORY
                + "/org/europa/together/properties/mail-test-filesystem.properties"));
        assertEquals("www.sample.com", mailClient.getDebugActiveConfiguration().get("mailer.host"));
    }

    @Test
    void testConnectSMTPServer() throws Exception {
        LOGGER.log("TEST CASE: connectSMTPServer", LogLevel.DEBUG);

        mailClient.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");
        Session session = mailClient.getSession();
        assertEquals("127.0.0.1", session.getProperty("mail.smtp.host"));
    }

    @Test
    void testComposeHtmlMail() throws Exception {
        LOGGER.log("TEST CASE: composeHtmlMail", LogLevel.DEBUG);

        Mail mail = new Mail();
        mail.setMimeTypeToHTML();
        mail.setSubject("TESTMAIL");
        mail.setMessage("<h1>TEST</h1><br/><p>This ist the testmail content.</p>");
        mail.addRecipent("otto@sample.org");
        mail.addAttachment(DIRECTORY + "/Attachment.pdf");

        mailClient.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");
        mailClient.composeMail(mail);
        MimeMessage message = mailClient.getMimeMessage();

        assertEquals("text/plain", message.getContentType());
        assertEquals("TESTMAIL", message.getSubject());
        assertEquals("noreply@sample.org", message.getFrom()[0].toString());
        assertEquals(new InternetAddress("noreply@sample.org"), message.getSender());
    }

    @Test
    void testComposeTextMail() throws Exception {
        LOGGER.log("TEST CASE: composeTextMail", LogLevel.DEBUG);

        Mail mail = new Mail();
        mail.setSubject("TESTMAIL");
        mail.setMessage("This ist the testmail content.");
        mail.addRecipent("otto@sample.org");
        mail.addAttachment(DIRECTORY + "/Attachment.pdf");

        mailClient.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");
        mailClient.composeMail(mail);
        MimeMessage message = mailClient.getMimeMessage();

        assertEquals("text/plain", message.getContentType());
        assertEquals("TESTMAIL", message.getSubject());
        assertEquals("noreply@sample.org", message.getFrom()[0].toString());
        assertEquals(new InternetAddress("noreply@sample.org"), message.getSender());
    }

    @Test
    void testGetMailObject() throws Exception {
        LOGGER.log("TEST CASE: getMailObject", LogLevel.DEBUG);

        Mail mail = new Mail();
        mail.setSubject("TESTMAIL");
        mail.setMessage("This ist the testmail content.");
        mail.addRecipent("otto@sample.org");
        mail.addAttachment(DIRECTORY + "/Attachment.pdf");

        mailClient.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");
        mailClient.composeMail(mail);

        assertEquals(mail, mailClient.getMailObject());
    }
}
