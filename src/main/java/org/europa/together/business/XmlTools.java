package org.europa.together.business;

import java.io.File;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.springframework.stereotype.Component;

/**
 * XML Tools are based on the event driven SAX concept. SAX = Simple API for
 * XML<br>
 * The implementation offers all necessary functions to deal with XML inside
 * Java Applications.
 *
 * @author elmar.dott@gmail.com
 * @version 1.2
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "SaxTools")
@Component
public interface XmlTools {

    /**
     * Identifier for the given feature to enable toggles.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-0010";

    /**
     * Parse a given XML File to grab the content.
     *
     * @param xmlFile as File
     * @return content as String
     */
    @API(status = STABLE, since = "1.0")
    String parseXmlFile(File xmlFile);

    /**
     * Parse a given XML String to grab the content.
     *
     * @param xml as String
     * @return content as String
     */
    @API(status = STABLE, since = "1.1")
    String parseXmlString(String xml);

    /**
     * Before you write a XML Document on a file, especially computer generated
     * XML Content, the content should formatted by a CodeBeautifier. This
     * function Beautifies the XML Sources with the following rules:<br>
     * <li> set content to UTF-8 without BOM (Byte Order Mark)
     * <li> automated line breaks and indents
     * <li> remove trailing space
     * <li> replace TAB with 4 space characters
     *
     * @return content as String
     */
    @API(status = STABLE, since = "1.0")
    String prettyPrintXml();

    /**
     * Shrink the XML Content to reduce the file size for a higher performance
     * in automated processing.The following option will be executed:
     * <li>remove XML comments</li>
     * <li>remove whitespace</li>
     * <li>remove linebreak</li>
     * The final result is an XML in one row without comments and whitespace in
     * one line. input and output is not parsed or checked for well formed.
     *
     * @param content as String
     * @return shrink content as String
     */
    @API(status = STABLE, since = "1.1")
    String shrinkContent(String content);

    /**
     * Transform an XML File by a given XSLT to a new Output.
     *
     * @param xml as File
     * @param xslt as File
     * @return transformation as String
     */
    @API(status = STABLE, since = "1.1")
    String transformXslt(File xml, File xslt);

    /**
     * Check if a external Schema File s configured.
     *
     * @return true on success
     */
    @API(status = STABLE, since = "1.1")
    boolean hasExternalSchemaFile();

    /**
     * Validate well formed XML content (XML 1.0) against a given grammar.
     * Grammar files can be DTD or XML Schema. THe validation contains also a
     * well formed test for the XML file.
     *
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean isValid();

    /**
     * Check XML Content (XML 1.0) if is well formed. the well formed rules for
     * XML are defined as:<br>
     * <li> the structure of a XML document is a tree and each node is defined
     * as TAG
     * <li> each document contains exact one root tag
     * <li> element tags are case-sensitive
     * <li> content be delimited with a beginning and end tag
     * <li> begin, end, and empty-element tags that delimit the elements are
     * correctly nested, with none missing and none overlapping Detail
     * <li> it contains only properly encoded legal Unicode characters
     * <li> special characters like: &gt; &lt; &amp; have to be encoded
     * (StringUtils.escapeXmlCharacters()) Information:
     * https://en.wikipedia.org/wiki/Well-formed_document
     *
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean isWellFormed();

    /**
     * Reset the external Schema File to NULL.
     */
    @API(status = STABLE, since = "1.1")
    void resetExternalSchema();

    /**
     * Set explicit the schema file for the validation option. This method
     * overwrites in the XML document declares Schemata.
     *
     * @param schema as File
     */
    @API(status = STABLE, since = "1.0")
    void setSchemaFile(File schema);

    /**
     * Writes a XML String into a new File.
     *
     * @param content as string
     * @param destinationFile as String
     */
    @API(status = STABLE, since = "1.0")
    void writeXmlToFile(String content, String destinationFile);
}
