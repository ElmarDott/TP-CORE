package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.europa.together.business.Logger;
import org.europa.together.business.QrCodeGenerator;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import static org.hamcrest.MatcherAssert.assertThat;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class ZxingGeneratorTest {

    private static final String FILE_PATH
            = "org/europa/together/qr_codes";
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + FILE_PATH;

    private static final Logger LOGGER = new LogbackLogger(ZxingGeneratorTest.class);

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);

        FF4jProcessor feature = new FF4jProcessor();
        boolean toggle = feature.deactivateUnitTests(QrCodeGenerator.FEATURE_ID);
        LOGGER.log("PERFORM TESTS :: FeatureToggle", LogLevel.TRACE);

        boolean check;
        String out;
        if (!toggle) {
            out = "skiped.";
            check = false;
        } else {
            out = "executed.";
            check = true;
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out, LogLevel.TRACE);
        Assumptions.assumeTrue(check);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED.\n", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(ZxingGenerator.class, hasValidBeanConstructor());
    }

    @Test
    void testQrCodeGenerator() {
        LOGGER.log("TEST CASE: qrCodeGenerator()", LogLevel.DEBUG);

        String out = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + "QrCode-0000.png";
        QrCodeGenerator generator = new ZxingGenerator();
        generator.setup(out, 100);
        generator.encode("Test CODE.");

        assertEquals("Test CODE.", generator.decode(new File(out)));
    }

    @Test
    void testFailEncode() {
        LOGGER.log("TEST CASE: failEncode()", LogLevel.DEBUG);

        QrCodeGenerator generator = new ZxingGenerator();
        generator.setup("/fail.png", 100);

        assertFalse(generator.encode(generator.generateDataForvCard(null)));
    }

    @Test
    void testFailDecode() {
        LOGGER.log("TEST CASE: failDecode()", LogLevel.DEBUG);

        QrCodeGenerator generator = new ZxingGenerator();
        assertNull(generator.decode(null));
    }

    @Test
    void testVCard() {
        LOGGER.log("TEST CASE: vCard()", LogLevel.DEBUG);

        Map<String, String> vCard = new HashMap<>();
        vCard.put("gender", "Mr.");
        vCard.put("name", "Doe");
        vCard.put("surname", "John");
        vCard.put("title", "MSc");
        vCard.put("organization", "org.europa");
        vCard.put("role", "Developer");
        vCard.put("home-street", "Poniente 15");
        vCard.put("home-city", "Oaxaca");
        vCard.put("home-zipcode", "12345");
        vCard.put("home-state", "Oaxaca");
        vCard.put("home-country", "Mexico");
        vCard.put("home-phone", "(55) 22365014");
        vCard.put("home-mobile", "1245 44548954");
        vCard.put("work-street", "Poniente 15");
        vCard.put("work-city", "Oaxaca");
        vCard.put("work-zipcode", "12345");
        vCard.put("work-state", "Oaxaca");
        vCard.put("work-country", "Mexico");
        vCard.put("work-phone", "(234) 12456457801");
        vCard.put("work-mobile", "145 78457 54");
        vCard.put("e-mail", "john.doe@sample.org");
        vCard.put("homepage", "http://www.sample.org");

        String out = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + "300_vCard.png";
        QrCodeGenerator generator = new ZxingGenerator();
        generator.setup(out, 300);
        assertTrue(generator.encode(generator.generateDataForvCard(vCard)));
    }

    @Test
    void testEmptyVCard() {
        LOGGER.log("TEST CASE: emptyVCard()", LogLevel.DEBUG);

        QrCodeGenerator generator = new ZxingGenerator();
        assertNull(generator.generateDataForvCard(new HashMap<>()));
        assertNull(generator.generateDataForvCard(null));
    }

    @Test
    void testGeoInfo() {
        LOGGER.log("TEST CASE: geoInfo()", LogLevel.DEBUG);

        String out = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + "100_geoInfo.png";
        QrCodeGenerator generator = new ZxingGenerator();
        generator.setup(out, 100);
        assertTrue(generator.encode(
                generator.generateDataForGeoLocation("40.71872", "-73.98905")));
    }

    @Test
    void testUrl() {
        LOGGER.log("TEST CASE: url()", LogLevel.DEBUG);

        String out = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + "100_url.png";
        QrCodeGenerator generator = new ZxingGenerator();
        generator.setup(out, 100);
        assertTrue(generator.encode(
                generator.generateDataForUrl("http://www.sample.org")));
    }

    @Test
    void testCaledar() {
        LOGGER.log("TEST CASE: caledar()", LogLevel.DEBUG);

        String out = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + "100_event.png";
        QrCodeGenerator generator = new ZxingGenerator();
        generator.setup(out, 100);
        assertTrue(generator.encode(
                generator.generateDataForCalenderEvent("Appointment",
                        new DateTime(2017, 1, 1, 0, 1, 0),
                        new DateTime(2017, 12, 31, 23, 59, 0))));
    }
}
