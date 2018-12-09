package org.europa.together.domain;

import java.util.Map;

/**
 * Contains all information about a JDBC connection. Immutable and read only
 * access.
 */
public final class JdbcConnection {

    public final String JDBC_VERSION;
    public final String DBMS_NAME;
    public final String DBMS_VERSION;
    public final String DRIVER_NAME;
    public final String DRIVER_VERSION;
    public final String USER;
    public final String URL;
    public final String PORT;
    public final String CATALOG;

    /**
     * Constructor.
     */
    private JdbcConnection() {
        throw new UnsupportedOperationException();
    }

    /**
     * Constructor.
     *
     * @param properties as List
     */
    public JdbcConnection(final Map<String, String> properties) {

        JDBC_VERSION = properties.get("metaJdbcVersion");
        DBMS_NAME = properties.get("metaDbmsName");
        DBMS_VERSION = properties.get("metaDbmsVersion");
        DRIVER_NAME = properties.get("metaJdbcDriverName");
        DRIVER_VERSION = properties.get("metaJdbcDriverVersion");
        USER = properties.get("metaUser");
        URL = properties.get("metaUrl");
        PORT = properties.get("metaPort");
        CATALOG = properties.get("metaCatalog");
    }

    @Override
    public String toString() {
        return "JdbcConnection{"
                + " JDBC_VERSION=" + JDBC_VERSION
                + " DBMS_NAME=" + DBMS_NAME
                + " DBMS_VERSION=" + DBMS_VERSION
                + " DRIVER_NAME=" + DRIVER_NAME
                + " DRIVER_VERSION=" + DRIVER_VERSION
                + " USER=" + USER
                + " URL=" + URL
                + " PORT=" + PORT
                + " CATALOG=" + CATALOG
                + " }";
    }
}
