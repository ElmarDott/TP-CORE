package org.europa.together.application;

import org.europa.together.business.DatabaseActions;
import org.europa.together.business.PaginationTestDAO;
import org.europa.together.domain.PaginationTestDO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.stereotype.Repository;

@Repository
public class PaginationTestHbmDAO extends GenericHbmDAO<PaginationTestDO, String>
        implements PaginationTestDAO {

    private static final long serialVersionUID = 99999L;
    private static DatabaseActions jdbcActions = new JdbcActions();

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(jdbcActions.connect("test"), "JDBC DBMS Connection failed.");
    }

    @AfterAll
    static void tearDown() {
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
    }
    //</editor-fold>

    public PaginationTestHbmDAO() {
        super();
    }
}
