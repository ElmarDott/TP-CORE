package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import java.util.ArrayList;
import java.util.List;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.DatabaseActions;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.SocketTimeout;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao-test.xml"})
public class ConfigurationDAOImplTest {

    private static final Logger LOGGER = new LoggerImpl(ConfigurationDAOImplTest.class);

    @Autowired
    @Qualifier("configurationDAOImpl")
    private ConfigurationDAO configurationDAO;

    private ConfigurationDO configDO;

    private static final String flush_table = "TRUNCATE TABLE app_config;";
    private static final String file = "org/europa/together/sql/configuration-test.sql";
    private static DatabaseActions actions = new DatabaseActionsImpl(true);

    public ConfigurationDAOImplTest() {
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
        actions.connect("default");
        boolean check = SocketTimeout.timeout(2000, actions.getUri(), actions.getPort());
        LOGGER.log("PERFORM TESTS :: Check DBMS availability -> " + check, LogLevel.TRACE);
        String out;
        if (check) {
            out = "executed.";

        } else {
            out = "skiped.";
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out, LogLevel.TRACE);
        Assumptions.assumeTrue(check);

        actions.executeSqlFromClasspath(file);
    }

    @AfterAll
    static void tearDown() {
        actions.executeQuery(flush_table);
        LOGGER.log("TEST SUITE TERMINATED.", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        assertThat(ConfigurationDAOImpl.class, hasValidBeanConstructor());
    }

    @Test
    void testListAll() {
        List<ConfigurationDO> result = configurationDAO.listAllElements();
        assertEquals(14, result.size());
    }

    @Test
    void testFlushTable() {
        configurationDAO.flushTable(ConfigurationDO.TABLE_NAME);
        assertEquals(0, configurationDAO.countEntries(ConfigurationDO.TABLE_NAME));

        actions.executeSqlFromClasspath(file);
        assertEquals(14, configurationDAO.countEntries(ConfigurationDO.TABLE_NAME));
    }

    @Test
    void testGetPrimaryKeyOfObject() {
        assertEquals("QWERTZ", configurationDAO.getPrimaryKeyOfObject(configDO));
        assertNull(configurationDAO.getPrimaryKeyOfObject(null));
    }

    @Test
    void testCountEntries() {
        assertEquals(14, configurationDAO.countEntries(ConfigurationDO.TABLE_NAME));
    }

    @Test
    void testFind() {
        ConfigurationDO config = configurationDAO.find("88888888-4444-4444-4444-cccccccccc");
        assertEquals("key", config.getKey());

        assertNull(configurationDAO.find("DumDiDum"));
    }

    @Test
    void testCreate() {
        configurationDAO.create(configDO);
        assertEquals(configDO, configurationDAO.find("QWERTZ"));
    }

    @Test
    void testFailCreate() {
        assertThrows(Exception.class, () -> {
            configurationDAO.create(null);
        });
    }

    @Test
    void testUpdate() {
        configDO.setValue("changed value");
        configurationDAO.update("QWERTZ", configDO);
        ConfigurationDO config = configurationDAO.find("QWERTZ");
        assertEquals("changed value", configDO.getValue());

        assertFalse(configurationDAO.update("QWERTZ", null));
        assertFalse(configurationDAO.update("DumDiDumm", configDO));
    }

    @Test
    void testDelete() {
        configurationDAO.delete("QWERTZ");
        assertNull(configurationDAO.find("QWERTZ"));

        assertFalse(configurationDAO.delete("DumDiDum"));
    }

    @Test
    void testFailDelete() {
        assertThrows(Exception.class, () -> {
            configurationDAO.delete(null);
        });
    }

    @Test
    void testSerilizeAsJson() {
        String json
                = "{\"class\":\"org.europa.together.domain.ConfigurationDO\",\"comment\":null,\"configurationSet\":\"confSet\",\"defaultValue\":\"DEFAULT\",\"depecated\":false,\"key\":\"key\",\"mandatory\":false,\"modulName\":\"MOD\",\"uuid\":\"QWERTZ\",\"value\":\"no value\",\"version\":\"1.0\"}";
        assertEquals(json, configurationDAO.serializeAsJson(configDO));

        assertNull(configurationDAO.serializeAsJson(null));
    }

    @Test
    void testDeserializeJson() {
        String json
                = "{\"class\":\"org.europa.together.domain.ConfigurationDO\",\"comment\":null,\"configurationSet\":\"confSet\",\"defaultValue\":\"DEFAULT\",\"depecated\":false,\"key\":\"key\",\"mandatory\":false,\"modulName\":\"MOD\",\"uuid\":\"QWERTZ\",\"value\":\"no value\",\"version\":\"1.0\"}";
        ConfigurationDO deserialize = configurationDAO.deserializeJsonAsObject(json);
        assertEquals(configDO, deserialize);
    }

    @Test
    void testGetConfigurationByKey() {
        ConfigurationDO config = configurationDAO.getConfigurationByKey("key", "Module_A", "2.0.1");
        assertEquals("Y.1", config.getValue());
    }

    @Test
    void testGetConfigurationByKeyNoExist() {
        assertNull(configurationDAO.getConfigurationByKey("noope", "Module", "1"));
    }

    @Test
    void testGetValueByKey() {
        String value = configurationDAO.getValueByKey("key", "Module_A", "2.0.1");
        assertEquals("Y.1", value);
    }

    @Test
    void testGetValueByKeyNoExist() {
        assertNull(configurationDAO.getValueByKey("key", "Module", "1"));
    }

    @Test
    void testGetValueByKeyFallbackToDefault() {
        String value = configurationDAO.getValueByKey("key", "Module", "1.0.1");
        assertEquals("default enry", value);
    }

    @Test
    void testRestoreKeyToDefault() {
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
        List<ConfigurationDO> set = configurationDAO.getAllDepecatedEntries();
        assertEquals(2, set.size());

        if (set.get(0).getUuid().equals("1de21a70-591b-4af8-8706-fc5581e90b0a")) {
            assertEquals("1de21a70-591b-4af8-8706-fc5581e90b0a", set.get(0).getUuid());
            assertEquals("6ff62a22-9820-406d-b55a-a86fa1c5a033", set.get(1).getUuid());
        } else {
            assertEquals("1de21a70-591b-4af8-8706-fc5581e90b0a", set.get(1).getUuid());
            assertEquals("6ff62a22-9820-406d-b55a-a86fa1c5a033", set.get(0).getUuid());
        }
    }

    @Test
    void testGetAllModuleEntries() {
        List<ConfigurationDO> set = configurationDAO.getAllModuleEntries("Module_A");
        assertEquals(10, set.size());
    }
}
