package org.europa.together.application;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.FileUtils;
import org.europa.together.utils.SaxDocumentHandler;
import org.europa.together.business.XmlTools;
import org.europa.together.exceptions.MisconfigurationException;
import org.europa.together.utils.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the XML Tools.
 */
@Repository
public class XmlToolsImpl implements XmlTools {

    private static final long serialVersionUID = 10L;
    private static final Logger LOGGER = new LoggerImpl(XmlToolsImpl.class);

    private File schemaFile = null;
    private String prettyPrint = null;
    private String xmlContent = null;
    private boolean wellformed = false;
    //Sax
    private SaxDocumentHandler saxHandler;
    private SAXParserFactory parserFactory;
    private SAXParser parser = null;

    /**
     * Constructor.
     */
    public XmlToolsImpl() {
        this.parserFactory = SAXParserFactory.newInstance();
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public String parseXmlFile(final File xmlFile) {

        try {
            LOGGER.log("parse XML File: " + xmlFile.getName(), LogLevel.DEBUG);
            xmlContent = FileUtils.readFileStream(xmlFile);
            parse(xmlContent);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return xmlContent;
    }

    @Override
    public String parseXmlString(final String xml) {
        try {
            LOGGER.log("parse XML String: " + xml.length() + " characters.", LogLevel.DEBUG);
            xmlContent = xml;
            parse(xmlContent);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return xmlContent;
    }

    @Override
    public String prettyPrintXml() {

        String content = null;
        if (!StringUtils.isEmpty(prettyPrint)) {
            content = prettyPrint;
        }
        LOGGER.log("prettyPrintXml() => \n" + prettyPrint, LogLevel.TRACE);
        return content;
    }

    @Override
    public String transformXslt(final File xml, final File xslt) {
        Writer writer = new StringWriter();
        try {
            Source template = new StreamSource(xslt);
            Source input = new StreamSource(xml);
            StreamResult output = new StreamResult(writer);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(template);
            transformer.transform(input, output);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }

        return writer.toString();
    }

    @Override
    public boolean hasExternalSchemaFile() {
        boolean success = false;
        if (this.schemaFile != null) {
            success = true;
        }
        return success;
    }

    @Override
    public boolean isValid() {
        boolean success = false;

        if (schemaFile != null && !schemaFile.exists()) {
            LOGGER.log("Schema file RESET TO NULL: " + schemaFile.getName() + " not exist.",
                    LogLevel.WARN);
            resetExternalSchema();
        }

        try {
            LOGGER.log("Start Validation. ", LogLevel.DEBUG);

            parser.reset();
            parserFactory.setNamespaceAware(true);
            parserFactory.setValidating(true);
            parser = parserFactory.newSAXParser();

            parser.setProperty("http://xml.org/sax/properties/lexical-handler", saxHandler);
            parser.setProperty("http://xml.org/sax/properties/declaration-handler", saxHandler);

            //External XSD
            if (schemaFile != null && schemaFile.exists()) {

                LOGGER.log("Validate by explicit XSD (" + this.schemaFile.getName() + ") :: "
                        + this.schemaFile.getAbsolutePath(), LogLevel.DEBUG);

                parserFactory.setSchema(SchemaFactory.newInstance(
                        XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(this.schemaFile));

            }

            //Internal XSD [reference inside XML Document]
            if (schemaFile == null && saxHandler.getSchemaFiles() != null) {
                LOGGER.log("Validate by implicit XSD (" + saxHandler.getSchemaFiles().length + ")",
                        LogLevel.DEBUG);

                parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                        XMLConstants.W3C_XML_SCHEMA_NS_URI);
                parserFactory.setSchema(SchemaFactory.newInstance(
                        XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(saxHandler.getSchemaFiles()));
            }

            parser.parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)),
                    saxHandler);

            success = true;
            LOGGER.log("XML validation successful", LogLevel.DEBUG);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return success;
    }

    @Override
    public boolean isWellFormed() {
        LOGGER.log("xml is wellformed.", LogLevel.DEBUG);
        return this.wellformed;
    }

    @Override
    public void resetExternalSchema() {
        this.schemaFile = null;
        LOGGER.log("External Schema File is reset to NULL.", LogLevel.DEBUG);
    }

    @Override
    public void setSchemaFile(final File schema) {
        if (schema == null || !schema.exists()) {
            LOGGER.log("Schema file does not exist.", LogLevel.ERROR);
        } else {

            schemaFile = schema;
            LOGGER.log(schemaFile.getName() + " schema file added.",
                    LogLevel.DEBUG);
        }
    }

    @Override
    public void writeXmlToFile(final String content, final String destinationFile) {
        FileUtils.writeStringToFile(content, destinationFile);
    }

    private void parse(final String xml) throws MisconfigurationException {

        if (StringUtils.isEmpty(xml)) {
            throw new MisconfigurationException("No XML to parse!");
        }

        wellformed = false;
        prettyPrint = null;

        try {
            //Sax
            saxHandler = new SaxDocumentHandler();
            parserFactory.setValidating(false);
            parserFactory.setNamespaceAware(true);
            parserFactory.setXIncludeAware(true);

            parser = parserFactory.newSAXParser();
            parser.setProperty("http://xml.org/sax/properties/lexical-handler", saxHandler);
            parser.setProperty("http://xml.org/sax/properties/declaration-handler", saxHandler);
            parser.parse(
                    new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)),
                    saxHandler);

            wellformed = true;
            prettyPrint = saxHandler.prettyPrintXml();

        } catch (Exception ex) {
            LOGGER.log("PARSING EXCEPTION", LogLevel.WARN);
            LOGGER.catchException(ex);
        }
        parser.reset();
    }

}
