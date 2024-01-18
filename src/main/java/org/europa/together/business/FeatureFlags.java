package org.europa.together.business;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.exceptions.MisconfigurationException;
import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.springframework.stereotype.Component;

/**
 * Feature Flags is a wrapper for the FF4j library to enable toggels for
 * activating and deactivating features.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "3.0", consumers = "FeatureFlagsFF4j")
@Component
public interface FeatureFlags {

    /**
     * Identifier for the given feature.
     */
    @API(status = STABLE, since = "3.0")
    String FEATURE_ID = "CM-13";

    /**
     * Define the configuration set for FeatureFlags.
     */
    @API(status = STABLE, since = "3.0")
    String CONFIG_SET = "features";

    /**
     * Defines for which MODULE_VERSION the configuration will work.
     */
    @API(status = STABLE, since = "3.0")
    String CONFIG_VERSION = "1.0";

    /**
     * Connect an application to a feature store by a given database connection
     * file.<li>jdbc.driverClassName</li>
     * <li>jdbc.url</li>
     * <li>jdbc.user</li>
     * <li>jdbc.password</li>
     *
     * @param propertyFile as String
     * @return feature store as FF4j
     * @throws java.io.IOException
     * @throws java.net.ConnectException
     * @throws org.europa.together.exceptions.MisconfigurationException
     */
    FF4j getFeatureStore(String propertyFile)
            throws IOException, ConnectException, MisconfigurationException;

    /**
     * Check if a feature is enbaled or not.
     *
     * @param featureId as String
     * @return check as boolean
     */
    boolean check(String featureId);

    /**
     * Activate a feature in the feature store by given ID.
     *
     * @param featureId as String
     */
    void activateFeature(String featureId);

    /**
     * Deactivate a feature in the feature store by given ID.
     *
     * @param featureId as String
     */
    void deactivateFeature(String featureId);

    /**
     * Add a feature to the feature store.
     *
     *
     * @param feature as Feature
     */
    void addFeature(Feature feature);

    /**
     * Get the full feature by a given ID from the feature store.
     *
     * @param featureId as String
     * @return feature as Feature
     */
    Feature getFeature(String featureId);

    /**
     * Update an existing feature in the feature store.
     *
     * @param feature as Feature
     */
    void updateFeature(Feature feature);

    /**
     * Remove an existing feature from the feature store.
     *
     * @param featureId
     */
    void removeFeature(String featureId);

    /**
     * Grab all in the feature store existing features.
     *
     * @return Feature by its featureId as Map
     */
    Map<String, Feature> listAllFeatures();
}
