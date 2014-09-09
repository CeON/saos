package pl.edu.icm.saos.search;

import java.io.File;
import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import pl.edu.icm.saos.common.CommonTestConfiguration;
import pl.edu.icm.saos.persistence.PersistenceConfiguration;
import pl.edu.icm.saos.search.config.EmbeddedSolrIndexReloader;
import pl.edu.icm.saos.search.config.model.IndexConfiguration;
import pl.edu.icm.saos.search.config.service.IndexReloader;
import pl.edu.icm.saos.search.util.SearchIOUtils;
import pl.edu.icm.saos.search.util.SolrConstants;

/**
 * @author madryk
 */
@Import({ SearchConfiguration.class, PersistenceConfiguration.class })
public class SearchTestConfiguration extends CommonTestConfiguration {

    private static Logger log = LoggerFactory.getLogger(SearchTestConfiguration.class);

    private static final String CONF_BASE_CLASSPATH = "pl/edu/icm/saos/search/config";

    @Autowired
    private Environment environment;

    @Bean
    public SolrServer solrServer() {
        return null;
    }
    
    @Bean 
    public IndexReloader indexReloader() throws IOException {
        EmbeddedSolrIndexReloader indexReloader = new EmbeddedSolrIndexReloader();
        indexReloader.setCoreContainer(solrCoreContainer());
        return indexReloader;
    }
    
    @Bean
    public CoreContainer solrCoreContainer() throws IOException {
        String solrHome = environment.getProperty("solr.index.configuration.home");
        
        ClassPathResource solrConfFile = new ClassPathResource(CONF_BASE_CLASSPATH + "/" + SolrConstants.SOLR_CONFIG_FILENAME);
        File f = File.createTempFile("solr", ".xml");
        SearchIOUtils.copyResource(solrConfFile, f);

        CoreContainer coreContainer = CoreContainer.createAndLoad(solrHome, f);
        f.delete();
        return coreContainer;
    }
    
    @Bean
    public SolrServer solrJudgmentsServer() throws IOException {
        IndexConfiguration judgmentsConfiguration = judgmentsIndexConfiguration();
        EmbeddedSolrServer solrServer = new EmbeddedSolrServer(solrCoreContainer(), judgmentsConfiguration.getName());
        return solrServer;
    }
    
    @Bean
    public IndexConfiguration judgmentsIndexConfiguration() {
        IndexConfiguration judgmentsIndex = new IndexConfiguration();
        judgmentsIndex.setName("judgments");
        judgmentsIndex.setInstanceDir("judgments");
        judgmentsIndex.setCreateIndexPropertyFile(true);
        
        String judgmentsConfFilesClassPath = CONF_BASE_CLASSPATH + "/judgments/conf/";
        
        judgmentsIndex.addConfigurationFile(new ClassPathResource(judgmentsConfFilesClassPath + "schema.xml"));
        judgmentsIndex.addConfigurationFile(new ClassPathResource(judgmentsConfFilesClassPath + "solrconfig.xml"));
        judgmentsIndex.addConfigurationFile(new ClassPathResource(judgmentsConfFilesClassPath + "stopwords.txt"));
        judgmentsIndex.addConfigurationFile(new ClassPathResource(judgmentsConfFilesClassPath + "synonyms.txt"));
        
        return judgmentsIndex;
    }
}
