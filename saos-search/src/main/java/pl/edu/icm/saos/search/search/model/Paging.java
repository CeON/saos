package pl.edu.icm.saos.search.search.model;

import com.google.common.base.Objects;

public class Paging {

    private int pageNumber;
    private int pageSize;
    
    private Sorting sort;
    
    public Paging(int pageNumber, int pageSize) {
        this(pageNumber, pageSize, null);
    }
    
    public Paging(int pageNumber, int pageSize, Sorting sort) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    //------------------------ GETTERS --------------------------
    
    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Sorting getSort() {
        return sort;
    }

    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        return Objects.hashCode(pageNumber, pageSize, sort);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Paging other = (Paging) obj;
        return Objects.equal(this.pageNumber, other.pageNumber) &&
                Objects.equal(this.pageSize, other.pageSize) &&
                Objects.equal(this.sort, other.sort);
    }

    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("pageNumber", pageNumber)
                .add("pageSize", pageSize)
                .add("sort", sort)
                .toString();
    }
}
