package pl.edu.icm.saos.api.search;



import java.util.List;

/**
 * @author pavtel
 */
public class ElementsSearchResults<T, S> {

    private List<? extends T> elements;

    private S requestParameters;

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
}
