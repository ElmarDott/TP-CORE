package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.util.ArrayList;
import java.util.List;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.LogLevel;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ConfigurationHbmDAOTest {

    private static final Logger LOGGER = new LogbackLogger(ConfigurationHbmDAOTest.class);
    private static DatabaseActions actions = new JdbcActions();
    private static final String FLUSH_TABLE = "TRUNCATE TABLE " + ConfigurationDO.TABLE_NAME + ";";
    private static final String FILE = "org/europa/together/sql/configuration-test.sql";

    @Autowired
    private ConfigurationDAO configurationDAO;
    private ConfigurationDO configDO;

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
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        boolean check = true;
        String out = "executed";
        FF4jProcessor feature = new FF4jProcessor();

        boolean toggle = feature.deactivateUnitTests(ConfigurationDAO.FEATURE_ID);
        boolean socket = actions.connect("default");
        if (!toggle || !socket) {
            out = "skiped.";
            check = false;
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out, LogLevel.TRACE);
        Assumptions.assumeTrue(check);

        actions.executeSqlFromClasspath(FILE);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.\n", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
        actions.executeSqlFromClasspath(FILE);
    }

    @AfterEach
    void testCaseTermination() {
        actions.executeQuery(FLUSH_TABLE);
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(ConfigurationHbmDAO.class, hasValidBeanConstructor());
    }

    @Test
    void testListAll() {
        LOGGER.log("TEST CASE: listAll()", LogLevel.DEBUG);

        List<ConfigurationDO> result = configurationDAO.listAllElements();
        assertEquals(14, result.size());
    }

    @Test
    void testGetPrimaryKeyOfObject() {
        LOGGER.log("TEST CASE: GetPrimaryKeyOfObject()", LogLevel.DEBUG);

        assertEquals("QWERTZ", configurationDAO.getPrimaryKeyOfObject(configDO));
        assertNull(configurationDAO.getPrimaryKeyOfObject(null));
    }

    @Test
    void testCountEntries() {
        LOGGER.log("TEST CASE: CountEntries()", LogLevel.DEBUG);

        assertEquals(14, configurationDAO.countEntries(ConfigurationDO.TABLE_NAME));
    }

    @Test
    void testFind() {
        LOGGER.log("TEST CASE: Find()", LogLevel.DEBUG);

        ConfigurationDO config = configurationDAO.find("88888888-4444-4444-4444-cccccccccc");
        assertEquals("key", config.getKey());

        assertNull(configurationDAO.find("DumDiDum"));
    }

    @Test
    void testCreate() {
        LOGGER.log("TEST CASE: Create()", LogLevel.DEBUG);

        configurationDAO.create(configDO);
        assertEquals(configDO, configurationDAO.find("QWERTZ"));
    }

    @Test
    void testFailCreate() throws IllegalArgumentException {
        LOGGER.log("TEST CASE: FailCreate()", LogLevel.DEBUG);

        assertFalse(configurationDAO.create(null));
    }

    @Test
    void testUpdate() {
        LOGGER.log("TEST CASE: Update()", LogLevel.DEBUG);

        configurationDAO.create(configDO);
        configDO.setValue("changed value");
        configurationDAO.update("QWERTZ", configDO);
        assertEquals("changed value", configurationDAO.find("QWERTZ").getValue());

        assertFalse(configurationDAO.update("QWERTZ", null));
        assertFalse(configurationDAO.update("DumDiDumm", configDO));
    }

    @Test
    void testDelete() {
        LOGGER.log("TEST CASE: Delete()", LogLevel.DEBUG);

        assertTrue(configurationDAO.create(configDO));
        assertNotNull(configurationDAO.find("QWERTZ"));
        assertTrue(configurationDAO.delete("QWERTZ"));
        assertNull(configurationDAO.find("QWERTZ"));
    }

    @Test
    void testFailDelete() throws Exception {
        LOGGER.log("TEST CASE: FailDelete()", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            configurationDAO.delete(null);
        });
    }

    @Test
    void testSerilizeAsJson() {
        LOGGER.log("TEST CASE: SerilizeAsJson()", LogLevel.DEBUG);

        String json
                = "{\"uuid\":\"QWERTZ\",\"key\":\"key\",\"value\":\"no value\",\"defaultValue\":\"DEFAULT\",\"modulName\":\"MOD\",\"version\":\"1.0\",\"configurationSet\":\"confSet\",\"depecated\":false,\"mandatory\":false,\"comment\":null}";
        assertEquals(json, configurationDAO.serializeAsJson(configDO));
    }

    @Test
    void testFailSerilizeAsJson() throws Exception {
        LOGGER.log("TEST CASE: FailSerilizeAsJson()", LogLevel.DEBUG);

        assertEquals("null", configurationDAO.serializeAsJson(null));
    }

    @Test
    void testDeserializeJson() {
        LOGGER.log("TEST CASE: DeserilizeAsJson()", LogLevel.DEBUG);

        String json
                = "{\"uuid\":\"QWERTZ\",\"key\":\"key\",\"value\":\"no value\",\"defaultValue\":\"DEFAULT\",\"modulName\":\"MOD\",\"version\":\"1.0\",\"configurationSet\":\"confSet\",\"depecated\":false,\"mandatory\":false,\"comment\":null}";
        ConfigurationDO deserialize = configurationDAO.deserializeJsonAsObject(json, ConfigurationDO.class);
        assertEquals(configDO, deserialize);
    }

    @Test
    void testFailDeserializeJson() {
        LOGGER.log("TEST CASE: FailDeserilizeAsJson()", LogLevel.DEBUG);

        String json
                = "{\"uuid\":\"QWERTZ\",\"key\":\"key\",\"value\":\"no value\",\"defaultValue\":\"DEFAULT\",\"modulName\":\"MOD\",\"version\":\"1.0\",\"configurationSet\":\"confSet\",\"depecated\":false,\"mandatory\":false,\"comment\":null}";
        ConfigurationDO deserialize = configurationDAO.deserializeJsonAsObject(json, null);
        assertNull(deserialize);
    }

    @Test
    void testGetConfigurationByKey() {
        LOGGER.log("TEST CASE: GetConfigurationByKey()", LogLevel.DEBUG);

        ConfigurationDO config = configurationDAO.getConfigurationByKey("key", "Module_A", "2.0.1");
        assertEquals("Y.1", config.getValue());
    }

    @Test
    void testGetConfigurationByKeyNoExist() throws Exception {
        LOGGER.log("TEST CASE: GetConfigurationByKeyNoExist()", LogLevel.DEBUG);

        assertThrows(Exception.class, () -> {
            configurationDAO.getConfigurationByKey("noope", "Module", "1");
        });
    }

    @Test
    void testGetValueByKey() {
        LOGGER.log("TEST CASE: GetValueByKey()", LogLevel.DEBUG);

        String value = configurationDAO.getValueByKey("key", "Module_A", "2.0.1");
        assertEquals("Y.1", value);
    }

    @Test
    void testGetValueByKeyNoExist() throws Exception {
        LOGGER.log("TEST CASE: GetValueByKeyNoExist()", LogLevel.DEBUG);
        assertThrows(Exception.class, () -> {
            configurationDAO.getValueByKey("key", "Module", "1");
        });
    }

    @Test
    void testGetValueByKeyFallbackToDefault() {
        LOGGER.log("TEST CASE: GetValueByKeyFallbackToDefault()", LogLevel.DEBUG);

        String value = configurationDAO.getValueByKey("key", "Module", "1.0.1");
        assertEquals("default entry", value);
    }

    @Test
    void testRestoreKeyToDefault() {
        LOGGER.log("TEST CASE: RestoreKeyToDefault()", LogLevel.DEBUG);

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
    void testGetAllSetEntries() {
        LOGGER.log("TEST CASE: GetAllSetEntries()", LogLevel.DEBUG);

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
    void testGetHistoryOfAEntry() {
        LOGGER.log("TEST CASE: GetHistoryOfAEntry()", LogLevel.DEBUG);

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
    void testUpdateConfigurationEntries() {
        LOGGER.log("TEST CASE: UpdateConfigurationEntries()", LogLevel.DEBUG);

        List<ConfigurationDO> set
                = configurationDAO.getAllConfigurationSetEntries("Module_B", "1.0", "Set_2");

        assertEquals(3, set.size());
        ConfigurationDO a = set.get(0);
        ConfigurationDO b = set.get(1);
        ConfigurationDO c = set.get(2);

        assertEquals("none", a.getValue());
        assertEquals("a", b.getValue());
        assertEquals("a", c.getValue());

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
    void testGetDeprecatedEntries() {
        LOGGER.log("TEST CASE: GetDeprecatedEntries()", LogLevel.DEBUG);

        List<ConfigurationDO> result = configurationDAO.getAllDepecatedEntries();
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
    void testGetAllModuleEntries() {
        LOGGER.log("TEST CASE: GetAllModuleEntries()", LogLevel.DEBUG);

        List<ConfigurationDO> set = configurationDAO.getAllModuleEntries("Module_A");
        assertEquals(10, set.size());
    }
}
