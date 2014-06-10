package pl.edu.icm.saos.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.batch.core.TestProcessor;
import pl.edu.icm.saos.batch.core.TestReader;
import pl.edu.icm.saos.batch.core.TestWriter;
import pl.edu.icm.saos.importer.ImportConfiguration;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@Import({BatchConfiguration.class, ImportConfiguration.class})
public class BatchTestConfiguration {

    
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private TestReader testReader;
    
    @Autowired
    private TestWriter testWriter;
    
    @Autowired
    private TestProcessor testProcessor;
    
    
    @Bean
    public Job testJob() {
        return jobs.get("testJob").start(step1()).build();
    }

    @Bean
    protected Step step1() {
        return steps.get("step1").<String, String> chunk(10)
            .reader(testReader)
            .processor(testProcessor)
            .writer(testWriter)
            .build();
    }
}
