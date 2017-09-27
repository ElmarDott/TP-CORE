package org.europa.together.application;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.SchemaFactory;
import org.europa.together.business.Logger;
import org.europa.together.business.XmlTools;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.FileUtils;
import org.europa.together.utils.SaxDocumentHandler;
import org.xml.sax.SAXException;

/**
 * Implementation of the XML Tools.
 */
public class XmlToolsImpl implements XmlTools {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = new LoggerImpl(XmlToolsImpl.class);

//    private String applicationPath
//            = this.getClass().getClassLoader().getResource("").getPath();
    private File xmlFile = null;
    private File schemaFile = null;

    //Sax
    private SaxDocumentHandler saxHandler = null;
    private SAXParserFactory parserFactory = null;
    private SAXParser parser = null;

    /**
     * Constructor.
     *
     * @param xmlFile as File
     */
    public XmlToolsImpl(final File xmlFile) {
        this.xmlFile = xmlFile;

        //Sax
        this.saxHandler = new SaxDocumentHandler();
        this.parserFactory = SAXParserFactory.newInstance();
        this.parserFactory = SAXParserFactory.newInstance();
        this.parserFactory.setNamespaceAware(true);
        this.parserFactory.setXIncludeAware(true);
        this.parserFactory.setValidating(false);

        //detect schema if exist
        try {
            this.parser = this.parserFactory.newSAXParser();
            this.parser.parse(this.xmlFile, saxHandler);

            String schema = saxHandler.getSchemaFile();

            //TODO: directory walker to search for XSD in classpath for automated detechtion
            if (schema != null) {
//                this.setSchemaFile(new File(this.applicationPath));
                LOGGER.log(">>> " + schema + " in File detected.", LogLevel.DEBUG);
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.log("instance class (" + this.xmlFile.getName() + ")", LogLevel.ERROR);
        } finally {
            this.parser = null;
            this.saxHandler = new SaxDocumentHandler();
        }

        LOGGER.log("instance class (" + this.xmlFile.getName() + ")", LogLevel.INFO);
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
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);
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

        } catch (IOException | ParserConfigurationException | SAXException ex) {
            success = false;
            LOGGER.log("Document not well formed.", LogLevel.ERROR);
        }
        return success;
    }

    @Override
    public String formatContent() {
        try {
            this.parserFactory.setValidating(false);

            this.parser = parserFactory.newSAXParser();
            parser.parse(this.xmlFile, saxHandler);

        } catch (IOException | ParserConfigurationException | SAXException ex) {
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);
        }
        return saxHandler.prettyPrintXml();
    }

    @Override
    public String shrinkContent() {
        //TODO: formatContent() implement me
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeXmlToFile(final String content, final String destinationFile) {
        FileUtils.writeStringToFile(content, destinationFile);
    }
}
