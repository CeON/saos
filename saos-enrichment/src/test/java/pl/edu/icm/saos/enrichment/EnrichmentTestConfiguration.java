package pl.edu.icm.saos.enrichment;

import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.persistence.PersistenceTestConfiguration;

/**
 * @author Łukasz Dumiszewski
 */

@Import({EnrichmentTestConfiguration.class, PersistenceTestConfiguration.class})
public class EnrichmentTestConfiguration {

}
