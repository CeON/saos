package pl.edu.icm.saos.search.search.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.search.exceptions.SolrSearchExecutionException;

/**
 * @author madryk
 */
@Service
public class SolrQueryExecutor {

    private SolrServer solrServer;
    
    
    //------------------------ LOGIC --------------------------
    
    public QueryResponse executeQuery(SolrQuery query) {
        QueryResponse response = null;
        
        try {
            response = solrServer.query(query);
        } catch (SolrServerException e) {
            throw new SolrSearchExecutionException("Couldn't execute query", e);
        }
        
        return response;
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    public void setSolrServer(SolrServer solrServer) {
        this.solrServer = solrServer;
    }
    
}
