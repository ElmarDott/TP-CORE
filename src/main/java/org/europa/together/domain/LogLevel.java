package org.europa.together.domain;

/**
 * Available LogLevels.
 */
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
