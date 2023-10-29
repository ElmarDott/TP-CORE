package org.europa.together;

import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

/**
 * JUnit5 Extension to run code beforr all test, like setup test suite and
 * shutdown after all the testenvironment.<br>
 * <b>USAGE: </b> @ExtendWith({JUnit5Preperator.class})
 */
public class JUnit5Preperator implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static final Logger LOGGER = new LogbackLogger(JUnit5Preperator.class);
    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext context) {
        if (!started) {
            LOGGER.log("TEST ENVIRONMENT will be prepared.", LogLevel.DEBUG);
            started = true;
            // Your "before all tests" startup logic goes here
            // The following line registers a callback hook when the root test context is shut down
            context.getRoot().getStore(GLOBAL).put("TogetherPlatform", this);
        }
    }

    @Override
    public void close() {

        LOGGER.log("TEST ENVIRONMENT will be shut down.", LogLevel.DEBUG);
        // Your "after all tests" logic goes here
    }

}
