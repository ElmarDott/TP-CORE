package org.europa.together.application;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import org.europa.together.application.internal.FF4jAnnotationPreProcessorHelper;
import org.europa.together.business.FeatureToggle;
import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.springframework.stereotype.Repository;

/**
 * Wrapper implementation.
 */
@Repository
@FeatureToggle(featureID = "CM-0013")
public class FF4jProcessor {

    private static final long serialVersionUID = 13L;

    private FF4j toggles = null;
    private String path = Paths.get("").toAbsolutePath().toString();
    private String configFile = "/src/main/resources"
            + "/org/europa/together/configuration/FeatureToggles.xml";

    /**
     * Constructor.
     */
    public FF4jProcessor() {
        FF4jAnnotationPreProcessorHelper
                .print("INFO: FeatureToggle PATH: " + this.path);
        FF4jAnnotationPreProcessorHelper
                .print("INFO: FeatureToggle configuration: " + this.configFile);
    }

    /**
     * Process if a feature exist and is activated. If a feature is deactivated
     * or not exist the return will ne null.
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

            FF4jAnnotationPreProcessorHelper.print("INFO: Feature(" + feature.getUid() + ") "
                    + feature.isEnable() + " " + feature.getDescription());

        } catch (Exception ex) {
            FF4jAnnotationPreProcessorHelper.print("ERROR: " + ex.getMessage());
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

        FF4jAnnotationPreProcessorHelper.print("INFO: change configuration to: " + configuration);
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
}
