package pl.edu.icm.saos.persistence;

import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.common.CommonTestConfiguration;
import pl.edu.icm.saos.common.TestConfigurationBase;

/**
 * @author Łukasz Dumiszewski
 */
@Import({PersistenceConfiguration.class, CommonTestConfiguration.class})
public class PersistenceTestConfiguration extends TestConfigurationBase {
    
}
