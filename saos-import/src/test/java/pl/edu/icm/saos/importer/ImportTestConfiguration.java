package pl.edu.icm.saos.importer;

import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.common.CommonTestConfiguration;
import pl.edu.icm.saos.persistence.PersistenceConfiguration;

/**
 * @author Łukasz Dumiszewski
 */

@Import({ImportConfiguration.class, PersistenceConfiguration.class})
public class ImportTestConfiguration extends CommonTestConfiguration {

}
