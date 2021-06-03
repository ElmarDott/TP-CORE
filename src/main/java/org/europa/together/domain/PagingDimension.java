package org.europa.together.domain;

/**
 *
 */
public class PagingDimension {

    private int start;
    private int end;
    private int page;
    private int pageSize;
    private int allEntries;

    public int getStart() {
        return start;
    }

    public void setStart(final int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(final int end) {
        this.end = end;
    }

    public int getPage() {
        return page;
    }

    public void setPage(final int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    public int getAllEntries() {
        return allEntries;
    }

    public void setAllEntries(final int allEntries) {
        this.allEntries = allEntries;
    }

}
