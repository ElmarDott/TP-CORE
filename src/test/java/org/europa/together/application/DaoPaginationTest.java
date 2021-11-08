package org.europa.together.application;

import java.sql.SQLException;
import java.util.List;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.JpaPagination;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.DAOException;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class DaoPaginationTest {

    private static final Logger LOGGER = new LogbackLogger(DaoPaginationTest.class);

    private static final String FLUSH_TABLE
            = "TRUNCATE TABLE " + ConfigurationDO.TABLE_NAME + ";";
    private static final String FILE
            = "org/europa/together/sql/pagination-test.sql";

    @Autowired
    private ConfigurationDAO configurationDAO;

    private static DatabaseActions jdbcActions = new JdbcActions();

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(jdbcActions.connect("test"));

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        jdbcActions.executeSqlFromClasspath(FILE);
    }

    @AfterAll
    static void tearDown() throws SQLException {
        jdbcActions.executeQuery(FLUSH_TABLE);
        LOGGER.log("### TEST SUITE TERMINATED.\n", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() throws SQLException {
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void listAllAsc() throws DAOException {
        LOGGER.log("TEST CASE: listAllAsc", LogLevel.DEBUG);

        JpaPagination<ConfigurationDO> seekElement = new JpaPagination<>("uuid");
        List<ConfigurationDO> result = configurationDAO.listAllElements(seekElement);

        assertEquals(101, result.size());
        assertEquals("88888888-0000-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0100-4444-4444-cccccccccc", result.get(result.size() - 1).getUuid());
    }

    @Test
    void listAllDesc() throws DAOException {
        LOGGER.log("TEST CASE: listAllDesc", LogLevel.DEBUG);

        JpaPagination<ConfigurationDO> seekElement = new JpaPagination<>("uuid");
        seekElement.setSorting(JpaPagination.ORDER_DESC);
        List<ConfigurationDO> result = configurationDAO.listAllElements(seekElement);

        assertEquals(101, result.size());
        assertEquals("88888888-0100-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0000-4444-4444-cccccccccc", result.get(result.size() - 1).getUuid());
    }

    @Test
    void limitAsc() throws DAOException {
        LOGGER.log("TEST CASE: limitAsc", LogLevel.DEBUG);

        JpaPagination<ConfigurationDO> seekElement = new JpaPagination<>("uuid");
        seekElement.setPageSize(35);
        List<ConfigurationDO> result = configurationDAO.listAllElements(seekElement);

        assertEquals(35, result.size());
        assertEquals("88888888-0000-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0034-4444-4444-cccccccccc", result.get(result.size() - 1).getUuid());
    }

    @Test
    void limitDesc() throws DAOException {
        LOGGER.log("TEST CASE: limitDesc", LogLevel.DEBUG);

        JpaPagination<ConfigurationDO> seekElement = new JpaPagination<>("uuid");
        seekElement.setPageSize(3);
        seekElement.setSorting(JpaPagination.ORDER_DESC);
        List<ConfigurationDO> result = configurationDAO.listAllElements(seekElement);

        assertEquals(3, result.size());
        assertEquals("88888888-0100-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0098-4444-4444-cccccccccc", result.get(result.size() - 1).getUuid());
    }

    @Test
    void pageAhead() throws DAOException {
        LOGGER.log("TEST CASE: pageAhead", LogLevel.DEBUG);

        //start element for pagination
        String primareyKey = "88888888-0027-4444-4444-cccccccccc";

        JpaPagination<ConfigurationDO> seekElement = new JpaPagination<>("uuid");
        seekElement.setPageSize(5);
        seekElement.setPageBreak(primareyKey);
        List<ConfigurationDO> result = configurationDAO.listAllElements(seekElement);

        assertEquals(5, result.size());
        assertEquals(primareyKey, result.get(0).getUuid());
        assertEquals("88888888-0031-4444-4444-cccccccccc", result.get(4).getUuid());
    }

    @Test
    void pageBackwards() throws DAOException {
        LOGGER.log("TEST CASE: pageBackwards", LogLevel.DEBUG);

        //start element for pagination
        String primareyKey = "88888888-0027-4444-4444-cccccccccc";

        JpaPagination<ConfigurationDO> seekElement = new JpaPagination<>("uuid");
        seekElement.setPageSize(5);
        seekElement.setPageBreak(primareyKey);
        seekElement.setPaging(JpaPagination.PAGING_BACKWARD);
        seekElement.setSorting(JpaPagination.ORDER_DESC);
        List<ConfigurationDO> result = configurationDAO.listAllElements(seekElement);

        assertEquals(5, result.size());
        assertEquals(primareyKey, result.get(0).getUuid());
        assertEquals("88888888-0023-4444-4444-cccccccccc", result.get(4).getUuid());
    }
}