package org.europa.together.application;

import java.util.ArrayList;
import java.util.List;
import org.europa.together.business.MailClient;
import org.europa.together.domain.ResourceType;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class MailClientImplTest {

    private final MailClient mailer = new MailClientImpl();

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

        List<String> success = new ArrayList<>();
        success.add("success@mail-domain");
        success.add("success@domain");
        success.add("success@domain");
        success.add("no_mail.domain");

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
        assertEquals(0, mailer.setContentByVelocityTemplate(ResourceType.LDAP, "", null));

        //CLASSPATH
        String classPathResource = "org/europa/together/velocity/template.vm";
        assertEquals(12, mailer.setContentByVelocityTemplate(ResourceType.CLASSPATH,
                classPathResource, null));

    }

    // -------------------------------------------------------------------------
    @Test
    public void testLoadConfigurationFromDatabase() {
    }

    @Test
    public void testLoadConfigurationFromProperties() {
    }

    @Test
    public void testMail() {
    }

    @Test
    public void testAddAttachment() {
    }

}
