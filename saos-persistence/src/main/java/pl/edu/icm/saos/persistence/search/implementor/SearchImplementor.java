package pl.edu.icm.saos.persistence.search.implementor;


import pl.edu.icm.saos.persistence.search.dto.SearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

/**
 * An interface describing classes that perform specific searches.
 * @author lukdumi
 *
 * @param <X> SearchFilter
 * @param <T> Search result record type. E.g. if we search objects of type, say Person - it can be Person class type, although it can be also (for example) PersonSearchResultRecord
 */

public interface SearchImplementor<X extends SearchFilter, T> {

    /**
     * Performs search
     */
    public SearchResult<T> search(X searchFilter);

    /**
     * Returns the number of records that met the criteria specified in searchFilter
     */
    public long count(X searchFilter);

    /**
     * Returns the class of the search filter the implementor is designed for
     */
    public Class<? extends SearchFilter> getSearchFilterClass();
}
