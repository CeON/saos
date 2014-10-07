package pl.edu.icm.saos.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.importer.simple.supremecourt.judgment.download.ScjImportDownloadProcessor;
import pl.edu.icm.saos.importer.simple.supremecourt.judgment.download.ScjImportDownloadReader;
import pl.edu.icm.saos.importer.simple.supremecourt.judgment.download.ScjImportDownloadStepExecutionListener;
import pl.edu.icm.saos.importer.simple.supremecourt.judgment.download.ScjImportDownloadWriter;
import pl.edu.icm.saos.persistence.model.importer.SimpleRawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
public class SimpleScjImportJobConfiguration {
   
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobRepository jobRepository;
    
    
    @Autowired
    private ScjImportDownloadWriter scjImportDownloadWriter;
    
    @Autowired
    private ScjImportDownloadProcessor scjImportDownloadProcessor;
    
    @Autowired
    private ScjImportDownloadReader scjImportDownloadReader;
    
    @Autowired
    private ScjImportDownloadStepExecutionListener scjImportDownloadStepExecutionListener;
    
    
    
    @Bean
    public Job scjJudgmentImportDownloadJob() {
        return jobs.get("scjJudgmentImportDownloadJob").start(scJudgmentImportDownloadStep()).incrementer(new RunIdIncrementer()).build();
    }
    
    
    @Bean
    @Autowired
    protected Step scJudgmentImportDownloadStep() {
        return steps.get("ccJudgmentImportDownloadStep").<String, SimpleRawSourceScJudgment> chunk(20)
            .reader(scjImportDownloadReader)
            .processor(scjImportDownloadProcessor)
            .writer(scjImportDownloadWriter)
            .listener(scjImportDownloadStepExecutionListener)
            .build();
    } 
}
