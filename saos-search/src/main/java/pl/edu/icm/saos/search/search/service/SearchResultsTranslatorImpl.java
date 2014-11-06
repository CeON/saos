package pl.edu.icm.saos.search.search.service;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.search.search.model.Searchable;

/**
 * Translates solr response into search results
 * @author madryk
 * @param <S> type of single result
 */
public class SearchResultsTranslatorImpl<S extends Searchable> implements SearchResultsTranslator<S> {

    private SearchResultTranslator<S> searchResultTranslator;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public SearchResults<S> translate(QueryResponse response) {
        SolrDocumentList documents = response.getResults();
        SearchResults<S> results = new SearchResults<S>();
        
        for (int i=0; i<documents.size(); ++i) {
            SolrDocument document = documents.get(i);
            S result = searchResultTranslator.translateSingle(document);
            
            checkAndApplyHighlighting(response.getHighlighting(), result);
            
            results.addResult(result);
        }
        
        results.setTotalResults(documents.getNumFound());
        
        return results;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void checkAndApplyHighlighting(Map<String, Map<String, List<String>>> highlighting, S result) {
        
        if (highlighting != null && highlighting.containsKey(String.valueOf(result.getId()))) {
            Map<String, List<String>> documentHighlighting = highlighting.get(String.valueOf(result.getId()));
            searchResultTranslator.applyHighlighting(documentHighlighting, result);
        }
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setSearchResultTranslator(
            SearchResultTranslator<S> searchResultTranslator) {
        this.searchResultTranslator = searchResultTranslator;
    }
}
