package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
public class ConfigurationDOTest {

    private static final Logger LOGGER = new LogbackLogger(ConfigurationDOTest.class);

    private static Validator validate
            = Validation.buildDefaultValidatorFactory().getValidator();

    private String key = "key";
    private String value = "value";
    private String defaultValue = "default";
    private String modulName = "module";
    private String configSet = "configuration";
    private String version = "1.0";
    private boolean deprecated = true;
    private boolean mandatory = false;
    private String comment = "no comment";

    @Test
    void prePersist() {
        LOGGER.log("TEST CASE: prePersist", LogLevel.DEBUG);

        ConfigurationDO domainObject = new ConfigurationDO();
        domainObject.prePersist();

        assertNotNull(domainObject);
        assertEquals("default", domainObject.getConfigurationSet());
        assertEquals("NIL", domainObject.getDefaultValue());
        assertEquals(false, domainObject.isDeprecated());
        assertEquals(false, domainObject.isMandatory());
    }

    @Test
    void domainObject() {
        LOGGER.log("TEST CASE: domainObject", LogLevel.DEBUG);

        assertThat(ConfigurationDO.class, hasValidBeanConstructor());
        assertThat(ConfigurationDO.class, hasValidBeanToString());
        assertThat(ConfigurationDO.class, hasValidBeanHashCodeFor("key", "modulName", "version"));
        assertThat(ConfigurationDO.class, hasValidBeanEqualsFor("key", "modulName", "version"));
    }

    @Test
    void hasValidGetter() {
        LOGGER.log("TEST CASE: hasValidGetter", LogLevel.DEBUG);

        ConfigurationDO domainObject = new ConfigurationDO();
        domainObject.setComment("comment");
        domainObject.setConfigurationSet(configSet);
        domainObject.setDefaultValue(defaultValue);
        domainObject.setKey(key);
        domainObject.setModulName(modulName);
        domainObject.setValue(value);
        domainObject.setVersion(version);

        assertEquals("comment", domainObject.getComment());
        assertEquals(configSet, domainObject.getConfigurationSet());
        assertEquals(defaultValue, domainObject.getDefaultValue());
        assertEquals(key, domainObject.getKey());
        assertEquals(modulName, domainObject.getModulName());
        assertNotNull(domainObject.getUuid());
        assertEquals(value, domainObject.getValue());
        assertEquals(version, domainObject.getVersion());
    }

    @Test
    void createDomainObjectBySetter() {
        LOGGER.log("TEST CASE: createDomainObjectBySetter", LogLevel.DEBUG);

        ConfigurationDO domainObject = new ConfigurationDO();
        domainObject.setKey(key);
        domainObject.setModulName(modulName);
        domainObject.setVersion(version);
        domainObject.setConfigurationSet(configSet);
        domainObject.setDefaultValue(defaultValue);
        domainObject.setValue(value);

        assertNotNull(domainObject);

        Set<ConstraintViolation<ConfigurationDO>> result
                = validate.validate(domainObject);

        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    @Test
    void correctBeanConstruction() {
        LOGGER.log("TEST CASE: correctBeanConstruction", LogLevel.DEBUG);

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
    void isEqual() {
        LOGGER.log("TEST CASE: isEqual", LogLevel.DEBUG);

        ConfigurationDO A
                = new ConfigurationDO(key, "111", modulName, version);
        ConfigurationDO B
                = new ConfigurationDO(key, "000", modulName, version);

        assertTrue(A.equals(B));
        assertTrue(B.equals(A));
    }

    @Test
    void isNotEqual() {
        LOGGER.log("TEST CASE: isNotEqual", LogLevel.DEBUG);

        ConfigurationDO A
                = new ConfigurationDO("AAA", "000", "test", "1.0.1");
        ConfigurationDO B
                = new ConfigurationDO("BBB", "001", "test", "1.0.1");
        ConfigurationDO C
                = new ConfigurationDO("BBB", "010", "module", "1.0.1");
        ConfigurationDO D
                = new ConfigurationDO("BBB", "011", "test", "2.0");

        //KEY
        assertFalse(A.equals(B));
        assertFalse(B.equals(A));
        //MODULE
        assertFalse(B.equals(C));
        //VERION
        assertFalse(B.equals(D));
    }

    @Test
    void validationEmptyComment() {
        LOGGER.log("TEST CASE: validationEmptyComment", LogLevel.DEBUG);

        ConfigurationDO domainObject
                = new ConfigurationDO(key, value, modulName, version);
        domainObject.prePersist();
        assertNotNull(domainObject);
        assertEquals("", domainObject.getComment());
        assertEquals(0, validate.validate(domainObject).size());
        assertTrue(validate.validate(domainObject).isEmpty());
    }

    @Test
    void validationEmptyValue() {
        LOGGER.log("TEST CASE: validationEmptyValue", LogLevel.DEBUG);

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
