package org.europa.together.application;

import java.util.ArrayList;
import java.util.List;
import javax.activation.FileDataSource;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.MailClient;
import org.europa.together.domain.ResourceType;
import org.europa.together.utils.Constraints;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao-test.xml"})
public class MailClientImplTest {

    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes";

    private static String flush_table = "TRUNCATE TABLE app_config;";
    private static String file = "org/europa/together/sql/email-config-test.sql";
    private static DatabaseActions actions;
    private final MailClient mailer = new MailClientImpl();

    @BeforeClass
    public static void setUp() {
        actions = new DatabaseActionsImpl(true);
        actions.connect(null);
        actions.executeSqlFromClasspath(file);
    }

    @AfterClass
    public static void tearDown() {
        actions.executeQuery(flush_table);
        actions.disconnect();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadConfigurationFromProperties() {

        //wrong ResourceType
        mailer.loadConfigurationFromProperties(ResourceType.LDAP, "");
        //CLASSPATH
        assertTrue(mailer.loadConfigurationFromProperties(ResourceType.CLASSPATH, "mail-test.properties"));
    }

    @Test
    public void testMimeType() {
        //Default config
        assertEquals("plain", mailer.getMimeType());
        //change to HTML
        mailer.setMimeTypeToHTML();
        assertEquals("html", mailer.getMimeType());
        //change to plain
        mailer.setMimeTypeToPlain();
        assertEquals("plain", mailer.getMimeType());
    }

    @Test
    public void testSetContent() {
        assertEquals(15, mailer.setContent("E-Mail Message."));
    }

    @Test
    public void testRecipients() {

        //recipientList
        List<String> failure = new ArrayList<>();
        failure.add("@domain");
        failure.add("no_mail.domain");
        failure.add("success@domain");
        failure.add("success@domain");
        failure.add("no_mail.domain");

        List<String> success = new ArrayList<>();
        success.add("success@mail-domain");
        success.add("success@domain.test");
        success.add("success@domain.test");
        success.add("no_mail@domain.eu");

        //Initial NULL
        assertNotNull(mailer.getRecipents());
        assertEquals(0, mailer.getRecipents().size());

        //success
        assertTrue(mailer.addRecipent("john.doe@sample.org"));
        assertEquals(1, mailer.getRecipents().size());

        // add another adress
        assertTrue(mailer.addRecipent("jane.doe@sample.org"));
        assertEquals(2, mailer.getRecipents().size());

        //add the same account again
        assertFalse(mailer.addRecipent("john.doe@sample.org"));
        assertEquals(2, mailer.getRecipents().size());

        //exception
        assertFalse(mailer.addRecipent("wrong_mail-adress"));
        assertEquals(2, mailer.getRecipents().size());

        //recipientList
        assertFalse(mailer.addRecipentList(failure));
        assertEquals(2, mailer.getRecipents().size());

        assertTrue(mailer.addRecipentList(success));
        assertEquals(4, mailer.getRecipents().size());

        //empty recipientList
        mailer.clearRecipents();
        assertEquals(0, mailer.getRecipents().size());
    }

    @Test
    public void testSetContentByVelocityTemplate() {

        //wrong resource - jumps to DEFAULT
        assertEquals(0, mailer.setContentByVelocityTemplate(ResourceType.LDAP, "", "", null));

        //CLASSPATH
        assertEquals(12, mailer.setContentByVelocityTemplate(ResourceType.CLASSPATH,
                "org/europa/together/velocity", "/template.vm", null));

        //FILE
        assertEquals(12, mailer.setContentByVelocityTemplate(ResourceType.FILE,
                DIRECTORY + "/org/europa/together/velocity", "/template.vm", null));

    }

    @Test
    public void testAddAttachment() {
        //Attachment are empty
        String file = DIRECTORY + "/Attachment.pdf";
        assertEquals(0, mailer.getAttachments().size());

        //add one attachment
        assertTrue(mailer.addAttachment(file));
        assertEquals(1, mailer.getAttachments().size());
        //check if the attachment appears in the array
        FileDataSource test_A = new FileDataSource(file);
        FileDataSource test_B = mailer.getAttachments().get(0);
        assertEquals(test_B.getFile().length(), test_A.getFile().length());
        //test clearing of attachment list
        mailer.clearAttachments();
        assertEquals(0, mailer.getAttachments().size());
    }

    // -------------------------------------------------------------------------
    @Test
    public void testLoadConfigurationFromDatabase() {
    }

    @Test
    public void testMail() {
    }

}
