package pl.edu.icm.saos.webapp;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.api.ApiTestConfiguration;
import pl.edu.icm.saos.batch.core.JobForcingExecutor;
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
@Import({WebappConfiguration.class, GeneralConfiguration.class, CacheConfiguration.class, SecurityConfiguration.class, SearchTestConfiguration.class, EnrichmentTestConfiguration.class, ApiTestConfiguration.class, UiAnalysisConfiguration.class})
public class WebappTestConfiguration extends TestConfigurationBase { 
    
    /**
     * Mock bean implementation of {@link JobForcingExecutor} to satisfy
     * beans that depends on it and not running real jobs.
     */
    @Bean
    public JobForcingExecutor mockJobForcingExecutor() {
        return Mockito.mock(JobForcingExecutor.class);
    }
    
}
