--- #### #######################################################################
--- ####                POPULATE CONFIGURATION TABLE
--- #### #######################################################################
---  Notes: CONF-KEY in table APP_CONFIG is SHA-256 protected

--- #### JSON Web Signature (JWS)
--- json.sharedSecret
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('78f580ed5-e83f-46fa-b903-003aa25eb4d1', 'f26fb22e6d3e89d267a33f59ff017033eb5263febe65f7e07e15c38f61a80e27', '0eb1261b-ddbb-4e4b-b039-81ec4d15791e', '', 'json', 'core', '1.0', true, false, 'JSON Web Signature');

--- json.issuer
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('78f580ed5-e83f-46fa-b903-003aa25eb4d1', 'b183e6bd8472a61700e09f01d42e0df6cbdf210552544683d26ae90e74ec3dc2', 'https://together-platform.org', 'https://together-platform.org', 'json', 'core', '1.0', true, false, 'JSON Web Signature');

--- json.subject
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('78f580ed5-e83f-46fa-b903-003aa25eb4d1', '80a250e43ae2917ccdb9dc26139c5013a2efb57b265041c459b2d94a16631100', 'tp-jws', 'tp-jws', 'json', 'core', '1.0', true, false, 'JSON Web Signature');
