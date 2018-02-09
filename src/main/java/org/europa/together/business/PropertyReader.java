package org.europa.together.business;

import java.util.Map;

/**
 * The PropertyReader is be able to read properties from different resources
 * like File, Database or from the Classpath. It is also possible to manipulate
 * the property list. The key feature are the different cast methods for the
 * property values to get them in the correct datatype.
 * <br><br>
 * A property set contains for each line a key=value pair. Comments starts with
 * the # character.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 */
public interface PropertyReader {

    /**
     * Add a single Property to the Property list. If the property already exist
     * in the List, the new entry will not added and the method return false.
     *
     * @param key as String
     * @param value as String
     * @return true on success
     */
    boolean addProperty(String key, String value);

    /**
     * Extend property list by Map&lt;String, String&gt;.
     *
     * @param propertyList as Map
     * @return true on success
     */
    boolean addPropertyList(final Map<String, String> propertyList);

    /**
     * Load a property List from an given file inside the classpath. eg:
     * org/europa/together/properties/file.properties
     *
     * @param resource as String
     * @return true on success
     */
    boolean appendPropertiesFromClasspath(final String resource);

    /**
     * Load a property List from an external file. eg:
     * /home/usr/application/file.properties
     *
     * @param resource as String
     * @return true on success
     */
    boolean appendPropertiesFromFile(final String resource);

    /**
     * Clear the entire property list.
     *
     * @return true on success
     */
    boolean clear();

    /**
     * Remove a property by the given key from the list. If the property not
     * exists the method return false.
     *
     * @param key as String
     * @return true on success
     */
    boolean removeProperty(String key);

    /**
     * Update an existing property entry. In the case the entry don't exist, he
     * will be created.
     *
     * @param key as String
     * @param value as String
     * @return true on success
     */
    boolean updateProperty(String key, String value);

    /**
     * Counts the amount of all properties and return the value.
     *
     * @return count of properties as int
     */
    int count();

    /**
     * Get the value of a property as boolean. Allowed values are:
     * <ul>
     * <li>0 | 1</li>
     * <li>false | true</li>
     * <li>FALSE | TRUE</li>
     * </ul>
     * All other values will evaluate to <b>null</b>.
     *
     * @param key property given as String
     * @return Boolean PropertyKeyAsBoolean
     */
    Boolean getPropertyAsBoolean(final String key);

    /**
     * Get the property value as Double.
     *
     * @param key as String
     * @return Double PropertyAsDouble
     */
    Double getPropertyAsDouble(final String key);

    /**
     * Get the property value as Float.
     *
     * @param key as String
     * @return Float PropertyAsFloat
     */
    Float getPropertyAsFloat(final String key);

    /**
     * Get the value of a property as Integer.
     *
     * @param key property given as String
     * @return int PropertyKeyAsInteger
     */
    Integer getPropertyAsInt(final String key);

    /**
     * Get the property value as String.
     *
     * @param key property given as Stirng
     * @return String PropertyAsString
     */
    String getPropertyAsString(final String key);

    /**
     * Get the full property list as Map.
     *
     * @return propertyLit as Map
     */
    Map<String, String> getPropertyList();
}
