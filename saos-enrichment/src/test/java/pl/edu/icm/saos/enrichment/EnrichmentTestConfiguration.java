package pl.edu.icm.saos.enrichment;

import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.batch.core.BatchCoreConfiguration;
import pl.edu.icm.saos.common.TestConfigurationBase;
import pl.edu.icm.saos.persistence.PersistenceTestConfiguration;

/**
 * @author Łukasz Dumiszewski
 */

@Import({EnrichmentConfiguration.class, BatchCoreConfiguration.class, PersistenceTestConfiguration.class})
public class EnrichmentTestConfiguration extends TestConfigurationBase {

     
    
    
}
