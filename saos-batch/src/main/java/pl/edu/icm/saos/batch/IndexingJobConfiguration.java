package pl.edu.icm.saos.batch;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingProcessor;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingReader;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingWriter;

@Configuration
@ComponentScan
public class IndexingJobConfiguration {
    
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;


    @Autowired
    private JudgmentIndexingReader judgmentIndexingReader;
    
    @Autowired
    private JudgmentIndexingProcessor judgmentIndexingProcessor;
    
    @Autowired
    private JudgmentIndexingWriter judgmentIndexingWriter;
    
    @Bean
    public Job judgmentIndexingJob() {
        return jobs.get("judgmentIndexingJob").start(ccJudgmentIndexingProcessStep()).incrementer(new RunIdIncrementer()).build();
    }
    
    @Bean
    @Autowired
    protected Step ccJudgmentIndexingProcessStep() {
        return steps.get("judgmentIndexingStep").<Judgment, SolrInputDocument> chunk(20)
                .reader(judgmentIndexingReader)
                .processor(judgmentIndexingProcessor)
                .writer(judgmentIndexingWriter)
                .build();
    }
}
