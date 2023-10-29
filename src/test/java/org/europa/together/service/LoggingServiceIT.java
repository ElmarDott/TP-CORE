package org.europa.together.service;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.io.File;
import java.io.IOException;
import org.europa.together.application.ConfigurationHbmDAOTest;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
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
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class LoggingServiceIT {

    private static final Logger LOGGER = new LogbackLogger(ConfigurationHbmDAOTest.class);

    @Autowired
    private LoggingService loggingSevice;

    private final String configFile = Constraints.SYSTEM_APP_DIR
            + "/target/test-classes/org/europa/together/";
    private String configuration = "";

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(true);
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
    @Order(0)
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(LoggingService.class, hasValidBeanConstructor());
    }

    @Test
    @Order(1)
    void ReadFileConfig() throws Exception {
        LOGGER.log("TEST CASE: Read configuration File", LogLevel.DEBUG);

        configuration = loggingSevice.readLogConfiguration(configFile + "logback.xml");
//        assertEquals(880, configuration.length());
    }

    @Test
    @Order(2)
    void writeFileConfig() throws Exception {
        LOGGER.log("TEST CASE: write Configuration File", LogLevel.DEBUG);

        loggingSevice.writeLogConfiguration(configuration + "\n<success/>",
                configFile + "logback-written.xml");

//        assertEquals(890,
//                FileUtils.readFileStream(new File(configFile + "logback-written.xml")).length());
    }
}
