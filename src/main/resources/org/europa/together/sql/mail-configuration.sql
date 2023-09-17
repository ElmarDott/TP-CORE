--- #### #######################################################################
--- ####                POPULATE CONFIGURATION TABLE
--- #### #######################################################################
---  Notes: CONF-KEY in table APP_CONFIG is SHA-256 protected

--- #### E-MAIL
--- mailer.host
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('8cbef5a9-81ee-496c-8486-4929f4a05749', '630fa1d4617b3f19bb631e93befc5462111f7e6ee927300b6778638cfcbdfae9', '', '${mailer.host}', 'email', 'core', '1.0', true, false, '');
-- mailer.port
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('01f05983-8f5c-4114-ad8a-7f4073c80f27', 'fca1f5a28199bae3e126181012f8e901f6746ff7c5204fc96c7a6a74698ad287', '', '${mailer.port}', 'email', 'core', '1.0', true, false, '');
-- mailer.sender
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('7d1f0e8e-81f4-4a23-a7d0-c1d96c42e3cb', 'dfb5634b02902fbfcf7bbed2dea5eec99a3a6b7cb5f44bd483e4916ae1fc9dfe', '', '${mailer.sender}', 'email', 'core', '1.0', true, false, '');
-- mailer.user
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('1a4f65f5-dc89-4b2f-8766-8092c812047f', 'b5255cb2a1425fa96e1b64182b58fa5b3b2d46c30462de64d7dc6465b02875c8', '', '${mailer.user}', 'email', 'core', '1.0', true, false, '');
-- mailer.password
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('dc73b07e-0174-4e60-920a-5607380b633b', '33e05ee4bd073156793d10793d980caf59ce8b016f0a6a8820b1d5ce18dc9b47', '', '${mailer.password}', 'email', 'core', '1.0', true, false, '');
-- mailer.ssl
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('3b7d8a78-30ec-4a53-9994-4e595acb7a26', '8e49cc844a943733ad9bbf0dca5f2cb76f2cd1555b0f714ed57447c9baa265c3', '', '${mailer.ssl}', 'email', 'core', '1.0', true, false, '');
-- mailer.tls
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('472746d8-2f2a-49dc-9b1a-a4f56dc8e2ba', 'def03e28ed295f97bf1d9121e6590de282c96bf99303177f4b123043c2bcc429', '', '${mailer.tls}', 'email', 'core', '1.0', true, false, '');
-- mailer.debug
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE,  CONF_SET,MODUL_NAME, MODUL_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('9b5cb2e2-65ea-4db6-b2d3-d343adcda29d', '2430c986c0f7a145cd0df711f316b027bca71024e963d9c6c0be792ec32813b9', '', '${mailer.debug}', 'email', 'core', '1.0', true, false, '');
-- mailer.count
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('e6892b88-aa8b-4900-b6fb-c35dc108add9', 'c14efd472af892f12baa67f772bbf13da061f12f9ba1eb77fa7acb2fd51d94fb', '', '${mailer.count}', 'email', 'core', '1.0', true, false, '');
-- mailer.wait
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, MODUL_VERSION, MANDATORY, DEPECATED, COMMENT)
VALUES ('3f4f8e09-f754-4285-a6d2-0bbce6354e4b', 'b20c815d1670e07e0d31066fc9b12c53af97ee9bb34fc85bc8f5312686622f05', '', '${mailer.wait}', 'email', 'core', '1.0', true, false, '');