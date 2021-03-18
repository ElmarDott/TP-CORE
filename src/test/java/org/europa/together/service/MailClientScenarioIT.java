package org.europa.together.service;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.tngtech.jgiven.junit5.ScenarioTest;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.AddressException;
import org.europa.together.application.JdbcActions;
import org.europa.together.application.FF4jProcessor;
import org.europa.together.application.LogbackLogger;
import org.europa.together.application.JavaMailClient;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.Mail;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.StringUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao.xml"})
public class MailClientScenarioIT extends
        ScenarioTest<MailServiceGiven, MailServiceAction, MailServiceOutcome> {

    private static final Logger LOGGER
            = new LogbackLogger(MailClientScenarioIT.class);
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes";
    private static final String SQL_FILE
            = "org/europa/together/sql/email-config-test.sql";

    public static DatabaseActions CONNECTION = new JdbcActions();
    public static GreenMail SMTP_SERVER = null;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);

        FF4jProcessor feature = new FF4jProcessor();
        boolean toggle = feature.deactivateUnitTests(MailClient.FEATURE_ID);
        LOGGER.log("PERFORM TESTS :: FeatureToggle", LogLevel.TRACE);

        boolean socket = CONNECTION.connect("default");
        LOGGER.log("PERFORM TESTS :: Check DBMS availability -> " + socket, LogLevel.TRACE);

        boolean check;
        String out;
        if (!toggle || !socket) {
            out = "skiped.";
            check = false;
        } else {
            out = "executed.";
            check = true;
        }

        LOGGER.log("Assumption terminated. TestSuite will be " + out + "\n", LogLevel.TRACE);
        Assumptions.assumeTrue(check);

        //DBMS Table setup
        CONNECTION.executeSqlFromClasspath(SQL_FILE);

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

            LOGGER.log("TEST SUITE TERMINATED.", LogLevel.TRACE);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        try {
            if (SMTP_SERVER.getReceivedMessages().length > 0) {
                SMTP_SERVER.purgeEmailFromAllMailboxes();
                LOGGER.log("Mailboxes deleted.", LogLevel.TRACE);
            }
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        LOGGER.log("TEST CASE TERMINATED. \n", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        assertThat(MailClientService.class, hasValidBeanConstructor());
    }

    @Test
    void scenario_sendBulkMail() throws AddressException {
        LOGGER.log("Scenario A: Bulk Mail", LogLevel.DEBUG);

        Mail mail = new Mail();
        mail.setSubject("JGiven Test E-Mail");
        mail.setMessage(StringUtils.generateLoremIpsum(0));
        mail.addRecipent("recipient_01@sample.org");
        mail.addRecipent("recipient_02@sample.org");
        mail.addRecipent("recipient_03@sample.org");
        mail.addRecipent("recipient_04@sample.org");
        mail.addRecipent("recipient_05@sample.org");
        mail.addRecipent("recipient_06@sample.org");
        mail.addRecipent("recipient_07@sample.org");
        mail.addRecipent("recipient_08@sample.org");
        mail.addRecipent("recipient_09@sample.org");
        mail.addRecipent("recipient_10@sample.org");
        mail.addAttachment(DIRECTORY + "/Attachment.pdf");

        try {
            //COMPOSE MAIL
            MailClient client = new JavaMailClient();
            client.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");

            // PreCondition
            given().email_get_configuration(client)
                    .and().smpt_server_is_available()
                    .and().email_has_recipients(client)
                    .and().email_contains_attachment(client)
                    .and().email_is_composed(client);

            // Invariant
            when().send_bulk_email(mail);

            //PostCondition
            then().mass_emails_are_arrived(SMTP_SERVER.getReceivedMessages());

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void scenario_sendSingleEmail() {
        LOGGER.log("Scenario B: Single Mail", LogLevel.DEBUG);

        try {
            //COMPOSE MAIL
            Mail mail = new Mail();
            mail.setSubject("JGiven Test E-Mail");
            mail.setMessage(StringUtils.generateLoremIpsum(0));
            mail.addRecipent("otto@sample.org");
            mail.addAttachment(DIRECTORY + "/Attachment.pdf");

            MailClient client = new JavaMailClient();
            client.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");

            // PreCondition
            given().email_get_configuration(client)
                    .and().smpt_server_is_available()
                    .and().email_has_recipient(client)
                    .and().email_contains_attachment(client)
                    .and().email_is_composed(client);

            // Invariant
            when().send_email(mail);

            //PostCondition
            then().email_is_arrived(SMTP_SERVER.getReceivedMessages()[0]);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void scenario_updateDatabaseConfiguration() {
        LOGGER.log("Scenario C: Update Database Configuration", LogLevel.DEBUG);

        Map<String, String> config = new HashMap<>();
        config.put("mailer.host", "SMTPS.localhost:5432");
        try {
            // PreCondition
            given().service_has_database_connection();

            // Invariant
            when().update_email_database_config(config);

            //PostCondition
            then().configuration_is_changed();

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void scenario_getDbConfiguration() {
        LOGGER.log("Scenario D: Get Database Configuration", LogLevel.DEBUG);

        try {
            // PreCondition
            given().service_has_database_connection();

            // Invariant
            when().load_service_database_configuration();

            //PostCondition
            then().db_service_configuration_is_accessible();
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }
}
