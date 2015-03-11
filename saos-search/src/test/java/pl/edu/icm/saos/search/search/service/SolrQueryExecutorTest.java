package pl.edu.icm.saos.search.search.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;

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
    
    @Test(expected = SolrServerException.class)
    public void executeQuery_SOLR_EXCEPTION() throws SolrServerException {
        // given
        when(solrServer.query(any())).thenThrow(new SolrServerException(""));
        
        // execute
        solrQueryExecutor.executeQuery(new SolrQuery());
    }
    
}
