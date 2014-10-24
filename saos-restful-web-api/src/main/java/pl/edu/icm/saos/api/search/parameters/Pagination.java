package pl.edu.icm.saos.api.search.parameters;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * Represent pagination.
 * @author pavtel
 */
public class Pagination {

    //******** fields *******
    private final int pageSize;

    private final int pageNumber;

    //******* END fields ********

    //***** constructors *******
    public Pagination(int pageSize, int pageNumber) {
        Preconditions.checkArgument(pageSize > 0, "pageSize should be positive");
        Preconditions.checkArgument(pageNumber >= 0, "pageNumber can't be negative");

        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }
    //********* END constructors *********

    //******** getters *********

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    //********** END getters ***********

    //------------------------ LOGIC --------------------------
    public boolean hasPrevious(){
        return pageNumber > 0;
    }

    public boolean hasNextIn(long allElementCount){
        return (pageNumber+1)*pageSize<allElementCount;
    }

    public Pagination getNext(){
        return new Pagination(pageSize, pageNumber+1);
    }

    public Pagination getPrevious(){
        int previousPageNumber = pageNumber - 1;
        if(previousPageNumber<0){
            return new Pagination(pageSize, 0);
        } else {
            return new Pagination(pageSize, previousPageNumber);
        }
    }

    public int getOffset(){
        return pageNumber * pageSize;
    }



    @Override
    public String toString() {
        return Objects.toStringHelper(Pagination.class)
                .add("pageSize", pageSize)
                .add("pageNumber", pageNumber)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Pagination){
            Pagination other = (Pagination) obj;
            return pageSize == other.pageSize && pageNumber == other.pageNumber;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pageSize, pageNumber);
    }
}
