# JsonWebToken

@**since**: 3.1 > @**api-version**: 1.0 > **Dependencies**:  Nimbus

Files:

* **API Interface** org.europa.together.business.[JsonWebToken](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/JsonWebToken)
* **Implementation** org.europa.together.application.[NimbusJwt](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/NimbusJwt.java)
* **SQL Import** org.europa.together.sql.[json-configuration.sql](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/resources-filtered/org/europa/together/sql/json-configuration.sql)

---

The JSON Web Token (JWT) implements also JSON Web Signing (JWS) by using the HMAC512 cryptography standard. This allows to secure the communication between REST Services. JWT is for verify that  no man is in between the sender and the reciver. JWS is hiding the content of a JSON Object on a non secure communication. A non secure communication is for example plain HTTP without TLS.

To allow an application wide signing there are a global database configuration. In the case the configuration has a need for a change, like a revoke of the secret it will applicate imediatly.

**Configuration**:

* **json.sharedSecret** a 128 character long random String (512 bit / 64 bytes)

**JSON Web Signature** (JWS) represents content secured with digital signatures or Message Authentication Codes (MACs) using JSON-based data structures. Cryptographic algorithms and identifiers for use with this specification are described in the separate JSON Web Algorithms (JWA) specification and an IANA registry defined by that specification. Related encryption capabilities are described in the separate JSON Web Encryption (JWE) specification.

**JSON Web Token** (JWT) is a compact, URL-safe means of representing claims to be transferred between two parties. The claims in a JWT are encoded as a JSON object that is used as the payload of a JSON Web Signature (JWS) structure or as the plaintext of a JSON Web Encryption (JWE) structure, enabling the claims to be digitally signed or integrity protected with a Message Authentication Code (MAC) and/or encrypted.

**Sample**:

```java
    @Autowired
    JsonWebToken jsonWebToken;

    //JSON Web Signing
    String jws = jsonWebToken.buildHMAC512SignedJws("{Hallo World JSON}");
    String jws.payload = jsonWebToken.parseHMAC512SingedJws(jws);

    //JSON Web Token
    String issuer = "https://togehter-platform.org";
    String subject = "togetherPlatform-API";
    List<String> audience = new ArrayList<>();
    audience.add("https://client.my-app.org");

    String jwt = jsonWebToken.buildHMAC512SignedJwt(issuer, subject, audience);
    String jwt.payload = jsonWebToken.parseHMAC512SingedJwt(jwt);
```

