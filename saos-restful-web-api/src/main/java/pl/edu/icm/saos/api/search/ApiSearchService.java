package pl.edu.icm.saos.api.search;


/**
 * @author pavtel
 */
public interface ApiSearchService<T,S> {

    ElementsSearchResults<T, S> performSearch(S parameters);
}
