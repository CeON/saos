package pl.edu.icm.saos.search.search.service;

import org.apache.solr.client.solrj.response.QueryResponse;

import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.search.search.model.Searchable;

public interface SearchResultsTranslator<S extends Searchable> {

    SearchResults<S> translate(QueryResponse response);
}
