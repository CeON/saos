package pl.edu.icm.saos.search;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.params.HighlightParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.config.service.IndexReloader;
import pl.edu.icm.saos.search.config.service.SolrIndexReloader;
import pl.edu.icm.saos.search.search.model.HighlightingFieldParams;
import pl.edu.icm.saos.search.search.model.HighlightingParams;

/**
 * @author madryk
 */
@Configuration
@ComponentScan
public class SearchConfiguration {
    
    private static Logger log = LoggerFactory.getLogger(SearchConfiguration.class);
    
    private static final String CONF_BASE_CLASSPATH = "pl/edu/icm/saos/search/config";
    
    @Autowired
    private Environment environment;
    
    @Bean
    public SolrServer solrServer() {
        String solrServerUrl = environment.getProperty("solr.index.url");
        log.info("== SOLR SERVER URL: " + solrServerUrl + "  == ");
        
        HttpSolrServer solrServer = new HttpSolrServer(solrServerUrl);

        return solrServer;
    }
    
    @Bean
    public IndexReloader indexReloader() {
        SolrIndexReloader indexReloader = new SolrIndexReloader();
        indexReloader.setSolrServer(solrServer());
        return indexReloader;
    }
    
    @Bean
    public SolrServer solrJudgmentsServer() {
        String solrServerUrl = environment.getProperty("solr.index.url");
        
        IndexConfiguration judgmentsConfiguration = judgmentsIndexConfiguration();
        HttpSolrServer solrServer = new HttpSolrServer(solrServerUrl + "/" + judgmentsConfiguration.getName());
        
        return solrServer;
    }
    
    @Bean
    public IndexConfiguration judgmentsIndexConfiguration() {
        IndexConfiguration judgmentsIndex = new IndexConfiguration();
        judgmentsIndex.setName("judgments");
        judgmentsIndex.setInstanceDir("judgments");
        
        String judgmentsConfFilesClassPath = CONF_BASE_CLASSPATH + "/judgments/conf/";
        
        judgmentsIndex.addConfigurationFile(new ClassPathResource(judgmentsConfFilesClassPath + "schema.xml"));
        judgmentsIndex.addConfigurationFile(new ClassPathResource(judgmentsConfFilesClassPath + "solrconfig.xml"));
        judgmentsIndex.addConfigurationFile(new ClassPathResource(judgmentsConfFilesClassPath + "stopwords.txt"));
        judgmentsIndex.addConfigurationFile(new ClassPathResource(judgmentsConfFilesClassPath + "synonyms.txt"));
        
        return judgmentsIndex;
    }
    
    @Bean
    public HighlightingParams judgmentsHighlightParams() {
        HighlightingParams params = new HighlightingParams();
        
        params.addParam(HighlightParams.SIMPLE_PRE, "<em>");
        params.addParam(HighlightParams.SIMPLE_POST, "</em>");
        params.addParam(HighlightParams.MERGE_CONTIGUOUS_FRAGMENTS, "true");
        
        HighlightingFieldParams contentFieldParams = new HighlightingFieldParams(JudgmentIndexField.CONTENT.getFieldName());
        contentFieldParams.addParam(HighlightParams.FRAGSIZE, "200");
        contentFieldParams.addParam(HighlightParams.SNIPPETS, "4");
        contentFieldParams.addParam(HighlightParams.ALTERNATE_FIELD, "content");
        contentFieldParams.addParam(HighlightParams.ALTERNATE_FIELD_LENGTH, "800");
        params.addFieldParams(contentFieldParams);
        
        return params;
    }
}
