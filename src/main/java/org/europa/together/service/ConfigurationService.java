package org.europa.together.service;

import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;

/**
 * Implementation of the Configuration Service.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.0")
public final class ConfigurationService {

    private static final long serialVersionUID = 205L;
    private static final Logger LOGGER = new LoggerImpl(ConfigurationService.class);

    /**
     * Constructor.
     */
    public ConfigurationService() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    // import full config from xml
    // export full config to xml
    // filter mandatory fields of configuration
    // reset modul to default
    // reset all to default
    // get module history
    //
}
