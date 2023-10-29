package org.europa.together.application;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.europa.together.business.Logger;
import org.europa.together.business.QrCodeGenerator;
import org.europa.together.domain.LogLevel;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the QR Code Generator.
 */
@Repository
public class ZxingGenerator implements QrCodeGenerator {

    private static final long serialVersionUID = 7L;
    private static final Logger LOGGER = new LogbackLogger(ZxingGenerator.class);

    private final String charset;
    private int dimensionHeight;
    private int dimensionWidth;
    private String fileOutputPath;
    private final Map<EncodeHintType, Object> errorMap = new HashMap<>();

    /**
     * Constructor.
     */
    public ZxingGenerator() {
        errorMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        charset = "UTF-8";
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public void setup(final String fileOutputPath, final int dimensions) {
        this.dimensionHeight = dimensions;
        this.dimensionWidth = dimensions;
        this.fileOutputPath = fileOutputPath;
    }

    @Override
    public String generateDataForvCard(final Map<String, String> contact) {
        String data = null;
        if (contact == null || contact.isEmpty()) {
            LOGGER.log("Contact information are empty.", LogLevel.WARN);
        } else {
            LocalDateTime now = LocalDateTime.now();
            String dateStr
                    = now.format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss"));
            data = "BEGIN:VCARD\n VERSION:3.0\n PROFILE:VCARD\n"
                    + "N:" + contact.get("name") + ";"
                    + contact.get("surname") + ";;" + contact.get("gender") + ";\n"
                    + "FN:" + contact.get("surname") + " " + contact.get("name") + "\n"
                    + "ORG:" + contact.get("organization") + "\n"
                    + "TITLE:" + contact.get("title") + "\n"
                    + "TEL;TYPE=WORK,CELL:" + contact.get("work-mobile") + "\n"
                    + "TEL;TYPE=WORK,VOICE:" + contact.get("work-phone") + "\n"
                    + "TEL;TYPE=HOME,CELL:" + contact.get("home-mobile") + "\n"
                    + "TEL;TYPE=HOME,VOICE:" + contact.get("home-phone") + "\n"
                    + "ADR;TYPE=WORK,PREF:;;" + contact.get("home-street") + ";"
                    + contact.get("home-city") + ";" + contact.get("home-state") + ";"
                    + contact.get("home-zipcode") + ";" + contact.get("home-country") + "\n"
                    + "LABEL;TYPE=WORK,PREF:" + contact.get("work-street") + "\\n"
                    + contact.get("work-city") + "\\, " + contact.get("work-state") + " "
                    + contact.get("work-zipcode") + "\\n " + contact.get("work-country") + "\n"
                    + "ADR;TYPE=WORK,PREF:;;" + contact.get("work-street") + ";"
                    + contact.get("work-city") + ";" + contact.get("work-state") + ";"
                    + contact.get("work-zipcode") + ";" + contact.get("work-country") + "\n"
                    + "ADR;TYPE=HOME:;;" + contact.get("home-street") + ";"
                    + contact.get("home-city") + ";" + contact.get("home-state") + ";"
                    + contact.get("home-zipcode") + ";" + contact.get("home-country") + "\n"
                    + "LABEL;TYPE=HOME:" + contact.get("home-street") + "\\n"
                    + contact.get("home-city") + "\\, " + contact.get("home-state") + " "
                    + contact.get("home-zipcode") + "\\n " + contact.get("home-country") + "\n"
                    + "URL:" + contact.get("homepage") + "\n"
                    + "EMAIL:" + contact.get("e-mail") + "\n"
                    + "REV:" + dateStr + "\nEND:VCARD";
        }
        return data;
    }

    @Override
    public String generateDataForCalendarEvent(final String event,
            final ZonedDateTime start, final ZonedDateTime end) {
        String data = "BEGIN:VEVENT\n"
                + "SUMMARY:" + event
                + "\n DTSTART:" + start.format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss"))
                + "\n DTEND: " + end.format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss"))
                + "\n END:VEVENT";
        return data;
    }

    @Override
    public String generateDataForUrl(final String url) {
        String data = url.replace("//", "");
        return "URLTO:" + data;
    }

    @Override
    public String generateDataForGeoLocation(final String latitude, final String longitude) {
        return "geo:" + latitude + "," + longitude + ",100";
    }

    @Override
    public boolean encode(final String data) {
        boolean success = false;
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(
                    new String(data.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE,
                    dimensionWidth,
                    dimensionHeight,
                    errorMap);
            MatrixToImageWriter.writeToPath(matrix, "png", Paths.get(fileOutputPath));
            success = true;
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return success;
    }

    @Override
    public String decode(final File qrCode) {
        String decode = null;
        try {
            BinaryBitmap binaryBitmap
                    = new BinaryBitmap(
                            new HybridBinarizer(
                                    new BufferedImageLuminanceSource(
                                            ImageIO.read(new FileInputStream(qrCode)))));
            decode = new MultiFormatReader().decode(binaryBitmap).getText();
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return decode;
    }
}
