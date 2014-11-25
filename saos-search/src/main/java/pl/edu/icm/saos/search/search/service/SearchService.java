package pl.edu.icm.saos.search.search.service;

import pl.edu.icm.saos.search.search.model.Criteria;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.search.search.model.Searchable;

/**
 * Searching interface
 * 
 * @author madryk
 * @param <S> type of objects that could be found by implementing service  
 * @param <C> type of search criteria that implementing service is able to work with 
 */
public interface SearchService<S extends Searchable, C extends Criteria> {
    
    /**
     * Search for items that matches specified criteria.
     * Limits results according to given paging. 
     * @param criteria
     * @param paging
     * @return search results
     */
    SearchResults<S> search(C criteria, Paging paging);
    
}
