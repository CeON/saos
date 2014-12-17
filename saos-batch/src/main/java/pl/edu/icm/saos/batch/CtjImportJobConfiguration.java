package pl.edu.icm.saos.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.importer.common.JudgmentImportDownloadWriter;
import pl.edu.icm.saos.importer.notapi.common.StringItemImportDownloadProcessor;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadReader;
import pl.edu.icm.saos.importer.notapi.common.NotApiImportDownloadStepExecutionListener;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;

@Configuration
public class CtjImportJobConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobRepository jobRepository;
    
    
    @Autowired
    @Qualifier("ctjImportDownloadReader")
    private JsonImportDownloadReader ctjImportDownloadReader;
    
    @Autowired
    @Qualifier("ctjImportDownloadProcessor")
    private StringItemImportDownloadProcessor<RawSourceCtJudgment> ctjImportDownloadProcessor;
    
    @Autowired
    private JudgmentImportDownloadWriter ctjImportDownloadWriter;
    
    @Autowired
    private NotApiImportDownloadStepExecutionListener ctjImportDownloadStepExecutionListener;
    
    
    
    @Bean
    public Job ctJudgmentImportDownloadJob() {
        return jobs.get("IMPORT_CT_JUDGMENTS_download").start(ctJudgmentImportDownloadStep()).incrementer(new RunIdIncrementer()).build();
    }
    
    @Bean
    protected Step ctJudgmentImportDownloadStep() {
        return steps.get("ctJudgmentImportDownloadStep").<String, RawSourceCtJudgment> chunk(20)
            .reader(ctjImportDownloadReader)
            .processor(ctjImportDownloadProcessor)
            .writer(ctjImportDownloadWriter)
            .listener(ctjImportDownloadStepExecutionListener)
            .build();
    } 
}
