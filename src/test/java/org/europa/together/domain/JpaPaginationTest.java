package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.*;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
public class JpaPaginationTest {

    private static final Logger LOGGER = new LogbackLogger(JpaPaginationTest.class);

    @Test
    void domainObject() {
        LOGGER.log("TEST CASE: domainObject", LogLevel.DEBUG);

        assertThat(JpaPagination.class, hasValidBeanConstructor());
        assertThat(JpaPagination.class, hasValidBeanToString());
        assertThat(JpaPagination.class, hasValidGettersAndSetters());
    }

}
