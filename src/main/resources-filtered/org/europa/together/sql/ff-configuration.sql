--- #### #######################################################################
--- ####                POPULATE CONFIGURATION TABLE
--- #### #######################################################################
---  Notes: CONF-KEY in table APP_CONFIG is SHA-256 protected

--- #### FEATURE FLAGS
--- ff.activation
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('776e0858-dac5-47c5-95f3-ca79234dc129', '2fea4c86a533fb1c557ae45431d94f0b41e010eec545fc12980ec2f675e8a035', 'true', 'false', 'features', 'core', '1.0', true, false, '');
--- ff.audit
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('7785e2b3-2621-459a-9361-01ef4d893182', '2adc20f26ec4555ec2922fd104e9aee76a22089e3c2d003b30f65db65e72d436', 'true', 'false', 'features', 'core', '1.0', true, false, '');
--- ff.autocreate
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('f03fff7b-fd2a-45bc-8cd7-999ddf9c2955', '5689e9c1cf9e82c617f45fbb19931ea6182967b71af5e1c26e117703dea87e7f', '', 'false', 'features', 'core', '1.0', true, false, '');
