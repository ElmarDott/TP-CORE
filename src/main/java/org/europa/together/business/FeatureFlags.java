package org.europa.together.business;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.springframework.stereotype.Component;

/**
 *
 */
@API(status = STABLE, since = "3.0", consumers = "FeatureFlagsFF4j")
@Component
public interface FeatureFlags {

    /**
     * Identifier for the given feature to enable toggles.
     */
    @API(status = STABLE, since = "3.0")
    String FEATURE_ID = "CM-13";

    /**
     * Define the Configuration Set for the MailClient.
     */
    @API(status = STABLE, since = "3.0")
    String CONFIG_SET = "features";

    /**
     * Defines for which MODULE_VERSION the configuration will work.
     */
    @API(status = STABLE, since = "3.0")
    String CONFIG_VERSION = "1.0";

    public FF4j getFeatureStore(String propertyFile) throws IOException, ConnectException;

    public boolean check(String featureId);

    public void activateFeature(String eeatureId);

    public void deactivateFeature(String featureId);

    public void addFeature(Feature feature);

    public Feature getFeature(String featureId);

    public void updateFeature(Feature feature);

    public void removeFeature(String featureId);

    public Map<String, Feature> listAllFeatures();
}
