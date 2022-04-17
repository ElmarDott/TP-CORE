package org.europa.together.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.JpaPagination;
import org.europa.together.domain.LogLevel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
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
    static void tearDown() throws Exception {
        jdbcActions.executeQuery(FLUSH_TABLE);
        LOGGER.log("### TEST SUITE TERMINATED.\n", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() throws Exception {
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void emptyListAll() throws Exception {
        LOGGER.log("TEST CASE: emptyListAllAsc", LogLevel.DEBUG);

        jdbcActions.executeQuery(FLUSH_TABLE);
        JpaPagination pivotElement = new JpaPagination("uuid");
        List<ConfigurationDO> result = configurationDAO.listAllElements(pivotElement);

        //restore DB before test in the case the tests fail
        jdbcActions.executeSqlFromClasspath(FILE);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void listAllAsc() throws Exception {
        LOGGER.log("TEST CASE: listAllAsc", LogLevel.DEBUG);

        JpaPagination pivotElement = new JpaPagination("uuid");
        List<ConfigurationDO> result = configurationDAO.listAllElements(pivotElement);

        assertEquals(101, result.size());
        assertEquals("88888888-0000-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0100-4444-4444-cccccccccc", result.get(result.size() - 1).getUuid());
    }

    @Test
    void listAllDesc() throws Exception {
        LOGGER.log("TEST CASE: listAllDesc", LogLevel.DEBUG);

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setSorting(JpaPagination.ORDER_DESC);
        List<ConfigurationDO> result = configurationDAO.listAllElements(pivotElement);

        assertEquals(101, result.size());
        assertEquals("88888888-0100-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0000-4444-4444-cccccccccc", result.get(result.size() - 1).getUuid());
    }

    @Test
    void limitAsc() throws Exception {
        LOGGER.log("TEST CASE: limitAsc", LogLevel.DEBUG);

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setPageSize(35);
        List<ConfigurationDO> result = configurationDAO.listAllElements(pivotElement);

        assertEquals(35, result.size());
        assertEquals("88888888-0000-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0034-4444-4444-cccccccccc", result.get(result.size() - 1).getUuid());
    }

    @Test
    void limitDesc() throws Exception {
        LOGGER.log("TEST CASE: limitDesc", LogLevel.DEBUG);

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setPageSize(3);
        pivotElement.setSorting(JpaPagination.ORDER_DESC);
        List<ConfigurationDO> result = configurationDAO.listAllElements(pivotElement);

        assertEquals(3, result.size());
        assertEquals("88888888-0100-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0098-4444-4444-cccccccccc", result.get(result.size() - 1).getUuid());
    }

    @Test
    void pageAhead() throws Exception {
        LOGGER.log("TEST CASE: pageAhead", LogLevel.DEBUG);

        //start element for pagination
        String primareyKey = "88888888-0027-4444-4444-cccccccccc";

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setPageSize(5);
        pivotElement.setPageBreak(primareyKey);
        List<ConfigurationDO> result = configurationDAO.listAllElements(pivotElement);

        assertEquals(5, result.size());
        assertEquals(primareyKey, result.get(0).getUuid());
        assertEquals("88888888-0031-4444-4444-cccccccccc", result.get(4).getUuid());
    }

    @Test
    void pageBackwards() throws Exception {
        LOGGER.log("TEST CASE: pageBackwards", LogLevel.DEBUG);

        //start element for pagination
        String primareyKey = "88888888-0027-4444-4444-cccccccccc";

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setPageSize(5);
        pivotElement.setPageBreak(primareyKey);
        pivotElement.setPaging(JpaPagination.PAGING_BACKWARD);
        pivotElement.setSorting(JpaPagination.ORDER_DESC);
        List<ConfigurationDO> result = configurationDAO.listAllElements(pivotElement);

        assertEquals(5, result.size());
        assertEquals(primareyKey, result.get(0).getUuid());
        assertEquals("88888888-0023-4444-4444-cccccccccc", result.get(4).getUuid());
    }

    @Test
    void fetchEndOfList() throws Exception {
        LOGGER.log("TEST CASE: fetchEndOfList", LogLevel.DEBUG);

        //start element for pagination
        String primareyKey = "88888888-0081-4444-4444-cccccccccc";

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setPageSize(27);
        pivotElement.setPageBreak(primareyKey);
        List<ConfigurationDO> result = configurationDAO.listAllElements(pivotElement);

        assertEquals(20, result.size());
        assertEquals(primareyKey, result.get(0).getUuid());
        assertEquals("88888888-0100-4444-4444-cccccccccc", result.get(19).getUuid());
    }

    @Test
    void fetchLessResultsThanAPage() throws Exception {
        LOGGER.log("TEST CASE: fetchLessResultsThanAPage", LogLevel.DEBUG);

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setPageSize(150);
        List<ConfigurationDO> result = configurationDAO.listAllElements(pivotElement);

        assertEquals(101, result.size());
        assertEquals("88888888-0000-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0100-4444-4444-cccccccccc", result.get(100).getUuid());
    }

    @Test
    void simpleResultFilterWhitoutPagination() throws Exception {
        LOGGER.log("TEST CASE: simpleResultFilterWhitoutPagination", LogLevel.DEBUG);

        Map<String, String> filters = new HashMap<>();
        filters.put("modulName", "default");

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setFilterStringCriteria(filters);

        List<ConfigurationDO> result = configurationDAO.listAllElements(pivotElement);

        assertEquals(26, result.size());
        assertEquals("88888888-0050-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0075-4444-4444-cccccccccc", result.get(25).getUuid());
    }

    @Test
    void simpleResultFilterWithPagination() throws Exception {
        LOGGER.log("TEST CASE: simpleResultFilterWithPagination", LogLevel.DEBUG);

        Map<String, String> filters = new HashMap<>();
        filters.put("modulName", "default");

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setFilterStringCriteria(filters);
        pivotElement.setPageSize(8);
        pivotElement.setPageBreak("88888888-0057-4444-4444-cccccccccc");

        List<ConfigurationDO> result = configurationDAO.listAllElements(pivotElement);

        assertEquals(8, result.size());
        assertEquals("88888888-0057-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0064-4444-4444-cccccccccc", result.get(7).getUuid());
    }

    @Test
    void complexResultFilterWithPagination() throws Exception {
        LOGGER.log("TEST CASE: complexResultFilterWithPagination", LogLevel.DEBUG);

        Map<String, String> filters = new HashMap<>();
        filters.put("modulName", "test");
        filters.put("configurationSet", "AAA");

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setFilterStringCriteria(filters);
        pivotElement.setPageSize(8);
        pivotElement.setPageBreak("88888888-0005-4444-4444-cccccccccc");

        List<ConfigurationDO> result = configurationDAO.listAllElements(pivotElement);

        assertEquals(5, result.size());
        assertEquals("88888888-0005-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0009-4444-4444-cccccccccc", result.get(4).getUuid());
    }
}
