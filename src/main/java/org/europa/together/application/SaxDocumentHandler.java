package org.europa.together.application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;

/**
 * This class extends the SAX2 base handler class to support the SAX2
 * <b>LexicalHandler</b>, <b>DeclHandler</b>, and <b>EntityResolver2</b>
 * extensions.
 */
@FeatureToggle(featureID = "CM-0010.H01")
public class SaxDocumentHandler extends DefaultHandler2 {

    private static final Logger LOGGER = new LogbackLogger(SaxDocumentHandler.class);

    private StringBuilder formattedXml
            = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    private StringBuilder inlineDtd = new StringBuilder();

    private final List<String> namespace = new ArrayList<>();
    private int level = 0;
    private boolean isTagOpend = false;
    private boolean hasInineDtd = false;
    private Source[] schematList = null;

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
     * Return all parsed Schema files in a SchemaFactory for validation.
     *
     * @return schemata as SchemaFactory
     */
    public Source[] getSchemaFiles() {
        Source[] copy = null;
        if (this.schematList != null) {
            copy = this.schematList.clone();
        }
        return copy;
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
    public void startDTD(final String name, final String publicId, final String systemId)
            throws SAXException {

        this.formattedXml.append("\n<!DOCTYPE ")
                .append(name);

        if (!StringUtils.isEmpty(publicId)) {
            this.formattedXml.append(" PUBLIC \"")
                    .append(publicId).append("\"");
        }

        if (!StringUtils.isEmpty(systemId)) {
            this.formattedXml.append(" SYSTEM \"")
                    .append(systemId).append("\"");
        }

        if (StringUtils.isEmpty(publicId) && StringUtils.isEmpty(systemId)) {
            this.formattedXml.append(" [\n");
            this.hasInineDtd = true;
        }
    }

    @Override
    public void endDTD() throws SAXException {

        if (this.hasInineDtd) {
            this.formattedXml.append(inlineDtd).append("]");
        }
        this.formattedXml.append(">");
    }

    @Override
    public void elementDecl(final String name, final String model)
            throws SAXException {

        this.inlineDtd.append("    <!ELEMENT ")
                .append(name)
                .append(" ")
                .append(model)
                .append(">\n");
    }

    @Override
    public void attributeDecl(final String eName, final String aName, final String type,
            final String mode, final String value) throws SAXException {

        this.inlineDtd.append("    <!ATTLIST ")
                .append(eName).append(" ")
                .append(aName).append(" ")
                .append(type).append(" ")
                .append(mode);
        if (!StringUtils.isEmpty(value)) {
            this.inlineDtd.append(" ").append(value);
        }
        this.inlineDtd.append(">\n");
    }

    @Override
    public void startCDATA() throws SAXException {

        if (this.level == 0) {
            this.formattedXml.append("\n");
        }
        if (this.level >= 1) {
            this.formattedXml.append("\n");
            for (int i = 0; i < this.level; i++) {
                this.formattedXml.append("    ");
            }
        }
        this.formattedXml.append("<![CDATA[");
    }

    @Override
    public void endCDATA() throws SAXException {
        this.formattedXml.append("]]>");
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName,
            final Attributes attributes) throws SAXException {

        //TAG
        this.formattedXml.append("\n");
        for (int i = 0; i < level; i++) {
            this.formattedXml.append("    ");
        }
        this.formattedXml.append("<").append(qName);

        //NAMESPACE
        if (this.level == 0) {
            this.processNamespace(qName, attributes);
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
    public void comment(final char[] ch, final int start, final int length)
            throws SAXException {

        if (this.level == 0) {
            this.formattedXml.append("\n");
        }
        if (this.level >= 1) {
            this.formattedXml.append("\n");
            for (int i = 0; i < this.level; i++) {
                this.formattedXml.append("    ");
            }
        }

        String comments = new String(ch, start, length).trim();
        this.formattedXml.append("<!-- ")
                .append(comments)
                .append(" -->");
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

    private void processNamespace(final String qName, final Attributes attributes) {
        if (attributes.getLength() > 0) {
            try {
                //Extract XSD Files
                String[] xsdExtraction = attributes.getValue(0).split(" ");
                List<String> schemaList = new ArrayList<>();
                StringBuilder attribute = new StringBuilder();
                attribute.append("Scanned Attributes: ");
                for (String schema : xsdExtraction) {
                    if (!StringUtils.isEmpty(schema)) {
                        attribute.append("\n\t");
                        attribute.append(schema);
                        if (schema.endsWith(".xsd")) {
                            schemaList.add(schema);
                        }
                    }
                }
                LOGGER.log(attribute.toString(), LogLevel.TRACE);
                int schemaCount = schemaList.size();
                LOGGER.log(schemaCount + " XSD Schemata file(s) extracted.", LogLevel.DEBUG);

                //Create XSD Sources
                this.schematList = new Source[schemaCount];
                int count = 0;
                for (int i = 0; i < schemaCount; i++) {

                    String resource = schemaList.get(count);

                    if (resource.contains("http")) {

                        schematList[count] = new StreamSource(resource);
                        LOGGER.log("ADD URL: " + schemaList.get(count), LogLevel.DEBUG);
                    } else {
                        schematList[count] = new StreamSource(new File(resource));
                        LOGGER.log("ADD FILE: " + schemaList.get(count), LogLevel.DEBUG);
                    }

                    count++;
                }
                LOGGER.log(schematList.length + " Elements added to schemata[]", LogLevel.DEBUG);

            } catch (Exception ex) {
                LOGGER.catchException(ex);
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

}
