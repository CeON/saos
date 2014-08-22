package pl.edu.icm.saos.search.config.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;
import pl.edu.icm.saos.search.config.model.SolrConfigurationException;

/**
 * @author madryk
 */
@Service
public class SolrLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger log = LoggerFactory.getLogger(SolrLoader.class);
    
    @Autowired SolrServer solrServer;
    
    @Autowired List<IndexConfiguration> indexesConfiguration;


    public void load() {
        for (IndexConfiguration indexConfiguration : indexesConfiguration) {
            log.info("Loading solr index with name {}", indexConfiguration.getName());
            try {
                loadIndex(indexConfiguration);
            } catch (SolrServerException | IOException e) {
                throw new SolrConfigurationException("Unable to load index with name " + indexConfiguration.getName(), e);
            }
        }
    }
    
    protected void loadIndex(IndexConfiguration indexConfiguration) throws SolrServerException, IOException {
        CoreAdminRequest request = new CoreAdminRequest();
        request.setAction(CoreAdminAction.STATUS);
        CoreAdminResponse cores = request.process(solrServer);
        
        NamedList<Object> judgmentsIndex = cores.getCoreStatus(indexConfiguration.getName());
        if (judgmentsIndex == null) {
            CoreAdminRequest.createCore(indexConfiguration.getName(), indexConfiguration.getInstanceDir(), solrServer);
        } else {
            CoreAdminRequest.reloadCore(indexConfiguration.getName(), solrServer);
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        load();
    }
}
