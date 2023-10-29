package org.europa.together.business;

import org.europa.together.JUnit5Preperator;
import org.europa.together.domain.PaginationTestDO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.stereotype.Component;

@Component
@ExtendWith({JUnit5Preperator.class})
public interface PaginationTestDAO extends GenericDAO<PaginationTestDO, String> {

    String FEATURE_ID = "CM-02";
}
