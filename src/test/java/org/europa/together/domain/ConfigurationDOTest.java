package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.*;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class ConfigurationDOTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validate;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validate = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

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
    void testDomainObject() {
        assertThat(ConfigurationDO.class, hasValidBeanConstructor());
        assertThat(ConfigurationDO.class, hasValidBeanToString());
        assertThat(ConfigurationDO.class, hasValidBeanHashCodeFor("key", "modulName", "version"));
        assertThat(ConfigurationDO.class, hasValidBeanEqualsFor("key", "modulName", "version"));
    }

    @Test
    void prePersist() {
        ConfigurationDO domainObject = new ConfigurationDO();
        domainObject.prePersist();

        assertNotNull(domainObject);
        assertEquals(false, domainObject.isDepecated());
        assertEquals("NIL", domainObject.getDefaultValue());
    }

    @Test
    void testCreateDomainObjectBySetter() {
        ConfigurationDO configurationDO = new ConfigurationDO();
        configurationDO.setKey(key);
        configurationDO.setModulName(modulName);
        configurationDO.setVersion(version);
        configurationDO.setConfigurationSet(configSet);
        configurationDO.setDefaultValue(defaultValue);
        configurationDO.setValue(value);

        assertTrue(validate.validate(configurationDO).isEmpty());
    }

    @Test
    void testCorrectValidation() {

        ConfigurationDO domainObject;

        domainObject = new ConfigurationDO(key, value,
                defaultValue, modulName, configSet, version, depecated, mandatory, comment);
        assertTrue(validate.validate(domainObject).isEmpty());

        domainObject = new ConfigurationDO(key, null,
                defaultValue, modulName, configSet, version, depecated, mandatory, comment);
        assertTrue(validate.validate(domainObject).isEmpty());

        domainObject = new ConfigurationDO(key, value,
                defaultValue, modulName, configSet, version, depecated, mandatory, null);
        assertTrue(validate.validate(domainObject).isEmpty());
    }

    @Test
    void testValidationNotNull() {

        ConfigurationDO domainObject = new ConfigurationDO();
        assertFalse(validate.validate(domainObject).isEmpty());
    }

    @Test
    void testValidationUuidNotNull() {
        //UUID
        ConfigurationDO domainObject = new ConfigurationDO(key, value,
                defaultValue, modulName, configSet, version, depecated, mandatory, comment);
        assertEquals(0, validate.validate(domainObject).size());
    }

    @Test
    void testValidationKeyNotNull() {
        //UUID
        ConfigurationDO domainObject = new ConfigurationDO(null, value,
                defaultValue, modulName, configSet, version, depecated, mandatory, comment);
        assertEquals(1, validate.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    void testValidationDefaultValueNotNull() {
        //UUID
        ConfigurationDO domainObject = new ConfigurationDO(key, value,
                null, modulName, configSet, version, depecated, mandatory, comment);
        assertEquals(1, validate.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    void testValidationModulNameNotNull() {
        //UUID
        ConfigurationDO domainObject = new ConfigurationDO(key, value,
                defaultValue, null, configSet, version, depecated, mandatory, comment);
        assertEquals(1, validate.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    void testValidationVersionNotNull() {
        //UUID
        ConfigurationDO domainObject = new ConfigurationDO(key, value,
                defaultValue, modulName, configSet, null, depecated, mandatory, comment);
        assertEquals(1, validate.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }
}
