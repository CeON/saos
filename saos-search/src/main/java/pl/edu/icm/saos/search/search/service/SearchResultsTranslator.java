package pl.edu.icm.saos.search.search.service;

import org.apache.solr.client.solrj.response.QueryResponse;

import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.search.search.model.Searchable;

/**
 * Translate Solr response into search results
 * @author madryk
 * @param <S> type of single result
 */
public interface SearchResultsTranslator<S extends Searchable> {

    /**
     * Translate Solr response into search results
     * @param response given by Solr
     * @return translated search results
     */
    SearchResults<S> translate(QueryResponse response);
}
