package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import java.util.ArrayList;
import java.util.List;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.DatabaseActions;
import org.europa.together.domain.ConfigurationDO;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:org/europa/together/configuration/spring-dao-test.xml"})
public class ConfigurationDAOImplTest {

    @Autowired
    @Qualifier("configurationDAOImpl")
    private ConfigurationDAO configurationDAO;

    private ConfigurationDO configDO;

    private static String flush_table = "TRUNCATE TABLE app_config;";
    private static String file = "org/europa/together/sql/configuration-test.sql";
    private static DatabaseActions actions;

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

    @BeforeClass
    public static void setUp() {
        actions = new DatabaseActionsImpl(true);
        actions.connect(null);
        actions.executeSqlFromClasspath(file);
    }

    @AfterClass
    public static void tearDown() {
        actions.executeQuery(flush_table);
        actions.disconnect();
    }

    @Test
    public void testConstructor() {
        assertThat(ConfigurationDAOImpl.class, hasValidBeanConstructor());
    }

    @Test
    public void testListAll() {
        List<ConfigurationDO> result = configurationDAO.listAllElements();
        assertEquals(13, result.size());
    }

    @Test
    public void testFlushTable() {
        configurationDAO.flushTable(ConfigurationDO.TABLE_NAME);
        assertEquals(0, configurationDAO.countEntries(ConfigurationDO.TABLE_NAME));

        actions.executeSqlFromClasspath(file);
        assertEquals(13, configurationDAO.countEntries(ConfigurationDO.TABLE_NAME));
    }

    @Test
    public void testGetPrimaryKeyOfObject() {
        assertEquals("QWERTZ", configurationDAO.getPrimaryKeyOfObject(configDO));
        assertNull(configurationDAO.getPrimaryKeyOfObject(null));
    }

    @Test
    public void testCountEntries() {
        assertEquals(13, configurationDAO.countEntries(ConfigurationDO.TABLE_NAME));
    }

    @Test
    public void testFind() {
        ConfigurationDO config = configurationDAO.find("88888888-4444-4444-4444-cccccccccc");
        assertEquals("key", config.getKey());

        assertNull(configurationDAO.find("DumDiDum"));
    }

    @Test
    public void testCreate() {
        configurationDAO.create(configDO);
        assertEquals(configDO, configurationDAO.find("QWERTZ"));
    }

    @Test
    public void testUpdate() {
        configDO.setValue("changed value");
        configurationDAO.update("QWERTZ", configDO);
        ConfigurationDO config = configurationDAO.find("QWERTZ");
        assertEquals("changed value", configDO.getValue());

        assertFalse(configurationDAO.update("QWERTZ", null));
        assertFalse(configurationDAO.update("DumDiDumm", configDO));
    }

    @Test
    public void testDelete() {
        configurationDAO.delete("QWERTZ");
        assertNull(configurationDAO.find("QWERTZ"));

        assertFalse(configurationDAO.delete("DumDiDum"));
    }

    @Test
    public void testSerilizeAsJson() {
        String json
                = "{\"class\":\"org.europa.together.domain.ConfigurationDO\",\"comment\":null,\"configurationSet\":\"confSet\",\"defaultValue\":\"DEFAULT\",\"depecated\":false,\"key\":\"key\",\"modulName\":\"MOD\",\"uuid\":\"QWERTZ\",\"value\":\"no value\",\"version\":\"1.0\"}";
        assertEquals(json, configurationDAO.serializeAsJson(configDO));

        assertNull(configurationDAO.serializeAsJson(null));
    }

    @Test
    public void testDeserializeJson() {
        String json
                = "{\"class\":\"org.europa.together.domain.ConfigurationDO\",\"comment\":null,\"configurationSet\":\"confSet\",\"defaultValue\":\"DEFAULT\",\"depecated\":false,\"key\":\"key\",\"modulName\":\"MOD\",\"uuid\":\"QWERTZ\",\"value\":\"no value\",\"version\":\"1.0\"}";
        ConfigurationDO deserialize = configurationDAO.deserializeAsObject(json);
        assertEquals(configDO, deserialize);
    }

    @Test
    public void testGetConfigurationByKey() {
        ConfigurationDO config = configurationDAO.getConfigurationByKey("key", "Module_A", "2.0.1");
        assertEquals("Y.1", config.getValue());
    }

    @Test
    public void testGetValueByKey() {
        String value = configurationDAO.getValueByKey("key", "Module_A", "2.0.1");
        assertEquals("Y.1", value);
    }

    @Test
    public void testRestorKeyToDefault() {
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
    public void testGetAllSetEntries() {
        List<ConfigurationDO> set
                = configurationDAO.getAllConfigurationSetEntries("Module_A", "1.0", "Set_1");

        assertEquals(3, set.size());

        assertEquals("a", set.get(0).getDefaultValue());
        assertEquals("b", set.get(1).getDefaultValue());
        assertEquals("c", set.get(2).getDefaultValue());
    }

    @Test
    public void testGetHistoryOfAEntry() {
        List<ConfigurationDO> set
                = configurationDAO.getHistoryOfAEntry("Module_A", "key", "none");

        assertEquals(6, set.size());
        assertEquals("1.0", set.get(0).getVersion());
        assertEquals("1.1", set.get(1).getVersion());
        assertEquals("1.2", set.get(2).getVersion());
        assertEquals("1.3", set.get(3).getVersion());
        assertEquals("2.0.1", set.get(4).getVersion());
        assertEquals("2.0", set.get(5).getVersion());
    }

    @Test
    public void testUpdateConfigurationEntries() {
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
    public void testGetDeprecatedEntries() {
        List<ConfigurationDO> set = configurationDAO.getDeprecatedEntries();
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
    public void testGetDeprecatedModuleEntries() {
        List<ConfigurationDO> set = configurationDAO.getDeprecatedModuleEntries("Module_A");
        assertEquals(2, set.size());

        if (set.get(0).getUuid().equals("1de21a70-591b-4af8-8706-fc5581e90b0a")) {
            assertEquals("1de21a70-591b-4af8-8706-fc5581e90b0a", set.get(0).getUuid());
            assertEquals("6ff62a22-9820-406d-b55a-a86fa1c5a033", set.get(1).getUuid());
        } else {
            assertEquals("1de21a70-591b-4af8-8706-fc5581e90b0a", set.get(1).getUuid());
            assertEquals("6ff62a22-9820-406d-b55a-a86fa1c5a033", set.get(0).getUuid());
        }
    }
}
