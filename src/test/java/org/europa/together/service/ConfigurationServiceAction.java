package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.Logger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JGiven Invariants for ConfigurationService Test Scenarios.
 */
@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao-test.xml"})
public class ConfigurationServiceAction extends Stage<ConfigurationServiceAction> {

    private static final Logger LOGGER
            = new LogbackLogger(ConfigurationServiceAction.class);

    @Autowired
    @Qualifier("configurationHbmDAO")
    private ConfigurationDAO configurationDAO;

    public ConfigurationServiceAction reset_module_to_default() {
        try {
            ConfigurationService configService = new ConfigurationService();
            configService.resetModuleToDefault("Module_A");

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public ConfigurationServiceAction filter_mandatory_fields_of_configSet() {
        try {
            String module = "Module_A";
            String version = "1.0";
            String configSet = "Set_1";

            ConfigurationService configService = new ConfigurationService();
            assertNotNull(configService.filterMandatoryFieldsOfConfigSet(module, version, configSet));

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

}
