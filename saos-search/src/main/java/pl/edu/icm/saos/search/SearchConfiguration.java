package pl.edu.icm.saos.search;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;

/**
 * @author madryk
 */
@Configuration
@ComponentScan
public class SearchConfiguration {
    
    private static Logger log = LoggerFactory.getLogger(SearchConfiguration.class);
    
    @Autowired
    private Environment environment;
    
    @Bean
    public SolrServer solrServer() {
        log.info("== SOLR SERVER URL: " + environment.getProperty("solr.index.url") + "  == ");
        String solrServerUrl = environment.getProperty("solr.index.url");
        
        HttpSolrServer solrServer = new HttpSolrServer(solrServerUrl);

        return solrServer;
    }
    
    @Bean
    public SolrServer solrJudgmentsServer() {
        String solrServerUrl = environment.getProperty("solr.index.url");
        
        HttpSolrServer solrServer = new HttpSolrServer(solrServerUrl + "/judgments");
        
        return solrServer;
    }
    
    @Bean
    public IndexConfiguration judgmentsIndexConfiguration() {
        IndexConfiguration judgmentsIndex = new IndexConfiguration();
        judgmentsIndex.setName("judgments");
        judgmentsIndex.setInstanceDir("judgments");
        
        return judgmentsIndex;
    }
}
