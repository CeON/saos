package pl.edu.icm.saos.persistence.search;


import pl.edu.icm.saos.persistence.search.dto.SearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

/**
 * A data base search service interface
 * 
 * @author lukdumi
 *
 */
public interface DatabaseSearchService {

    /**
     * Performs search according to the given criteria 
     * @param <E> A search result record type
     * @param <T> A search filter type
     */
    public <E, T extends SearchFilter> SearchResult<E> search(T searchFilter);
    
    
    /**
     * Returns the number of all records that met the criteria specified in searchFilter 
     */
    public <T extends SearchFilter> long count(T searchFilter);
}
