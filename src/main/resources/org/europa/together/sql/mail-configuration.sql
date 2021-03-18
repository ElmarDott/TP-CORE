--- #### #######################################################################
--- ####                POPULATE CONFIGURATION TABLE
--- #### #######################################################################
---  Notes: CONF-KEY in table APP_CONFIG is SHA-256 protected

--- #### ACL
--- password.pattern
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('', '', '', '${}', 'conf', 'acl', '1.0', true, false, '');
-- logout.timeout
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('', '', '', '${}', 'conf', 'acl', '1.0', true, false, '');
-- pepperized
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('', '', '', '${}', 'conf', 'acl', '1.0', true, false, '');
--
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('', '', '', '${}', 'conf', 'acl', '1.0', true, false, '');
