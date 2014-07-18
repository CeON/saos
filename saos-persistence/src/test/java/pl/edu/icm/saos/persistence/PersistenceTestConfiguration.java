package pl.edu.icm.saos.persistence;

import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.common.CommonTestConfiguration;

/**
 * @author Łukasz Dumiszewski
 */
@Import(PersistenceConfiguration.class)
public class PersistenceTestConfiguration extends CommonTestConfiguration {
    
}
