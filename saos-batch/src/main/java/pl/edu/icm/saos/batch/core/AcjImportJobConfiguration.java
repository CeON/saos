package pl.edu.icm.saos.batch.core;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.importer.common.JudgmentImportDownloadWriter;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadProcessor;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadReader;
import pl.edu.icm.saos.importer.notapi.common.NotApiImportDownloadStepExecutionListener;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceAcJudgment;

/**
 * @author madryk
 */
@Configuration
@ComponentScan
public class AcjImportJobConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobRepository jobRepository;
    
    
    @Autowired
    @Qualifier("acjImportDownloadReader")
    private JsonImportDownloadReader acjImportDownloadReader;
    
    @Autowired
    @Qualifier("acjImportDownloadProcessor")
    private JsonImportDownloadProcessor<RawSourceAcJudgment> acjImportDownloadProcessor;
    
    @Autowired
    private JudgmentImportDownloadWriter acjImportDownloadWriter;
    
    @Autowired
    private NotApiImportDownloadStepExecutionListener acjImportDownloadStepExecutionListener;
    
    
    
    @Bean
    public Job acJudgmentImportDownloadJob() {
        return jobs.get("IMPORT_AC_JUDGMENTS_download")
                .start(acJudgmentImportDownloadStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }
    
    @Bean
    protected Step acJudgmentImportDownloadStep() {
        return steps.get("acJudgmentImportDownloadStep").<String, RawSourceAcJudgment> chunk(20)
            .reader(acjImportDownloadReader)
            .processor(acjImportDownloadProcessor)
            .writer(acjImportDownloadWriter)
            .listener(acjImportDownloadStepExecutionListener)
            .build();
    }
}
