package org.europa.together.service;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.io.File;
import org.europa.together.JUnit5Preperator;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.domain.ByteOrderMark;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.FileUtils;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
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
public class LoggingServiceTest {

    private static final Logger LOGGER
            = new LogbackLogger(LoggingServiceTest.class);
    private final String configFile
            = Constraints.SYSTEM_APP_DIR + "/";

    @Autowired
    private LoggingService loggingSevice;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(true);

        LOGGER.log("Assumptions passed ...\n\n", LogLevel.DEBUG);
    }

    @AfterAll
    static void tearDown() {
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() throws Exception {
    }
    //</editor-fold>

    @Test
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);
        assertThat(LoggingService.class, hasValidBeanConstructor());
    }

    @Test
    void readLogConfig() throws Exception {
        LOGGER.log("TEST CASE: readLogConfig", LogLevel.DEBUG);
        assertNotNull(loggingSevice.readLogConfiguration(
                Constraints.SYSTEM_APP_DIR + "/target/test-classes/logback-test.xml"));
    }

    @Test
    void failReadLogConfig() throws Exception {
        LOGGER.log("TEST CASE: failReadLogConfig", LogLevel.DEBUG);
        assertThrows(Exception.class, () -> {
            loggingSevice.readLogConfiguration(null);
        });
    }

    @Test
    void writeLogConfig() throws Exception {
        LOGGER.log("TEST CASE: writeLogConfig", LogLevel.DEBUG);

        String input = FileUtils.readFileStream(new File(
                Constraints.SYSTEM_APP_DIR + "/target/test-classes/logback-test.xml"),
                ByteOrderMark.UTF_8);
        String output = Constraints.SYSTEM_APP_DIR + "/target/logback.xml";

        loggingSevice.writeLogConfiguration(input, output);
        assertTrue(new File(output).exists());
    }

    @Test
    void failWriteLogConfig() throws Exception {
        LOGGER.log("TEST CASE: failWriteLogConfig", LogLevel.DEBUG);

        String input = FileUtils.readFileStream(new File(
                Constraints.SYSTEM_APP_DIR + "/target/test-classes/org/europa/together/xml/test_non_wellformed_01.xml"),
                ByteOrderMark.UTF_8);

        assertThrows(Exception.class, () -> {
            loggingSevice.writeLogConfiguration(input, null);
        });
    }
}
