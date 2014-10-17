package pl.edu.icm.saos.api.search.services;


/** This service provides functionality for searching elements (of type T)
 *  according to the given parameters S.
 * @author pavtel
 */
public interface ApiSearchService<T,S> {

    /**
     * Performs search operation according to the given parameters S.
     * @param parameters search parameters.
     * @return search result.
     */
    ElementsSearchResults<T, S> performSearch(S parameters);
}
