package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import java.io.File;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
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
    void testParseXml() {
        String xml = xmlTools.parseXmlFile(new File(DIRECTORY + "/test_wellformed.xml"));
        String compare = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "\n"
                + "<root xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n"
                + "\n"
                + "    <!-- COMMENT -->\n"
                + "    <![CDATA[cdata element content]]>\n"
                + "\n"
                + "    <node>\n"
                + "        <child>child 1</child>\n"
                + "        <child>child 2</child>\n"
                + "        <child>child 2</child>\n"
                + "    </node>\n"
                + "\n"
                + "    <attribute value=\"attribute a\">attribute node</attribute>\n"
                + "\n"
                + "    <emptyTag />\n"
                + "\n"
                + "    <char>\n"
                + "        <escape>&#0060;</escape>\n"
                + "        <escape>&#0062;</escape>\n"
                + "        <escape>&#0039;</escape>\n"
                + "        <escape>&#0034;</escape>\n"
                + "    </char>\n"
                + "</root>\n";
        assertEquals(compare, xml);
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
        String input = xmlTools.parseXmlFile(new File(DIRECTORY + "/test_pretty_print_01.xml"));
        assertTrue(xmlTools.isWellFormed());

        String output = xmlTools.prettyPrintXml();
        String reference = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "\n"
                + "<!DOCTYPE EmployeeInfo SYSTEM \"simple.dtd\">\n"
                + "<!-- THIs Is AN XML COMMENT -->\n"
                + "<EmployeeInfo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
                + "              xmlns:e=\"http://www.sample.com/EmployeeInfo\" \n"
                + "              xsi:schemaLocation=\"http://www.sample.com/EmployeeInfo simple.xsd\">\n"
                + "    <!-- THIs Is AN XML COMMENT -->\n"
                + "    <![CDATA[some stuff]]>\n"
                + "    <Employee EmployeeNumber=\"1\">\n"
                + "        <!-- THIs Is AN XML COMMENT -->\n"
                + "        <Name>Masashi Okamura</Name>\n"
                + "        <Department>Design Department</Department>\n"
                + "        <Telephone>03-1452-4567</Telephone>\n"
                + "        <Email>okamura@xmltr.co.jp\n"
                + "            <!-- THIs Is AN XML COMMENT --></Email>\n"
                + "        <Email>okamura@xmltr.co.jp</Email>\n"
                + "        <XXX>\n"
                + "            <ABC>03-1452-4567</ABC>\n"
                + "            <!-- THIs Is AN XML COMMENT -->\n"
                + "        </XXX>\n"
                + "    </Employee>\n"
                + "</EmployeeInfo>\n";

        assertNotEquals(input, output);
        assertEquals(reference, output);
    }

    @Test
    void testPrettyPrintXmlInlineDtd() {
        String input = xmlTools.parseXmlFile(new File(DIRECTORY + "/test_pretty_print_02.xml"));
        assertTrue(xmlTools.isWellFormed());

        String output = xmlTools.prettyPrintXml();
        String reference = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "\n"
                + "<!DOCTYPE EmployeeInfo [\n"
                + "    <!ELEMENT EmployeeInfo (Employee)*>\n"
                + "    <!ELEMENT Employee (Name,Department,Telephone,Email)>\n"
                + "    <!ELEMENT Name (#PCDATA)>\n"
                + "    <!ELEMENT Department (#PCDATA)>\n"
                + "    <!ELEMENT Telephone (#PCDATA)>\n"
                + "    <!ELEMENT Email (#PCDATA)>\n"
                + "    <!ATTLIST Employee EmployeeNumber CDATA #REQUIRED>\n"
                + "]>\n"
                + "<EmployeeInfo>\n"
                + "    <![CDATA[some stuff]]>\n"
                + "    <Employee EmployeeNumber=\"105\">\n"
                + "        <Name>Masashi Okamura</Name>\n"
                + "        <Department>Design Department</Department>\n"
                + "        <Telephone>03-1452-4567</Telephone>\n"
                + "        <Email>okamura@xmltr.co.jp</Email>\n"
                + "    </Employee>\n"
                + "    <!-- THIs Is AN XML COMMENT -->\n"
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
    void testInvalidSchema() {
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_schema_invalid.xml"));
        xmlTools.setSchemaFile(new File(DIRECTORY + "/simple.xsd"));
        assertTrue(xmlTools.isWellFormed());
        assertFalse(xmlTools.isValid());
    }

    @Test
    void testValidSchemaByWebResorce() {
        //check spring config
        String xmlFile = Constraints.SYSTEM_APP_DIR
                + "/target/test-classes/org/europa/together/configuration/spring-dao-test.xml";
        xmlTools.parseXmlFile(new File(xmlFile));

        xmlTools.prettyPrintXml();

        assertTrue(xmlTools.isWellFormed());
        assertTrue(xmlTools.isValid());
    }

}
