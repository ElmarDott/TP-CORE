package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import java.io.IOException;
import java.security.Security;
import java.util.Map;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import org.europa.together.domain.ConfigurationDO;
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

    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/";
    private static final String SQL_FILE
            = "org/europa/together/sql/email-config-test.sql";
    private static final String FLUSH_TABLE
            = "TRUNCATE TABLE " + ConfigurationDO.TABLE_NAME + ";";

    @Autowired
    private MailClient mailClient;

    private static GreenMail SMTP_SERVER;
    private static DatabaseActions jdbcActions = new JdbcActions();

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        //SMTP Test Server
        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        SMTP_SERVER = new GreenMail(ServerSetupTest.SMTPS);
        SMTP_SERVER.start();
        SMTP_SERVER.setUser("john.doe@localhost", "JohnDoe", "s3cr3t");
        Assumptions.assumeTrue(SMTP_SERVER.getSmtps().isRunning());
        //DBMS
        Assumptions.assumeTrue(jdbcActions.connect("default"));

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() {
        try {
            SMTP_SERVER.stop();
            jdbcActions.executeQuery(FLUSH_TABLE);

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
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(JavaMailClient.class, hasValidBeanConstructor());
    }

    @Test
    void initialConfiguration() {
        LOGGER.log("TEST CASE: initialConfiguration", LogLevel.DEBUG);

        MailClient client = new JavaMailClient();
        assertEquals(-1, client.getBulkMailLimiter());
        assertEquals(-1, client.getWaitTime());
        assertEquals(10, client.getDebugActiveConfiguration().size());
        assertEquals("mailer.host", client.getDebugActiveConfiguration().get("mailer.host"));
    }

    @Test
    void cleanConfiguration() {
        LOGGER.log("TEST CASE: cleanConfiguration", LogLevel.DEBUG);

        MailClient client = new JavaMailClient();
        client.clearConfiguration();
        assertEquals(0, client.getDebugActiveConfiguration().size());
    }

    @Test
    void loadConfigurationFromClasspath() throws IOException {
        LOGGER.log("TEST CASE: loadConfigurationFromClasspath", LogLevel.DEBUG);

        mailClient.loadConfigurationFromProperties("org/europa/together/properties/mail-test-classpath.properties");
        assertEquals("localhost", mailClient.getDebugActiveConfiguration().get("mailer.host"));
    }

    @Test
    void loadConfigurationFromFileSystem() throws IOException {
        LOGGER.log("TEST CASE: loadConfigurationFromFileSystem", LogLevel.DEBUG);

        mailClient.loadConfigurationFromProperties(DIRECTORY
                + "/org/europa/together/properties/mail-test-filesystem.properties");
        assertEquals("www.sample.com", mailClient.getDebugActiveConfiguration().get("mailer.host"));
    }

    @Test
    void loadConfigurationFromDatabase() {
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
    void failLoadConfigurationFromDatabase() {
        LOGGER.log("TEST CASE: failLoadConfigurationFromDatabase", LogLevel.DEBUG);

        assertTrue(jdbcActions.executeQuery(FLUSH_TABLE));
        assertFalse(mailClient.loadConfigurationFromDatabase());
    }

    @Test
    void failLoadConfigurationFromPropertyFile() throws IOException {
        LOGGER.log("TEST CASE: failLoadConfigurationFromPropertyFile", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            mailClient.loadConfigurationFromProperties(DIRECTORY + "/not-exist.properties");
        });
    }

    @Test
    void failPopulateDatabaseConfiguration() {
        LOGGER.log("TEST CASE: failPopulateDatabaseConfiguration", LogLevel.DEBUG);

        MailClient client = new JavaMailClient();
        client.populateDbConfiguration(null);
        assertEquals("mailer.host", client.getDebugActiveConfiguration().get("mailer.host"));
    }

    @Test
    void connectSMTPServer() throws Exception {
        LOGGER.log("TEST CASE: connectSMTPServer", LogLevel.DEBUG);

        mailClient.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");
        Map<String, String> conf = mailClient.getDebugActiveConfiguration();
        assertEquals("3465", conf.get("mailer.port"));

        Session session = mailClient.getSession();
        assertNotNull(session);
        assertEquals("127.0.0.1", session.getProperty("mail.smtp.host"));
    }

    @Test
    void failConnetSMTPServer() throws Exception {
        LOGGER.log("TEST CASE: failConnectSMTPServer", LogLevel.DEBUG);

        mailClient.clearConfiguration();
        assertThrows(Exception.class, () -> {
            mailClient.getSession();
        });
    }

    @Test
    void composeHtmlMail() throws Exception {
        LOGGER.log("TEST CASE: composeHtmlMail", LogLevel.DEBUG);

        Mail mail = new Mail();
        mail.setMimeTypeToHTML();
        mail.setSubject("TESTMAIL");
        mail.setMessage("<h1>TEST</h1><br/><p>This ist the testmail content.</p>");
        mail.addRecipent("otto@sample.org");

        mailClient.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");
        mailClient.composeMail(mail);
        MimeMessage message = mailClient.getMimeMessage();

        assertEquals("text/plain", message.getContentType());
        assertEquals("TESTMAIL", message.getSubject());
        assertEquals("noreply@sample.org", message.getFrom()[0].toString());
        assertEquals(new InternetAddress("noreply@sample.org"), message.getSender());
    }

    @Test
    void composeTextMail() throws Exception {
        LOGGER.log("TEST CASE: composeTextMail", LogLevel.DEBUG);

        Mail mail = new Mail();
        mail.setSubject("TESTMAIL");
        mail.setMessage("This ist the testmail content.");
        mail.addRecipent("otto@sample.org");

        mailClient.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");
        mailClient.composeMail(mail);
        MimeMessage message = mailClient.getMimeMessage();

        assertEquals("text/plain", message.getContentType());
        assertEquals("TESTMAIL", message.getSubject());
        assertEquals("noreply@sample.org", message.getFrom()[0].toString());
        assertEquals(new InternetAddress("noreply@sample.org"), message.getSender());
    }

    @Test
    void composeTextMailWithAttachment() throws Exception {
        LOGGER.log("TEST CASE: composeTextMailWithAttachment", LogLevel.DEBUG);

        Mail mail = new Mail();
        mail.setSubject("TESTMAIL");
        mail.setMessage("This ist the testmail content.");
        mail.addRecipent("otto@sample.org");
        mail.addAttachment(DIRECTORY + "/Attachment.pdf");

        mailClient.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");
        mailClient.composeMail(mail);
        MimeMessage message = mailClient.getMimeMessage();

        assertEquals("text/plain", message.getContentType());
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
