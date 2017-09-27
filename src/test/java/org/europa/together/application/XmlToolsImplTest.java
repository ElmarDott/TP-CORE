package org.europa.together.application;

import java.io.File;
import org.europa.together.business.XmlTools;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class XmlToolsImplTest {

    private static final String FILE_PATH = "org/europa/together/xml";
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + FILE_PATH;

    @Test
    public void testWriteXmlToFile() {
        String file = DIRECTORY + "/formatted_to_file.xml";
        XmlTools tools
                = new XmlToolsImpl(new File(DIRECTORY + "/unformattedXml.xml"));
        tools.writeXmlToFile(tools.formatContent(), file);

        FileUtils.getFileSize(file, "kilo");

        File xmlFile = new File(file);
        assertTrue(xmlFile.exists());
        xmlFile.delete();
        assertFalse(xmlFile.exists());
    }

    @Test
    public void testIsWellFormed() {
        assertTrue(new XmlToolsImpl(new File(DIRECTORY + "/valid_document.xml")).isWellFormed());

        assertTrue(new XmlToolsImpl(new File(DIRECTORY + "/test_valid_schema.xml")).isWellFormed());
        assertTrue(new XmlToolsImpl(new File(DIRECTORY + "/test_invalid_schema.xml")).isWellFormed());
        assertTrue(new XmlToolsImpl(new File(DIRECTORY + "/test_valid_dtd.xml")).isWellFormed());
        assertTrue(new XmlToolsImpl(new File(DIRECTORY + "/test_invalid_dtd.xml")).isWellFormed());
    }

    @Test
    public void testIsNoneWellFormed() {
        assertFalse(new XmlToolsImpl(new File(DIRECTORY + "/invalid_document_01.xml")).isWellFormed());
        assertFalse(new XmlToolsImpl(new File(DIRECTORY + "/invalid_document_02.xml")).isWellFormed());
        assertFalse(new XmlToolsImpl(new File(DIRECTORY + "/invalid_document_03.xml")).isWellFormed());
        assertFalse(new XmlToolsImpl(new File(DIRECTORY + "/invalid_document_04.xml")).isWellFormed());
        assertFalse(new XmlToolsImpl(new File(DIRECTORY + "/invalid_document_05.xml")).isWellFormed());
    }

    @Test
    public void testSetSchema() {
        XmlTools test = new XmlToolsImpl(new File(DIRECTORY + "/test_valid_schema.xml"));
        test.setSchemaFile(null);
        assertFalse(test.isValid());
    }

    @Test
    public void testValidSchema() {
        XmlTools testValid
                = new XmlToolsImpl(new File(DIRECTORY + "/test_valid_schema.xml"));
        testValid.setSchemaFile(new File(DIRECTORY + "/simple.xsd"));
        assertTrue(testValid.isWellFormed());
        boolean validationTrue = testValid.isValid();
        assertTrue(validationTrue);
    }

    @Test
    public void testInvalidSchema() {
        XmlTools testInvalid
                = new XmlToolsImpl(new File(DIRECTORY + "/test_invalid_schema.xml"));
        testInvalid.setSchemaFile(new File(DIRECTORY + "/simple.xsd"));
        assertTrue(testInvalid.isWellFormed());
        boolean validatonFalse = testInvalid.isValid();
        assertFalse(validatonFalse);
    }

//    @Test
    public void testValidSchemaWithUrlSchema() {
        //check spring config
        String xml = Constraints.SYSTEM_APP_DIR
                + "/target/test-classes/org/europa/together/configuration/spring-dao-test.xml";
        assertTrue(new XmlToolsImpl(new File(xml)).isValid());
    }

//    @Test
    public void testAutodetectSchema() {
        assertTrue(new XmlToolsImpl(new File(DIRECTORY + "/test_valid_schema.xml")).isValid());
        assertFalse(new XmlToolsImpl(new File(DIRECTORY + "/test_invalid_schema.xml")).isValid());
    }

    @Test
    public void testValidDtd() {
        File test_01 = new File(DIRECTORY + "/test_valid_dtd.xml");
        assertTrue(new XmlToolsImpl(test_01).isWellFormed());
        assertTrue(new XmlToolsImpl(test_01).isValid());

        File test_02 = new File(DIRECTORY + "/internalDTD.xml");
        assertTrue(new XmlToolsImpl(test_02).isWellFormed());
        assertTrue(new XmlToolsImpl(test_02).isValid());
    }

    @Test
    public void testInvalidDtd() {
        File test_02 = new File(DIRECTORY + "/test_invalid_dtd.xml");
        assertTrue(new XmlToolsImpl(test_02).isWellFormed());
        assertFalse(new XmlToolsImpl(test_02).isValid());
    }

//    @Test
    public void testValidDtdUrlDtd() {
        //TODO
    }

//    @Test
    public void testMixtedGrammar() {

        XmlTools testValid
                = new XmlToolsImpl(new File(DIRECTORY + "/MixtedGrammar.xml"));
        testValid.setSchemaFile(new File(DIRECTORY + "/simple.xsd"));
        boolean validationTrue = testValid.isValid();
        assertTrue(validationTrue);
    }

    @Test
    public void testInternalDocumentGrammar() {
        //TODO
    }

    @Test
    public void testFormattedXml() {
        XmlTools tools
                = new XmlToolsImpl(new File(DIRECTORY + "/unformattedXml.xml"));
        System.out.println(tools.formatContent());
    }
}
