package org.europa.together.business;

import java.io.IOException;
import java.util.Map;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.springframework.stereotype.Component;

/**
 * The PropertyReader is able to read properties from different resources like
 * File, Database or from the classpath. It is also possible to manipulate the
 * property list. The key feature are the different cast methods for the
 * property values to load them in the correct datatype.
 * <br><br>
 * A property set contains for each line a key=value pair. Comments starts with
 * the # character.
 *
 * @author elmar.dott@gmail.com
 * @version 1.2
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "PropertyFileReader")
@Component
public interface PropertyReader {

    /**
     * Identifier for the given feature to enable toggles.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-04";

    /**
     * Add a single Property to the Property list. If the property already exist
     * in the List, the new entry will not added and the method return false.
     *
     * @param key as String
     * @param value as String
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean addProperty(String key, String value);

    /**
     * Extend property list by Map&lt;String, String&gt;.
     *
     * @param propertyList as Map
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean addPropertyList(Map<String, String> propertyList);

    /**
     * Load a property List from an given file inside the classpath. eg:
     * org/europa/together/properties/file.properties
     *
     * @param resource as String
     * @return true on success
     * @throws java.io.IOException on failure
     */
    @API(status = STABLE, since = "3.0")
    boolean appendPropertiesFromClasspath(String resource) throws IOException;

    /**
     * Load a property List from an external file. eg:
     * /home/usr/application/file.properties
     *
     * @param resource as String
     * @return true on success
     * @throws java.io.IOException on failure
     */
    @API(status = STABLE, since = "3.0")
    boolean appendPropertiesFromFile(String resource) throws IOException;

    /**
     * Clear the entire property list.
     *
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean clear();

    /**
     * Remove a property by the given key from the list. If the property not
     * exists the method return false.
     *
     * @param key as String
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean removeProperty(String key);

    /**
     * Update an existing property entry. In the case the entry don't exist, it
     * will be created.
     *
     * @param key as String
     * @param value as String
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean updateProperty(String key, String value);

    /**
     * Counts the amount of all properties and return the value.
     *
     * @return count of properties as int
     */
    @API(status = STABLE, since = "1.0")
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
    @API(status = STABLE, since = "1.0")
    Boolean getPropertyAsBoolean(String key);

    /**
     * Get the property value as Double.
     *
     * @param key as String
     * @return Double PropertyAsDouble
     */
    @API(status = STABLE, since = "1.0")
    Double getPropertyAsDouble(String key);

    /**
     * Get the property value as Float.
     *
     * @param key as String
     * @return Float PropertyAsFloat
     */
    @API(status = STABLE, since = "1.0")
    Float getPropertyAsFloat(String key);

    /**
     * Get the value of a property as Integer.
     *
     * @param key property given as String
     * @return int PropertyKeyAsInteger
     */
    @API(status = STABLE, since = "1.0")
    Integer getPropertyAsInt(String key);

    /**
     * Get the property value as String.
     *
     * @param key property given as String
     * @return String PropertyAsString
     */
    @API(status = STABLE, since = "1.0")
    String getPropertyAsString(String key);

    /**
     * Get the full property list as Map.
     *
     * @return propertyLit as Map
     */
    @API(status = STABLE, since = "1.0")
    Map<String, String> getPropertyList();
}
