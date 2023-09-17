package org.europa.together.service;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.tngtech.jgiven.junit5.ScenarioTest;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.europa.together.application.DatabaseActionsImpl;
import org.europa.together.application.LoggerImpl;
import org.europa.together.application.MailClientImpl;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.SocketTimeout;
import org.europa.together.utils.StringUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class MailClientScenarioTest extends
        ScenarioTest<MailServiceGiven, MailServiceAction, MailServiceOutcome> {

    private static final Logger LOGGER
            = new LoggerImpl(MailClientScenarioTest.class);
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes";
    private static final String SQL_FILE
            = "org/europa/together/sql/email-config-test.sql";

    public static DatabaseActions CONNECTION = new DatabaseActionsImpl(true);
    public static GreenMail SMTP_SERVER = null;
    private MailClient client = null;

    public MailClientScenarioTest() {
        client = new MailClientImpl();
        //COMPOSE MAIL
        client.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");
        client.setSubject("JGiven Test E-Mail");
        client.setContent(StringUtils.generateLoremIpsum(0));
        client.addAttachment(DIRECTORY + "/Attachment.pdf");
    }

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {

        CONNECTION.connect("default");
        boolean check = SocketTimeout.timeout(2000, CONNECTION.getUri(), CONNECTION.getPort());
        LOGGER.log("PERFORM TESTS :: Check DBMS availability -> " + check, LogLevel.TRACE);
        String out;
        if (check) {
            out = "executed.";
        } else {
            out = "skiped.";
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out + "\n", LogLevel.TRACE);
        Assumptions.assumeTrue(check);

        //DATABASE CONFIGURATION ENTIES FOR E-MAIL
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
    void scenario_sendBulkMail() {
        LOGGER.log("Scenario A: Bulk Mail", LogLevel.DEBUG);

        List<String> recipientList = new ArrayList<>();
        recipientList.add("recipient_01@sample.org");
        recipientList.add("recipient_02@sample.org");
        recipientList.add("recipient_03@sample.org");
        recipientList.add("recipient_04@sample.org");
        recipientList.add("recipient_05@sample.org");
        recipientList.add("recipient_06@sample.org");
        recipientList.add("recipient_07@sample.org");
        recipientList.add("recipient_08@sample.org");
        recipientList.add("recipient_09@sample.org");
        recipientList.add("recipient_10@sample.org");

        client.clearRecipents();
        client.addRecipientList(recipientList);

        try {
            // PreCondition
            given().email_get_configuration(client)
                    .and().smpt_server_is_available()
                    .and().email_has_recipients(client)
                    .and().email_contains_attachment(client)
                    .and().email_is_composed(client);

            // Invariant
            when().smpt_server_is_available()
                    .and().send_bulk_email(client);

            //PostCondition
            then().mass_emails_are_arrived(SMTP_SERVER.getReceivedMessages());

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void scenario_sendSingleEmail() {
        LOGGER.log("Scenario B: Single Mail", LogLevel.DEBUG);

        client.clearRecipents();
        client.addRecipent("otto@sample.org");
        try {
            // PreCondition
            given().email_get_configuration(client)
                    .and().smpt_server_is_available()
                    .and().email_has_recipient(client)
                    .and().email_contains_attachment(client)
                    .and().email_is_composed(client);

            // Invariant
            when().smpt_server_is_available()
                    .and().send_email(client);

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
            given().service_has_database_connection()
                    .and().service_get_db_configuration();

            // Invariant
            when().update_email_database_config(config);

            //PostCondition
            then().configuration_is_changed();

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }
}
