package org.europa.together.application;

import org.europa.together.business.PaginationTestDAO;
import org.europa.together.domain.PaginationTestDO;
import org.springframework.stereotype.Repository;

@Repository
public class PaginationTestHbmDAO extends GenericHbmDAO<PaginationTestDO, String>
        implements PaginationTestDAO {

    private static final long serialVersionUID = 99999L;

    public PaginationTestHbmDAO() {
        super();
    }
}
