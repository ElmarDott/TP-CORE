package org.europa.together.application;

import org.europa.together.business.DatabaseActions;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class DatabaseActionsImplTest {

    private final String sql_create
            = "CREATE TABLE IF NOT EXISTS test (clolumn_01 int, clolumn_02 char(255));";
    private final String sql_drop = "DROP TABLE IF EXISTS test;";

    private final DatabaseActions actions = new DatabaseActionsImpl(true);

    @Test
    public void testConnection() {
        assertTrue(actions.connect(null));
        assertTrue(actions.disconnect());
    }

    @Test
    public void testExecuteQuery() {
        String file = "org/europa/together/sql/test.sql";
        actions.connect(null);
        assertTrue(actions.executeQuery(sql_drop));
        assertTrue(actions.executeQuery(sql_create));
        assertTrue(actions.executeSqlFromClasspath(file));
        actions.disconnect();
    }
}
