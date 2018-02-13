package org.europa.together.application;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.SchemaFactory;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.FileUtils;
import org.europa.together.utils.SaxDocumentHandler;
import org.xml.sax.SAXException;
import org.europa.together.business.XmlTools;

/**
 * Implementation of the XML Tools.
 */
public class XmlToolsImpl implements XmlTools {

    private static final long serialVersionUID = 10L;
    private static final Logger LOGGER = new LoggerImpl(XmlToolsImpl.class);

    private File xmlFile = null;
    private File schemaFile = null;

    //Sax
    private SaxDocumentHandler saxHandler = null;
    private SAXParserFactory parserFactory = null;
    private SAXParser parser = null;

    /**
     * Constructor.
     */
    public XmlToolsImpl() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public String parseXmlFile(final File xmlFile) {

        String content = null;
        try {

            if (xmlFile == null) {
                throw new NullPointerException("No XML File to parse!");
            }
            this.xmlFile = xmlFile;
            LOGGER.log("Parse Document: " + xmlFile.getName(), LogLevel.DEBUG);

            //Sax
            this.saxHandler = new SaxDocumentHandler();
            this.parserFactory = SAXParserFactory.newInstance();
            this.parserFactory.setNamespaceAware(true);
            this.parserFactory.setXIncludeAware(true);
            this.parserFactory.setValidating(false);

            //detect schema if exist
            this.parser = this.parserFactory.newSAXParser();
            this.parser.parse(this.xmlFile, saxHandler);

            String schema = saxHandler.getSchemaFile();
            if (schema != null) {
                LOGGER.log(">>> " + schema + " in File detected.", LogLevel.DEBUG);
            }
            content = FileUtils.readFileStream(xmlFile);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }

        this.parser = null;
        this.saxHandler = new SaxDocumentHandler();
        return content;
    }

    @Override
    public String prettyPrintXml() {
        String content = null;
        try {
            if (this.parserFactory != null && this.xmlFile != null) {

                this.parserFactory.setValidating(false);
                this.parser = parserFactory.newSAXParser();
                parser.parse(this.xmlFile, saxHandler);
                content = saxHandler.prettyPrintXml();
            }
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return content;
    }

    @Override
    public boolean isValid() {
        boolean success = false;
        try {
            this.parserFactory.setValidating(true);
            this.parser = this.parserFactory.newSAXParser();

            //External XSD
            if (this.schemaFile != null && this.schemaFile.exists()) {
                SchemaFactory schemaFactory
                        = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
                this.parserFactory.setSchema(schemaFactory.newSchema(this.schemaFile));

                this.parser.setProperty(
                        "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                        "http://www.w3.org/2001/XMLSchema");
            }

            parser.parse(this.xmlFile, saxHandler);
            success = true;
            LOGGER.log("Validation for document " + this.xmlFile.getName() + " successful",
                    LogLevel.DEBUG);

        } catch (IOException | ParserConfigurationException | SAXException ex) {
            success = false;
            LOGGER.catchException(ex);
        }
        return success;
    }

    @Override
    public boolean isWellFormed() {
        boolean success = false;
        try {
            this.parserFactory.setValidating(false);

            this.parser = this.parserFactory.newSAXParser();
            this.parser.parse(this.xmlFile, saxHandler);

            success = true;
            LOGGER.log(this.xmlFile.getName() + " is well formed.",
                    LogLevel.DEBUG);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return success;
    }

    @Override
    public void setSchemaFile(final File schema) {
        if (schema == null || !schema.exists()) {
            LOGGER.log("Schema file does not exist.", LogLevel.ERROR);
        } else {
            this.schemaFile = schema;
            LOGGER.log(this.schemaFile.getName() + " schema file added.",
                    LogLevel.DEBUG);
        }
    }

    @Override
    public void writeXmlToFile(final String content, final String destinationFile) {
        FileUtils.writeStringToFile(content, destinationFile);
    }

}
