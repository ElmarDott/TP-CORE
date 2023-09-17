package org.europa.together.business;

import org.europa.together.domain.PaginationTestDO;
import org.springframework.stereotype.Component;

@Component
public interface PaginationTestDAO extends GenericDAO<PaginationTestDO, String> {

    String FEATURE_ID = "CM-02";
}
