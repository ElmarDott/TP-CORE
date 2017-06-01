package org.europa.together.application;

import java.io.File;
import org.europa.together.business.QrCodeGenerator;
import org.europa.together.utils.Constraints;
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

}
