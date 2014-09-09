package pl.edu.icm.saos.batch;

import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.common.CommonTestConfiguration;
import pl.edu.icm.saos.importer.ImportConfiguration;
import pl.edu.icm.saos.persistence.PersistenceConfiguration;
import pl.edu.icm.saos.search.SearchTestConfiguration;

/**
 * @author Łukasz Dumiszewski
 */
@Import({BatchConfiguration.class, ImportConfiguration.class, PersistenceConfiguration.class, SearchTestConfiguration.class})
public class BatchTestConfiguration extends CommonTestConfiguration {
    
     
}
