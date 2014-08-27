package pl.edu.icm.saos.api.search;

import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.api.search.ElementsSearchResults;

/**
 * @author pavtel
 */
public interface ApiSearchService<T> {

    ElementsSearchResults<T> performSearch(RequestParameters requestParameters);
}
