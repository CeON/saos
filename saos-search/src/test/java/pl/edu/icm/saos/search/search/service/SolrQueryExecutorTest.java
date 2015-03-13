package pl.edu.icm.saos.search.search.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.search.exceptions.SolrSearchExecutionException;

/**
 * @author madryk
 */
public class SolrQueryExecutorTest {

    private SolrQueryExecutor solrQueryExecutor = new SolrQueryExecutor();
    
    private SolrServer solrServer = mock(SolrServer.class);
    
    @Before
    public void setUp() {
        solrQueryExecutor.setSolrServer(solrServer);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void executeQuery() throws SolrServerException {
        // given
        QueryResponse response = new QueryResponse();
        when(solrServer.query(any())).thenReturn(response);
        
        // execute
        QueryResponse retResponse = solrQueryExecutor.executeQuery(new SolrQuery());
        
        // assert
        assertTrue(retResponse == response);
        
    }
    
    @Test(expected = SolrSearchExecutionException.class)
    public void executeQuery_SOLR_EXCEPTION() throws SolrServerException {
        // given
        when(solrServer.query(any())).thenThrow(new SolrServerException(""));
        
        // execute
        solrQueryExecutor.executeQuery(new SolrQuery());
    }
    
    @Test(expected = NullPointerException.class)
    public void executeQuery_NULL_QUERY() {
        // execute
        solrQueryExecutor.executeQuery(null);
    }
    
}
