package org.europa.together.application;

import org.europa.together.business.JsonTools;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.JacksonObjectTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})

public class JacksonJsonToolsTest {

    private static final Logger LOGGER = new LogbackLogger(JacksonJsonToolsTest.class);

    @Autowired
    private JsonTools<JacksonObjectTest> jsonTools;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.\n", LogLevel.TRACE);
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
    void serializeObjectToJson() throws Exception {
        LOGGER.log("TEST CASE: serializeObjectToJson", LogLevel.DEBUG);

        JacksonObjectTest domainObject = new JacksonObjectTest();
        domainObject.setId("identifier");
        domainObject.setKey("key");
        domainObject.setValue(12345);
        domainObject.setActivation(true);

        String json = "{\"id\":\"identifier\",\"key\":\"key\",\"value\":12345,\"activation\":true}";
        assertEquals(json, jsonTools.serializeAsJsonObject(domainObject));
    }

    @Test
    void failSerializeObjectToJson() throws Exception {
        LOGGER.log("TEST CASE: failSerializeObjectToJson", LogLevel.DEBUG);

        assertNotNull(jsonTools.serializeAsJsonObject(null));
        assertNotNull(jsonTools.serializeAsJsonObject(new JacksonObjectTest()));
        LOGGER.log(new JacksonObjectTest().toString(), LogLevel.DEBUG);
    }

    @Test
    void deserializeJsonToObject() throws Exception {
        LOGGER.log("TEST CASE: deserializeJsonToObject", LogLevel.DEBUG);

        JacksonObjectTest domainObject = new JacksonObjectTest();
        domainObject.setId("identifier");
        domainObject.setKey("key");
        domainObject.setValue(12345);
        domainObject.setActivation(true);

        String json = "{\"id\":\"identifier\",\"key\":\"key\",\"value\":12345,\"activation\":true}";
        JacksonObjectTest deserializion = jsonTools.deserializeJsonAsObject(json, JacksonObjectTest.class);
        assertEquals("JacksonObjectTest{id=identifier, key=key, value=12345, activation=true}",
                deserializion.toString());
    }

    @Test
    void failDeserializeJsonToObject() throws Exception {
        LOGGER.log("TEST CASE: failDeserializeJsonToObject", LogLevel.DEBUG);

        String json = "{\"xx\":\"identifier01\",\"key\":\"key\",\"value\":'123',\"activation\":false}";
        assertThrows(Exception.class, () -> {
            jsonTools.deserializeJsonAsObject(json, JacksonObjectTest.class);
        });
    }

    @Test
    void deserializeJsonAsObjectList() throws Exception {
        LOGGER.log("TEST CASE: deserializeJsonAsObjectList", LogLevel.DEBUG);

        String json = "["
                + "{\"id\":\"identifier00\",\"key\":\"low.number\",\"value\":12345,\"activation\":true}, "
                + "{\"id\":\"identifier01\",\"key\":\"high.number\",\"value\":67890,\"activation\":false}, "
                + "{\"id\":\"identifier02\",\"key\":\"amount\",\"value\":100,\"activation\":false}, "
                + "{\"id\":\"identifier03\",\"key\":\"key\",\"value\":99099,\"activation\":true}"
                + "]";
        assertEquals(4, jsonTools.deserializeJsonAsList(json).size());
    }

    @Test
    void failDeserializeJsonAsObjectList() throws Exception {
        LOGGER.log("TEST CASE: failDeserializeJsonAsObjectList", LogLevel.DEBUG);

        String json = "{"
                + "{\"id\":\"identifier00\",\"key\":\"low.number\",\"value\":12345,\"activation\":true}"
                + "{\"id\":\"identifier01\",\"key\":\"high.number\",\"value\":67890,\"activation\":false}"
                + "}";
        assertThrows(Exception.class, () -> {
            jsonTools.deserializeJsonAsList(json);
        });
    }
}
