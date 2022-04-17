package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.sql.ResultSet;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.JdbcConnection;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class JdbcActionsTest {

    private static final Logger LOGGER = new LogbackLogger(JdbcActionsTest.class);

    private final String sql_create
            = "CREATE TABLE IF NOT EXISTS test (column_01 int, column_02 char(255));";
    private final String sql_drop = "DROP TABLE IF EXISTS test;";

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(true);

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.\n", LogLevel.TRACE);
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
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(JdbcActions.class, hasValidBeanConstructor());
    }

    @Test
    void connect() {
        LOGGER.log("TEST CASE: connect", LogLevel.DEBUG);

        DatabaseActions dbms = new JdbcActions();
        assertTrue(dbms.connect("test"));
    }

    @Test
    void failConnect() {
        LOGGER.log("TEST CASE: failConnect", LogLevel.DEBUG);

        DatabaseActions dbms = new JdbcActions();
        assertFalse(dbms.connect(Constraints.SYSTEM_APP_DIR
                + "/target/test-classes/"
                + "org/europa/together/properties/jdbc-test-fail.properties"));
    }

    @Test
    void fallbackLoadProperties() {
        LOGGER.log("TEST CASE: fallbackLoadProperties", LogLevel.DEBUG);

        DatabaseActions dbms = new JdbcActions();
        assertTrue(dbms.connect(null));
    }

    @Test
    void executeQuery() throws Exception {
        LOGGER.log("TEST CASE: executeQuery", LogLevel.DEBUG);

        DatabaseActions dbms = new JdbcActions();
        dbms.connect("test");

        assertNull(dbms.executeQuery(sql_drop));
        assertNull(dbms.executeQuery(sql_create));
        dbms.executeQuery(sql_drop);
    }

    @Test
    void failExecuteQuery() throws Exception {
        LOGGER.log("TEST CASE: failExecuteQuery", LogLevel.DEBUG);

        DatabaseActions dbms = new JdbcActions();
        dbms.connect("test");
        assertThrows(Exception.class, () -> {
            dbms.executeQuery("SELECT * FROM foo;");
        });
    }

    @Test
    void failExecuteQueryNoConnection() throws Exception {
        LOGGER.log("TEST CASE: failExecuteQuery", LogLevel.DEBUG);

        DatabaseActions dbms = new JdbcActions();
        assertNull(dbms.executeQuery("SELECT * FROM foo;"));
    }

    @Test
    void executeSqlFileFromClasspath() throws Exception {
        LOGGER.log("TEST CASE: executeSqlFileFromClasspath", LogLevel.DEBUG);

        DatabaseActions dbms = new JdbcActions();
        dbms.connect("test");
        dbms.executeQuery(sql_drop);
        String file = "org/europa/together/sql/test.sql";
        assertTrue(dbms.executeSqlFromClasspath(file));
        dbms.executeQuery(sql_drop);
    }

    @Test
    void sqlFileNotFound() {
        LOGGER.log("TEST CASE: sqlFileNotFound", LogLevel.DEBUG);

        DatabaseActions dbms = new JdbcActions();
        dbms.connect("test");
        String file = "org/europa/together/sql/file-not-exist.sql";
        assertFalse(dbms.executeSqlFromClasspath(file));
    }

    @Test
    void getResultSet() throws Exception {
        LOGGER.log("TEST CASE: getResultSet", LogLevel.DEBUG);

        String SQL_FILE
                = "org/europa/together/sql/test.sql";
        DatabaseActions dbms = new JdbcActions();
        dbms.connect("test");

        assertTrue(dbms.executeSqlFromClasspath(SQL_FILE));

        ResultSet results = dbms.executeQuery("SELECT * FROM test;");
        assertEquals(1, dbms.countResultSets(results));

        dbms.executeQuery(sql_drop);
    }

    @Test
    void getJdbcMetaData() throws Exception {
        LOGGER.log("TEST CASE: getJdbcMetaData", LogLevel.DEBUG);

        DatabaseActions dbms = new JdbcActions();
        dbms.connect("test");
        JdbcConnection metaData = dbms.getJdbcMetaData();
        LOGGER.log(metaData.toString(), LogLevel.DEBUG);
        assertNotNull(metaData);
    }

    @Test
    void failGetJdbcMetaData() throws Exception {
        LOGGER.log("TEST CASE: failGetJdbcMetaData", LogLevel.DEBUG);

        DatabaseActions dbms = new JdbcActions();
        assertThrows(Exception.class, () -> {
            dbms.getJdbcMetaData();
        });
    }
}
