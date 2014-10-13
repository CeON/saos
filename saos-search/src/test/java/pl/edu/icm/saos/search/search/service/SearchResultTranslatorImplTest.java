package pl.edu.icm.saos.search.search.service;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchResultTranslatorImplTest {

    private SearchResultsTranslatorImpl<JudgmentSearchResult> resultsTranslator = new SearchResultsTranslatorImpl<JudgmentSearchResult>();
    
    @Mock
    private SearchResultTranslator<JudgmentSearchResult> searchResultTranslator;
    
    @Before
    public void setUp() {
        resultsTranslator.setSearchResultTranslator(searchResultTranslator);
    }
    
    @Test
    public void translate() {
        when(searchResultTranslator.translateSingle(any(SolrDocument.class))).thenAnswer(new Answer<JudgmentSearchResult>() {

            @Override
            public JudgmentSearchResult answer(InvocationOnMock invocation) throws Throwable {
                SolrDocument argument = (SolrDocument) invocation.getArguments()[0];
                JudgmentSearchResult result = new JudgmentSearchResult();
                result.setId((String) argument.getFieldValue("databaseId"));
                return result;
            }
        });
        
        SolrDocument firstDocument = fetchBasicDocument("1");
        SolrDocument secondDocument = fetchBasicDocument("2");
        SolrDocument thirdDocument = fetchBasicDocument("4");

        QueryResponse response = createBasicSolrResponse(205, firstDocument, secondDocument, thirdDocument);


        SearchResults<JudgmentSearchResult> results = resultsTranslator.translate(response);


        assertEquals(3, results.getResults().size());
        assertEquals(205, results.getTotalResults());

        JudgmentSearchResult firstResult = results.getResults().get(0);
        assertEquals("1", firstResult.getId());

        JudgmentSearchResult secondResult = results.getResults().get(1);
        assertEquals("2", secondResult.getId());

        JudgmentSearchResult thirdResult = results.getResults().get(2);
        assertEquals("4", thirdResult.getId());

    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private SolrDocument fetchBasicDocument(String databaseId) {
        SolrDocument doc = new SolrDocument();
        doc.addField("databaseId", databaseId);
        
        return doc;
    }
    
    private QueryResponse createBasicSolrResponse(long totalResults, SolrDocument ... documents) {
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
}
