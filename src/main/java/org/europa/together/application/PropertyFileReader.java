package org.europa.together.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.Logger;
import org.europa.together.business.PropertyReader;
import static org.europa.together.business.PropertyReader.FEATURE_ID;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.MisconfigurationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the PropertyReader.
 */
@Repository
@FeatureToggle(featureID = FEATURE_ID)
public class PropertyFileReader implements PropertyReader {

    private static final long serialVersionUID = 4L;
    private static final Logger LOGGER = new LogbackLogger(PropertyFileReader.class);

    private Map<String, String> propertyList = new HashMap<>();

    /**
     * Constructor.
     */
    public PropertyFileReader() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public boolean addProperty(final String key, final String value) {
        boolean success = false;
        try {
            this.lookupForPropertyKey(key);

        } catch (Exception ex) {
            propertyList.put(key, value);
            LOGGER.catchException(ex);
            success = true;
        }
        return success;
    }

    @Override
    public boolean addPropertyList(final Map<String, String> resource) {
        boolean success = false;
        int sizeOrginalList = propertyList.size();
        int sizeNewList = resource.size();
        int size = sizeOrginalList + sizeNewList;
        propertyList.putAll(resource);

        if (size > sizeOrginalList) {
            success = true;
            LOGGER.log(sizeNewList + " Properties appended.", LogLevel.DEBUG);
        } else {
            LOGGER.log("Could not append Properties. Size Original List: "
                    + sizeOrginalList + " : Size New List: " + sizeNewList, LogLevel.WARN);
        }
        return success;
    }

    @Override
    public boolean appendPropertiesFromClasspath(final String resource) {
        boolean success = false;
        ApplicationContext context = new ClassPathXmlApplicationContext();

        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            context.getResource(resource).getInputStream(), "UTF8")
            );

            String line;
            int count = 1;
            while ((line = reader.readLine()) != null) {
                String test = line;
                if (test.isEmpty() || test.charAt(0) == '#') {
                    continue;
                }

                String[] parts = test.split("=");
                String key = parts[0];
                String value = "";
                if (parts.length == 2) {
                    value = parts[1];
                }
                propertyList.put(key, value);
                count++;
            }
            reader.close();

            String logMsg = "readPropertyFromClasspath(" + resource + ") "
                    + count + " Properties read.";
            LOGGER.log(logMsg, LogLevel.DEBUG);

            success = true;

        } catch (IOException ex) {
            LOGGER.catchException(ex);
        }

        this.printPropertyList();
        return success;
    }

    @Override
    public boolean appendPropertiesFromFile(final String resource) {
        boolean success = false;

        try {

            Stream<String> stream = Files.lines(Paths.get(resource));
            List<String> content = stream
                    .filter(line -> !line.startsWith("#"))
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toList());

            int count = 1;
            for (String entry : content) {

                String[] parts = entry.split("=");
                String key = parts[0];
                String value = "";
                if (parts.length == 2) {
                    value = parts[1];
                }
                propertyList.put(key, value);
                count++;
            }

            String logMsg = "readPropertyFromFile(" + resource + ") "
                    + count + " Properties read.";
            LOGGER.log(logMsg, LogLevel.DEBUG);

            success = true;

        } catch (IOException ex) {
            LOGGER.catchException(ex);
        }

        this.printPropertyList();
        return success;
    }

    @Override
    public boolean clear() {
        boolean success = false;
        if (propertyList.isEmpty()) {
            LOGGER.log("PropertyList is EMPTY, nothing to remove.", LogLevel.WARN);
        } else {
            propertyList.clear();
            success = true;
            LOGGER.log("PropertyList cleaned.", LogLevel.TRACE);
        }
        return success;
    }

    @Override
    public boolean removeProperty(final String key) {
        boolean success = false;
        try {
            this.lookupForPropertyKey(key);
            propertyList.remove(key);
            success = true;
            LOGGER.log(key + " successful removed.", LogLevel.DEBUG);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return success;
    }

    @Override
    public boolean updateProperty(final String key, final String value) {
        try {
            this.lookupForPropertyKey(key);
            propertyList.put(key, value);
            LOGGER.log("Entry " + key + " will be updated.", LogLevel.INFO);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
            propertyList.put(key, value);
        }
        return true;
    }

    @Override
    public int count() {
        return propertyList.size();
    }

    @Override
    public Boolean getPropertyAsBoolean(final String key) {
        Boolean value = null;
        try {
            this.lookupForPropertyKey(key);
            if (propertyList.get(key).matches("true|false|TRUE|FALSE|0")) {
                value = Boolean.parseBoolean(propertyList.get(key));
            }
            if (propertyList.get(key).equals("1")) {
                value = true;
            }
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return value;
    }

    @Override
    public Double getPropertyAsDouble(final String key) {
        Double value = null;
        try {
            this.lookupForPropertyKey(key);
            value = Double.parseDouble(propertyList.get(key));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return value;
    }

    @Override
    public Float getPropertyAsFloat(final String key) {
        Float value = null;
        try {
            this.lookupForPropertyKey(key);
            value = Float.parseFloat(propertyList.get(key));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return value;
    }

    @Override
    public Integer getPropertyAsInt(final String key) {
        Integer value = null;
        try {
            this.lookupForPropertyKey(key);
            value = Integer.parseInt(propertyList.get(key));
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return value;
    }

    @Override
    public String getPropertyAsString(final String key) {
        String value = null;
        try {
            this.lookupForPropertyKey(key);
            value = propertyList.get(key);
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return value;
    }

    @Override
    public Map<String, String> getPropertyList() {
        return propertyList;
    }

    private void printPropertyList() {
        LOGGER.log(propertyList.toString(), LogLevel.TRACE);
    }

    private boolean lookupForPropertyKey(final String key) throws Exception {
        boolean success = false;
        if (propertyList.containsKey(key)) {
            success = true;
        } else {
            LOGGER.log("Property Entry " + key + " don't exist.", LogLevel.WARN);
            throw new MisconfigurationException(
                    "Configuration entry in property file not found.");
        }
        return success;
    }
}
