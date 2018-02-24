package org.europa.together.service;

import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.tngtech.jgiven.junit5.ScenarioTest;
import java.security.Security;
import org.europa.together.application.LoggerImpl;
import org.europa.together.application.MailClientImpl;
import org.europa.together.business.Logger;
import org.europa.together.business.MailClient;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

    private static MailClient mailer = null;
    public static GreenMail SMTP_SERVER = null;

    @BeforeAll
    static void setUp() {
        //SMTP Test Server
        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        SMTP_SERVER = new GreenMail(ServerSetupTest.SMTPS);
        SMTP_SERVER.start();
        SMTP_SERVER.setUser("john.doe@localhost", "JohnDoe", "s3cr3t");

        //COMPOSE MAIL
        String content = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
        mailer = new MailClientImpl();
        mailer.loadConfigurationFromProperties("org/europa/together/properties/mail-test.properties");
        mailer.setSubject("JGiven Test E-Mail");
        mailer.setContent(content);
        mailer.addRecipent("otto@sample.org");
        mailer.addAttachment(DIRECTORY + "/Attachment.pdf");
    }

    @AfterAll
    static void tearDown() {
        try {
            SMTP_SERVER.stop();
            LOGGER.log("TEST SUITE TERMINATED.", LogLevel.TRACE);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void scenario_sendSingleEmail() {

        try {
            given().email_get_configuration(mailer)
                    .and().smpt_server_is_available()
                    .and().email_has_recipient(mailer)
                    .and().email_contains_attachment(mailer)
                    .and().email_is_composed(mailer);

            when().smpt_server_is_available()
                    .and().send_email(mailer);

            SMTP_SERVER.waitForIncomingEmail(1);

            then().email_is_arrived(SMTP_SERVER.getReceivedMessages()[0]);

            SMTP_SERVER.purgeEmailFromAllMailboxes();

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }
}
