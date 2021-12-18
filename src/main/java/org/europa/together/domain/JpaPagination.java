package org.europa.together.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Be always aware, when using listAll() because some database tables with a
 * giant amount of rows will slow down your application dramatically. The
 * JpaPagination is designed to limit your result set. It avoid a slow
 * performing OFFSET and implement instead the seek method.
 * <br>
 * To secure a correct result set, the results aer by default sorted by the
 * primary key of the database table in an ascending manner. This behavior can
 * be changed.
 * <br>
 * Its necessary to define the Primary Key from the Domain Object and not from
 * the database table name.
 *
 * @param <T> as GeneriClass
 */
public class JpaPagination {

    /**
     * Ascending sorting order of the whole result set.
     */
    public static final String ORDER_ASC = "ASC";
    /**
     * Descending sorting order of the whole result set.
     */
    public static final String ORDER_DESC = "DESC";
    /**
     * Define if the pagination goes ahead.
     */
    public static final String PAGING_FOREWARD = "FW";
    /**
     * Define if the pgination goes backwards.
     */
    public static final String PAGING_BACKWARD = "BW";

    private String sorting;
    private String paging;
    private int pageSize;
    private String primaryKey;
    private String pageBreak;
    private String additionalOrdering;
    private Map<String, String> filterStringCriteria = new HashMap<>();
    private Map<String, Boolean> filterBooleanCriteria = new HashMap<>();
    private Map<String, Integer> filterIntegerCriteria = new HashMap<>();

    /**
     * Default Constructor.
     */
    public JpaPagination() {
        this.pageSize = 0;
        this.sorting = ORDER_ASC;
        this.paging = PAGING_FOREWARD;
    }

    /**
     * Allow to set the PrimaryKey (PK) of the database table. Be aware, that
     * the PK is not the colum name of the table. The PK is defined by Domain
     * Object is used.
     *
     * @param primaryKey as String
     */
    public JpaPagination(final String primaryKey) {
        this.primaryKey = primaryKey;
        this.pageSize = 0;
        this.sorting = ORDER_ASC;
        this.paging = PAGING_FOREWARD;
    }

    @Override
    public String toString() {
        return "JpaPagination{"
                + "sorting=" + sorting
                + ", paging=" + paging
                + ", pageSize=" + pageSize
                + ", primaryKey=" + primaryKey
                + ", pageBreak=" + pageBreak
                + ", additionalOrdering=" + additionalOrdering
                + ", filterStringCriteria=" + filterStringCriteria
                + ", filterBooleanCriteria=" + filterBooleanCriteria
                + ", filterIntegerCriteria=" + filterIntegerCriteria
                + '}';
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(final int limit) {
        this.pageSize = limit;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSorting(final String ordering) {
        this.sorting = ordering;
    }

    public String getPaging() {
        return paging;
    }

    public void setPaging(final String direction) {
        this.paging = direction;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(final String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getPageBreak() {
        return pageBreak;
    }

    public void setPageBreak(final String pageBreak) {
        this.pageBreak = pageBreak;
    }

    public String getAdditionalOrdering() {
        return additionalOrdering;
    }

    public void setAdditionalOrdering(String additionalOrdering) {
        this.additionalOrdering = additionalOrdering;
    }

    public Map<String, String> getFilterStringCriteria() {
        return filterStringCriteria;
    }

    public void setFilterStringCriteria(Map<String, String> filterStringCriteria) {
        this.filterStringCriteria = filterStringCriteria;
    }

    public Map<String, Boolean> getFilterBooleanCriteria() {
        return filterBooleanCriteria;
    }

    public void setFilterBooleanCriteria(Map<String, Boolean> filterBooleanCriteria) {
        this.filterBooleanCriteria = filterBooleanCriteria;
    }

    public Map<String, Integer> getFilterIntegerCriteria() {
        return filterIntegerCriteria;
    }

    public void setFilterIntegerCriteria(Map<String, Integer> filterIntegerCriteria) {
        this.filterIntegerCriteria = filterIntegerCriteria;
    }

}
