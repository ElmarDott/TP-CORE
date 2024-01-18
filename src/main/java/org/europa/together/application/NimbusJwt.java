package org.europa.together.application;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Date;
import java.util.List;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.JsonWebToken;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.DAOException;
import org.europa.together.exceptions.JsonProcessingException;
import org.europa.together.utils.Constraints;
import org.europa.together.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ed
 */
@Repository
@Transactional
public class NimbusJwt implements JsonWebToken {

    private static final long serialVersionUID = 16L;
    private static final Logger LOGGER = new LogbackLogger(NimbusJwt.class);

    private String sharedSecret = "";

    @Autowired
    private ConfigurationDAO configurationDAO;

    public NimbusJwt() throws DAOException {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public String buildHMAC512SignedJws(final String jsonPayload)
            throws JsonProcessingException {
        String jws = "";
        try {
            fetchSharedSecret();
            if (!validateLengthOfSharedSecret(sharedSecret)) {
                String msg = "The length of the secret is less than 128 characters.";
                throw new JsonProcessingException(msg);
            }

            JWSSigner signer = new MACSigner(sharedSecret);
            JWSHeader jswHeader = new JWSHeader(JWSAlgorithm.HS512);

            Payload payload = new Payload(jsonPayload);

            JWSObject jwsObject = new JWSObject(jswHeader, payload);
            jwsObject.sign(signer);

            jws = jwsObject.serialize();
        } catch (Exception ex) {
            throw new JsonProcessingException(ex.getClass().getTypeName()
                    + " " + ex.getMessage());
        }
        return jws;
    }

    @Override
    public String buildHMAC512SignedJwt(final String issuer, final String subject,
            final List<String> audience)
            throws JsonProcessingException {
        String jwt = "";
        try {
            fetchSharedSecret();
            if (!validateLengthOfSharedSecret(sharedSecret)) {
                String msg = "The length of the secret is less than 128 characters.";
                throw new JsonProcessingException(msg);
            }

            JWSSigner signer = new MACSigner(sharedSecret);
            JWSHeader jswHeader = new JWSHeader(JWSAlgorithm.HS512);

            Date currentTime = new Date();
            //TODO: make json.expiration.time configurable: 1000 * 60 * 10 := 10 minutes
            JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder()
                    .issuer(issuer)
                    .subject(subject)
                    .audience(audience)
                    .expirationTime(new Date(new Date().getTime() + 600000))
                    .notBeforeTime(currentTime)
                    .issueTime(currentTime)
                    .jwtID(StringUtils.generateUUID())
                    .build();

            SignedJWT signedJWT = new SignedJWT(jswHeader, jwtClaims);
            signedJWT.sign(signer);
            jwt = signedJWT.serialize();
        } catch (Exception ex) {
            throw new JsonProcessingException(ex.getClass().getTypeName()
                    + " " + ex.getMessage());
        }
        return jwt;
    }

    @Override
    public String parseHMAC512SingedJws(final String jws)
            throws JsonProcessingException {
        String payload = "";
        try {
            fetchSharedSecret();

            JWSObject jwsObject = JWSObject.parse(jws);
            JWSVerifier verifier = new MACVerifier(sharedSecret);
            payload = jwsObject.getPayload().toString();

            if (!jwsObject.verify(verifier)) {
                LOGGER.log("JWS is not valid.", LogLevel.ERROR);
                LOGGER.log("\t payload: " + payload, LogLevel.ERROR);
                throw new Exception("JWS is not valid. - for security reasons rejected!");
            }

        } catch (Exception ex) {
            throw new JsonProcessingException(ex.getClass().getTypeName()
                    + " " + ex.getMessage());
        }
        return payload;
    }

    @Override
    public String parseHMAC512SingedJwt(final String jwt)
            throws JsonProcessingException {
        String payload = "";
        try {
            fetchSharedSecret();

            SignedJWT signedJWT = SignedJWT.parse(jwt);
            JWSVerifier verifier = new MACVerifier(sharedSecret);
            payload = signedJWT.getPayload().toString();

            if (!signedJWT.verify(verifier)) {
                LOGGER.log("JWT is not valid.", LogLevel.ERROR);
                LOGGER.log("\t payload: " + payload, LogLevel.ERROR);
                throw new Exception("JWT is not valid. - for security reasons rejected!");
            }

        } catch (Exception ex) {
            throw new JsonProcessingException(ex.getClass().getTypeName()
                    + " " + ex.getMessage());
        }
        return payload;
    }

    //--------------------------------------------------------------------------
    private boolean validateLengthOfSharedSecret(final String secret) {
        boolean success = false;
        // 512 bits -> 64 byte = 128 chars
        if (secret.length() >= Constraints.INT_128) {
            success = true;
        }
        return success;
    }

    private void fetchSharedSecret() throws DAOException {
        List<ConfigurationDO> configuration = configurationDAO
                .getAllConfigurationSetEntries("core", VERSION, "json");
        this.sharedSecret = configuration.get(0).getValue();

        LOGGER.log("SEC: " + configuration.toString().substring(0, 30), LogLevel.DEBUG);
    }

}
