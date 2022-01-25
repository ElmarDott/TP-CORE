package org.europa.together.application;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.dbcp2.BasicDataSource;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.CryptoTools;
import org.europa.together.business.FeatureFlags;
import org.europa.together.business.Logger;
import org.europa.together.business.PropertyReader;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.StringUtils;
import org.ff4j.FF4j;
import org.ff4j.audit.repository.JdbcEventRepository;
import org.ff4j.core.Feature;
import org.ff4j.property.store.JdbcPropertyStore;
import org.ff4j.store.JdbcFeatureStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Repository
public class FeatureFlagsFF4j extends GenericHbmDAO<Feature, String>
        implements FeatureFlags {

    private static final long serialVersionUID = 13L;

    private static final Logger LOGGER = new LogbackLogger(FeatureFlagsFF4j.class);

    @Autowired
    private ConfigurationDAO configurationDAO;

    @Autowired
    private CryptoTools cryptoTools;

    @Autowired
    private PropertyReader reader;

    private FF4j ff4j;
    private Map<String, String> configuration;

    /**
     * Constructor.
     *
     * @param propertyFile as String
     */
    public FeatureFlagsFF4j() {
        LOGGER.log("instance class", LogLevel.INFO);
        configuration = new HashMap<>();
    }

    @Override
    public FF4j getFeatureStore(final String propertyFile)
            throws IOException, ConnectException {

        loadConfigurationFromDatabase();
        if (Boolean.parseBoolean(configuration.get("ff.activation"))) {

            reader.appendPropertiesFromClasspath(propertyFile);

            BasicDataSource cpds = new BasicDataSource();
            cpds.setDriverClassName(reader.getPropertyAsString("jdbc.driverClassName"));
            cpds.setUrl(reader.getPropertyAsString("jdbc.url"));
            cpds.setUsername(reader.getPropertyAsString("jdbc.user"));
            cpds.setPassword(reader.getPropertyAsString("jdbc.password"));

            ff4j = new FF4j();
            ff4j.setFeatureStore(new JdbcFeatureStore(cpds));
            ff4j.setPropertiesStore(new JdbcPropertyStore(cpds));
            ff4j.setEventRepository(new JdbcEventRepository(cpds));

            ff4j.audit(Boolean.parseBoolean(configuration.get("ff.audit")));
            ff4j.autoCreate(Boolean.parseBoolean(configuration.get("ff.autocreate")));

        } else {
            LOGGER.log("Feature Flags are deactivated.", LogLevel.DEBUG);
        }
        return ff4j;
    }

    @Override
    public boolean check(String featureId) {
        return ff4j.check(featureId);
    }

    @Override
    public void activateFeature(String featureId) {
        ff4j.enable(featureId);
    }

    @Override
    public void deactivateFeature(String featureId) {
        ff4j.disable(featureId);
    }

    @Override
    public void addFeature(Feature feature) {
        ff4j.createFeature(feature);
    }

    @Override
    public Feature getFeature(String featureId) {
        return ff4j.getFeature(featureId);
    }

    @Override
    public void updateFeature(Feature feature) {
        if (ff4j.exist(feature.getUid())) {
            ff4j.delete(feature.getUid());
        }
        ff4j.createFeature(feature);
    }

    @Override
    public void removeFeature(String featureId) {
        ff4j.delete(featureId);
    }

    @Override
    public Map<String, Feature> listAllFeatures() {
        return ff4j.getFeatures();
    }

    private void loadConfigurationFromDatabase() {

        LOGGER.log("Load all configuration sets of: " + CONFIG_SET
                + " - Version: " + CONFIG_VERSION
                + " - Module: " + Constraints.MODULE_NAME, LogLevel.DEBUG);

        List<ConfigurationDO> configurationEntries
                = configurationDAO.getAllConfigurationSetEntries(Constraints.MODULE_NAME,
                        CONFIG_VERSION, CONFIG_SET);

        if (configurationEntries != null && !configurationEntries.isEmpty()) {
            LOGGER.log("Size of config SET: " + configurationEntries.size(), LogLevel.DEBUG);

            for (ConfigurationDO entry : configurationEntries) {
                String value;
                if (StringUtils.isEmpty(entry.getValue())) {
                    value = entry.getDefaultValue();
                } else {
                    value = entry.getValue();
                }

                if (entry.getKey()
                        .equals(cryptoTools.calculateHash("ff.activation",
                                HashAlgorithm.SHA256))) {
                    this.configuration.put("ff.activation", value);
                } else if (entry.getKey()
                        .equals(cryptoTools.calculateHash("ff.audit",
                                HashAlgorithm.SHA256))) {
                    this.configuration.put("ff.audit", value);
                } else if (entry.getKey()
                        .equals(cryptoTools.calculateHash("ff.autocreate",
                                HashAlgorithm.SHA256))) {
                    this.configuration.put("ff.autocreate", value);
                }
            }
        }
    }

}
