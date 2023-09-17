package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.*;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class ConfigurationDOTest {

    private static final Logger LOGGER = new LoggerImpl(ConfigurationDOTest.class);

    private static ValidatorFactory validatorFactory;
    private static Validator validate;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        LOGGER.log("Assumption terminated. TestSuite will be executed.\n", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validate = validatorFactory.getValidator();
    }

    @AfterEach
    void testCaseTermination() {
        validatorFactory.close();
        LOGGER.log("TEST CASE TERMINATED.\n", LogLevel.TRACE);
    }
    //</editor-fold>

    private String key = "key";
    private String value = "value";
    private String defaultValue = "default";
    private String modulName = "module";
    private String configSet = "configuration";
    private String version = "1.0";
    private boolean depecated = true;
    private boolean mandatory = false;
    private String comment = "no comment";

    @Test
    void testPrePersist() {
        LOGGER.log("TEST CASE: prePersist()", LogLevel.DEBUG);

        ConfigurationDO domainObject = new ConfigurationDO();
        domainObject.prePersist();

        assertNotNull(domainObject);
        assertEquals("default", domainObject.getConfigurationSet());
        assertEquals("NIL", domainObject.getDefaultValue());
        assertEquals(false, domainObject.isDepecated());
        assertEquals(false, domainObject.isMandatory());
    }

    @Test
    void testDomainObject() {
        LOGGER.log("TEST CASE: domainObject()", LogLevel.DEBUG);

        assertThat(ConfigurationDO.class, hasValidBeanConstructor());
        assertThat(ConfigurationDO.class, hasValidBeanToString());
        assertThat(ConfigurationDO.class, hasValidBeanHashCodeFor("key", "modulName", "version"));
        assertThat(ConfigurationDO.class, hasValidBeanEqualsFor("key", "modulName", "version"));
    }

    @Test
    void testCreateDomainObjectBySetter() {
        LOGGER.log("TEST CASE: createDomainObjectBySetter()", LogLevel.DEBUG);

        ConfigurationDO domainObject = new ConfigurationDO();
        domainObject.setKey(key);
        domainObject.setModulName(modulName);
        domainObject.setVersion(version);
        domainObject.setConfigurationSet(configSet);
        domainObject.setDefaultValue(defaultValue);
        domainObject.setValue(value);

        assertNotNull(domainObject);
        assertEquals(0, validate.validate(domainObject).size());
        assertTrue(validate.validate(domainObject).isEmpty());
    }

    @Test
    void testCorrectBeanConstruction() {
        LOGGER.log("TEST CASE: correctBeanConstruction()", LogLevel.DEBUG);

        ConfigurationDO domainObject;

        domainObject = new ConfigurationDO(key, value, modulName, version);
        domainObject.prePersist();
        assertNotNull(domainObject);
        assertEquals(0, validate.validate(domainObject).size());
        assertTrue(validate.validate(domainObject).isEmpty());

        domainObject = new ConfigurationDO(key, null, modulName, version);
        domainObject.prePersist();
        assertNotNull(domainObject);
        assertEquals(0, validate.validate(domainObject).size());
        assertTrue(validate.validate(domainObject).isEmpty());
    }

    @Test
    void testIncorrectBeanConstruction() {
        LOGGER.log("TEST CASE: incorrectBeanConstruction()", LogLevel.DEBUG);

        ConfigurationDO domainObject;

        LOGGER.log("Create DomainObject with empty KEY", LogLevel.DEBUG);
        domainObject = new ConfigurationDO(null, value, modulName, version);
        domainObject.prePersist();
        assertNotNull(domainObject);
        assertEquals(1, validate.validate(domainObject).size());
        assertFalse(validate.validate(domainObject).isEmpty());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());

        LOGGER.log("Create DomainObject with empty MODULE_NAME", LogLevel.DEBUG);
        domainObject = new ConfigurationDO(key, value, null, version);
        domainObject.prePersist();
        assertNotNull(domainObject);
        assertEquals(1, validate.validate(domainObject).size());
        assertFalse(validate.validate(domainObject).isEmpty());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());

        LOGGER.log("Create DomainObject with empty VERSION", LogLevel.DEBUG);
        domainObject = new ConfigurationDO(key, value, modulName, null);
        domainObject.prePersist();
        assertNotNull(domainObject);
        assertEquals(1, validate.validate(domainObject).size());
        assertFalse(validate.validate(domainObject).isEmpty());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());

        LOGGER.log("Create DomainObject with empty CONF_SET", LogLevel.DEBUG);
        domainObject = new ConfigurationDO(key, value, modulName, version);
        domainObject.prePersist();
        domainObject.setConfigurationSet(null);

        assertNotNull(domainObject);
        assertEquals(null, domainObject.getConfigurationSet());
        assertEquals(1, validate.validate(domainObject).size());
        assertFalse(validate.validate(domainObject).isEmpty());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());

        LOGGER.log("Create DomainObject with empty DEFAULT_VALUE", LogLevel.DEBUG);
        domainObject = new ConfigurationDO(key, value, modulName, version);
        domainObject.prePersist();
        domainObject.setDefaultValue(null);

        assertNotNull(domainObject);
        assertEquals(null, domainObject.getDefaultValue());
        assertEquals(1, validate.validate(domainObject).size());
        assertFalse(validate.validate(domainObject).isEmpty());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    void testValidationEmptyComment() {
        LOGGER.log("TEST CASE: validationEmptyComment()", LogLevel.DEBUG);

        ConfigurationDO domainObject
                = new ConfigurationDO(key, value, modulName, version);
        domainObject.prePersist();
        assertNotNull(domainObject);
        assertEquals("", domainObject.getComment());
        assertEquals(0, validate.validate(domainObject).size());
        assertTrue(validate.validate(domainObject).isEmpty());
    }

    @Test
    void testValidationEmptyValue() {
        LOGGER.log("TEST CASE: validationEmptyValue()", LogLevel.DEBUG);

        ConfigurationDO domainObject
                = new ConfigurationDO(key, null, modulName, version);
        domainObject.prePersist();
        assertNotNull(domainObject);
        assertNull(domainObject.getValue());
        assertEquals(0, validate.validate(domainObject).size());
        assertTrue(validate.validate(domainObject).isEmpty());

        domainObject.setValue("");
        assertEquals("", domainObject.getValue());
    }
}
