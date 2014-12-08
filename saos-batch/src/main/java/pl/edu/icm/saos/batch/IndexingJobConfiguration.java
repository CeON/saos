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
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingProcessor;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingReader;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingWriter;
import pl.edu.icm.saos.search.indexing.ResetJudgmentIndexFlagStepExecutionListener;

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
    
    
    // reseting indexed flag beans
    
    @Autowired
    private ResetJudgmentIndexFlagStepExecutionListener resetJudgmentIndexFlagStepExecutionListener;
    
    
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
        return steps.get("judgmentIndexingStep").<Judgment, SolrInputDocument> chunk(INDEXING_CHUNK_SIZE)
                .reader(judgmentIndexingReader)
                .processor(judgmentIndexingProcessor)
                .writer(judgmentIndexingWriter)
                .taskExecutor(judgmentIndexingTaskExecutor)
                .build();
    }
    
    @Bean
    protected Step judgmentReindexingProcessStep(TaskExecutor judgmentIndexingTaskExecutor) {
        return steps.get("judgmentResetIndexFlagStep").<Judgment, SolrInputDocument> chunk(INDEXING_CHUNK_SIZE)
                .reader(judgmentIndexingReader)
                .processor(judgmentIndexingProcessor)
                .writer(judgmentIndexingWriter)
                .listener(resetJudgmentIndexFlagStepExecutionListener)
                .taskExecutor(judgmentIndexingTaskExecutor)
                .build();
    }
}
