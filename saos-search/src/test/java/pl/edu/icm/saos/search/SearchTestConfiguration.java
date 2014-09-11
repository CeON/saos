package pl.edu.icm.saos.search;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import pl.edu.icm.saos.common.CommonTestConfiguration;
import pl.edu.icm.saos.persistence.PersistenceConfiguration;
import pl.edu.icm.saos.search.config.CoreContainerFactory;
import pl.edu.icm.saos.search.config.EmbeddedSolrIndexReloader;
import pl.edu.icm.saos.search.config.model.IndexConfiguration;
import pl.edu.icm.saos.search.config.service.IndexReloader;

/**
 * @author madryk
 */
@Import({ SearchConfiguration.class, PersistenceConfiguration.class })
public class SearchTestConfiguration extends CommonTestConfiguration {

    private static final String CONF_BASE_CLASSPATH = "pl/edu/icm/saos/search/config";

    @Autowired
    private Environment environment;

    @Bean
    public SolrServer solrServer() {
        return null;
    }

    @Bean
    @Autowired
    public IndexReloader indexReloader(CoreContainerFactory coreContainerFactory) throws IOException {
        EmbeddedSolrIndexReloader indexReloader = new EmbeddedSolrIndexReloader();
        indexReloader.setCoreContainer(coreContainerFactory.fetchCoreContainer());
        return indexReloader;
    }

    @Bean
    @Autowired
    public SolrServer solrJudgmentsServer(CoreContainerFactory coreContainerFactory) throws IOException {
        IndexConfiguration judgmentsConfiguration = judgmentsIndexConfiguration();
        CoreContainer coreContainer = coreContainerFactory.fetchCoreContainer();
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

        return judgmentsIndex;
    }
}
