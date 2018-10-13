package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.SocketTimeout;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
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
            = "CREATE TABLE IF NOT EXISTS test (column_01 int, column_02 char(255));";
    private final String sql_drop = "DROP TABLE IF EXISTS test;";

    private static final Logger LOGGER = new LoggerImpl(DatabaseActionsImplTest.class);
    private static final DatabaseActions actions = new DatabaseActionsImpl(true);

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        actions.connect("default");
        boolean check = SocketTimeout.timeout(2000, actions.getUri(), actions.getPort());
        LOGGER.log("PERFORM TESTS :: Check DBMS availability -> " + check, LogLevel.TRACE);
        String out;
        if (check) {
            out = "executed.";
        } else {
            out = "skiped.";
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out + "\n", LogLevel.TRACE);
        Assumptions.assumeTrue(check);
    }

    @AfterAll
    static void tearDown() {
        actions.executeQuery("TRUNCATE TABLE test;");
        actions.executeQuery("TRUNCATE TABLE app_config;");
        LOGGER.log("### TEST SUITE TERMINATED.", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED.\n", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(DatabaseActionsImpl.class, hasValidBeanConstructor());
    }

    @Test
    void testGetter() {
        LOGGER.log("TEST CASE: getter()", LogLevel.DEBUG);

        DatabaseActions dbms = new DatabaseActionsImpl();
        assertEquals(null, dbms.getMetaCatalog());
        assertEquals(null, dbms.getMetaDbmsName());
        assertEquals(null, dbms.getMetaDbmsVersion());
        assertEquals(null, dbms.getMetaJdbcDriverName());
        assertEquals(null, dbms.getMetaJdbcDriverVersion());
        assertEquals(null, dbms.getMetaJdbcVersion());
        assertEquals(null, dbms.getMetaUrl());
        assertEquals(null, dbms.getMetaUser());
    }

    @Test
    void testConnection() {
        LOGGER.log("TEST CASE: connection()", LogLevel.DEBUG);
        DatabaseActions dbms = new DatabaseActionsImpl();
        assertTrue(dbms.connect("default"));
    }

    @Test
    void testFailedConnection() {
        LOGGER.log("TEST CASE: faildConnection()", LogLevel.DEBUG);

        DatabaseActions dbms = new DatabaseActionsImpl();
        assertFalse(dbms.connect(Constraints.SYSTEM_APP_DIR
                + "/target/test-classes/"
                + "org/europa/together/properties/jdbc-test-fail.properties"));
    }

    @Test
    void testFallbackLoadProperties() {
        LOGGER.log("TEST CASE: fallbackLoadProperties()", LogLevel.DEBUG);

        DatabaseActions dbms = new DatabaseActionsImpl();
        assertTrue(dbms.connect(null));
    }

    @Test
    void testExecuteQuery() {
        LOGGER.log("TEST CASE: executeQuery()", LogLevel.DEBUG);

        assertTrue(actions.executeQuery(sql_drop));
        assertTrue(actions.executeQuery(sql_create));
    }

    @Test
    void testFailSqlExecuteQuery() {
        LOGGER.log("TEST CASE: failSqlExecuteQuery()", LogLevel.DEBUG);
        assertFalse(actions.executeQuery("SELECT * FROM foo;"));
    }

    @Test
    void testFailExecuteQuery() {
        LOGGER.log("TEST CASE: failExecuteQuery()", LogLevel.DEBUG);

        DatabaseActions dbms = new DatabaseActionsImpl();
        dbms.connect(Constraints.SYSTEM_APP_DIR
                + "/target/test-classes/"
                + "org/europa/together/properties/jdbc-test-fail.properties");
        assertFalse(dbms.executeQuery("SELECT * FROM test;"));
    }

    @Test
    void testExecuteSqlFile() {
        LOGGER.log("TEST CASE: executeSqlFile()", LogLevel.DEBUG);

        String file = "org/europa/together/sql/test.sql";
        assertTrue(actions.executeSqlFromClasspath(file));
    }

    @Test
    void testSqlFileNotFound() {
        LOGGER.log("TEST CASE: sqlFileNotFound()", LogLevel.DEBUG);

        String file = "org/europa/together/sql/file-not-exist.sql";
        assertFalse(actions.executeSqlFromClasspath(file));
    }

    @Test
    void testResultSet() {
        LOGGER.log("TEST CASE: resultSet()", LogLevel.DEBUG);

        String SQL_FILE
                = "org/europa/together/sql/configuration-test.sql";
        actions.connect("default");
        assertTrue(actions.executeSqlFromClasspath(SQL_FILE));
        assertTrue(actions.executeQuery("SELECT * FROM app_config;"));
        assertNotNull(actions.getResultSet());
        assertEquals(14, actions.getResultCount());
    }
}
