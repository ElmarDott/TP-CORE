package org.europa.together;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import java.sql.SQLException;
import org.europa.together.application.JdbcActions;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.junit.ClassRule;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * JUnit5 Extension to run code beforr all test, like setup test suite and
 * shutdown after all the testenvironment.<br>
 * <b>USAGE: </b> @ExtendWith({JUnit5DbPreperator.class})
 */
public class JUnit5Preperator implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static final Logger LOGGER = new LogbackLogger(JUnit5Preperator.class);
    private static boolean started = false;

    @ClassRule
    private PostgreSQLContainer<?> postgreSQLContainer;

    public static final String JDBC_CONNECTION_STRING = "jdbc:tc:postgresql:14:///together-test";

    public JUnit5Preperator() {
        int containerPort = 5432;
        int localPort = 5432;
        postgreSQLContainer
                = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14"))
                        .withExposedPorts(containerPort)
                        .withDatabaseName("together-test")
                        .withUsername("together")
                        .withPassword("together")
                        .withCreateContainerCmdModifier(cmd
                                -> cmd.withHostConfig(
                                new HostConfig().withPortBindings(
                                        new PortBinding(Ports.Binding.bindPort(localPort),
                                                new ExposedPort(containerPort))
                                )
                        ));
    }

    @Override
    public void beforeAll(ExtensionContext context) throws InterruptedException {

        if (!started) {
            LOGGER.log("TEST ENVIRONMENT will be prepared.", LogLevel.DEBUG);

            postgreSQLContainer.start();
            // check if the JDBC Connection is well established
            DatabaseActions jdbcConection = new JdbcActions();
            try {
                jdbcConection.connect("test");
                LOGGER.log("JDBC: " + jdbcConection.getJdbcMetaData().toString(), LogLevel.DEBUG);

                started = true;
                context.getRoot().getStore(GLOBAL).put("TogetherPlatform", this);

            } catch (SQLException ex) {
                LOGGER.catchException(ex);
            }
            LOGGER.log("TEST ENVIRONMENT prepared...\n\n", LogLevel.DEBUG);
        }
    }

    @Override
    public void close() {
        LOGGER.log("TEST ENVIRONMENT will be shut down.", LogLevel.DEBUG);

        postgreSQLContainer.stop();
    }

}
