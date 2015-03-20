package pl.edu.icm.saos.webapp;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.api.ApiTestConfiguration;
import pl.edu.icm.saos.common.TestConfigurationBase;
import pl.edu.icm.saos.enrichment.EnrichmentTestConfiguration;
import pl.edu.icm.saos.search.SearchTestConfiguration;
import pl.edu.icm.saos.webapp.analysis.UiAnalysisConfiguration;


/**
 * 
 * @author Łukasz Pawełczak
 *
 */
@Configuration
@Import({WebappConfiguration.class, GeneralConfiguration.class, SecurityConfiguration.class, SearchTestConfiguration.class, EnrichmentTestConfiguration.class, ApiTestConfiguration.class, UiAnalysisConfiguration.class})
public class WebappTestConfiguration extends TestConfigurationBase { 
}
