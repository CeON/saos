package pl.edu.icm.saos.batch.jobs;

import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.batch.core.BatchCoreTestConfiguration;
import pl.edu.icm.saos.common.TestConfigurationBase;
import pl.edu.icm.saos.importer.ImportTestConfiguration;
import pl.edu.icm.saos.search.SearchTestConfiguration;

@Import({ BatchJobsConfiguration.class, BatchCoreTestConfiguration.class, ImportTestConfiguration.class, SearchTestConfiguration.class })
public class BatchJobsTestConfiguration extends TestConfigurationBase {

    
    
}
