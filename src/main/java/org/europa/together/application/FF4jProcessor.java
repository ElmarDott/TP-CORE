package org.europa.together.application;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import org.ff4j.FF4j;
import org.ff4j.core.Feature;

/**
 * Wrapper implementation.
 */
public class FF4jProcessor {

    private static final long serialVersionUID = 13L;

    private FF4j toggles = null;
    private String configFile;
    private String path = Paths.get("").toAbsolutePath().toString();

    /**
     * Constructor.
     */
    public FF4jProcessor() {

        configFile
                = "/src/main/resources"
                + "/org/europa/together/configuration/FeatureToggles.xml";
//              = "/src/test/resources"
//              + "/org/europa/together/configuration/DeactivatedFeatureToggles.xml";
        print("INFO: FeatureToggle PATH: " + this.path);
        print("INFO: FeatureToggle configuration: " + this.configFile);
    }

    /**
     * Process if a feature exist and is activated. If a feature is deactivated
     * or not exist teh return will ne null.
     *
     * @param featureId as String
     * @return feature as Feature
     */
    public Feature scanToggels(final String featureId) {

        Feature feature = null;
        try {
            String configuration = path + configFile;

            InputStream source = new FileInputStream(configuration);
            toggles = new FF4j(source);
            feature = toggles.getFeature(featureId);

            print("Feature: " + feature.getDescription()
                    + " (" + feature.getUid() + ") " + feature.isEnable());

        } catch (Exception ex) {
            print(ex.getMessage());

            if (ex.getClass().getSimpleName().equals("NullPointerException")) {
                ex.printStackTrace();
            }
        }
        return feature;
    }

    /**
     * Set the path of the configuration file.
     *
     * @param configuration as String
     */
    public void setConfigFile(final String configuration) {
        this.configFile = configuration;

        print("INFO: change configuration to: " + this.configFile);
    }

    /**
     * Toggle to deactivate the unit test case execution in the case a feature
     * is disabled.
     *
     * @param featureId as string
     * @return true on success
     */
    public boolean deactivateUnitTests(final String featureId) {
        return scanToggels(featureId).isEnable();
    }

    /**
     * Write logging information on the command line.
     *
     * @param message as String
     */
    public void print(final String message) {
        System.out.println(message);
    }
}
