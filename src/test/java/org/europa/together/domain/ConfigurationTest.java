package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class ConfigurationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private String uuid = "a3ae3672-22bc-411f-81c5-103652a5846e";
    private String key = "key";
    private String value = "value";
    private String defaultValue = "default";
    private String modulName = "module";
    private String configSet = "configuration";
    private String version = "1.0";
    private boolean depecated = true;
    private String comment = "no comment";

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

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
    public void testCorrectValidation() {

        Configuration domainObject;

        domainObject = new Configuration(uuid, key, value,
                defaultValue, modulName, configSet, version, depecated, comment);
        assertTrue(validator.validate(domainObject).isEmpty());

        domainObject = new Configuration(uuid, key, null,
                defaultValue, modulName, configSet, version, depecated, comment);
        assertTrue(validator.validate(domainObject).isEmpty());

        domainObject = new Configuration(uuid, key, value,
                defaultValue, modulName, configSet, version, depecated, null);
        assertTrue(validator.validate(domainObject).isEmpty());
    }

    @Test
    public void testValidationNotNull() {

        Configuration domainObject = new Configuration();
        assertFalse(validator.validate(domainObject).isEmpty());
    }

    @Test
    public void testValidationUuidNotNull() {
        //UUID
        Configuration domainObject = new Configuration(null, key, value,
                defaultValue, modulName, configSet, version, depecated, comment);
        assertEquals(1, validator.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validator.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    public void testValidationKeyNotNull() {
        //UUID
        Configuration domainObject = new Configuration(uuid, null, value,
                defaultValue, modulName, configSet, version, depecated, comment);
        assertEquals(1, validator.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validator.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    public void testValidationDefaultValueNotNull() {
        //UUID
        Configuration domainObject = new Configuration(uuid, key, value,
                null, modulName, configSet, version, depecated, comment);
        assertEquals(1, validator.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validator.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    public void testValidationModulNameNotNull() {
        //UUID
        Configuration domainObject = new Configuration(uuid, key, value,
                defaultValue, null, configSet, version, depecated, comment);
        assertEquals(1, validator.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validator.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    public void testValidationConfigurationSetNotNull() {
        //UUID
        Configuration domainObject = new Configuration(uuid, key, value,
                defaultValue, modulName, null, version, depecated, comment);
        assertEquals(1, validator.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validator.validate(domainObject).iterator().next().getMessage());
    }

    @Test
    public void testValidationVersionNotNull() {
        //UUID
        Configuration domainObject = new Configuration(uuid, key, value,
                defaultValue, modulName, configSet, null, depecated, comment);
        assertEquals(1, validator.validate(domainObject).size());
        assertEquals("{validation.notnull}",
                validator.validate(domainObject).iterator().next().getMessage());
    }
}
