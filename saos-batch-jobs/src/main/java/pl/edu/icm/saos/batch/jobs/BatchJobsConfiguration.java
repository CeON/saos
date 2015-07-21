package pl.edu.icm.saos.batch.jobs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Service;


/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan(useDefaultFilters=false, includeFilters={@Filter(type=FilterType.ANNOTATION, value=Service.class)})
@Import({ CcjImportJobConfiguration.class, ScjImportJobConfiguration.class, CtjImportJobConfiguration.class,
    NacjImportJobConfiguration.class, IndexingJobConfiguration.class, TagPostUploadJobConfiguration.class })
public class BatchJobsConfiguration {
    
    
    
}
