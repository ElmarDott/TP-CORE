package org.europa.together.service;

import java.util.ArrayList;
import java.util.List;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the Configuration Service.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.0")
@Service
@FeatureToggle(featureID = ConfigurationDAO.FEATURE_ID)
public final class ConfigurationService {

    private static final long serialVersionUID = 205L;
    private static final Logger LOGGER = new LogbackLogger(ConfigurationService.class);

    @Autowired
    private ConfigurationDAO configurationDAO;

    /**
     * Constructor.
     */
    @API(status = STABLE, since = "1.0")
    public ConfigurationService() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    /**
     * Reset all configuration key entries for a module back to the default
     * values.
     *
     * @param module as String
     */
    @API(status = STABLE, since = "1.0")
    @FeatureToggle(featureID = "CM-0005.S001")
    public void resetModuleToDefault(final String module) {

        List<ConfigurationDO> configurationEntries = configurationDAO.getAllModuleEntries(module);
        for (ConfigurationDO entry : configurationEntries) {
            configurationDAO.restoreKeyToDefault(entry);
        }
    }

    /**
     * Filter all mandatory Entries of a ConfigSet.
     *
     * @param module as String
     * @param version as String
     * @param configSet as String
     * @return mandatory as List&lt;Configuration&gt;
     */
    @API(status = STABLE, since = "1.0")
    @FeatureToggle(featureID = "CM-0005.S002")
    public List<ConfigurationDO> filterMandatoryFieldsOfConfigSet(
            final String module, final String version, final String configSet) {

        List<ConfigurationDO> mandantoryEntries = new ArrayList<>();
        List<ConfigurationDO> configurationEntries
                = configurationDAO.getAllConfigurationSetEntries(module, version, configSet);
        for (ConfigurationDO entry : configurationEntries) {
            if (entry.isMandatory()) {
                mandantoryEntries.add(entry);
            }
        }
        return mandantoryEntries;
    }

}
