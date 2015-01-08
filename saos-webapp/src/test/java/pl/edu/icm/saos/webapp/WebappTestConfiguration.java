package pl.edu.icm.saos.webapp;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.edu.icm.saos.api.ApiTestConfiguration;
import pl.edu.icm.saos.common.TestConfigurationBase;
import pl.edu.icm.saos.enrichment.EnrichmentConfiguration;
import pl.edu.icm.saos.persistence.PersistenceTestConfiguration;
import pl.edu.icm.saos.search.SearchTestConfiguration;


/**
 * 
 * @author Łukasz Pawełczak
 *
 */
@Import({WebappConfiguration.class, GeneralConfiguration.class, PersistenceTestConfiguration.class, SearchTestConfiguration.class, EnrichmentConfiguration.class, ApiTestConfiguration.class})
@Configuration
public class WebappTestConfiguration extends TestConfigurationBase { 
}
