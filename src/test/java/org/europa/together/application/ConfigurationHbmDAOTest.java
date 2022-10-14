package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.util.ArrayList;
import java.util.List;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.JpaPagination;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.JsonProcessingException;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ConfigurationHbmDAOTest {

    private static final Logger LOGGER = new LogbackLogger(ConfigurationHbmDAOTest.class);

    private static final String FLUSH_TABLE
            = "TRUNCATE TABLE " + ConfigurationDO.TABLE_NAME + ";";
    private static final String FILE
            = "org/europa/together/sql/configuration-test.sql";

    @Autowired
    private ConfigurationDAO configurationDAO;

    private ConfigurationDO configDO;
    private static DatabaseActions jdbcActions = new JdbcActions();

    public ConfigurationHbmDAOTest() {
        configDO = new ConfigurationDO();
        configDO.setUuid("QWERTZ");
        configDO.setKey("key");
        configDO.setValue("no value");
        configDO.setDefaultValue("DEFAULT");
        configDO.setConfigurationSet("confSet");
        configDO.setModulName("MOD");
        configDO.setVersion("1.0");
    }

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(jdbcActions.connect("test"));

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        jdbcActions.executeSqlFromClasspath(FILE);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.\n", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
        jdbcActions.executeSqlFromClasspath(FILE);
    }

    @AfterEach
    void testCaseTermination() throws Exception {
        jdbcActions.executeQuery(FLUSH_TABLE);
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(ConfigurationHbmDAO.class, hasValidBeanConstructor());
    }

    @Test
    void countEntries() {
        LOGGER.log("TEST CASE: countEntries", LogLevel.DEBUG);

        assertEquals(14, configurationDAO.countAllElements());
    }

    @Test
    void listAll() {
        LOGGER.log("TEST CASE: listAll", LogLevel.DEBUG);

        JpaPagination seekElement = new JpaPagination("uuid");
        seekElement.setPageSize(25);
        List<ConfigurationDO> result = configurationDAO.listAllElements(seekElement);
        assertEquals(14, result.size());
    }

    @Test
    void getPrimaryKeyOfObject() {
        LOGGER.log("TEST CASE: getPrimaryKeyOfObject", LogLevel.DEBUG);

        assertEquals("QWERTZ", configurationDAO.getPrimaryKeyOfObject(configDO));
    }

    @Test
    void failGetPrimaryKeyOfObject() {
        LOGGER.log("TEST CASE: failGetPrimaryKeyOfObject", LogLevel.DEBUG);

        assertNull(configurationDAO.getPrimaryKeyOfObject(null));
    }

    @Test
    void find() {
        LOGGER.log("TEST CASE: find", LogLevel.DEBUG);

        ConfigurationDO config = configurationDAO.find("88888888-4444-4444-4444-cccccccccc");
        assertEquals("key", config.getKey());
    }

    @Test
    void findNotExist() {
        LOGGER.log("TEST CASE: findNotExist", LogLevel.DEBUG);

        assertNull(configurationDAO.find("NotExist"));
    }

    @Test
    void failFind() throws Exception {
        LOGGER.log("TEST CASE: failFind", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            configurationDAO.find(null);
        });
    }

    @Test
    void create() throws Exception {
        LOGGER.log("TEST CASE: create", LogLevel.DEBUG);

        configurationDAO.create(configDO);
        assertEquals(configDO, configurationDAO.find("QWERTZ"));
    }

    @Test
    void failCreate() throws Exception {
        LOGGER.log("TEST CASE: failCreate", LogLevel.DEBUG);

        assertFalse(configurationDAO.create(null));
    }

    @Test
    void update() throws Exception {
        LOGGER.log("TEST CASE: update", LogLevel.DEBUG);

        assertTrue(configurationDAO.create(configDO));
        configDO.setValue("changed value");

        configurationDAO.update("QWERTZ", configDO);
        assertEquals("changed value", configurationDAO.find("QWERTZ").getValue());
    }

    @Test
    void updateNotExist() throws Exception {
        LOGGER.log("TEST CASE: updateNotExist", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            configurationDAO.update("NotExist", configDO);
        });
    }

    @Test
    void failUpdate() throws Exception {
        LOGGER.log("TEST CASE: failUpdate", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            configurationDAO.update(null, configDO);
        });
        assertThrows(Exception.class, () -> {
            configurationDAO.update("", null);
        });
        assertThrows(Exception.class, () -> {
            configurationDAO.update(null, null);
        });
    }

    @Test
    void delete() throws Exception {
        LOGGER.log("TEST CASE: delete", LogLevel.DEBUG);

        configurationDAO.create(configDO);
        assertNotNull(configurationDAO.find("QWERTZ"));

        configurationDAO.delete(configDO.getUuid());
        assertNull(configurationDAO.find(configDO.getUuid()));
    }

    @Test
    void deleteNotExist() throws Exception {
        LOGGER.log("TEST CASE: deleteNotExist", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            configurationDAO.delete("notExist");
        });
    }

    @Test
    void failDelete() throws Exception {
        LOGGER.log("TEST CASE: failDelete", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            configurationDAO.delete(null);
        });
    }

    @Test
    void serilizeJsonasObject() throws Exception {
        LOGGER.log("TEST CASE: serilizeAsJson", LogLevel.DEBUG);

        String json
                = "{\"uuid\":\"QWERTZ\",\"key\":\"key\",\"value\":\"no value\",\"defaultValue\":\"DEFAULT\",\"modulName\":\"MOD\",\"version\":\"1.0\",\"configurationSet\":\"confSet\",\"deprecated\":false,\"mandatory\":false,\"comment\":null}";
        assertEquals(json, configurationDAO.serializeAsJson(configDO));
    }

    @Test
    void deserializeJsonAsObject() throws Exception {
        LOGGER.log("TEST CASE: deserilizeJsonAsObject", LogLevel.DEBUG);

        String json
                = "{\"uuid\":\"QWERTZ\",\"key\":\"key\",\"value\":\"no value\",\"defaultValue\":\"DEFAULT\",\"modulName\":\"MOD\",\"version\":\"1.0\",\"configurationSet\":\"confSet\",\"deprecated\":false,\"mandatory\":false,\"comment\":null}";
        ConfigurationDO deserialize
                = configurationDAO.deserializeJsonAsObject(json, ConfigurationDO.class);
        assertEquals(configDO, deserialize);
    }

    @Test
    void deserializeJsonAsObjectList() throws Exception {
        LOGGER.log("TEST CASE: deserializeJsonAsObjectList", LogLevel.DEBUG);

        String json
                = "["
                + "{\"uuid\":\"QWERTZ\",\"key\":\"key\",\"value\":\"no value\",\"defaultValue\":\"DEFAULT\",\"modulName\":\"MOD\",\"version\":\"1.0\",\"configurationSet\":\"confSet\",\"deprecated\":false,\"mandatory\":false,\"comment\":null}"
                + "]";
        assertEquals(1, configurationDAO.deserializeJsonAsList(json).size());
    }

    @Test
    void getConfigurationByKey() {
        LOGGER.log("TEST CASE: getConfigurationByKey", LogLevel.DEBUG);

        ConfigurationDO config = configurationDAO.getConfigurationByKey("key", "Module_A", "2.0.1");
        assertEquals("Y.1", config.getValue());
    }

    @Test
    void getConfigurationByKeyNoExist() throws Exception {
        LOGGER.log("TEST CASE: getConfigurationByKeyNoExist", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            configurationDAO.getConfigurationByKey("noope", "Module", "1");
        });
    }

    @Test
    void getValueByKey() {
        LOGGER.log("TEST CASE: getValueByKey", LogLevel.DEBUG);

        String value = configurationDAO.getValueByKey("key", "Module_A", "2.0.1");
        assertEquals("Y.1", value);
    }

    @Test
    void getValueByKeyNoExist() throws Exception {
        LOGGER.log("TEST CASE: getValueByKeyNoExist", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            configurationDAO.getValueByKey("key", "Module", "1");
        });
    }

    @Test
    void getValueByKeyFallbackToDefault() {
        LOGGER.log("TEST CASE: getValueByKeyFallbackToDefault", LogLevel.DEBUG);

        String value = configurationDAO.getValueByKey("key", "Module", "1.0.1");
        assertEquals("default entry", value);
    }

    @Test
    void restoreKeyToDefault() throws Exception {
        LOGGER.log("TEST CASE: restoreKeyToDefault", LogLevel.DEBUG);

        ConfigurationDO before = configurationDAO.getConfigurationByKey("key", "Module_A", "2.0");
        assertEquals("6ff62a22-9820-406d-b55a-a86fa1c5a033", before.getUuid());
        assertEquals("Y.1", before.getValue());
        assertEquals("Y", before.getDefaultValue());

        configurationDAO.restoreKeyToDefault(before);

        ConfigurationDO after = configurationDAO.getConfigurationByKey("key", "Module_A", "2.0");
        assertEquals("6ff62a22-9820-406d-b55a-a86fa1c5a033", after.getUuid());
        assertEquals("Y", after.getValue());
        assertEquals("Y", after.getDefaultValue());
    }

    @Test
    void failRestoreKeyToDefault() throws Exception {
        LOGGER.log("TEST CASE: failRestoreKeyToDefault", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            ConfigurationDO entry = new ConfigurationDO();
            entry.setKey("NOT-EXIST");
            entry.setModulName("no-module");
            configurationDAO.restoreKeyToDefault(entry);
        });
    }

    @Test
    void getAllSetEntries() {
        LOGGER.log("TEST CASE: getAllSetEntries", LogLevel.DEBUG);

        List<ConfigurationDO> set
                = configurationDAO.getAllConfigurationSetEntries("Module_A", "1.0", "Set_1");

        assertEquals(3, set.size());

        for (ConfigurationDO entry : set) {
            if (entry.getUuid().equals("7127260b-ded6-4fab-a7d8-e09fd91bc2bc")) {
                assertEquals("a", entry.getDefaultValue());
            }
            if (entry.getUuid().equals("2fedf7e2-ef5e-41da-a82f-eb22e40a02b7")) {
                assertEquals("b", entry.getDefaultValue());
            }
            if (entry.getUuid().equals("e2a14186-4d63-4596-b8f0-419ca095830f")) {
                assertEquals("c", entry.getDefaultValue());
            }
        }
    }

    @Test
    void getHistoryOfAEntry() {
        LOGGER.log("TEST CASE: getHistoryOfAEntry", LogLevel.DEBUG);

        List<ConfigurationDO> set
                = configurationDAO.getHistoryOfAEntry("Module_A", "key", "none");

        assertEquals(6, set.size());
        for (ConfigurationDO entry : set) {
            if (entry.getUuid().equals("69173a15-185f-4338-ab49-5de2c704d029")) {
                assertEquals("1.0", entry.getVersion());
            }
            if (entry.getUuid().equals("b82ea5d2-f682-4309-b229-f6fe835bf69c")) {
                assertEquals("1.1", entry.getVersion());
            }
            if (entry.getUuid().equals("1e6c8151-831c-4eae-97fb-3c60846ba2a0")) {
                assertEquals("1.2", entry.getVersion());
            }
            if (entry.getUuid().equals("1a59f6fd-0300-40b1-ad41-5b77e3766b50")) {
                assertEquals("1.3", entry.getVersion());
            }
            if (entry.getUuid().equals("6ff62a22-9820-406d-b55a-a86fa1c5a033")) {
                assertEquals("2.0", entry.getVersion());
            }
            if (entry.getUuid().equals("1de21a70-591b-4af8-8706-fc5581e90b0a")) {
                assertEquals("2.0.1", entry.getVersion());
            }
        }
    }

    @Test
    void updateConfigurationEntries() throws Exception {
        LOGGER.log("TEST CASE: updateConfigurationEntries", LogLevel.DEBUG);

        List<ConfigurationDO> set
                = configurationDAO.getAllConfigurationSetEntries("Module_B", "1.0", "Set_2");

        LOGGER.log(set.get(0).toString(), LogLevel.DEBUG);
        LOGGER.log(set.get(1).toString(), LogLevel.DEBUG);
        LOGGER.log(set.get(2).toString(), LogLevel.DEBUG);

        assertEquals(3, set.size());
        ConfigurationDO a = set.get(0);
        ConfigurationDO b = set.get(1);
        ConfigurationDO c = set.get(2);

        assertEquals("d", a.getDefaultValue());
        assertEquals("f", b.getDefaultValue());
        assertEquals("e", c.getDefaultValue());

        a.setValue("a_01");
        b.setValue("a_02");
        c.setValue("a_03");

        List<ConfigurationDO> newSet = new ArrayList<>();
        newSet.add(a);
        newSet.add(b);
        newSet.add(c);

        configurationDAO.updateConfigurationEntries(newSet);

        assertEquals("a_01", configurationDAO.find(a.getUuid()).getValue());
        assertEquals("a_02", configurationDAO.find(b.getUuid()).getValue());
        assertEquals("a_03", configurationDAO.find(c.getUuid()).getValue());
    }

    @Test
    void failUpdateConfigurationEntries() throws Exception {
        LOGGER.log("TEST CASE: failUpdateConfigurationEntries", LogLevel.DEBUG);

        ConfigurationDO entry = new ConfigurationDO();
        entry.setKey("NOT-EXIST");
        entry.setModulName("no-module");

        List<ConfigurationDO> set = new ArrayList<>();
        set.add(entry);

        assertThrows(Exception.class, () -> {
            configurationDAO.updateConfigurationEntries(set);
        });
    }

    @Test
    void getDeprecatedEntries() {
        LOGGER.log("TEST CASE: getDeprecatedEntries", LogLevel.DEBUG);

        List<ConfigurationDO> result = configurationDAO.getAllDeprecatedEntries();
        assertEquals(2, result.size());

        if (result.get(0).getUuid().equals("1de21a70-591b-4af8-8706-fc5581e90b0a")) {
            assertEquals("1de21a70-591b-4af8-8706-fc5581e90b0a", result.get(0).getUuid());
            assertEquals("6ff62a22-9820-406d-b55a-a86fa1c5a033", result.get(1).getUuid());
        } else {
            assertEquals("1de21a70-591b-4af8-8706-fc5581e90b0a", result.get(1).getUuid());
            assertEquals("6ff62a22-9820-406d-b55a-a86fa1c5a033", result.get(0).getUuid());
        }
    }

    @Test
    void getAllModuleEntries() {
        LOGGER.log("TEST CASE: getAllModuleEntries", LogLevel.DEBUG);

        List<ConfigurationDO> set = configurationDAO.getAllModuleEntries("Module_A");
        assertEquals(10, set.size());
    }
}
