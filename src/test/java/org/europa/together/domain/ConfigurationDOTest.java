package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("unchecked")
public class ConfigurationDOTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validate;

    @BeforeClass
    public static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validate = validatorFactory.getValidator();

        ApplicationContext context
                = new ClassPathXmlApplicationContext("classpath:org/europa/together/configuration/spring-dao-test.xml");
    }

    @AfterClass
    public static void tearDown() {
        validatorFactory.close();
    }

    private String key = "key";
    private String value = "value";
    private String defaultValue = "default";
    private String modulName = "module";
    private String configSet = "configuration";
    private String version = "1.0";
    private boolean depecated = true;
    private String comment = "no comment";

    @Test
    public void testDomainObject() {
        assertThat(ConfigurationDO.class, hasValidBeanConstructor());
        assertThat(ConfigurationDO.class, hasValidGettersAndSetters());
        assertThat(ConfigurationDO.class, hasValidBeanToString());
        assertThat(ConfigurationDO.class, hasValidBeanHashCodeFor("key", "modulName", "version"));
        assertThat(ConfigurationDO.class, hasValidBeanEqualsFor("key", "modulName", "version"));
    }

    @Test
    public void prePersist() {
        ConfigurationDO domainObject = new ConfigurationDO();
        domainObject.prePersist();

        assertNotNull(domainObject);
        assertEquals(false, domainObject.isDepecated());
        assertEquals(null, domainObject.getValue());
        assertEquals("none", domainObject.getDefaultValue());
        assertEquals("none", domainObject.getConfigurationSet());
    }

    @Test
    public void testCreateDomainObjectBySetter() {
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
    public void testCorrectValidation() {

        ConfigurationDO domainObject;

        domainObject = new ConfigurationDO(key, value,
                defaultValue, modulName, configSet, version, depecated, comment);
        assertTrue(validate.validate(domainObject).isEmpty());

        domainObject = new ConfigurationDO(key, null,
                defaultValue, modulName, configSet, version, depecated, comment);
        assertTrue(validate.validate(domainObject).isEmpty());

        domainObject = new ConfigurationDO(key, value,
                defaultValue, modulName, configSet, version, depecated, null);
        assertTrue(validate.validate(domainObject).isEmpty());
    }

    @Test
    public void testValidationNotNull() {

        ConfigurationDO domainObject = new ConfigurationDO();
        assertFalse(validate.validate(domainObject).isEmpty());
    }

    @Test
    public void testValidationUuidNotNull() {
        //UUID
        ConfigurationDO domainObject = new ConfigurationDO(key, value,
                defaultValue, modulName, configSet, version, depecated, comment);
        assertEquals(0, validate.validate(domainObject).size());
    }

    @Test
    public void testValidationKeyNotNull() {
        //UUID
        ConfigurationDO domainObject = new ConfigurationDO(null, value,
                defaultValue, modulName, configSet, version, depecated, comment);
        assertEquals(1, validate.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    public void testValidationDefaultValueNotNull() {
        //UUID
        ConfigurationDO domainObject = new ConfigurationDO(key, value,
                null, modulName, configSet, version, depecated, comment);
        assertEquals(1, validate.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    public void testValidationModulNameNotNull() {
        //UUID
        ConfigurationDO domainObject = new ConfigurationDO(key, value,
                defaultValue, null, configSet, version, depecated, comment);
        assertEquals(1, validate.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    public void testValidationVersionNotNull() {
        //UUID
        ConfigurationDO domainObject = new ConfigurationDO(key, value,
                defaultValue, modulName, configSet, null, depecated, comment);
        assertEquals(1, validate.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }
}
