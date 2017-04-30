--- #### #######################################################################
--- ####                POPULATE CONFIGURATION TABLE
--- #### #######################################################################
--  Notes: CONF-KEY in table APP_CONFIG is SHA-256 protected

-- #### E-MAIL
-- mailer.host
INSERT INTO APP_CONFIG (CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, DEPECATED, COMMENT)
VALUES ('630fa1d4617b3f19bb631e93befc5462111f7e6ee927300b6778638cfcbdfae9', '', '', 'email', 'core', '1.0', false, '');
-- mailer.port
INSERT INTO APP_CONFIG (CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, DEPECATED, COMMENT)
VALUES ('fca1f5a28199bae3e126181012f8e901f6746ff7c5204fc96c7a6a74698ad287', '', '', 'email', 'core', '1.0', false, '');
-- mailer.sender
INSERT INTO APP_CONFIG (CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, DEPECATED, COMMENT)
VALUES ('dfb5634b02902fbfcf7bbed2dea5eec99a3a6b7cb5f44bd483e4916ae1fc9dfe', '', 'noreply@sample.org', 'email', 'core', '1.0', false, '');
-- mailer.user
INSERT INTO APP_CONFIG (CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, DEPECATED, COMMENT)
VALUES ('b5255cb2a1425fa96e1b64182b58fa5b3b2d46c30462de64d7dc6465b02875c8', '', 'JohnDoe', 'email', 'core', '1.0', false, '');
-- mailer.passwort
INSERT INTO APP_CONFIG (CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, DEPECATED, COMMENT)
VALUES ('2be6dca3046a6fecf7acbffe3ace6e043b741398ded3f28349fc2cce52f25fd8', '', 'none', 'email', 'core', '1.0', false, '');
-- mailer.ssl
INSERT INTO APP_CONFIG (CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, DEPECATED, COMMENT)
VALUES ('8e49cc844a943733ad9bbf0dca5f2cb76f2cd1555b0f714ed57447c9baa265c3', '', 'false', 'email', 'core', '1.0', false, '');
-- mailer.tls
INSERT INTO APP_CONFIG (CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, DEPECATED, COMMENT)
VALUES ('def03e28ed295f97bf1d9121e6590de282c96bf99303177f4b123043c2bcc429', '', 'false', 'email', 'core', '1.0', false, '');
-- mailer.debug
INSERT INTO APP_CONFIG (CONF_KEY, CONF_VALUE, DEFAULT_VALUE,  CONF_SET,MODUL_NAME, MODUL_VERSION, DEPECATED, COMMENT)
VALUES ('2430c986c0f7a145cd0df711f316b027bca71024e963d9c6c0be792ec32813b9', '', 'false', 'email', 'core', '1.0', false, '');
-- mailer.count
INSERT INTO APP_CONFIG (CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, DEPECATED, COMMENT)
VALUES ('c14efd472af892f12baa67f772bbf13da061f12f9ba1eb77fa7acb2fd51d94fb', '', '1', 'email', 'core', '1.0', false, '');
-- mailer.wait
INSERT INTO APP_CONFIG (CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, DEPECATED, COMMENT)
VALUES ('b20c815d1670e07e0d31066fc9b12c53af97ee9bb34fc85bc8f5312686622f05', '', '0', 'email', 'core', '1.0', false, '');