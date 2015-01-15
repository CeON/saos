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
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceNacJudgment;

/**
 * @author madryk
 */
@Configuration
@ComponentScan
public class NacjImportJobConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobRepository jobRepository;
    
    
    @Autowired
    @Qualifier("nacjImportDownloadReader")
    private JsonImportDownloadReader nacjImportDownloadReader;
    
    @Autowired
    @Qualifier("nacjImportDownloadProcessor")
    private JsonImportDownloadProcessor<RawSourceNacJudgment> nacjImportDownloadProcessor;
    
    @Autowired
    private JudgmentImportDownloadWriter nacjImportDownloadWriter;
    
    @Autowired
    private NotApiImportDownloadStepExecutionListener nacjImportDownloadStepExecutionListener;
    
    
    
    @Bean
    public Job acJudgmentImportDownloadJob() {
        return jobs.get("IMPORT_NAC_JUDGMENTS_download")
                .start(nacJudgmentImportDownloadStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }
    
    @Bean
    protected Step nacJudgmentImportDownloadStep() {
        return steps.get("nacJudgmentImportDownloadStep").<String, RawSourceNacJudgment> chunk(20)
            .reader(nacjImportDownloadReader)
            .processor(nacjImportDownloadProcessor)
            .writer(nacjImportDownloadWriter)
            .listener(nacjImportDownloadStepExecutionListener)
            .build();
    }
}
