package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
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
        ConfigurationDO config = configurationDAO.getConfigurationByKey("key", "Module_A", "2.0");
        assertEquals("Y.1", config.getValue());
    }

    @Test
    public void testGetValueByKey() {
        String value = configurationDAO.getValueByKey("key", "Module_A", "2.0.1");
        assertEquals("Y.1", value);
    }
}
