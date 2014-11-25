package pl.edu.icm.saos.importer;

import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.common.TestConfigurationBase;
import pl.edu.icm.saos.persistence.PersistenceTestConfiguration;

/**
 * @author Łukasz Dumiszewski
 */

@Import({ImportConfiguration.class, PersistenceTestConfiguration.class})
public class ImportTestConfiguration extends TestConfigurationBase {

}
