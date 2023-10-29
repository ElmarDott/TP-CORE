package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.*;
import org.europa.together.JUnit5Preperator;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
public class JpaPaginationTest {

    private static final Logger LOGGER = new LogbackLogger(JpaPaginationTest.class);

    @Test
    void domainObject() {
        LOGGER.log("TEST CASE: domainObject", LogLevel.DEBUG);

        assertThat(JpaPagination.class, hasValidBeanConstructor());
        assertThat(JpaPagination.class, hasValidBeanToStringExcluding("filterBooleanCriteria",
                "filterIntegerCriteria",
                "filterStringCriteria"));
        assertThat(JpaPagination.class,
                hasValidGettersAndSettersExcluding("filterBooleanCriteria",
                        "filterIntegerCriteria",
                        "filterStringCriteria"));
    }

}
