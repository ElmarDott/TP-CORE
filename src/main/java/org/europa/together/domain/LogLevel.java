package org.europa.together.domain;

import org.europa.together.business.FeatureToggle;

/**
 * Available LogLevels.
 */
@FeatureToggle(featureID = "CM-0001.DO01")
public enum LogLevel {

    DEBUG("DEBUG"),
    ERROR("ERROR"),
    INFO("INFO"),
    TRACE("TRACE"),
    WARN("WARN");

    private final String value;

    //CONSTRUCTOR
    LogLevel(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
