package org.europa.together.service;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.util.HashMap;
import java.util.Map;
import org.europa.together.JUnit5Preperator;
import org.europa.together.application.JdbcActions;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.Mail;
import org.europa.together.utils.StringUtils;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class MailClientServiceTest {

    private static final Logger LOGGER
            = new LogbackLogger(MailClientServiceTest.class);
    private static final String SQL_FILE
            = "/org/europa/together/sql/email-config-test.sql";
    public static DatabaseActions CONNECTION
            = new JdbcActions();

    @Autowired
    private MailClientService mailClientService;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(CONNECTION.connect("test"), "JDBC DBMS Connection failed.");

        LOGGER.log("Assumptions passed ...\n\n", LogLevel.DEBUG);
    }

    @AfterAll
    static void tearDown() {
    }

    @BeforeEach
    void testCaseInitialization() {
        CONNECTION.executeSqlFromClasspath(SQL_FILE);
    }

    @AfterEach
    void testCaseTermination() throws Exception {
        LOGGER.log("CleanUp Test Suite.", LogLevel.DEBUG);
        try {
            CONNECTION.executeQuery("TRUNCATE TABLE APP_CONFIG;");
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }
    //</editor-fold>

    @Test
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);
        assertThat(MailClientService.class, hasValidBeanConstructor());
    }

    @Test
    void loadInitConfiguration() {
        LOGGER.log("TEST CASE: loadConfiguration", LogLevel.DEBUG);

        Map<String, String> config = mailClientService.loadConfiguration();
        LOGGER.log("Mail Config: " + config.toString(), LogLevel.DEBUG);

        assertNotNull(config);
        assertEquals(10, config.size());
        assertEquals("smtp.sample.org", config.get("mailer.host"));
    }

    @Test
    void updateConfiguration() {
        LOGGER.log("TEST CASE: updateConfiguration", LogLevel.DEBUG);

        //Arrage
        Map<String, String> config = new HashMap<>();
        config.put("mailer.port", "9999");
        //Act
        try {
            mailClientService.updateConfiguration(config);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        //Assume
        Map<String, String> newConfig
                = mailClientService.loadConfiguration();
        assertEquals("9999", newConfig.get("mailer.port"));
    }

    @Test
    @Disabled
    void sendMail() throws Exception {
        LOGGER.log("TEST CASE: sendMail", LogLevel.DEBUG);

        Mail mail = new Mail();
        mail.setSubject("JGiven Test E-Mail");
        mail.setMessage(StringUtils.generateLoremIpsum(0));
        mail.addRecipent("recipient_01@sample.org");

        mailClientService.sendEmail(mail);
    }

    @Test
    @Disabled
    void sendBulkMail() throws Exception {
        LOGGER.log("TEST CASE: sendBulkMail", LogLevel.DEBUG);

        Mail mail = new Mail();
        mail.setSubject("JGiven Test E-Mail");
        mail.setMessage(StringUtils.generateLoremIpsum(0));
        mail.addRecipent("recipient_01@sample.org");
        mail.addRecipent("recipient_02@sample.org");
        mail.addRecipent("recipient_03@sample.org");
        mail.addRecipent("recipient_04@sample.org");
        mail.addRecipent("recipient_05@sample.org");

        mailClientService.sendBulkMail(mail);
    }
}
