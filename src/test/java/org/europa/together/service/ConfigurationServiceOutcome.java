package org.europa.together.service;

import com.tngtech.jgiven.Stage;
import java.util.List;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JGiven PostConditions for ConfigurationService Test Scenarios.
 */
@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao-test.xml"})
public class ConfigurationServiceOutcome extends Stage<ConfigurationServiceOutcome> {

    private static final Logger LOGGER
            = new LogbackLogger(ConfigurationServiceOutcome.class);

    @Autowired
    @Qualifier("configurationDAOImpl")
    private ConfigurationDAO configurationDAO;

    public ConfigurationServiceOutcome check_default_entries() {
        try {
            List<ConfigurationDO> entryList = configurationDAO.getAllModuleEntries("Module_A");
            assertEquals(10, entryList.size());

            boolean test = false;
            for (ConfigurationDO entry : entryList) {
                if (entry.getValue().equals("X") || entry.equals("Y")
                        || entry.equals("a") || entry.equals("b") || entry.equals("c")) {
                    test = true;
                } else {
                    test = false;
                    break;
                }
            }
            assertTrue(test);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }

    public ConfigurationServiceOutcome check_mandantory_entries() {
        try {
            String module = "Module_A";
            String version = "1.0";
            String configSet = "Set_1";

            ConfigurationService configService = new ConfigurationService();
            List<ConfigurationDO> entryList
                    = configService.filterMandatoryFieldsOfConfigSet(module, version, configSet);
            assertEquals(1, entryList.size());
            assertTrue(entryList.get(0).isMandatory());

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return self();
    }
}
