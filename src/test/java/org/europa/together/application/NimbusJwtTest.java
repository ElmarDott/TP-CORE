package org.europa.together.application;

import static org.junit.jupiter.api.Assertions.*;
import static com.google.code.beanmatchers.BeanMatchers.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.europa.together.JUnit5Preperator;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.JsonWebToken;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.JsonProcessingException;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class NimbusJwtTest {

    private static final Logger LOGGER = new LogbackLogger(NimbusJwtTest.class);

    private static final String FLUSH_TABLE
            = "TRUNCATE TABLE " + ConfigurationDO.TABLE_NAME + ";";
    private static final String FILE
            = "org/europa/together/sql/jwt-test.sql";

    private static DatabaseActions jdbcActions = new JdbcActions();

    private String jws
            = "eyJhbGciOiJIUzUxMiJ9.e0hhbGxvIFdvcmxkIEpTT059.H4_JPE4135eUQUnAJrQD9nkLavNuEBYxxp4FnU7k7FOZbjWtiE-F6DjnThaxBpTRzuGpck0H3lud_H0U7_mHbQ";

    @Autowired
    JsonWebToken jsonWebToken;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(jdbcActions.connect("test"), "JDBC DBMS Connection failed.");
        jdbcActions.executeSqlFromClasspath(FILE);

        LOGGER.log("Assumptions passed ...\n\n", LogLevel.DEBUG);
    }

    @AfterAll
    static void tearDown() throws SQLException {
        jdbcActions.executeQuery(FLUSH_TABLE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
    }
    //</editor-fold>

    @Test
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);
        assertThat(NimbusJwt.class, hasValidBeanConstructor());
    }

    @Test
    void buildHMAC512SignedJws() throws Exception {
        LOGGER.log("TEST CASE: buildHMAC512SignedJws", LogLevel.DEBUG);

        assertEquals(jws, jsonWebToken.buildHMAC512SignedJws("{Hallo World JSON}"));
    }

    @Test
    void buildHMAC512SignedJwt() throws Exception {
        LOGGER.log("TEST CASE: buildHMAC512SignedJwt", LogLevel.DEBUG);

        String issuer = "https://togehter-platform.org";
        String subject = "togetherPlatform-API";
        List<String> audience = new ArrayList<>();
        audience.add("https://app.togehter-platform.org");

        assertEquals(383, jsonWebToken.buildHMAC512SignedJwt(issuer, subject, audience).length());
    }

    @Test
    void parseHMAC512SingedJws() throws Exception {
        LOGGER.log("TEST CASE: parseHMAC512SingedJws", LogLevel.DEBUG);

        assertEquals("{Hallo World JSON}", jsonWebToken.parseHMAC512SingedJws(jws));
    }

    @Test
    void failParseHMAC512SingedJws() throws Exception {
        LOGGER.log("TEST CASE: failParseHMAC512SingedJws", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            jsonWebToken.parseHMAC512SingedJws(jws.substring(1));
        });
    }

    @Test
    void parseHMAC512SingedJwt() throws Exception {
        LOGGER.log("TEST CASE: parseHMAC512SingedJwt", LogLevel.DEBUG);

        String issuer = "https://togehter-platform.org";
        String subject = "togetherPlatform-API";
        List<String> audience = new ArrayList<>();
        audience.add("https://app.togehter-platform.org");
        String jwt = jsonWebToken.buildHMAC512SignedJwt(issuer, subject, audience);

        String payload = jsonWebToken.parseHMAC512SingedJwt(jwt);
        LOGGER.log("JWT Payload: " + payload, LogLevel.DEBUG);
        assertEquals("{\"sub\":\"togetherPlatform-API\",", payload.substring(0, 30));
    }

    @Test
    void failparseHMAC512SingedJwt() throws Exception {
        LOGGER.log("TEST CASE: parseHMAC512SingedJwt", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            String jwt = jsonWebToken.buildHMAC512SignedJwt("", "", null);
            jsonWebToken.parseHMAC512SingedJwt(jwt.substring(1));
        });
    }
}
