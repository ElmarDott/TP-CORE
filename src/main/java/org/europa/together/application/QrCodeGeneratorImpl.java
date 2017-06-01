package org.europa.together.application;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.europa.together.business.Logger;
import org.europa.together.business.QrCodeGenerator;
import org.europa.together.domain.LogLevel;

/**
 * Implementation of the QR Code Generator.
 */
public class QrCodeGeneratorImpl implements QrCodeGenerator {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = new LoggerImpl(PropertyReaderImpl.class);

    private final String charset;
    private int dimensionHeight;
    private int dimensionWidth;
    private String fileOutputPath;
    Map<EncodeHintType, Object> errorMap = new HashMap<>();

    /**
     * Constructor.
     */
    public QrCodeGeneratorImpl() {
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
    public String generateDataForContactInformation() {
        //ToDo: generateDataForContactInformation() implement me
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generateDataForCalenderEvent() {
        //ToDo: generateDataForCalenderEvent() implement me
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generateDataForUrl() {
        //ToDo: generateDataForUrl() implement me
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generateDataForGeoLocation() {
        //ToDo: generateDataForGeoLocation() implement me
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generteDataForCreditCardInformation() {
        //ToDo: generateDataForCreditCardInformation() implement me
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean encode(String data
    ) {
        boolean success = false;

        try {
            BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE,
                    dimensionWidth,
                    dimensionWidth,
                    errorMap);
            MatrixToImageWriter.writeToPath(matrix, "png", Paths.get(fileOutputPath));
            success = true;

        } catch (WriterException | IOException ex) {
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);
        }

        return success;
    }

    @Override
    public String decode(File qrCode
    ) {
        String decode = null;
        try {
            BinaryBitmap binaryBitmap
                    = new BinaryBitmap(
                            new HybridBinarizer(
                                    new BufferedImageLuminanceSource(
                                            ImageIO.read(new FileInputStream(qrCode)))));
            decode = new MultiFormatReader().decode(binaryBitmap).getText();

        } catch (NotFoundException | IOException ex) {
            LOGGER.log(ex.getMessage(), LogLevel.ERROR);
        }

        return decode;
    }
}
