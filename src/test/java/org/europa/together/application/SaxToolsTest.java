package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.io.File;
import org.europa.together.business.Logger;
import org.europa.together.business.XmlTools;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.FileUtils;
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
public class SaxToolsTest {

    private static final Logger LOGGER = new LogbackLogger(SaxToolsTest.class);

    private static final String FILE_PATH
            = "org/europa/together/xml";
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + FILE_PATH;

    private static final File DTD
            = new File(Constraints.SYSTEM_APP_DIR + "/simple.dtd");
    private static final File SCHEMA
            = new File(Constraints.SYSTEM_APP_DIR + "/simple.xsd");

    @Autowired
    private XmlTools xmlTools;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        try {
            FileUtils.copyFile(new File(DIRECTORY + "/simple.dtd"), DTD);
            FileUtils.copyFile(new File(DIRECTORY + "/simple.xsd"), SCHEMA);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        Assumptions.assumeTrue(true);

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() {
        if (DTD.exists()) {
            DTD.delete();
        }
        if (SCHEMA.exists()) {
            SCHEMA.delete();
        }

        LOGGER.log("### TEST SUITE TERMINATED.\n", LogLevel.TRACE);
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

        assertThat(SaxTools.class, hasValidBeanConstructor());
    }

    @Test
    void parseXmlString() {
        LOGGER.log("TEST CASE: parseXmlString", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        String xml = FileUtils.readFileStream(new File(DIRECTORY + "/test_wellformed.xml"));
        String output = xmlTools.parseXmlString(xml);
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

        assertNotNull(output);
        assertEquals(compare, output);
    }

    @Test
    void fFailParseXmlString() {
        LOGGER.log("TEST CASE: failParseXmlString", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        assertNull(xmlTools.parseXmlString(null));
    }

    @Test
    void parseXmlFile() {
        LOGGER.log("TEST CASE: parseXmlFile", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        String output = xmlTools.parseXmlFile(new File(DIRECTORY + "/test_wellformed.xml"));
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

        assertNotNull(output);
        assertEquals(compare, output);
    }

    @Test
    void failParsingXmlFile() {
        LOGGER.log("TEST CASE: failParseXmlFile", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        assertNull(xmlTools.parseXmlFile(null));
    }

    @Test
    void isWellFormed() {
        LOGGER.log("TEST CASE: isWellFormed", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_wellformed.xml"));
        assertTrue(xmlTools.isWellFormed());
    }

    @Test
    void isNotWellFormed() {
        LOGGER.log("TEST CASE: isNotWellFormed", LogLevel.DEBUG);

        xmlTools = new SaxTools();

        LOGGER.log("Case Sensitive call", LogLevel.DEBUG);
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_non_wellformed_01.xml"));
        assertFalse(xmlTools.isWellFormed());

        LOGGER.log("Missing close TAG call", LogLevel.DEBUG);
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_non_wellformed_02.xml"));
        assertFalse(xmlTools.isWellFormed());

        LOGGER.log("Mixted TAG order call", LogLevel.DEBUG);
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_non_wellformed_03.xml"));
        assertFalse(xmlTools.isWellFormed());

        LOGGER.log("no ROOT Element call", LogLevel.DEBUG);
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_non_wellformed_04.xml"));
        assertFalse(xmlTools.isWellFormed());

        LOGGER.log("character escape failure call", LogLevel.DEBUG);
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_non_wellformed_05.xml"));
        assertFalse(xmlTools.isWellFormed());
    }

    @Test
    void failPrettyPrint() {
        LOGGER.log("TEST CASE: failPrettyPrint", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        xmlTools.parseXmlFile(null);
        assertNull(xmlTools.prettyPrintXml());
    }

    @Test
    void prettyPrintXml() {
        LOGGER.log("TEST CASE: prettyPrint", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        String input = xmlTools.parseXmlFile(new File(DIRECTORY + "/test_pretty_print_01.xml"));

        assertNotNull(input);
        assertTrue(xmlTools.isWellFormed());

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
        String output = xmlTools.prettyPrintXml();

        assertNotEquals(input, output);
        assertEquals(reference, output);
    }

    @Test
    void prettyPrintXmlInlineDtd() {
        LOGGER.log("TEST CASE: prettyPrintXmlInlineDtd", LogLevel.DEBUG);

        try {
            xmlTools = new SaxTools();
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

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void writeXmlToFile() {
        LOGGER.log("TEST CASE: writeXmlToFile", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        String newFilePath = DIRECTORY + "/new_xml_file.xml";
        String xmlFileContent = xmlTools.parseXmlFile(new File(DIRECTORY + "/test_wellformed.xml"));

        xmlTools.writeXmlToFile(xmlFileContent, newFilePath);

        File file = new File(newFilePath);
        assertTrue(file.exists());
        file.delete();
        assertFalse(file.exists());
    }

    @Test
    void noGrammar() {
        LOGGER.log("TEST CASE: noGrammar", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_no_grammar.xml"));
        assertTrue(xmlTools.isWellFormed());
        assertFalse(xmlTools.isValid());
    }

    @Test
    void failValidation() {
        LOGGER.log("TEST CASE: failValidation", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        xmlTools.parseXmlFile(new File(""));
        assertFalse(xmlTools.isWellFormed());
        assertFalse(xmlTools.isValid());
    }

    @Test
    void validInternalDtd() {
        LOGGER.log("TEST CASE: validInternalDtd", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_dtd_valid_internal.xml"));
        assertTrue(xmlTools.isWellFormed());
        assertTrue(xmlTools.isValid());
    }

    @Test
    void invalidDtd() {
        LOGGER.log("TEST CASE: invalidDtd", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_dtd_invalid.xml"));
        assertTrue(xmlTools.isWellFormed());
        assertFalse(xmlTools.isValid());
    }

    @Test
    void vValidDtd() {
        LOGGER.log("TEST CASE: invalidDtd", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_dtd_valid.xml"));
        assertTrue(xmlTools.isWellFormed());
        assertTrue(xmlTools.isValid());
    }

    @Test
    void failSetSchema() {
        LOGGER.log("TEST CASE: failSetSchema", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_schema_valid.xml"));
        assertTrue(xmlTools.isWellFormed());

        LOGGER.log("CASE 1: NULL", LogLevel.DEBUG);
        xmlTools.setSchemaFile(null);
        assertFalse(xmlTools.hasExternalSchemaFile());

        LOGGER.log("CASE 2: Schema File not exist", LogLevel.DEBUG);
        xmlTools.setSchemaFile(new File(DIRECTORY + "/no_schema.xsd"));
        assertFalse(xmlTools.hasExternalSchemaFile());
    }

    @Test
    void setSchema() {
        LOGGER.log("TEST CASE: setSchema", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_schema_valid.xml"));
        assertFalse(xmlTools.hasExternalSchemaFile());

        xmlTools.setSchemaFile(new File(DIRECTORY + "/simple.xsd"));
        assertTrue(xmlTools.hasExternalSchemaFile());

        assertTrue(xmlTools.isWellFormed());
        assertTrue(xmlTools.isValid());
    }

    @Test
    void resetExternalSchemaFile() {
        LOGGER.log("TEST CASE: resetExternalSchemaFile", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        assertFalse(xmlTools.hasExternalSchemaFile());

        xmlTools.setSchemaFile(new File(DIRECTORY + "/simple.xsd"));
        assertTrue(xmlTools.hasExternalSchemaFile());

        xmlTools.resetExternalSchema();
        assertFalse(xmlTools.hasExternalSchemaFile());
    }

    @Test
    void invalidSchema() {
        LOGGER.log("TEST CASE: invalidSchema", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        xmlTools.parseXmlFile(new File(DIRECTORY + "/test_schema_invalid.xml"));
        xmlTools.setSchemaFile(new File(DIRECTORY + "/simple.xsd"));
        assertTrue(xmlTools.isWellFormed());
        assertFalse(xmlTools.isValid());
    }

    @Test
    void validSchemaByWebResorce() {
        LOGGER.log("TEST CASE: validSchemaByWebResorce", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        //check spring config
        String xmlFile = Constraints.SYSTEM_APP_DIR
                + "/target/test-classes/org/europa/together/xml/spring.xml";
        xmlTools.parseXmlFile(new File(xmlFile));

        xmlTools.prettyPrintXml();

        assertTrue(xmlTools.isWellFormed());
        assertTrue(xmlTools.isValid());
    }

    @Test
    void xsltTransformation() {
        LOGGER.log("TEST CASE: xsltTransformation", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        String xml = DIRECTORY + "/xml_datasource.xml";
        String xslt = DIRECTORY + "/template.xslt";
        String transformed = xmlTools.transformXslt(new File(xml), new File(xslt));

        //fix problems with whitespace - just to see if the transformation works well
        String out = xmlTools.shrinkContent(transformed);

        LOGGER.log(">>> XSLT; \n " + out, LogLevel.DEBUG);
        assertEquals(857, out.length());
    }

    @Test
    void failTransformation() {
        LOGGER.log("TEST CASE: failXsltTransformation", LogLevel.DEBUG);

        xmlTools = new SaxTools();
        assertEquals("", xmlTools.transformXslt(null, null));
    }

    @Test
    void shrinkXml() {
        LOGGER.log("TEST CASE: shrinkXml", LogLevel.DEBUG);

        String file = Constraints.SYSTEM_APP_DIR
                + "/target/test-classes/org/europa/together/xml/shrink.xml";
        String orgin = FileUtils.readFileStream(new File(file));

        xmlTools = new SaxTools();
        String transform = xmlTools.shrinkContent(orgin);
        xmlTools.parseXmlString(transform);

        LOGGER.log("Shrink: " + transform, LogLevel.DEBUG);

        assertNotEquals(orgin, transform);
        assertTrue(xmlTools.isWellFormed());
    }
}
