package org.europa.together.application;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.europa.together.business.QrCodeGenerator;
import org.europa.together.utils.Constraints;
import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class QrCodeGeneratorImplTest {

    private static final String FILE_PATH
            = "org/europa/together/qr_codes";
    private static final String DIRECTORY
            = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + FILE_PATH;

    @Test
    public void testQrCodeGenerator() {

        String out = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + "QrCode-0000.png";
        QrCodeGenerator generator = new QrCodeGeneratorImpl();
        generator.setup(out, 100);
        generator.encode("Test CODE.");

        assertEquals("Test CODE.", generator.decode(new File(out)));
    }

    @Test
    public void testVCard() {

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
        QrCodeGenerator generator = new QrCodeGeneratorImpl();
        generator.setup(out, 300);
        assertTrue(generator.encode(generator.generateDataForvCard(vCard)));
    }

    @Test
    public void testGeoInfo() {
        String out = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + "100_geoInfo.png";
        QrCodeGenerator generator = new QrCodeGeneratorImpl();
        generator.setup(out, 100);
        assertTrue(generator.encode(
                generator.generateDataForGeoLocation("40.71872", "-73.98905")));
    }

    @Test
    public void testUrl() {
        String out = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + "100_url.png";
        QrCodeGenerator generator = new QrCodeGeneratorImpl();
        generator.setup(out, 100);
        assertTrue(generator.encode(
                generator.generateDataForUrl("http://www.sample.org")));
    }

    @Test
    public void testCaledar() {
        String out = Constraints.SYSTEM_APP_DIR + "/target/test-classes/" + "100_event.png";
        QrCodeGenerator generator = new QrCodeGeneratorImpl();
        generator.setup(out, 100);
        assertTrue(generator.encode(
                generator.generateDataForCalenderEvent("Appointment",
                        new DateTime(2017, 1, 1, 0, 1, 0),
                        new DateTime(2017, 12, 31, 23, 59, 0))));
    }
}
