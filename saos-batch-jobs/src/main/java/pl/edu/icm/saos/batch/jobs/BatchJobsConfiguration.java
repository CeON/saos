package pl.edu.icm.saos.batch.jobs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@Import({ CcjImportJobConfiguration.class, ScjImportJobConfiguration.class, CtjImportJobConfiguration.class,
    NacjImportJobConfiguration.class, IndexingJobConfiguration.class, TagPostUploadJobConfiguration.class })
public class BatchJobsConfiguration {
    
    
    
}
