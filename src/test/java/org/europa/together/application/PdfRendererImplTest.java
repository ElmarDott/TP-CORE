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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class PdfRendererImplTest {

    private static final String FILE_PATH = "org/europa/together/pdf";
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/";

    private static final Logger LOGGER = new LoggerImpl(PdfRendererImplTest.class);
    private PdfRenderer pdf = new PdfRendererImpl();

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("Assumption terminated. TestSuite will be excecuted.", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("TEST SUITE TERMINATED.", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED. \n", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        assertThat(PdfRendererImpl.class, hasValidBeanConstructor());

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
    void testFailRenderHtmlToPdf() {
        pdf.renderDocumentFromHtml(DIRECTORY + "fail.pdf", "");
        assertFalse(new File(DIRECTORY + "fail.pdf").exists());
    }

    @Test
    void testLoadAndWritePdf() {
        File file = new File(DIRECTORY + FILE_PATH + "/document.pdf");
        PdfReader document = pdf.readDocument(file);
        assertNotNull(document);

        String out = DIRECTORY + FILE_PATH + "/copy.pdf";
        pdf.writeDocument(document, out);

        assertEquals(true, new File(out).exists());
    }

    @Test
    void testRenderHtmlToPdf() {
        String html = "<h1>My First PDF Document</h1 > <p>"
                + StringUtils.generateLoremIpsum(0) + "</p>";

        pdf.renderDocumentFromHtml(DIRECTORY + "test.pdf", html);
        assertTrue(new File(DIRECTORY + "test.pdf").exists());
    }

    @Test
    void testRemovePages() {
        File file = new File(DIRECTORY + FILE_PATH + "/document.pdf");
        PdfReader document = pdf.readDocument(file);
        assertEquals(5, document.getNumberOfPages());

        PdfReader reduced = pdf.removePage(document, 1, 3, 5);

        String out = DIRECTORY + FILE_PATH + "/reduced.pdf";
        pdf.writeDocument(reduced, out);

        assertEquals(2, reduced.getNumberOfPages());

    }
}
