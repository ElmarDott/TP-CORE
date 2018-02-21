package org.europa.together.application;

import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.SchemaFactory;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.FileUtils;
import org.europa.together.utils.SaxDocumentHandler;
import org.europa.together.business.XmlTools;
import org.europa.together.utils.StringUtils;

/**
 * Implementation of the XML Tools.
 */
public class XmlToolsImpl implements XmlTools {

    private static final long serialVersionUID = 10L;
    private static final Logger LOGGER = new LoggerImpl(XmlToolsImpl.class);

    private File xmlFile = null;
    private File schemaFile = null;
    private String prettyPrint;
    private boolean wellformed = false;
    //Sax
    private final SaxDocumentHandler saxHandler;
    private final SAXParserFactory parserFactory;
    private SAXParser parser = null;

    /**
     * Constructor.
     */
    public XmlToolsImpl() {
        this.saxHandler = new SaxDocumentHandler();
        this.parserFactory = SAXParserFactory.newInstance();
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
            this.parserFactory.setValidating(false);
            this.parserFactory.setNamespaceAware(true);
            this.parserFactory.setXIncludeAware(true);

            this.parser = this.parserFactory.newSAXParser();
            this.parser
                    .setProperty("http://xml.org/sax/properties/lexical-handler", saxHandler);
            this.parser
                    .setProperty("http://xml.org/sax/properties/declaration-handler", saxHandler);
            this.parser.parse(this.xmlFile, saxHandler);

            this.prettyPrint = saxHandler.prettyPrintXml();
            this.wellformed = true;
            content = FileUtils.readFileStream(xmlFile);

            this.parser.reset();

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }

        return content;
    }

    @Override
    public String prettyPrintXml() {

        String content = null;
        if (!StringUtils.isEmpty(this.prettyPrint)) {
            content = this.prettyPrint;
        }
        return content;
    }

    @Override
    public boolean isValid() {
        boolean success = false;
        try {
            this.parserFactory.setValidating(true);
            this.parser = this.parserFactory.newSAXParser();

            SchemaFactory schemaFactory
                    = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            LOGGER.log("Start Validation. ", LogLevel.DEBUG);

            //Internal XSD [reference inside XML Document]
            if (this.schemaFile == null && saxHandler.getSchemaFiles() != null) {
                LOGGER.log("Validate by implizit XSD (" + saxHandler.getSchemaFiles().length + ")",
                        LogLevel.DEBUG);
                this.parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                        XMLConstants.W3C_XML_SCHEMA_NS_URI);
                this.parserFactory.setSchema(schemaFactory.newSchema(saxHandler.getSchemaFiles()));
            }

            //External XSD
            if (this.schemaFile != null && this.schemaFile.exists()) {

                LOGGER.log("Validate by explizit XSD (" + this.schemaFile.getName() + ")",
                        LogLevel.DEBUG);
                this.parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                        XMLConstants.W3C_XML_SCHEMA_NS_URI);
                this.parserFactory.setSchema(schemaFactory.newSchema(this.schemaFile));
            }

            this.parser
                    .setProperty("http://xml.org/sax/properties/lexical-handler", saxHandler);
            this.parser
                    .setProperty("http://xml.org/sax/properties/declaration-handler", saxHandler);

            parser.parse(this.xmlFile, saxHandler);
            LOGGER.log("Validation for document " + this.xmlFile.getName() + " successful",
                    LogLevel.DEBUG);
            this.parser.reset();
            success = true;

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return success;
    }

    @Override
    public boolean isWellFormed() {
        LOGGER.log("wellformed: " + this.wellformed, LogLevel.DEBUG);
        return this.wellformed;
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
