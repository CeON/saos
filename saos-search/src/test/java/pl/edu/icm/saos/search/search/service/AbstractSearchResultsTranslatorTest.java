package pl.edu.icm.saos.search.search.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;

/**
 * @author madryk
 */
public class AbstractSearchResultsTranslatorTest {

    private AbstractSearchResultsTranslator<JudgmentSearchResult> searchResultsTranslator = new TestSearchResultsTranslator();
    
    
    @Test
    public void translate() {
        List<SolrDocument> docs = IntStream.range(0, 10)
                .mapToObj(x -> fetchBasicDocument(String.valueOf(x)))
                .collect(Collectors.toList());
        QueryResponse response = createBasicSolrResponse(123, docs);
        
        
        SearchResults<JudgmentSearchResult> results = searchResultsTranslator.translate(response);
        
        
        assertEquals(123, results.getTotalResults());
        assertEquals(10, results.getResults().size());
        List<String> resultIds = results.getResults()
                .stream()
                .map(JudgmentSearchResult::getId)
                .collect(Collectors.toList());
        
        IntStream.range(0, 10).forEach(x -> assertTrue(resultIds.contains(String.valueOf(x))));
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private SolrDocument fetchBasicDocument(String databaseId) {
        SolrDocument doc = new SolrDocument();
        doc.addField("databaseId", databaseId);
        return doc;
    }
    
    private QueryResponse createBasicSolrResponse(long totalResults, List<SolrDocument> documents) {
        QueryResponse response = new QueryResponse();
        
        SolrDocumentList documentList = new SolrDocumentList();
        documentList.setNumFound(totalResults);
        for (SolrDocument doc : documents) {
            documentList.add(doc);
        }
        
        NamedList<Object> namedList = new NamedList<Object>();
        namedList.add("response", documentList);
        response.setResponse(namedList);
        
        return response;
    }
    
    
    private class TestSearchResultsTranslator extends AbstractSearchResultsTranslator<JudgmentSearchResult> {

        @Override
        protected JudgmentSearchResult translateSingle(SolrDocument document) {
            JudgmentSearchResult result = new JudgmentSearchResult();
            result.setId((String)document.getFieldValue("databaseId"));
            return result;
        }

        @Override
        protected void applyHighlighting(
                Map<String, List<String>> documentHighlighting,
                JudgmentSearchResult result) {
        }
        
    }
}
