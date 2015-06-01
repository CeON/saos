package pl.edu.icm.saos.batch.core;

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

import pl.edu.icm.saos.enrichment.hash.JudgmentEnrichmentTags;
import pl.edu.icm.saos.enrichment.hash.MarkProcessedJobExecutionListener;
import pl.edu.icm.saos.enrichment.hash.UpdateEnrichmentHashProcessor;
import pl.edu.icm.saos.enrichment.hash.UpdateEnrichmentHashReader;
import pl.edu.icm.saos.enrichment.hash.UpdateEnrichmentHashWriter;
import pl.edu.icm.saos.persistence.enrichment.model.JudgmentEnrichmentHash;

@Configuration
@ComponentScan
public class TagPostUploadJobConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobRepository jobRepository;
    
    
    @Autowired
    private UpdateEnrichmentHashReader updateEnrichmentHashReader;
    
    @Autowired
    private UpdateEnrichmentHashProcessor updateEnrichmentHashProcessor;
    
    @Autowired
    private UpdateEnrichmentHashWriter updateEnrichmentHashWriter;
    
    @Autowired
    private MarkProcessedJobExecutionListener markProcessedJobExecutionListener;
    
    
    @Bean
    public Job tagPostUploadJob() {
        return jobs.get("TAG_POST_UPLOAD_PROCESSING")
                .start(updateEnrichmentHash())
                .listener(markProcessedJobExecutionListener)
                .incrementer(new RunIdIncrementer()).build();
    }
    
    @Bean
    protected Step updateEnrichmentHash() {
        return steps.get("updateEnrichmentHashStep").<JudgmentEnrichmentTags, JudgmentEnrichmentHash> chunk(50)
            .reader(updateEnrichmentHashReader)
            .processor(updateEnrichmentHashProcessor)
            .writer(updateEnrichmentHashWriter)
            .build();
    } 
}
