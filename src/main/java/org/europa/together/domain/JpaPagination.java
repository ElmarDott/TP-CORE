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
                + ", filterStringCriteria=" + this.filterStringCriteria
                + ", filterBooleanCriteria=" + this.filterBooleanCriteria
                + ", filterIntegerCriteria=" + this.filterIntegerCriteria
                + '}';
    }

    //<editor-fold defaultstate="collapsed" desc="Getter / Setter">
    /**
     * get the amount of elements will be placed on a page.
     *
     * @return pageSize as Integer
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Define the amount of elements will be placed on a page.
     *
     * @param limit as Integer
     */
    public void setPageSize(final int limit) {
        this.pageSize = limit;
    }

    /**
     * Get if the sorting is DESC or ASC.
     *
     * @return sorting as String
     */
    public String getSorting() {
        return sorting;
    }

    /**
     * Define if the sorting is DESC or ASC.
     *
     * @param ordering as String
     */
    public void setSorting(final String ordering) {
        this.sorting = ordering;
    }

    /**
     * Get the direction (forward or backward) of the paging.
     *
     * @return paging as String
     */
    public String getPaging() {
        return paging;
    }

    /**
     * Define the direction (forward or backward) of the paging.
     *
     * @param direction as String
     */
    public void setPaging(final String direction) {
        this.paging = direction;
    }

    /**
     * Get the defined primarey key of the Domain Object.
     *
     * @return primaryKey as String
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    /**
     * Set the primarey key of the Domain Object.
     *
     * @param primaryKey as String
     */
    public void setPrimaryKey(final String primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * Get the pivot element, where the page break occur.
     *
     * @return peageBreak as String
     */
    public String getPageBreak() {
        return pageBreak;
    }

    /**
     * Define the pivot element, where the page break occur.
     *
     * @param pageBreak as String
     */
    public void setPageBreak(final String pageBreak) {
        this.pageBreak = pageBreak;
    }

    /**
     * Get the 2 nd parameter, besides the primarey key for ordering the result
     * set.
     *
     * @return additionalOrdering as String
     */
    public String getAdditionalOrdering() {
        return additionalOrdering;
    }

    /**
     * Set the 2 nd parameter, besides the primarey key for ordering the result
     * set.
     *
     * @param additionalOrdering as String
     */
    public void setAdditionalOrdering(final String additionalOrdering) {
        this.additionalOrdering = additionalOrdering;
    }

    /**
     * Get all <b>String</b> based filters &gt;KEY, Value&lt; to reduce the
     * result set.
     *
     * @return filterStringCriteria as Map
     */
    public Map<String, String> getFilterStringCriteria() {
        return Map.copyOf(filterStringCriteria);
    }

    /**
     * Set all <b>String</b> based filters &gt;KEY, Value&lt; to reduce the
     * result set.
     *
     * @param filterStringCriteria as Map
     */
    public void setFilterStringCriteria(final Map<String, String> filterStringCriteria) {
        this.filterStringCriteria.putAll(filterStringCriteria);
    }

    /**
     * Get all <b>Boolean</b> based filters &gt;KEY, Value&lt; to reduce the
     * result set.
     *
     * @return filterBooleanCriteria as Map
     */
    public Map<String, Boolean> getFilterBooleanCriteria() {
        return Map.copyOf(filterBooleanCriteria);
    }

    /**
     * Set all <b>Boolean</b> based filters &gt;KEY, Value&lt; to reduce the
     * result set.
     *
     * @param filterBooleanCriteria as Map
     */
    public void setFilterBooleanCriteria(final Map<String, Boolean> filterBooleanCriteria) {
        this.filterBooleanCriteria.putAll(filterBooleanCriteria);
    }

    /**
     * Get all <b>Integer</b> based filters &gt;KEY, Value&lt; to reduce the
     * result set.
     *
     * @return filterIntegerCriteria as Map
     */
    public Map<String, Integer> getFilterIntegerCriteria() {
        return Map.copyOf(filterIntegerCriteria);
    }

    /**
     * Set all <b>Integer</b> based filters &gt;KEY, Value&lt; to reduce the
     * result set.
     *
     * @param filterIntegerCriteria as Map
     */
    public void setFilterIntegerCriteria(final Map<String, Integer> filterIntegerCriteria) {
        this.filterIntegerCriteria.putAll(filterIntegerCriteria);
    }
    //</editor-fold>
}
