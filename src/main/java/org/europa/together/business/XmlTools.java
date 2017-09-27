package org.europa.together.business;

import java.io.File;

/**
 * XML Tools are based on the event driven SAX concept. SAX = Simple API for
 * XML<br>
 * The implementation offers all necessary functions to deal with XML inside
 * Java Applications.
 *
 * @author elmar.dott@gmail.com
 */
public interface XmlTools {

    /**
     * Before you write a XML Document on a file, especially computer generated
     * XML Content, the content should formated by a CodeBeautifier. This
     * function Beautifies the XML Sources with the following rules:<br>
     * <li> set content to UTF-8 without BOM (Byte Order Mark)
     * <li> automated line breaks and indents
     * <li> remove trailing space
     * <li> replace TAB with 4 space characters
     *
     * @return content as String
     */
    String formatContent();

    /**
     * Shrink the XML Content to reduce the file size for a higher performance
     * in automated processing. The following option will be executed:
     * <li>remove XML comments</li>
     * <li>remove whitespace</li>
     * <li>remove linebreaks</li>
     * The final result is an XML in one row without comments and whitespace.
     *
     * @return content as String
     */
    String shrinkContent();

    /**
     * Validate well formed XML content (XML 1.0) against a given grammar.
     * Grammar files can be DTD or XML Schema. THe validation contains also a
     * well formed test for the XML file.
     *
     * @return true on success
     */
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
    boolean isWellFormed();

    /**
     * Set explicit the schema file for the validation option.
     *
     * @param schema as File
     */
    void setSchemaFile(File schema);

    /**
     * Writes a XML String into a new File.
     *
     * @param content as string
     * @param destinationFile as String
     */
    void writeXmlToFile(String content, String destinationFile);
}
