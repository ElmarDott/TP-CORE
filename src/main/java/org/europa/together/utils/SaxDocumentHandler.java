package org.europa.together.utils;

import java.util.ArrayList;
import java.util.List;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;

/**
 * This class extends the SAX2 base handler class to support the SAX2
 * {@link LexicalHandler}, {@link DeclHandler}, and {@link EntityResolver2}
 * extensions. Except for overriding the original SAX1
 * {@link DefaultHandler#resolveEntity resolveEntity()} method the added handler
 * methods just return. Subclassers may override everything on a
 * method-by-method basis.
 */
public class SaxDocumentHandler extends DefaultHandler2 {

    private static final Logger LOGGER = new LoggerImpl(SaxDocumentHandler.class);

    private StringBuilder formattedXml
            = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    private final List<String> namespace = new ArrayList<>();
    private int level = 0;
    private boolean isTagOpend = false;
    private String schemaFile = null;

    /**
     * Constructor.
     */
    public SaxDocumentHandler() {
        super();
        LOGGER.log("instance class", LogLevel.INFO);
    }

    /**
     * Simple XML Beautifier.
     *
     * @return xmlBeautified as String
     */
    public String prettyPrintXml() {
        return this.formattedXml.toString();
    }

    /**
     * Get the Filenames of included XML Schema Files.
     *
     * @return schemaLocation as String
     */
    public String getSchemaFile() {
        return this.schemaFile;
    }

    @Override
    public void startDocument() throws SAXException {
        LOGGER.log("SAX DefaultHandler start to parse Document.", LogLevel.DEBUG);
    }

    @Override
    public void endDocument() throws SAXException {
        this.formattedXml.append("\n");
        LOGGER.log("SAX DefaultHandler finish to parse Document.", LogLevel.DEBUG);
    }

    @Override
    public void startPrefixMapping(final String prefix, final String uri)
            throws SAXException {
        this.namespace.add(" xmlns:" + prefix + "=\"" + uri + "\" \n");
    }

    @Override
    public void startElement(final String uri, final String localName,
            final String qName, final Attributes attributes)
            throws SAXException {

        //TAG
        this.formattedXml.append("\n");
        for (int i = 0; i < level; i++) {
            this.formattedXml.append("    ");
        }
        this.formattedXml.append("<").append(qName);

        //NAMESPACE
        if (this.level == 0) {
            if (attributes.getLength() > 0) {
                String[] schema = attributes.getValue(0).split(" ");
                if (schema != null) {
                    this.schemaFile = schema[1];
                }
            }

            int space = qName.length() + 1;
            if (this.namespace != null && this.namespace.size() > 0) {
                for (int x = 0; x < this.namespace.size(); x++) {
                    this.formattedXml.append(this.namespace.get(x));
                    for (int i = 0; i < space; i++) {
                        this.formattedXml.append(" ");
                    }
                }
            }
        }

        //ATTRIBUTES
        if (attributes != null && attributes.getLength() != 0) {
            for (int i = 0; i < attributes.getLength(); i++) {

                this.formattedXml.append(" ")
                        .append(attributes.getQName(i))
                        .append("=\"")
                        .append(attributes.getValue(i))
                        .append("\"");
            }
        }

        //CLOSE
        this.formattedXml.append(">");
        this.level++;
        this.isTagOpend = true;
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName)
            throws SAXException {

        if (!isTagOpend) {
            this.formattedXml.append("\n");
            for (int i = 1; i < this.level; i++) {
                this.formattedXml.append("    ");
            }
        } else {
            this.isTagOpend = false;
        }
        this.formattedXml.append("</").append(qName).append(">");
        this.level--;
    }

    @Override
    public void characters(final char[] ch, final int start, final int length)
            throws SAXException {

        String characters = new String(ch, start, length).trim();
        this.formattedXml.append(characters.trim());
    }

    @Override
    public void warning(final SAXParseException ex)
            throws SAXException {
        LOGGER.log("SAX LexicalParser: " + ex.getMessage(), LogLevel.WARN);
        throw new SAXException("SAX Validation Error");
    }

    @Override
    public void error(final SAXParseException ex)
            throws SAXException {
        LOGGER.log("SAX LexicalParser: " + ex.getMessage(), LogLevel.ERROR);
        throw new SAXException("SAX Validation Error");
    }

    @Override
    public void fatalError(final SAXParseException ex)
            throws SAXException {
        LOGGER.log("SAX LexicalParser: " + ex.getMessage(), LogLevel.ERROR);
        throw new SAXException("SAX Validation Error");
    }
}
