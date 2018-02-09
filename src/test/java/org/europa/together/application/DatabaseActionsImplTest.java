package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.SocketTimeout;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class DatabaseActionsImplTest {

    private final String sql_create
            = "CREATE TABLE IF NOT EXISTS test (clolumn_01 int, clolumn_02 char(255));";
    private final String sql_drop = "DROP TABLE IF EXISTS test;";

    private static final Logger LOGGER = new LoggerImpl(DatabaseActionsImplTest.class);
    private static final DatabaseActions actions = new DatabaseActionsImpl(true);

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        actions.connect("default");
        boolean check = SocketTimeout.timeout(2000, actions.getUri(), actions.getPort());
        LOGGER.log("PERFORM TESTS :: Check DBMS availability -> " + check, LogLevel.TRACE);
        String out;
        if (check) {
            out = "executed.";
        } else {
            out = "skiped.";
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out, LogLevel.TRACE);
        Assumptions.assumeTrue(check);
    }

    @AfterEach
    void tearDown() {
        LOGGER.log("TEST SUITE TERMINATED.", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        assertThat(DatabaseActionsImpl.class, hasValidBeanConstructor());
    }

    @Test
    void testConnection() {
        DatabaseActions dbms = new DatabaseActionsImpl();
        assertTrue(dbms.connect("default"));
    }

    @Test
    void testFailedConnection() {
        DatabaseActions dbms = new DatabaseActionsImpl();
        assertFalse(dbms.connect(Constraints.SYSTEM_APP_DIR
                + "/target/test-classes/"
                + "org/europa/together/properties/jdbc-test-fail.properties"));
    }

    @Test
    void testExecuteQuery() {
        assertTrue(actions.executeQuery(sql_drop));
        assertTrue(actions.executeQuery(sql_create));
    }

    @Test
    void testFailSqlExecuteQuery() {
        assertFalse(actions.executeQuery("SELECT * FROM foo;"));
    }

    @Test
    void testFailExecuteQuery() {
        DatabaseActions dbms = new DatabaseActionsImpl();
        dbms.connect(Constraints.SYSTEM_APP_DIR
                + "/target/test-classes/"
                + "org/europa/together/properties/jdbc-test-fail.properties");
        assertFalse(dbms.executeQuery("SELECT * FROM test;"));
    }

    @Test
    void testExecuteSqlFile() {
        String file = "org/europa/together/sql/test.sql";
        assertTrue(actions.executeSqlFromClasspath(file));
    }

    @Test
    void testSqlFileNotFound() {
        String file = "org/europa/together/sql/file-not-exist.sql";
        assertFalse(actions.executeSqlFromClasspath(file));
    }
}
