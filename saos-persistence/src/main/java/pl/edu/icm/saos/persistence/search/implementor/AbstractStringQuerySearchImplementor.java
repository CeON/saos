package pl.edu.icm.saos.persistence.search.implementor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import pl.edu.icm.saos.persistence.search.dto.SearchFilter;
import pl.edu.icm.saos.persistence.search.model.Order;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import com.google.common.base.Preconditions;

/**
 * A {@link SearchImplementor} abstract implementation that is based on string queries (eg. sql or hql). 
 * It is recommended to use it as a base class for classes that perform specific paging searches using hql or sql query language.
 *
 * @author lukdumi
 *
 * @param <X> SearchFilter
 * @param <T> Type of the searched object
 */

public abstract class AbstractStringQuerySearchImplementor<X extends SearchFilter, T> implements SearchImplementor<X, T> {
    private static Logger logger = LoggerFactory.getLogger(AbstractStringQuerySearchImplementor.class);


    

    /**
     * Returns the class of the search filter the implementor is designed for.
     * <p>
     * This implementation returns the first class argument given to the superclass.
     * Useful for <code>FooImplementor extends SomeAbstractImplementor&lt;FooFilter></code>, where it returns
     * <code>FooFilter.class</code>. Needs to be overridden otherwise.
     *
     * @return supported filter class
     */
    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends SearchFilter> getSearchFilterClass() {
        Type filterType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (filterType instanceof ParameterizedType) {
            filterType = ((ParameterizedType) filterType).getRawType();
        }
        return (Class<? extends SearchFilter>) filterType;
    }

    /**
     * Search template method
     * Invokes (in the following order):
     * {@link #createQuery(SearchFilter)}
     * {@link #addOrders(String, SearchFilter)} - private method that uses {@link #getOrderField(String)}, invoked if {@link #shouldAddOrders(String)} returns true
     * {@link #createParametersMap(SearchFilter)}
     * {@link #executeQuery(String, java.util.Map, int, int)}
     * {@link #createCountQuery(SearchFilter)}
     * {@link #executeCountQuery(String, java.util.Map)} - executes count query if {@link #createCountQuery(SearchFilter)} returns not empty string
     * {@link #processResult(SearchResult, SearchFilter)}
     *
     *
     */
    @Override
    public final SearchResult<T> search(X searchFilter) {
        if (searchFilter == null) {
            throw new NullPointerException("filter must not be null");
        }

        long startTime = System.currentTimeMillis();

        // PREPARE QUERY
        String query = createQuery(searchFilter);
        if (shouldAddOrders(query)) {
            query = addOrders(query, searchFilter);
        }
        Map<String, Object> parametersMap = createParametersMap(searchFilter);

        // EXECUTE
        List<T> list = executeQuery(query, parametersMap, searchFilter.getFirst(), searchFilter.getLimit()+1); // get one more record to see if there are more records in datasource



        Long allRecordsCount = count(searchFilter, parametersMap);

        
        // CREATE RESULT OBJECT
        SearchResult<T> searchResult = createSearchResult(list, allRecordsCount==null?null:allRecordsCount.longValue(), searchFilter.getFirst(), searchFilter.getLimit());


        // POSTPROCESS THE RESULT
        processResult(searchResult, searchFilter);

        String delta = StringUtils.rightPad((System.currentTimeMillis() - startTime)+"",3);
        logger.info("search request ["+this.getClass().getSimpleName()+"] processed in " + delta + " millis");


        return searchResult;
    }




    /**
     * Returns the record count for the specified filter. The {@link #createCountQuery(SearchFilter)} must be properly overridden.
     * @throws IllegalStateException if createCountQuery returns empty query
     */
    @Override
    public final long count(X searchFilter) throws IllegalStateException {
        Map<String, Object> parametersMap = createParametersMap(searchFilter);

        String countQuery = createCountQuery(searchFilter);

        Preconditions.checkState(!StringUtils.isEmpty(countQuery), "Undefined count query in createCountQuery " + this.getClass().getName());

        Long recordsCount = (Long)executeQuery(countQuery, parametersMap, 0, 1).get(0);

        return recordsCount.intValue();

    }



    //******************** TO IMPLEMENT BY CONCRETE SEARCH CLASSES ********************

    /**
     * Creates a search query (e.g. sql or hql) according to the given searchFilter.
     * <b>If you don't add the 'order by' clause</b>, it will be added automatically by the {@link #addOrders(String, SearchFilter)} method
     *
     *  <b>Take care</b>: your query will not be paged in db if you apply collection fetching (!), so avoid using phrases like 'join fetch collection_name'
     *  You can fetch collections in {@link #processResult(SearchResult)} in additional query
     */
    protected abstract String createQuery(X searchFilter);

