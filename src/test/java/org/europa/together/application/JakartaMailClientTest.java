package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import java.security.Security;
import java.util.Map;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.europa.together.JUnit5Preperator;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class JakartaMailClientTest {

    private static final Logger LOGGER = new LogbackLogger(JakartaMailClientTest.class);

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
        //DBMS
        Assumptions.assumeTrue(jdbcActions.connect("test"), "JDBC DBMS Connection failed.");
        //SMTP Test Server
        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        SMTP_SERVER = new GreenMail(ServerSetupTest.SMTPS);
        SMTP_SERVER.start();
        SMTP_SERVER.setUser("john.doe@localhost", "JohnDoe", "s3cr3t");
        Assumptions.assumeTrue(SMTP_SERVER.getSmtps().isRunning(), "GreenMail embedded SMTP Server fail to start.");

        LOGGER.log("Assumptions passed ...\n\n", LogLevel.DEBUG);
    }

    @AfterAll
    static void tearDown() {
        try {
            SMTP_SERVER.stop();
            jdbcActions.executeQuery(FLUSH_TABLE);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
    }
    //</editor-fold>

    @Test
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(JakartaMailClient.class, hasValidBeanConstructor());
    }

    @Test
    void initialConfiguration() {
        LOGGER.log("TEST CASE: initialConfiguration", LogLevel.DEBUG);

        MailClient client = new JakartaMailClient();
        assertEquals(-1, client.getBulkMailLimiter());
        assertEquals(-1, client.getWaitTime());
        assertEquals(10, client.getDebugActiveConfiguration().size());
        assertEquals("mailer.host", client.getDebugActiveConfiguration().get("mailer.host"));
    }

    @Test
    void cleanConfiguration() {
        LOGGER.log("TEST CASE: cleanConfiguration", LogLevel.DEBUG);

        MailClient client = new JakartaMailClient();
        client.clearConfiguration();
        assertEquals(0, client.getDebugActiveConfiguration().size());
    }

    @Test
    void loadConfigurationFromClasspath() throws Exception {
        LOGGER.log("TEST CASE: loadConfigurationFromClasspath", LogLevel.DEBUG);

        mailClient.loadConfigurationFromProperties("org/europa/together/properties/mail-test-classpath.properties");
        assertEquals("localhost", mailClient.getDebugActiveConfiguration().get("mailer.host"));
    }

    @Test
    void loadConfigurationFromFileSystem() throws Exception {
        LOGGER.log("TEST CASE: loadConfigurationFromFileSystem", LogLevel.DEBUG);

        mailClient.loadConfigurationFromProperties(DIRECTORY
                + "/org/europa/together/properties/mail-test-filesystem.properties");
        assertEquals("www.sample.com", mailClient.getDebugActiveConfiguration().get("mailer.host"));
    }

    @Test
    void loadConfigurationFromDatabase() {
        LOGGER.log("TEST CASE: loadConfigurationFromDatabase", LogLevel.DEBUG);

        jdbcActions.executeSqlFromClasspath(SQL_FILE);
        assertTrue(mailClient.loadConfigurationFromDatabase());

        assertEquals("smtp.sample.org", mailClient.getDebugActiveConfiguration().get("mailer.host"));
        assertEquals("465", mailClient.getDebugActiveConfiguration().get("mailer.port"));
        assertEquals("send.from@mail.me", mailClient.getDebugActiveConfiguration().get("mailer.sender"));
        assertEquals("JohnDoe", mailClient.getDebugActiveConfiguration().get("mailer.user"));
        assertEquals("s3cr3t", mailClient.getDebugActiveConfiguration().get("mailer.password"));
        assertEquals("true", mailClient.getDebugActiveConfiguration().get("mailer.ssl"));
        assertEquals("true", mailClient.getDebugActiveConfiguration().get("mailer.tls"));
        assertEquals("false", mailClient.getDebugActiveConfiguration().get("mailer.debug"));
        assertEquals("1", mailClient.getDebugActiveConfiguration().get("mailer.count"));
        assertEquals("0", mailClient.getDebugActiveConfiguration().get("mailer.wait"));
    }

    @Test
    void tryLoadConfigEntryNotExist() {
        LOGGER.log("TEST CASE: tryLoadConfigEntryNotExist", LogLevel.DEBUG);

        jdbcActions.executeSqlFromClasspath(SQL_FILE);
        assertTrue(mailClient.loadConfigurationFromDatabase());

        assertNull(mailClient.getDebugActiveConfiguration().get("entry.do.not.exist"));
    }

    @Test
    void failLoadConfigurationFromDatabase() throws Exception {
        LOGGER.log("TEST CASE: failLoadConfigurationFromDatabase", LogLevel.DEBUG);

        jdbcActions.executeQuery(FLUSH_TABLE);
        assertFalse(mailClient.loadConfigurationFromDatabase());
    }

    @Test
    void failLoadConfigurationFromPropertyFile() throws Exception {
        LOGGER.log("TEST CASE: failLoadConfigurationFromPropertyFile", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            mailClient.loadConfigurationFromProperties(DIRECTORY + "/not-exist.properties");
        });
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
