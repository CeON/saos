package pl.edu.icm.saos.search.search.service;

import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;

import pl.edu.icm.saos.search.search.model.Searchable;

/**
 * Translates document provided by Solr into result
 * @author madryk
 * @param <S> type of result
 */
public interface SearchResultTranslator<S extends Searchable> {

    /**
     * Translates document provided by Solr into result
     * @param document
     * @return
     */
    S translateSingle(SolrDocument document);
    
    /**
     * Applies highlighting into result based on associated documentHighlighting
     * @param documentHighlighting
     * @param result 
     */
    void applyHighlighting(Map<String, List<String>> documentHighlighting, S result);
}
