package pl.edu.icm.saos.search.search.model;

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
    
}
