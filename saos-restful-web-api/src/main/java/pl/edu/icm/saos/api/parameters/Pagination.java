package pl.edu.icm.saos.api.parameters;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * Represent pagination.
 * @author pavtel
 */
public class Pagination {

    //******** fields *******
    private final int limit;

    private final int offset;

    //******* END fields ********

    //***** constructors *******
    public Pagination(int limit, int offset) {
        Preconditions.checkArgument(limit > 0, "limit should be positive");
        Preconditions.checkArgument(offset >= 0, "offset can't be negative");

        this.limit = limit;
        this.offset = offset;
    }
    //********* END constructors *********

    //******** getters *********

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    //********** END getters ***********

    //********** business methods ***********
    public boolean hasPrevious(){
        return offset > 0;
    }

    public boolean hasNextIn(long allElementCount){
        return limit+offset<allElementCount;
    }

    public Pagination getNext(){
        return new Pagination(limit, offset+limit);
    }

    public Pagination getPrevious(){
        int difference = offset - limit;
        if(difference>0)
            return new Pagination(limit, difference);
        else
            return new Pagination(limit, 0);
    }

    //************ END business methods ************

    @Override
    public String toString() {
        return Objects.toStringHelper(Pagination.class)
                .add("limit", limit)
                .add("offset", offset)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Pagination){
            Pagination other = (Pagination) obj;
            return limit == other.limit && offset == other.offset;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(limit, offset);
    }
}
