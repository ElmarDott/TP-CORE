package org.europa.together.business;

import java.io.File;

/**
 * Generates Quick Response Codes (QR Codes) with the Zxing Library. The
 * Generator provides DataStructures like Calendar Entry or Contact Informations
 * (PIM). It's implemeted an encoder and also an decoder.
 *
 * @author elmar.dott@gmail.com
 */
public interface QrCodeGenerator {

    /**
     * Configure the Generator with the dimension (height and width) of the QR
     * Code Image. Height = Weight; the produced Image will be always an square.
     * It also define where the file will be stored.
     *
     * @param dimensions as int
     * @param fileOutputPath s String
     */
    void setup(String fileOutputPath, int dimensions);

    /**
     * Encode from a given String the Image and store it on the configured path.
     *
     * @param data as String
     * @return true on success
     */
    boolean encode(String data);

    /**
     * Extract (decode) from a Image file the Information as String.
     *
     * @param qrCode as File
     * @return the content as String
     */
    String decode(File qrCode);

    String generateDataForContactInformation();

    String generateDataForCalenderEvent();

    String generateDataForUrl();

    String generateDataForGeoLocation();

    String generteDataForCreditCardInformation();
}
