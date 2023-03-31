package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import org.europa.together.business.Logger;
import org.europa.together.business.QrCodeGenerator;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.Constraints;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ZxingGeneratorTest {

    private static final Logger LOGGER = new LogbackLogger(ZxingGeneratorTest.class);

    private static final String FILE_PATH
            = "org/europa/together/qr_codes";
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + FILE_PATH;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(true, "Assumtion failed.");
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

    @Test
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(ZxingGenerator.class, hasValidBeanConstructor());
    }

    @Test
    void qrCodeGenerator() {
        LOGGER.log("TEST CASE: qrCodeGenerator", LogLevel.DEBUG);

        String out = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + "QrCode-0000.png";
        QrCodeGenerator generator = new ZxingGenerator();
        generator.setup(out, 100);
        generator.encode("Test CODE.");

        assertEquals("Test CODE.", generator.decode(new File(out)));
    }

    @Test
    void failEncode() {
        LOGGER.log("TEST CASE: failEncode", LogLevel.DEBUG);

        QrCodeGenerator generator = new ZxingGenerator();
        generator.setup("/fail.png", 100);

        assertFalse(generator.encode(generator.generateDataForvCard(null)));
    }

    @Test
    void failDecode() {
        LOGGER.log("TEST CASE: failDecode", LogLevel.DEBUG);

        QrCodeGenerator generator = new ZxingGenerator();
        assertNull(generator.decode(null));
    }

    @Test
    void vCard() {
        LOGGER.log("TEST CASE: vCard", LogLevel.DEBUG);

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
    void emptyVCard() {
        LOGGER.log("TEST CASE: emptyVCard", LogLevel.DEBUG);

        QrCodeGenerator generator = new ZxingGenerator();
        assertNull(generator.generateDataForvCard(new HashMap<>()));
        assertNull(generator.generateDataForvCard(null));
    }

    @Test
    void geoInfo() {
        LOGGER.log("TEST CASE: geoInfo", LogLevel.DEBUG);

        String out = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + "100_geoInfo.png";
        QrCodeGenerator generator = new ZxingGenerator();
        generator.setup(out, 100);
        assertTrue(generator.encode(
                generator.generateDataForGeoLocation("40.71872", "-73.98905")));
    }

    @Test
    void url() {
        LOGGER.log("TEST CASE: url", LogLevel.DEBUG);

        String out = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + "100_url.png";
        QrCodeGenerator generator = new ZxingGenerator();
        generator.setup(out, 100);
        assertTrue(generator.encode(
                generator.generateDataForUrl("http://www.sample.org")));
    }

    @Test
    void calendar() {
        LOGGER.log("TEST CASE: calendar", LogLevel.DEBUG);

        String out = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + "100_event.png";
        QrCodeGenerator generator = new ZxingGenerator();
        generator.setup(out, 100);

        assertTrue(generator.encode(
                generator.generateDataForCalendarEvent("Appointment",
                        ZonedDateTime.of(2017, 1, 1, 1, 0, 0, 0, ZoneId.of("UTC")),
                        ZonedDateTime.of(2017, 12, 31, 23, 59, 0, 0, ZoneId.of("UTC"))
                )
        ));
    }
}
