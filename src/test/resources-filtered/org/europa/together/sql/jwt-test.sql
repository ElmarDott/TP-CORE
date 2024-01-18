--- #### #######################################################################
--- ####                POPULATE CONFIGURATION TABLE
--- #### #######################################################################
---  Notes: CONF-KEY in table APP_CONFIG is SHA-256 protected

CREATE TABLE IF NOT EXISTS APP_CONFIG (
    IDX char(255) PRIMARY KEY,
    CONF_KEY char(255),
    CONF_VALUE char(255),
    DEFAULT_VALUE char(255),
    CONF_SET char(255),
    MODUL_NAME char(255),
    SERVICE_VERSION char(255),
    MANDATORY boolean,
    DEPRECATED boolean,
    COMMENT char(255)
);

--- #### JSON Web Signature (JWS)
--- json.sharedSecret
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('78f580ed5-e83f-46fa-b903-003aa25eb4d1', 'f26fb22e6d3e89d267a33f59ff017033eb5263febe65f7e07e15c38f61a80e27', 'b6b9873ed374d93d1ba5cb1727f2dfdc2176661991ffd06f40ffc9d6afecd5230a7694d519957035692e39ee9a820d1ce02eb0b35ce45c1db4afea7cdaefcf0a', 'b6b9873ed374d93d1ba5cb1727f2dfdc2176661991ffd06f40ffc9d6afecd5230a7694d519957035692e39ee9a820d1ce02eb0b35ce45c1db4afea7cdaefcf0a', 'json', 'core', '1.0', true, false, 'JSON Web Signature');
