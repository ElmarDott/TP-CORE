package org.europa.together;

import java.sql.SQLException;
import org.europa.together.application.JdbcActions;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

/**
 * JUnit5 Extension to run code beforr all test, like setup test suite and
 * shutdown after all the testenvironment.<br>
 * <b>USAGE: </b> @ExtendWith({JUnit5DbPreperator.class})
 */
public class JUnit5Preperator
        implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static final Logger LOGGER = new LogbackLogger(JUnit5Preperator.class);

    public static DatabaseActions JDBC_CONNECTION;
    private static boolean isConnected = false;

    @Override
    public void beforeAll(ExtensionContext context) {
        LOGGER.log("TEST ENVIRONMENT will be prepared.", LogLevel.DEBUG);

        // check if the JDBC Connection is well established
        JDBC_CONNECTION = new JdbcActions();
        try {
            JDBC_CONNECTION.connect("test");
            LOGGER.log("JDBC: " + JDBC_CONNECTION.getJdbcMetaData().toString(), LogLevel.DEBUG);

            JUnit5Preperator.isConnected = true;
            context.getRoot().getStore(GLOBAL).put("TogetherPlatform", this);

        } catch (SQLException ex) {
            LOGGER.catchException(ex);
        }
        LOGGER.log("TEST ENVIRONMENT prepared...", LogLevel.DEBUG);
    }

    @Override
    public void close() {
        LOGGER.log("TEST ENVIRONMENT will be shut down.", LogLevel.DEBUG);
    }

    public static boolean isConnected() {
        return JUnit5Preperator.isConnected;
    }
}
