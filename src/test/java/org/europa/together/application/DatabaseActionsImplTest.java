package org.europa.together.application;

import org.europa.together.business.DatabaseActions;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class DatabaseActionsImplTest {

    private final String sql_create
            = "CREATE TABLE IF NOT EXISTS test (clolumn_01 int, clolumn_02 char(255));";
    private final String sql_drop = "DROP TABLE IF EXISTS test;";

    private final DatabaseActions actions = new DatabaseActionsImpl();

    @Test
    public void testSqlActions() {
        String file = "org/europa/together/sql/test.sql";
        actions.activateTestMode();
        actions.connect(null);
        assertTrue(actions.executeQuery(sql_drop));
        assertTrue(actions.executeQuery(sql_create));
        assertTrue(actions.executeSqlFromClasspath(file));
    }
}
