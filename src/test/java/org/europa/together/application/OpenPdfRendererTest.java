package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import com.lowagie.text.pdf.PdfReader;
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
public class OpenPdfRendererTest {

    private static final Logger LOGGER = new LogbackLogger(OpenPdfRendererTest.class);
    private static final String FILE_PATH = "org/europa/together/pdf";
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/";

    @Autowired
    private PdfRenderer pdf;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(true);

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
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
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(OpenPdfRenderer.class, hasValidBeanConstructor());
        assertThat(OpenPdfRenderer.class, hasValidGettersAndSetters());
    }

    @Test
    void loadPdf() {
        LOGGER.log("TEST CASE: loadPdf", LogLevel.DEBUG);

        File file = new File(DIRECTORY + FILE_PATH + "/document.pdf");
        PdfReader document = pdf.loadDocument(file);
        assertEquals(5, document.getNumberOfPages());
    }

    @Test
    void failLoadPdf() {
        LOGGER.log("TEST CASE: failLoadPdf", LogLevel.DEBUG);
        assertNull(pdf.loadDocument(null));
    }

    @Test
    void loadAndWritePdf() {
        LOGGER.log("TEST CASE: loadAndWritePdf", LogLevel.DEBUG);

        File file = new File(DIRECTORY + FILE_PATH + "/document.pdf");
        PdfReader document = pdf.loadDocument(file);

        String out = DIRECTORY + FILE_PATH + "/copy.pdf";
        pdf.writeDocument(document, out);

        assertEquals(true, new File(out).exists());
    }

    @Test
    void failWritePdf() {
        LOGGER.log("TEST CASE: failWritePdf", LogLevel.DEBUG);

        String out = DIRECTORY + FILE_PATH + "/fail.pdf";
        pdf.writeDocument(pdf.loadDocument(null), null);

        assertNull(pdf.loadDocument(new File(out)));
    }

    @Test
    void testRemovePages() {
        LOGGER.log("TEST CASE: removePages", LogLevel.DEBUG);

        File file = new File(DIRECTORY + FILE_PATH + "/document.pdf");
        PdfReader document = pdf.loadDocument(file);
        assertEquals(5, document.getNumberOfPages());

        PdfReader reduced = pdf.removePage(document, 1, 3, 5);

        String out = DIRECTORY + FILE_PATH + "/reduced.pdf";
        pdf.writeDocument(reduced, out);

        assertEquals(2, reduced.getNumberOfPages());
    }

    @Test
    void failRemovePages() {
        LOGGER.log("TEST CASE: failRemovePages", LogLevel.DEBUG);
        assertNull(pdf.removePage(pdf.loadDocument(null), 1));
    }

    @Test
    void failRenderHtmlToPdf() throws Exception {
        LOGGER.log("TEST CASE: failRenderHtmlToPdf", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            pdf.renderDocumentFromHtml(null, "no content");
        });
    }

    @Test
    void simpleRenderHtmlToPdf() {
        LOGGER.log("TEST CASE: renderHtmlToPdf", LogLevel.DEBUG);

        String html = "<h1>My First PDF Document</h1 > <p>"
                + StringUtils.generateLoremIpsum(0) + "</p>";

        pdf.setTitle("OpenPDF test document");
        pdf.setAuthor("Elmar Dott");
        pdf.setSubject("JUnit test");
        pdf.setKeywords("test, junit5, openPDF");
        pdf.renderDocumentFromHtml(DIRECTORY + "test.pdf", html);

        assertTrue(new File(DIRECTORY + "test.pdf").exists());
        assertNotEquals(0, new File(DIRECTORY + "test.pdf").length());
    }

}
