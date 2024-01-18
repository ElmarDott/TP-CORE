package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import java.util.ArrayList;
import java.util.List;
import org.europa.together.JUnit5Preperator;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.utils.Constraints;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
public class MailTest {

    private static final Logger LOGGER = new LogbackLogger(MailTest.class);

    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes";

    @Test
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(Mail.class, hasValidBeanConstructor());
    }

    @Test
    void getDefaultMimeType() {
        LOGGER.log("TEST CASE: getDefaultMimeType", LogLevel.DEBUG);

        Mail mail = new Mail();
        assertEquals("plain", mail.getMimeType());
    }

    @Test
    void setHtmlMimeType() {
        LOGGER.log("TEST CASE: setHtmlMimeType", LogLevel.DEBUG);

        Mail mail = new Mail();
        mail.setMimeTypeToHTML();
        assertEquals("html", mail.getMimeType());
    }

    @Test
    void setPlainMimeType() {
        LOGGER.log("TEST CASE: setPlainMimeType", LogLevel.DEBUG);

        Mail mail = new Mail();
        mail.setMimeTypeToHTML();
        mail.setMimeTypeToText();
        assertEquals("plain", mail.getMimeType());
    }

    @Test
    void recipients() throws Exception {
        LOGGER.log("TEST CASE: recipients", LogLevel.DEBUG);

        Mail mail = new Mail();
        LOGGER.log("Inital empty recipient list.", LogLevel.DEBUG);
        assertEquals(0, mail.getRecipentList().size());

        mail.addRecipent("user_01@sample.org");
        assertEquals(1, mail.getRecipentList().size());
        assertEquals("user_01@sample.org", mail.getRecipentList().get(0).getAddress());

        mail.addRecipent("user_02@sample.org");
        assertEquals(2, mail.getRecipentList().size());
        assertEquals("user_02@sample.org", mail.getRecipentList().get(1).getAddress());

        LOGGER.log("Clear recipient list.", LogLevel.DEBUG);
        mail.clearRecipents();
        assertEquals(0, mail.getRecipentList().size());
    }

    @Test
    void addRecipentDetectDoubleEntries() throws Exception {
        LOGGER.log("TEST CASE: addRecipentDetectDoubleEntries", LogLevel.DEBUG);

        Mail mail = new Mail();
        mail.addRecipent("success@domain.test");
        mail.addRecipent("success@domain.test");
        mail.addRecipent("no_mail@domain.eu");
        mail.addRecipent("no_mail@domain.eu");
        assertEquals(2, mail.getRecipentList().size());
    }

    @Test
    void addRecipentValidationFail() throws Exception {
        LOGGER.log("TEST CASE: addRecipentValidationFail", LogLevel.DEBUG);

        Mail mail = new Mail();

        assertThrows(Exception.class, () -> {
            assertFalse(mail.addRecipent("@domain"));
        });
        assertThrows(Exception.class, () -> {
            assertFalse(mail.addRecipent("no_mail.domain"));
        });
        assertThrows(Exception.class, () -> {
            assertFalse(mail.addRecipent("success@domain"));
        });
        assertThrows(Exception.class, () -> {
            assertFalse(mail.addRecipent("fail"));
        });
    }

    @Test
    void addRecipientList() throws Exception {
        LOGGER.log("TEST CASE: addRecipientList", LogLevel.DEBUG);

        List<String> recipients = new ArrayList<>();
        recipients.add("test@sample.com");
        recipients.add("test_01@sample.com");
        recipients.add("test_02@sample.com");
        recipients.add("test_03@sample.com");
        recipients.add("test@sample.com");

        Mail mail = new Mail();
        mail.addRecipientList(recipients);
        assertEquals(4, mail.getRecipentList().size());
    }

    @Test
    void content() {
        LOGGER.log("TEST CASE: content", LogLevel.DEBUG);

        String content = "<h1>E Mail>/h1><p>this is the content of the email.";
        Mail mail = new Mail();
        mail.setMessage(content);
        assertEquals(content, mail.getMessage());
    }

    @Test
    void subject() {
        LOGGER.log("TEST CASE: subject", LogLevel.DEBUG);

        String subject = "E-MAil Topic";
        Mail mail = new Mail();
        mail.setSubject(subject);
        assertEquals(subject, mail.getSubject());
    }

    @Test
    void attachments() {
        LOGGER.log("TEST CASE: attachments", LogLevel.DEBUG);

        Mail mail = new Mail();
        LOGGER.log("Inital empty attachment list.", LogLevel.DEBUG);
        assertEquals(0, mail.getAttachmentList().size());

        mail.addAttachment(DIRECTORY + "/Attachment.pdf");
        assertEquals(1, mail.getAttachmentList().size());

        mail.addAttachment(DIRECTORY + "/TestFile");
        assertEquals(2, mail.getAttachmentList().size());

        LOGGER.log("Clear attachment list.", LogLevel.DEBUG);
        mail.clearAttachments();
        assertEquals(0, mail.getAttachmentList().size());
    }

    @Test
    void failAddEmptyAttachment() {
        LOGGER.log("TEST CASE: failAddEmptyAttachment", LogLevel.DEBUG);

        Mail mail = new Mail();
        assertFalse(mail.addAttachment(DIRECTORY + "/empty.attachment"));
        assertEquals(0, mail.getAttachmentList().size());
    }

    @Test
    void failAddNotExistingAttachment() {
        LOGGER.log("TEST CASE: failAddNotExistingAttachment", LogLevel.DEBUG);

        Mail mail = new Mail();
        assertFalse(mail.addAttachment(DIRECTORY + "/FileNotExist.pdf"));
        assertEquals(0, mail.getAttachmentList().size());
    }

    @Test
    void attachmentSize() {
        LOGGER.log("TEST CASE: attachmentSize", LogLevel.DEBUG);

        Mail mail = new Mail();
        assertEquals(-1, mail.getAttachmentSize());
        mail.setAttachmentSize(100);
        assertEquals(100, mail.getAttachmentSize());
    }

    @Test
    void addAttachmentLimitSize() {
        LOGGER.log("TEST CASE: addAttachmentLimitSize", LogLevel.DEBUG);

        Mail mail = new Mail();
        mail.setAttachmentSize(100);
        assertFalse(mail.addAttachment(DIRECTORY + "/Attachment.pdf"));
        assertTrue(mail.addAttachment(DIRECTORY + "/TestFile"));
        assertEquals(1, mail.getAttachmentList().size());
    }

    @Test
    void addAttachmentList() {
        LOGGER.log("TEST CASE: addAttachmentList", LogLevel.DEBUG);

        List<String> attachments = new ArrayList<>();
        attachments.add(DIRECTORY + "/Attachment.pdf");
        attachments.add(DIRECTORY + "/TestFile");
        Mail mail = new Mail();
        mail.addAttachmentList(attachments);
        assertEquals(2, mail.getAttachmentList().size());
    }
}
