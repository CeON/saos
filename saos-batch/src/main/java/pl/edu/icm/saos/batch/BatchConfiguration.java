package pl.edu.icm.saos.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
@EnableBatchProcessing
@Import(JobConfiguration.class)
public class BatchConfiguration {
    
    
}
