package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import com.itextpdf.text.pdf.PdfReader;
import java.io.File;
import org.europa.together.business.Logger;
import org.europa.together.business.PdfRenderer;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.StringUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
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
public class PdfTextRendererTest {

    private static final Logger LOGGER = new LogbackLogger(PdfTextRendererTest.class);
    private static final String FILE_PATH = "org/europa/together/pdf";
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/";

    @Autowired
    private PdfRenderer pdf;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        boolean check = true;
        String out = "executed";
        FF4jProcessor feature = new FF4jProcessor();

        boolean toggle = feature.deactivateUnitTests(PdfRenderer.FEATURE_ID);
        if (!toggle) {
            out = "skiped.";
            check = false;
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out, LogLevel.TRACE);
        Assumptions.assumeTrue(check);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("TEST SUITE TERMINATED.\n", LogLevel.TRACE);
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

        assertThat(ITextRenderer.class, hasValidBeanConstructor());

        pdf.setAuthor("John Doe");
        assertEquals(pdf.getAuthor(), "John Doe");

        pdf.setKeywords("John, Doe");
        assertEquals(pdf.getKeywords(), "John, Doe");

        pdf.setSubject("Document Subject");
        assertEquals(pdf.getSubject(), "Document Subject");

        pdf.setTitle("MyTitle");
        assertEquals(pdf.getTitle(), "MyTitle");
    }

    @Test
    void testReadPdf() {
        LOGGER.log("TEST CASE: readPdf", LogLevel.DEBUG);

        File file = new File(DIRECTORY + FILE_PATH + "/document.pdf");
        PdfReader document = pdf.readDocument(file);
        assertEquals(5, document.getNumberOfPages());
    }

    @Test
    void testFailReadPdf() {
        LOGGER.log("TEST CASE: failReadPdf", LogLevel.DEBUG);
        assertNull(pdf.readDocument(null));
    }

    @Test
    void testFailRenderHtmlToPdf() {
        LOGGER.log("TEST CASE: failRenderHtmlToPdf", LogLevel.DEBUG);

        pdf.renderDocumentFromHtml(DIRECTORY + "fail.pdf", "");
        assertFalse(new File(DIRECTORY + "fail.pdf").exists());
    }

    @Test
    void testLoadAndWritePdf() {
        LOGGER.log("TEST CASE: loadAndWritePdf", LogLevel.DEBUG);

        File file = new File(DIRECTORY + FILE_PATH + "/document.pdf");
        PdfReader document = pdf.readDocument(file);

        String out = DIRECTORY + FILE_PATH + "/copy.pdf";
        pdf.writeDocument(document, out);

        assertEquals(true, new File(out).exists());
    }

    @Test
    void testFailWritePdf() {
        LOGGER.log("TEST CASE: failWritePdf", LogLevel.DEBUG);

        String out = DIRECTORY + FILE_PATH + "/fail.pdf";
        pdf.writeDocument(null, out);

        assertNull(pdf.readDocument(new File(out)));
    }

    @Test
    void testRenderHtmlToPdf() {
        LOGGER.log("TEST CASE: renderHtmlToPdf", LogLevel.DEBUG);

        String html = "<h1>My First PDF Document</h1 > <p>"
                + StringUtils.generateLoremIpsum(0) + "</p>";

        pdf.renderDocumentFromHtml(DIRECTORY + "test.pdf", html);
        assertTrue(new File(DIRECTORY + "test.pdf").exists());
    }

    @Test
    void testRemovePages() {
        LOGGER.log("TEST CASE: removePages", LogLevel.DEBUG);

        File file = new File(DIRECTORY + FILE_PATH + "/document.pdf");
        PdfReader document = pdf.readDocument(file);
        assertEquals(5, document.getNumberOfPages());

        PdfReader reduced = pdf.removePage(document, 1, 3, 5);

        String out = DIRECTORY + FILE_PATH + "/reduced.pdf";
        pdf.writeDocument(reduced, out);

        assertEquals(2, reduced.getNumberOfPages());
    }

    @Test
    void testFailRemovePages() {
        LOGGER.log("TEST CASE: failRemovePages", LogLevel.DEBUG);
        assertNull(pdf.removePage(null, 1));
    }
}
