package org.europa.together.business;

import java.util.List;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.exceptions.JsonProcessingException;
import org.springframework.stereotype.Component;

/**
 *
 * @author ed
 */
@API(status = STABLE, since = "3.1", consumers = "NimbusJwt")
@Component
public interface JsonWebToken {

    /**
     * Identifier for the given feature.
     */
    @API(status = STABLE, since = "3.1")
    String FEATURE_ID = "CM-16";

    @API(status = STABLE, since = "3.1")
    String VERSION = "1.0";

    /**
     * JSON Web Signature (JWS) represents content secured with digital
     * signatures or Message Authentication Codes (MACs) using JSON-based data
     * structures. Cryptographic algorithms and identifiers for use with this
     * specification are described in the separate JSON Web Algorithms (JWA)
     * specification and an IANA registry defined by that specification. Related
     * encryption capabilities are described in the separate JSON Web Encryption
     * (JWE) specification.
     *
     * @param payload as String
     * @return JWS as String
     * @throws JsonProcessingException
     */
    String buildHMAC512SignedJws(String payload)
            throws JsonProcessingException;

    /**
     * Create a JSON Web Token. JSON Web Token (JWT) is a compact, URL-safe
     * means of representing claims to be transferred between two parties. The
     * claims in a JWT are encoded as a JSON object that is used as the payload
     * of a JSON Web Signature (JWS) structure or as the plaintext of a JSON Web
     * Encryption (JWE) structure, enabling the claims to be digitally signed or
     * integrity protected with a Message Authentication Code (MAC) and/or
     * encrypted.
     *
     * @param issuer as String
     * @param subject as String
     * @param audience as List of Strings
     * @return JWT as String
     * @throws JsonProcessingException
     */
    String buildHMAC512SignedJwt(String issuer, String subject, List<String> audience)
            throws JsonProcessingException;

    String parseHMAC512SingedJws(String jws)
            throws JsonProcessingException;

    String parseHMAC512SingedJwt(String jwt)
            throws JsonProcessingException;
}
