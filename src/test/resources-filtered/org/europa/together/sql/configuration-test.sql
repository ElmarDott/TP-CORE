--- #### #######################################################################
--- ####                POPULATE CONFIGURATION TABLE
--- #### #######################################################################
---  Notes: CONF-KEY in table APP_CONFIG is SHA-256 protected

--- #### SIMPLE FIND
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('88888888-4444-4444-4444-cccccccccc', 'key', 'empty', 'X',
'none', 'Module_A', '1.0', false, false, 'confKey=key, defaultValue=X, MODULE A, no confSet, different versions for the same key');

--- #### SAMPLE for empty value fallback to default
--- key = 2c70e12b7a0646f92279f427c7b38e7334d8e5389cff167a1dc30e73f826b683
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('6a173a15-185f-6838-ab49-5de2c704d029', '2c70e12b7a0646f92279f427c7b38e7334d8e5389cff167a1dc30e73f826b683', '', 'default entry',
'none', 'Module', '1.0.1', false, false, 'Fallback sample for empty values to use default entry.');

--- #### SAMPLE for History
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('69173a15-185f-4338-ab49-5de2c704d029', '2c70e12b7a0646f92279f427c7b38e7334d8e5389cff167a1dc30e73f826b683', '', 'X',
'none', 'Module_A', '1.0', false, false, 'confKey=key, defaultValue=X, MODULE A, no confSet, different versions for the same key');

INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('b82ea5d2-f682-4309-b229-f6fe835bf69c', '2c70e12b7a0646f92279f427c7b38e7334d8e5389cff167a1dc30e73f826b683', '', 'X',
'none', 'Module_A', '1.1', false, false, 'confKey=key, defaultValue=X, MODULE A, no confSet, different versions for the same key');

INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('1e6c8151-831c-4eae-97fb-3c60846ba2a0', '2c70e12b7a0646f92279f427c7b38e7334d8e5389cff167a1dc30e73f826b683', '', 'X',
'none', 'Module_A', '1.2', false, false, 'confKey=key, defaultValue=X, MODULE A, no confSet, different versions for the same key');

INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('1a59f6fd-0300-40b1-ad41-5b77e3766b50', '2c70e12b7a0646f92279f427c7b38e7334d8e5389cff167a1dc30e73f826b683', '', 'X',
'none', 'Module_A', '1.3', false, false, 'confKey=key, defaultValue=X, MODULE A, no confSet, different versions for the same key');

INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('6ff62a22-9820-406d-b55a-a86fa1c5a033', '2c70e12b7a0646f92279f427c7b38e7334d8e5389cff167a1dc30e73f826b683', 'Y.1', 'Y',
'none', 'Module_A', '2.0', false, true, 'deprecated, confKey=key, change defaultValue to Y, MODULE A, no confSet, different versions for the same key');

INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('1de21a70-591b-4af8-8706-fc5581e90b0a', '2c70e12b7a0646f92279f427c7b38e7334d8e5389cff167a1dc30e73f826b683', 'Y.1', 'Y',
'none', 'Module_A', '2.0.1', false, true, 'deprecated, confKey=key, change defaultValue to Y, MODULE A, no confSet, different versions for the same key');

--- #### SAMPLE for confSet
INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('7127260b-ded6-4fab-a7d8-e09fd91bc2bc', '895ea3b4ec22bc68e4d0a6e9516c719cd3f2a074dd82c7176db93cc52f5dce2d', 'a', 'a',
'Set_1', 'Module_A', '1.0', false, false, 'confKey=key_01');

INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('2fedf7e2-ef5e-41da-a82f-eb22e40a02b7', '797d5c425993b47764b5ffceebe20f99b3dcd940c184ce6ecd9d45a238ebd840', 'a', 'b',
'Set_1', 'Module_A', '1.0', false, false, 'confKey=key_02');

INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('e2a14186-4d63-4596-b8f0-419ca095830f', '0bbc67bd24cb251078b48d1631ac2e7a2f5f9f5d0aa83982d1caab43dd271518', 'a', 'c',
'Set_1', 'Module_A', '1.0', true, false, 'confKey=key_03');

INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('7055ce70-06dc-45c1-ba27-f3d4afa42462', '4e5c0d3b2d75b706394084c7935a46c9868e67aee6f055f7d6a9fcbbc44615c8', 'a', 'd',
'Set_2', 'Module_B', '1.0', false, false, 'confKey=key_04');

INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('cf043982-3c32-48f3-9bd5-7e563fc5bd34', '9b98d2cc80cbf0b01fe2cc63bd56016d59300cda94697b805e810ca3438bce82', 'a', 'e',
'Set_2', 'Module_B', '1.0', false, false, 'confKey=key_05');

INSERT INTO APP_CONFIG (IDX, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME, SERVICE_VERSION, MANDATORY, DEPRECATED, COMMENT)
VALUES ('745a5706-a74f-481e-afc0-fe41e2a94563', '35358cbbba46ef2c94ec1287f1e3e5749d75393f74cd1810460cbba6af22a4cd', 'none', 'f',
'Set_2', 'Module_B', '1.0', false, false, 'confKey=key_06');
