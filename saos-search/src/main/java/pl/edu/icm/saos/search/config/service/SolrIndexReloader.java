package pl.edu.icm.saos.search.config.service;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;
import pl.edu.icm.saos.search.config.model.SolrConfigurationException;

/**
 * Informs solr instance that index should be reloaded. If index doesn't
 * exists then attempts to create it.
 * Can be used only with Solr instances that supports {@link CoreAdminRequest} processing.
 * @author madryk
 */
public class SolrIndexReloader implements IndexReloader {
    
    private static Logger log = LoggerFactory.getLogger(SolrIndexReloader.class);
    
    private SolrServer solrServer;

    
    //------------------------ LOGIC --------------------------

    @Override
    public void reloadIndex(IndexConfiguration indexConfiguration) {
        Preconditions.checkNotNull(solrServer);
        
        boolean indexExists = checkIndexExists(indexConfiguration);

        if (indexExists) {
            reloadIndex(indexConfiguration.getName());
        } else {
            createIndex(indexConfiguration);
        }
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private boolean checkIndexExists(IndexConfiguration indexConfiguration) {
        CoreAdminResponse cores = null;
        
        try {
            CoreAdminRequest request = new CoreAdminRequest();
            request.setAction(CoreAdminAction.STATUS);
            cores = request.process(solrServer);
        } catch (SolrServerException | IOException e) {
            throw new SolrConfigurationException("Unable to check solr indexes status", e);
        }
        
        NamedList<Object> indexStatus = cores.getCoreStatus(indexConfiguration.getName());
        
        return indexStatus != null;
    }
    
    private void reloadIndex(String indexName) {
        log.info("Reloading solr index with name {}", indexName);
        try {
            CoreAdminRequest.reloadCore(indexName, solrServer);
        } catch (SolrServerException | IOException e) {
            throw new SolrConfigurationException("Unable to reload index with name " + indexName, e);
        }
    }
    
    private void createIndex(IndexConfiguration indexConfiguration) {
        log.info("Creating solr index with name {}", indexConfiguration.getName());
        try {
            CoreAdminRequest.createCore(indexConfiguration.getName(), indexConfiguration.getInstanceDir(), solrServer);
        } catch (SolrServerException | IOException e) {
            throw new SolrConfigurationException("Unable to create index with name " + indexConfiguration.getName(), e);
        }
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setSolrServer(SolrServer solrServer) {
        this.solrServer = solrServer;
    }
}
