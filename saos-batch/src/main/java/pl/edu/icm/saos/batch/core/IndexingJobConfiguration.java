package pl.edu.icm.saos.batch.core;

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
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.search.indexing.IndexingJobStepExecutionListener;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingProcessor;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingReader;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingWriter;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingData;
import pl.edu.icm.saos.search.indexing.ReindexJobStepExecutionListener;

@Configuration
@ComponentScan
public class IndexingJobConfiguration {
    
    private final static int INDEXING_CHUNK_SIZE = 10;
    
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    
    // indexing beans

    @Autowired
    private JudgmentIndexingReader judgmentIndexingReader;
    
    @Autowired
    private JudgmentIndexingProcessor judgmentIndexingProcessor;
    
    @Autowired
    private JudgmentIndexingWriter judgmentIndexingWriter;
    
    @Autowired
    private IndexingJobStepExecutionListener indexingJobStepExecutionListener;
    
    
    // reindex specific beans
    
    @Autowired
    private ReindexJobStepExecutionListener reindexJobStepExecutionListener;
    
    
    //------------------------ LOGIC --------------------------
    
    @Bean
    @Autowired
    public Job judgmentIndexingJob(TaskExecutor judgmentIndexingTaskExecutor) {
        return jobs.get("INDEX_NOT_INDEXED_JUDGMENTS")
                .start(judgmentIndexingProcessStep(judgmentIndexingTaskExecutor))
                .incrementer(new RunIdIncrementer())
                .build();
    }
    
    /**
     * Job that reindexes all judgments.
     * It supports job parameter <code>sourceCode</code>. It limits reindexing
     * to judgments with {@link JudgmentSourceInfo#getSourceCode()} equals to
     * parameter value.
     * 
     * @param judgmentIndexingTaskExecutor
     * @return
     */
    @Bean
    @Autowired
    public Job judgmentReindexingJob(TaskExecutor judgmentIndexingTaskExecutor) {
        return jobs.get("REINDEX_JUDGMENTS")
                .start(judgmentReindexingProcessStep(judgmentIndexingTaskExecutor))
                .incrementer(new RunIdIncrementer())
                .build();
    }
    
    @Bean
    public TaskExecutor judgmentIndexingTaskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(4);
        return taskExecutor;
    }
    
    @Bean
    @Autowired
    protected Step judgmentIndexingProcessStep(TaskExecutor judgmentIndexingTaskExecutor) {
        return steps.get("judgmentIndexingStep").<JudgmentIndexingData, SolrInputDocument> chunk(INDEXING_CHUNK_SIZE)
                .reader(judgmentIndexingReader)
                .processor(judgmentIndexingProcessor)
                .writer(judgmentIndexingWriter)
                .listener(indexingJobStepExecutionListener)
                .taskExecutor(judgmentIndexingTaskExecutor)
                .build();
    }
    
    @Bean
    protected Step judgmentReindexingProcessStep(TaskExecutor judgmentIndexingTaskExecutor) {
        return steps.get("judgmentReindexingProcessStep").<JudgmentIndexingData, SolrInputDocument> chunk(INDEXING_CHUNK_SIZE)
                .reader(judgmentIndexingReader)
                .processor(judgmentIndexingProcessor)
                .writer(judgmentIndexingWriter)
                .listener(reindexJobStepExecutionListener)
                .taskExecutor(judgmentIndexingTaskExecutor)
                .build();
    }
}
