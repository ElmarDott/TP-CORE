package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class ConfigurationTest {

    @Test
    public void testDomainObject() {
        assertThat(Configuration.class, hasValidBeanConstructor());
        assertThat(Configuration.class, hasValidGettersAndSetters());
        assertThat(Configuration.class, hasValidBeanToString());
        assertThat(Configuration.class, hasValidBeanHashCodeFor("key", "modulName", "version"));
        assertThat(Configuration.class, hasValidBeanEqualsFor("key", "modulName", "version"));
    }

}
