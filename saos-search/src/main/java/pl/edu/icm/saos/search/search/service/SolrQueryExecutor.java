package pl.edu.icm.saos.search.search.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pl.edu.icm.saos.search.search.exceptions.SolrSearchExecutionException;

/**
 * Executor of solr {@link SolrQuery queries}
 * 
 * @author madryk
 */
@Service
public class SolrQueryExecutor {

    private SolrServer solrServer;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Executes provided {@link SolrQuery}
     * 
     * @throws SolrSearchExecutionException if solr responded with error
     */
    public QueryResponse executeQuery(SolrQuery query) {
        
        Preconditions.checkNotNull(query);
        
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
