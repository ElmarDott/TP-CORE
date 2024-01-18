package org.europa.together.application;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.europa.together.JUnit5Preperator;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.business.PaginationTestDAO;
import org.europa.together.domain.JpaPagination;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.PaginationTestDO;
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
@ExtendWith({JUnit5Preperator.class})
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class DaoPaginationTest {

    private static final Logger LOGGER = new LogbackLogger(DaoPaginationTest.class);

    private static final String FLUSH_TABLE
            = "TRUNCATE TABLE " + PaginationTestDO.TABLE_NAME + ";";
    private static final String FILE
            = "org/europa/together/sql/pagination-test.sql";

    @Autowired
    private PaginationTestDAO paginationTestDAO;

    private static DatabaseActions jdbcActions = new JdbcActions();

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(jdbcActions.connect("test"), "JDBC DBMS Connection failed.");
        jdbcActions.executeSqlFromClasspath(FILE);

        LOGGER.log("Assumptions passed ...\n\n", LogLevel.DEBUG);
    }

    @AfterAll
    static void tearDown() throws Exception {
        jdbcActions.executeQuery(FLUSH_TABLE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() throws Exception {
    }
    //</editor-fold>

    @Test
    void emptyListAll() throws Exception {
        LOGGER.log("TEST CASE: emptyListAllAsc", LogLevel.DEBUG);

        jdbcActions.executeQuery(FLUSH_TABLE);
        JpaPagination pivotElement = new JpaPagination("uuid");
        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

        //restore DB before test in the case the tests fail
        jdbcActions.executeSqlFromClasspath(FILE);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void listAllAsc() throws Exception {
        LOGGER.log("TEST CASE: listAllAsc", LogLevel.DEBUG);

        JpaPagination pivotElement = new JpaPagination("uuid");
        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

        assertEquals(101, result.size());
        assertEquals("88888888-0000-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0100-4444-4444-cccccccccc", result.get(result.size() - 1).getUuid());
    }

    @Test
    void listAllDesc() throws Exception {
        LOGGER.log("TEST CASE: listAllDesc", LogLevel.DEBUG);

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setSorting(JpaPagination.ORDER_DESC);
        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

        assertEquals(101, result.size());
        assertEquals("88888888-0100-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0000-4444-4444-cccccccccc", result.get(result.size() - 1).getUuid());
    }

    @Test
    void limitAsc() throws Exception {
        LOGGER.log("TEST CASE: limitAsc", LogLevel.DEBUG);

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setPageSize(35);
        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

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
        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

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
        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

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
        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

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
        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

        assertEquals(20, result.size());
        assertEquals(primareyKey, result.get(0).getUuid());
        assertEquals("88888888-0100-4444-4444-cccccccccc", result.get(19).getUuid());
    }

    @Test
    void fetchLessResultsThanAPage() throws Exception {
        LOGGER.log("TEST CASE: fetchLessResultsThanAPage", LogLevel.DEBUG);

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setPageSize(150);
        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

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

        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

        assertEquals(26, result.size());
        assertEquals("88888888-0050-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0075-4444-4444-cccccccccc", result.get(25).getUuid());
    }

    @Test
    void complexResultFilterWithPagination() throws Exception {
        LOGGER.log("TEST CASE: complexResultFilterWithPagination", LogLevel.DEBUG);

        Map<String, String> stringFilters = new HashMap<>();
        stringFilters.put("modulName", "default");

        Map<String, Boolean> boolFilters = new HashMap<>();
        boolFilters.put("deprecated", true);

        Map<String, Integer> intFilters = new HashMap<>();
        intFilters.put("intNumber", 3);

        Map<String, Float> floatFilters = new HashMap<>();
        floatFilters.put("floatNumber", 6.3f);

        Timestamp selctedDate = Timestamp.from(LocalDateTime
                .parse("1984-04-01T12:00:01.0")
                .toInstant(ZoneOffset.UTC)
        );

        Map<String, Date> dateFilters = new HashMap<>();
        dateFilters.put("dateValue", selctedDate);

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setFilterStringCriteria(stringFilters);
        pivotElement.setFilterBooleanCriteria(boolFilters);
        pivotElement.setFilterIntegerCriteria(intFilters);
        pivotElement.setFilterFloatCriteria(floatFilters);
        pivotElement.setFilterDateCriteria(dateFilters);
        pivotElement.setPageSize(10);
        pivotElement.setPageBreak("88888888-0060-4444-4444-cccccccccc");

        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

        assertEquals(1, result.size());
        assertEquals("88888888-0063-4444-4444-cccccccccc", result.get(0).getUuid());
    }

    @Test
    void simpleResultStringEqualFilterWithPagination() throws Exception {
        LOGGER.log("TEST CASE: simpleResultStringEqualFilterWithPagination", LogLevel.DEBUG);

        Map<String, String> filters = new HashMap<>();
        filters.put("modulName", "default");

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setFilterStringCriteria(filters);
        pivotElement.setPageSize(8);
        pivotElement.setPageBreak("88888888-0057-4444-4444-cccccccccc");

        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

        assertEquals(8, result.size());
        assertEquals("88888888-0057-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0064-4444-4444-cccccccccc", result.get(7).getUuid());
    }

    @Test
    void simpleResultBooleanEqualFilterWithPagination() throws Exception {
        LOGGER.log("TEST CASE: simpleResultBooleanEqualFilterWithPagination", LogLevel.DEBUG);

        Map<String, Boolean> filters = new HashMap<>();
        filters.put("deprecated", true);

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setFilterBooleanCriteria(filters);
        pivotElement.setPageSize(8);
        pivotElement.setPageBreak("88888888-0057-4444-4444-cccccccccc");

        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

        assertEquals(8, result.size());
        assertEquals("88888888-0057-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0064-4444-4444-cccccccccc", result.get(7).getUuid());
    }

    @Test
    void simpleResultIntegerEqualFilterWithPagination() throws Exception {
        LOGGER.log("TEST CASE: simpleResultIntegerEqualFilterWithPagination", LogLevel.DEBUG);

        Map<String, Integer> filters = new HashMap<>();
        filters.put("intNumber", 1);

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setFilterIntegerCriteria(filters);
        pivotElement.setPageSize(8);
        pivotElement.setPageBreak("88888888-0057-4444-4444-cccccccccc");

        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

        assertEquals(2, result.size());
        assertEquals("88888888-0061-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0091-4444-4444-cccccccccc", result.get(1).getUuid());
    }

    @Test
    void simpleResultFloatEqualFilterWithPagination() throws Exception {
        LOGGER.log("TEST CASE: simpleResultFloatEqualFilterWithPagination", LogLevel.DEBUG);

        Map<String, Float> filters = new HashMap<>();
        filters.put("floatNumber", 6.8f);

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setFilterFloatCriteria(filters);
        pivotElement.setPageSize(8);
        pivotElement.setPageBreak("88888888-0057-4444-4444-cccccccccc");

        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

        assertEquals(1, result.size());
        assertEquals("88888888-0068-4444-4444-cccccccccc", result.get(0).getUuid());
    }

    @Test
    void simpleResultDateEqualFilterWithPagination() throws Exception {
        LOGGER.log("TEST CASE: simpleResultDateEqualFilterWithPagination", LogLevel.DEBUG);

        Timestamp selctedDate = Timestamp.from(LocalDateTime
                .parse("1984-04-01T12:00:01.0")
                .toInstant(ZoneOffset.UTC)
        );
        Map<String, Date> filters = new HashMap<>();
        filters.put("dateValue", selctedDate);

        JpaPagination pivotElement = new JpaPagination("uuid");
        pivotElement.setFilterDateCriteria(filters);
        pivotElement.setPageSize(8);
        pivotElement.setPageBreak("88888888-0057-4444-4444-cccccccccc");

        List<PaginationTestDO> result = paginationTestDAO.listAllElements(pivotElement);

        assertEquals(8, result.size());
        assertEquals("88888888-0057-4444-4444-cccccccccc", result.get(0).getUuid());
        assertEquals("88888888-0064-4444-4444-cccccccccc", result.get(7).getUuid());
    }
}
