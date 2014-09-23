package pl.edu.icm.saos.api.parameters;

import com.google.common.base.Objects;

/**
 * Represents request parameters
 * @author pavtel
 */
public class RequestParameters {

    //********* fields ******
    private JoinedParameter expandParameter;
    private Pagination pagination;
    //******* END fields ********

    //******** constructors ********
    public RequestParameters(JoinedParameter expandParameter, Pagination pagination) {
        this.expandParameter = expandParameter;
        this.pagination = pagination;
    }
    //********* END constructors **********

    //********* getters ***********
    public JoinedParameter getExpandParameter() {
        return expandParameter;
    }

    public Pagination getPagination() {
        return pagination;
    }
    //************ END getters **************

    @Override
    public String toString() {
        return Objects.toStringHelper(RequestParameters.class)
                .add("expandParameter", expandParameter)
                .add("pagination", pagination)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if(obj instanceof RequestParameters){
            RequestParameters other = (RequestParameters) obj;
            return Objects.equal(expandParameter, other.expandParameter) &&
                    Objects.equal(pagination, other.pagination);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expandParameter, pagination);
    }
}
