package pl.edu.icm.saos.api.search;

import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.List;

/**
 * @author pavtel
 */
public class ElementsSearchResults<T> {

    private RequestParameters requestParameters;

    private List<? extends T> elements;

    public ElementsSearchResults(RequestParameters requestParameters, List<? extends T> elements) {
        this.requestParameters = requestParameters;
        this.elements = elements;
    }

    public RequestParameters getRequestParameters() {
        return requestParameters;
    }

    public List<? extends T> getElements() {
        return elements;
    }
}
