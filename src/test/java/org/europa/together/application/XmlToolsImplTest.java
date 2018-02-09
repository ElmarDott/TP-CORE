package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import java.io.File;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.FileUtils;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.europa.together.business.XmlTools;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class XmlToolsImplTest {

    private static final String FILE_PATH = "org/europa/together/xml";
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + FILE_PATH;

    private static final Logger LOGGER = new LoggerImpl(XmlToolsImplTest.class);
    private XmlTools xmlTools = new XmlToolsImpl();

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
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);

        //reset schema
        xmlTools.setSchemaFile(null);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        assertThat(XmlToolsImpl.class, hasValidBeanConstructor());
    }

    @Test
    void testFailParsing() {
        assertNull(xmlTools.parseXmlFile(null));
    }

    @Test
    void testIsWellFormed() {
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_wellformed.xml"));
        assertTrue(xmlTools.isWellFormed());
    }

    @Test
    void testIsNotWellFormed() {
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_non_wellformed_01.xml"));
        assertFalse(xmlTools.isWellFormed());
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_non_wellformed_02.xml"));
        assertFalse(xmlTools.isWellFormed());
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_non_wellformed_03.xml"));
        assertFalse(xmlTools.isWellFormed());
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_non_wellformed_04.xml"));
        assertFalse(xmlTools.isWellFormed());
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_non_wellformed_05.xml"));
        assertFalse(xmlTools.isWellFormed());
    }

    @Test
    void testFailPrettyPrint() {
        xmlTools.parseXmlFile(null);
        assertNull(xmlTools.prettyPrintXml());
    }

    @Test
    void testPrettyPrintXml() {

        String input = xmlTools.parseXmlFile(new File(DIRECTORY + "/test_pretty_print.xml"));
        String output = xmlTools.prettyPrintXml();
        String reference = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "\n"
                + "<EmployeeInfo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
                + "              xmlns:e=\"http://www.sample.com/EmployeeInfo\" \n"
                + "              xsi:schemaLocation=\"http://www.sample.com/EmployeeInfo simple.xsd\">\n"
                + "    <Employee EmployeeNumber=\"1\">\n"
                + "        <Name>Masashi Okamura</Name>\n"
                + "        <Department>Design Department</Department>\n"
                + "        <Telephone>03-1452-4567</Telephone>\n"
                + "        <Email>okamura@xmltr.co.jp</Email>\n"
                + "        <Email>okamura@xmltr.co.jp</Email>\n"
                + "        <XXX>\n"
                + "            <ABC>03-1452-4567</ABC>\n"
                + "        </XXX>\n"
                + "    </Employee>\n"
                + "</EmployeeInfo>\n";

        assertNotEquals(input, output);
        assertEquals(reference, output);
    }

    @Test
    void testWriteXmlToFile() {
        String newFilePath = DIRECTORY + "/new_xml_file.xml";
        String xmlFileContent = xmlTools.parseXmlFile(new File(DIRECTORY + "/test_wellformed.xml"));

        xmlTools.writeXmlToFile(xmlFileContent, newFilePath);

        File file = new File(newFilePath);
        assertTrue(file.exists());
        file.delete();
        assertFalse(file.exists());
    }

    @Test
    void testNoGrammar() {
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_no_grammar.xml"));
        assertTrue(xmlTools.isWellFormed());
        assertFalse(xmlTools.isValid());
    }

    @Test
    void testValidInternalDtd() {
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_dtd_valid_internal.xml"));
        assertTrue(xmlTools.isWellFormed());
        assertTrue(xmlTools.isValid());
    }

    @Test
    void testInvalidDtd() {
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_dtd_invalid.xml"));
        assertTrue(xmlTools.isWellFormed());
        assertFalse(xmlTools.isValid());
    }

    @Test
    void testValidDtd() {
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_dtd_valid.xml"));
        assertTrue(xmlTools.isWellFormed());
        assertTrue(xmlTools.isValid());
    }

    @Test
    void testSetSchema() {
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_schema_valid.xml"));
        assertTrue(xmlTools.isWellFormed());

        xmlTools.setSchemaFile(new File(DIRECTORY + "/simple.xsd"));
        assertTrue(xmlTools.isValid());
    }

    @Test
    void testFailSetSchema() {
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_schema_valid.xml"));
        assertTrue(xmlTools.isWellFormed());
        assertFalse(xmlTools.isValid());

        xmlTools.setSchemaFile(null);
        assertFalse(xmlTools.isValid());

        xmlTools.setSchemaFile(new File(DIRECTORY + "/no_schema.xsd"));
        assertFalse(xmlTools.isValid());
    }

    @Test
    @Disabled
    void testValidInternalSchema() {
        System.out.println("\n\n # ");

        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_valid_schema_internal.xml"));
        assertTrue(xmlTools.isWellFormed());
        assertTrue(xmlTools.isValid());
    }

    @Test
    @Disabled
    void testInvalidSchema() {
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_invalid_schema.xml"));
        xmlTools.setSchemaFile(new File(DIRECTORY + "/simple.xsd"));
        assertTrue(xmlTools.isWellFormed());
        assertFalse(xmlTools.isValid());
    }

    @Test
    @Disabled
    void testValidSchemaByWebResorce() {
        System.out.println("\n\n # ");
        //check spring config
        String xmlFile = Constraints.SYSTEM_APP_DIR
                + "/target/test-classes/org/europa/together/configuration/spring-dao-test.xml";
        xmlTools.parseXmlFile(new File(xmlFile));

        assertTrue(xmlTools.isWellFormed());
        assertTrue(xmlTools.isValid());
    }

    @Test
    @Disabled
    void testMixtedGrammar() {
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_mixted_grammar.xml"));
        assertTrue(xmlTools.isWellFormed());
        assertTrue(xmlTools.isValid());
    }

    @Test
    @Disabled
    void testMultipleSchema() {

    }
}
