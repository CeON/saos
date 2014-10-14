package pl.edu.icm.saos.api.search;



import java.util.List;

/**
 * Search result container consisting form search parameters S (requestParameters)
 * and result (elements of type T)
 * @author pavtel
 */
public class ElementsSearchResults<T, S> {

    private List<? extends T> elements;

    private S requestParameters;

    private long totalResults;

    public ElementsSearchResults( List<? extends T> elements, S requestParameters) {
        this.elements = elements;
        this.requestParameters = requestParameters;
    }

    public S getRequestParameters() {
        return requestParameters;
    }

    public List<? extends T> getElements() {
        return elements;
    }

    public ElementsSearchResults<T,S> totalResults(long totalResults){
        this.totalResults = totalResults;
        return this;
    }

    public long getTotalResults() {
        return totalResults;
    }
}
