package pl.edu.icm.saos.search.search.service;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.search.search.model.Searchable;

/**
 * Base class for translating solr response into search results
 * @author madryk
 * @param <S> type of single result
 */
public abstract class AbstractSearchResultsTranslator<S extends Searchable> implements SearchResultsTranslator<S> {

    @Override
    public SearchResults<S> translate(QueryResponse response) {
        SolrDocumentList documents = response.getResults();
        SearchResults<S> results = new SearchResults<S>();
        
        for (int i=0; i<documents.size(); ++i) {
            SolrDocument document = documents.get(i);
            S result = translateSingle(document);
            
            if (response.getHighlighting() != null && response.getHighlighting().containsKey(result.getId())) {
                Map<String, List<String>> documentHighlighting = response.getHighlighting().get(result.getId());
                applyHighlighting(documentHighlighting, result);
            }
            
            results.addResult(result);
        }
        
        results.setTotalResults(documents.getNumFound());
        
        return results;
    }

    /**
     * Translate document provided by Solr into result
     * @param document
     * @return
     */
    protected abstract S translateSingle(SolrDocument document);
    
    /**
     * Applies highlighting into result based on associated documentHighlighting
     * @param documentHighlighting
     * @param result 
     */
    protected abstract void applyHighlighting(Map<String, List<String>> documentHighlighting, S result);
}
