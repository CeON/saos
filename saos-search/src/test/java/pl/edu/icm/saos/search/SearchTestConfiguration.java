package pl.edu.icm.saos.search;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.ConfigSolr;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import pl.edu.icm.saos.common.TestConfigurationBase;
import pl.edu.icm.saos.enrichment.EnrichmentTestConfiguration;
import pl.edu.icm.saos.search.config.model.IndexConfiguration;
import pl.edu.icm.saos.search.config.model.SolrConfigurationException;
import pl.edu.icm.saos.search.config.service.EmbeddedSolrIndexReloader;
import pl.edu.icm.saos.search.config.service.IndexReloader;
import pl.edu.icm.saos.search.config.service.SolrHomeLocationPolicy;
import pl.edu.icm.saos.search.util.SolrConstants;

/**
 * @author madryk
 */
@Import({ SearchConfiguration.class, EnrichmentTestConfiguration.class})
public class SearchTestConfiguration extends TestConfigurationBase {

    private static final String CONF_BASE_CLASSPATH = "pl/edu/icm/saos/search/config";

    @Autowired
    private Environment environment;

    @Bean
    @Autowired
    public SolrServer solrServer(CoreContainer coreContainer) {
        return null;
    }
    
    @Bean
    @Autowired
    public CoreContainer coreContainer(SolrHomeLocationPolicy solrHomeLocationPolicy) {
        CoreContainer coreContainer;
        ClassPathResource solrConfFile = new ClassPathResource(CONF_BASE_CLASSPATH + "/" + SolrConstants.SOLR_CONFIG_FILENAME);
        String solrHome = solrHomeLocationPolicy.getSolrHome();
        
        SolrResourceLoader loader = new SolrResourceLoader(solrHome);
        
        try (InputStream solrConfStream = solrConfFile.getInputStream()) {
            ConfigSolr cs = ConfigSolr.fromInputStream(loader, solrConfStream);
            coreContainer = new CoreContainer(loader, cs);
        } catch (IOException e) {
            IOUtils.closeQuietly(loader);
            throw new SolrConfigurationException("Unable to read solr.xml file", e);
        }
        
        return coreContainer;
    }

    @Bean
    @Autowired
    public IndexReloader indexReloader(CoreContainer coreContainer) {
        EmbeddedSolrIndexReloader indexReloader = new EmbeddedSolrIndexReloader();
        indexReloader.setCoreContainer(coreContainer);        
        return indexReloader;
    }

    @Bean
    @Autowired
    public SolrServer solrJudgmentsServer(CoreContainer coreContainer) {
        IndexConfiguration judgmentsConfiguration = judgmentsIndexConfiguration();
        EmbeddedSolrServer solrServer = new EmbeddedSolrServer(coreContainer, judgmentsConfiguration.getName());
        return solrServer;
    }

    @Bean
    public IndexConfiguration judgmentsIndexConfiguration() {
        IndexConfiguration judgmentsIndex = new IndexConfiguration();
        judgmentsIndex.setName("judgmentsTest");
        judgmentsIndex.setInstanceDir("judgmentsTest");
        judgmentsIndex.setCreateIndexPropertyFile(true);
        judgmentsIndex.setPersistent(false);

        String judgmentsConfFilesClassPath = CONF_BASE_CLASSPATH + "/judgments/conf/";

        judgmentsIndex.addConfigurationFile(new ClassPathResource(judgmentsConfFilesClassPath + "schema.xml"));
        judgmentsIndex.addConfigurationFile(new ClassPathResource(judgmentsConfFilesClassPath + "solrconfig.xml"));
        judgmentsIndex.addConfigurationFile(new ClassPathResource(judgmentsConfFilesClassPath + "stopwords.txt"));
        judgmentsIndex.addConfigurationFile(new ClassPathResource(judgmentsConfFilesClassPath + "synonyms.txt"));
        judgmentsIndex.addConfigurationFile(new ClassPathResource(judgmentsConfFilesClassPath + "currency.xml"));

        return judgmentsIndex;
    }
}
