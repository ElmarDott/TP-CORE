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
public class ConfigurationTest {

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
        assertThat(Configuration.class, hasValidBeanConstructor());
        assertThat(Configuration.class, hasValidGettersAndSetters());
        assertThat(Configuration.class, hasValidBeanToString());
        assertThat(Configuration.class, hasValidBeanHashCodeFor("key", "modulName", "version"));
        assertThat(Configuration.class, hasValidBeanEqualsFor("key", "modulName", "version"));
    }

    @Test
    public void prePersist() {
        Configuration domainObject = new Configuration();
        domainObject.prePersist();

        assertNotNull(domainObject);
        assertEquals(false, domainObject.isDepecated());
        assertEquals(null, domainObject.getValue());
        assertEquals("none", domainObject.getDefaultValue());
        assertEquals("none", domainObject.getConfigurationSet());
    }

    @Test
    public void testCreateDomainObjectBySetter() {
        Configuration configurationDO = new Configuration();
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

        Configuration domainObject;

        domainObject = new Configuration(key, value,
                defaultValue, modulName, configSet, version, depecated, comment);
        assertTrue(validate.validate(domainObject).isEmpty());

        domainObject = new Configuration(key, null,
                defaultValue, modulName, configSet, version, depecated, comment);
        assertTrue(validate.validate(domainObject).isEmpty());

        domainObject = new Configuration(key, value,
                defaultValue, modulName, configSet, version, depecated, null);
        assertTrue(validate.validate(domainObject).isEmpty());
    }

    @Test
    public void testValidationNotNull() {

        Configuration domainObject = new Configuration();
        assertFalse(validate.validate(domainObject).isEmpty());
    }

    @Test
    public void testValidationUuidNotNull() {
        //UUID
        Configuration domainObject = new Configuration(key, value,
                defaultValue, modulName, configSet, version, depecated, comment);
        assertEquals(0, validate.validate(domainObject).size());
    }

    @Test
    public void testValidationKeyNotNull() {
        //UUID
        Configuration domainObject = new Configuration(null, value,
                defaultValue, modulName, configSet, version, depecated, comment);
        assertEquals(1, validate.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    public void testValidationDefaultValueNotNull() {
        //UUID
        Configuration domainObject = new Configuration(key, value,
                null, modulName, configSet, version, depecated, comment);
        assertEquals(1, validate.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    public void testValidationModulNameNotNull() {
        //UUID
        Configuration domainObject = new Configuration(key, value,
                defaultValue, null, configSet, version, depecated, comment);
        assertEquals(1, validate.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    public void testValidationConfigurationSetNotNull() {
        //UUID
        Configuration domainObject = new Configuration(key, value,
                defaultValue, modulName, null, version, depecated, comment);
        assertEquals(1, validate.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    public void testValidationVersionNotNull() {
        //UUID
        Configuration domainObject = new Configuration(key, value,
                defaultValue, modulName, configSet, null, depecated, comment);
        assertEquals(1, validate.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validate.validate(domainObject).iterator().next().getMessage());
    }
}
