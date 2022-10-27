package org.europa.together.application;

import org.europa.together.business.DatabaseActions;
import org.europa.together.business.FeatureFlags;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.LogLevel;
import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
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
public class FeatureFlagsFF4jTest {

    private static final Logger LOGGER = new LogbackLogger(FeatureFlagsFF4jTest.class);

    private static final String FLUSH_TABLE
            = "TRUNCATE TABLE " + ConfigurationDO.TABLE_NAME + ";";
    private static final String FILE
            = "org/europa/together/sql/ff-configuration.sql";
    private static final String PROPERTIES
            = "org/europa/together/configuration/jdbc.properties";

    @Autowired
    private FeatureFlags featureFlags;

    private static DatabaseActions jdbcActions = new JdbcActions();
    private Feature feature;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(jdbcActions.connect("test"));

        jdbcActions.executeSqlFromClasspath(FILE);
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() throws Exception {
        jdbcActions.executeQuery(FLUSH_TABLE);
        LOGGER.log("### TEST SUITE TERMINATED.\n", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() throws Exception {
        jdbcActions.executeSqlFromClasspath("schema-drop.sql");
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    public FeatureFlagsFF4jTest() {
        feature = new Feature("TEST");
        feature.setDescription("Temporary test feature.");
        feature.setGroup("none");
        feature.setEnable(true);
    }

    @Test
    void failConnectDatabase() throws Exception {
        LOGGER.log("TEST CASE: failConnectDatabase", LogLevel.DEBUG);

        jdbcActions.executeQuery(FLUSH_TABLE);
        assertThrows(Exception.class, () -> {
            featureFlags.getFeatureStore(PROPERTIES);
        });
        jdbcActions.executeSqlFromClasspath(FILE);
    }

    @Test
    void deactivatedFeatureStore() throws Exception {
        LOGGER.log("TEST CASE: deactivatedFeatureStore", LogLevel.DEBUG);

        //deactivate feature store in DB
        jdbcActions.executeQuery("UPDATE " + ConfigurationDO.TABLE_NAME
                + " SET CONF_VALUE = false WHERE IDX = '776e0858-dac5-47c5-95f3-ca79234dc129';");
        assertThrows(Exception.class, () -> {
            featureFlags.getFeatureStore(PROPERTIES);
        });
        //activate feature store in DB
        jdbcActions.executeQuery("UPDATE " + ConfigurationDO.TABLE_NAME
                + " SET CONF_VALUE = true WHERE IDX = '776e0858-dac5-47c5-95f3-ca79234dc129';");
    }

    @Test
    void activateFeatureFlags() throws Exception {
        LOGGER.log("TEST CASE: activateFeatureFlags", LogLevel.DEBUG);

        FF4j ff4j = featureFlags.getFeatureStore(PROPERTIES);
        ff4j.createSchema();

        assertTrue(ff4j.isEnableAudit());
        assertFalse(ff4j.isAutocreate());
    }

    @Test
    void addFeature() throws Exception {
        LOGGER.log("TEST CASE: addFeature", LogLevel.DEBUG);

        featureFlags.getFeatureStore(PROPERTIES).createSchema();
        featureFlags.addFeature(this.feature);

        assertNotNull(featureFlags.getFeature("TEST"));
    }

    @Test
    void updateFeature() throws Exception {
        LOGGER.log("TEST CASE: updateFeature", LogLevel.DEBUG);

        featureFlags.getFeatureStore(PROPERTIES).createSchema();
        featureFlags.addFeature(this.feature);

        Feature changedFeature = featureFlags.getFeature("TEST");
        changedFeature.setGroup("changes");

        featureFlags.updateFeature(changedFeature);
        assertNotEquals(feature, featureFlags.getFeature("TEST"));
        assertEquals(1, featureFlags.listAllFeatures().size());
    }

    @Test
    void updateFeatureNotExist() throws Exception {
        LOGGER.log("TEST CASE: updateFeatureNotExist", LogLevel.DEBUG);

        featureFlags.getFeatureStore(PROPERTIES).createSchema();
        featureFlags.addFeature(this.feature);

        Feature changedFeature = featureFlags.getFeature("TEST");
        changedFeature.setUid("UPDATE");

        featureFlags.updateFeature(changedFeature);
        assertEquals(2, featureFlags.listAllFeatures().size());
    }

    @Test
    void deleteFeature() throws Exception {
        LOGGER.log("TEST CASE: deleteFeature", LogLevel.DEBUG);

        featureFlags.getFeatureStore(PROPERTIES).createSchema();
        featureFlags.addFeature(this.feature);

        assertNotNull(featureFlags.getFeature("TEST"));

        featureFlags.removeFeature("TEST");
        assertEquals(0, featureFlags.listAllFeatures().size());
    }

    @Test
    void activateDeactivateFeature() throws Exception {
        LOGGER.log("TEST CASE: activateDeactivateFeature", LogLevel.DEBUG);

        featureFlags.getFeatureStore(PROPERTIES).createSchema();
        featureFlags.addFeature(this.feature);

        featureFlags.deactivateFeature("TEST");
        assertFalse(featureFlags.check("TEST"));

        featureFlags.activateFeature("TEST");
        assertTrue(featureFlags.check("TEST"));
    }
}