    /**
     * Creates a Map of the named properties. The Map will be used to bind the given values to the query named parameters.
     * {@see Query#setProperties(Map)}
     */
    protected abstract Map<String, Object> createParametersMap(X searchFilter);


    /**
     * Creates a search query that returns the number of all records that meet the specified criteria.
     * Take care - the returned count should give the same value as the number of records given by the <b>unlimited</b> {{@link #createQuery(SearchFilter)} query
     * If the returned value is null or is an empty string then the {@link SearchResult#getAllRecordsCount()} will be set to null.
     *
     * Defaults to null.
     */
    protected String createCountQuery(X searchFilter) {
        return null;

    }
    
    
    /**
     * If set to true then count query {@link #createCountQuery(SearchFilter)} will be executed.
     * Otherwise the count query will not be executed and {@link SearchResult#getAllRecordsCount()} will be set to null.
     * 
     *  Defaults to <b>true</b>.
     */
    protected boolean isCountEnabled(X searchFilter) {
        return true;
    }

    /**
     * Maps the order field name to the field name in query
     * E.g. search filter order name = FieldNames.F_WORK_PUBLICATION_DATE might have the corresponding order field name in hql 'contribution.work.publicationDate'
     * The default implementation returns the passed value
     * @param orderNameInSearchFilter
     * @return
     */
    protected String getOrderField(String orderNameInSearchFilter) {
        return orderNameInSearchFilter;
    }

    /**
     * Additional operations on the search result before returning to the client.
     * The default implementation does nothing.
     */
    protected void processResult(SearchResult<T> searchResult, X searchFilter) {}

    /**
     * Helper method, puts the paramValue in the parametersMap under the paramName name if the paramValue is not null
     */
    protected void putParameter(Map<String, Object> parametersMap, String parameterName, Object parameterValue) {
        if (parameterValue!=null) {parametersMap.put(parameterName, parameterValue);}
    }

    /**
     * Returns query with added orders or the same query if no orders are specified {@link SearchFilter#getOrders()}.
     * Executes {@link #getOrderField(String)} for every {@link SearchFilter#getOrders()}
     * Will be invoked only if {@link #shouldAddOrders(String)} returns true for the given query
     */
    protected String addOrders(String query, X searchFilter) {
        if (CollectionUtils.isEmpty(searchFilter.getOrders())) {
            return query;
        }
        StringBuilder sb = new StringBuilder(query);
        sb.append(" order by ");
        boolean first = true;
        for (Order order : searchFilter.getOrders()) {
            if (!first) { sb.append(","); }
            sb.append(getOrderField(order.getField()));
            if (!order.isAscending()) { sb.append(" desc "); }
            first = false;
        }
        String result=sb.toString();
        logger.debug("Query after adding ORDER clause: \"" + result + "\"");
        return result;

    }

    /** 
     * Returns true if the query does not contain the 'order' word (case insensitive).
     * If the method returns true then {@link #addOrders(String, SearchFilter)} will be invoked by the {@link #search(SearchFilter)}
     * after the {@link #createQuery(SearchFilter)}
     * */
    protected boolean shouldAddOrders(String query) {
        if (!query.matches(".*[o|O][r|R][d|D][e|E][r|R].*")) {
            return true;
        }
        return false;
    }
    
    /** 
     * Executes paged query
     */
    protected abstract List<T> executeQuery(String query, Map<String, Object> parametersMap, int first, int limit);

    
    /**
     * Executes count query 
     */
    protected abstract long executeCountQuery(String query, Map<String, Object> parametersMap);
    
    //******************** PRIVATE ********************

    private Long count(X searchFilter, Map<String, Object> parametersMap) {
        // PREPARE COUNT QUERY
        Long allRecordsCount = null;

        if (isCountEnabled(searchFilter)) {
            String countQuery = createCountQuery(searchFilter);
    
    
            // EXECUTE COUNT QUERY
            if (!StringUtils.isEmpty(countQuery)) {
                allRecordsCount = executeCountQuery(countQuery, parametersMap);
            }
        }
        return allRecordsCount;
    }
    
    private SearchResult<T> createSearchResult(List<T> list, Long allRecordsCount, int first, int limit) {
        SearchResult<T> searchResult = new SearchResult<T>(list, allRecordsCount, first, limit);
        return searchResult;
    }
}
