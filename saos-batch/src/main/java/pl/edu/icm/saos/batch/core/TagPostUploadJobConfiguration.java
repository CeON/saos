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
import pl.edu.icm.saos.enrichment.hash.EnrichmentHashProcessedFlagMarker;
import pl.edu.icm.saos.enrichment.hash.MarkChangedTagJudgmentsAsNotIndexedReader;
import pl.edu.icm.saos.enrichment.hash.MarkChangedTagJudgmentsAsNotIndexedWriter;
import pl.edu.icm.saos.enrichment.hash.UpdateEnrichmentHashProcessor;
import pl.edu.icm.saos.enrichment.hash.UpdateEnrichmentHashReader;
import pl.edu.icm.saos.enrichment.hash.UpdateEnrichmentHashWriter;
import pl.edu.icm.saos.persistence.enrichment.model.JudgmentEnrichmentHash;

/**
 * Configuration of job that needs to be executed after enrichment tags upload.
 * It perform some changes in various parts of system to maintain coherence
 * with newly uploaded tags.
 * 
 * @author madryk
 */
@Configuration
@ComponentScan
public class TagPostUploadJobConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobRepository jobRepository;
    
    
    //--- update judgment enrichment tags hashes beans --- 
    
    @Autowired
    private UpdateEnrichmentHashReader updateEnrichmentHashReader;
    
    @Autowired
    private UpdateEnrichmentHashProcessor updateEnrichmentHashProcessor;
    
    @Autowired
    private UpdateEnrichmentHashWriter updateEnrichmentHashWriter;
    
    @Autowired
    private EnrichmentHashProcessedFlagMarker enrichmentHashProcessedFlagMarker;
    
    
    //--- mark as not indexed beans ---
    
    @Autowired
    private MarkChangedTagJudgmentsAsNotIndexedReader markChangedTagJudgmentsAsNotIndexedReader;
    
    @Autowired
    private MarkChangedTagJudgmentsAsNotIndexedWriter markChangedTagJudgmentsAsNotIndexedWriter;
    
    
    //--- index not indexed beans ---
    
    @Autowired
    private Step judgmentIndexingProcessStep;
    
    
    //------------------------ LOGIC --------------------------
    
    @Bean
    public Job tagPostUploadJob() {
        return jobs.get("TAG_POST_UPLOAD_PROCESSING")
                .start(updateEnrichmentHashStep())
                .next(markChangedTagJudgmentsAsNotIndexedStep())
                .next(judgmentIndexingProcessStep)
                .listener(enrichmentHashProcessedFlagMarker)
                .incrementer(new RunIdIncrementer()).build();
    }
    
    @Bean
    protected Step updateEnrichmentHashStep() {
        return steps.get("updateEnrichmentHashStep").<JudgmentEnrichmentTags, JudgmentEnrichmentHash> chunk(50)
            .reader(updateEnrichmentHashReader)
            .processor(updateEnrichmentHashProcessor)
            .writer(updateEnrichmentHashWriter)
            .build();
    }
    
    @Bean
    protected Step markChangedTagJudgmentsAsNotIndexedStep() {
        return steps.get("markChangedTagJudgmentsAsNotIndexedStep").<Long, Long> chunk(1000)
                .reader(markChangedTagJudgmentsAsNotIndexedReader)
                .writer(markChangedTagJudgmentsAsNotIndexedWriter)
                .build();
    }
    
}
