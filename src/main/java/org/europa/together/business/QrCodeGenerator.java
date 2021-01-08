package org.europa.together.business;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.Map;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import static org.apiguardian.api.API.Status.DEPRECATED;
import org.springframework.stereotype.Component;

/**
 * Generates Quick Response Codes (QR Codes). The Generator provides
 * DataStructures like Calendar Entry or Contact Information (PIM). It's
 * implemented an encoder and also an decoder.
 *
 * @author elmar.dott@gmail.com
 * @version 1.2
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "ZxingGenerator")
@Component
public interface QrCodeGenerator {

    /**
     * Identifier for the given feature to enable toggles.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-0007";

    /**
     * Configure the Generator with the dimension (height and width) of the QR
     * Code Image. Height = Weight; the produced Image will be always an square.
     * It also define where the file will be stored.
     *
     * @param dimensions as int
     * @param fileOutputPath s String
     */
    @API(status = STABLE, since = "1.0")
    void setup(String fileOutputPath, int dimensions);

    /**
     * Encode from a given String the Image and store it on the configured path.
     *
     * @param data as String
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean encode(String data);

    /**
     * Extract (decode) from a Image file the Information as String.
     *
     * @param qrCode as File
     * @return the content as String
     */
    @API(status = STABLE, since = "1.0")
    String decode(File qrCode);

    /**
     * Create a vCard (Version 3.0) data structure.URL:
     * https://en.wikipedia.org/wiki/VCard<br><br>
     *
     * Property Set:<br>
     * gender: Mr.<br>
     * name: Doe<br>
     * surname: John<br>
     * title: <br>
     *
     * organization: <br>
     * role <br>
     *
     * home-street: <br>
     * home-city: <br>
     * home-zipcode: <br>
     * home-state: <br>
     * home-country: <br>
     * home-phone: <br>
     * home-mobile: <br>
     *
     * work-street: <br>
     * work-city: <br>
     * work-zipcode: <br>
     * work-state: <br>
     * work-country: <br>
     * work-phone: <br>
     * work-mobile: <br>
     *
     * e-mail: john.doe@sample.org<br>
     * homepage: <br>
     *
     *
     * @param contact as Map
     * @return dataStructure as String
     */
    @API(status = STABLE, since = "1.0")
    String generateDataForvCard(Map<String, String> contact);

    /**
     * Encode an calender event.
     *
     * @param event as String
     * @param start as ZonedDateTime
     * @param end as ZonedDateTime
     * @return dataStructure as String
     */
    @API(status = STABLE, since = "2.1")
    String generateDataForCalenderEvent(String event, ZonedDateTime start, ZonedDateTime end);

    /**
     * Encode an URL which will opened in a web browser.
     *
     * @param url as String
     * @return dataStructure as String
     */
    @API(status = STABLE, since = "1.0")
    String generateDataForUrl(String url);

    /**
     * A geo URI may be used to encode a point on the earth, including altitude.
     * For example, to encode the Google's New York office, which is at 40.71872
     * deg N latitude, 73.98905 deg W longitude, at a point 100 meters above the
     * office, one would encode "geo:40.71872,-73.98905,100".
     * <br>
     * A reader might open a local mapping application like Google Maps to this
     * location and zoom accordingly, or could open a link to this location on a
     * mapping web site like Google Maps in the device's web browser.
     *
     * @param longitude as String
     * @param latitude as String
     * @return dataStructure as String
     */
    @API(status = STABLE, since = "1.0")
    String generateDataForGeoLocation(String latitude, String longitude);
}
