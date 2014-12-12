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

import pl.edu.icm.saos.importer.common.JudgmentImportProcessWriter;
import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadReader;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download.ScjImportDownloadProcessor;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download.ScjImportDownloadStepExecutionListener;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download.ScjImportDownloadWriter;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process.ScjImportProcessProcessor;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process.ScjImportProcessReader;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process.ScjImportProcessStepExecutionListener;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
public class ScjImportJobConfiguration {
   
    
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobRepository jobRepository;
    
    
    
    
    @Autowired
    private JsonImportDownloadReader scjImportDownloadReader;

    @Autowired
    private ScjImportDownloadProcessor scjImportDownloadProcessor;

    @Autowired
    private ScjImportDownloadWriter scjImportDownloadWriter;
    
    @Autowired
    private ScjImportDownloadStepExecutionListener scjImportDownloadStepExecutionListener;
    
    
    
    @Autowired
    private ScjImportProcessReader scjImportProcessReader;

    @Autowired
    private ScjImportProcessProcessor scjImportProcessProcessor;
    
       
    @Autowired
    private ScjImportProcessStepExecutionListener scjImportProcessStepExecutionListener;
    

    @Bean
    public JudgmentImportProcessWriter<SupremeCourtJudgment> scjImportProcessWriter() {
        return new JudgmentImportProcessWriter<>();
    }
    
    /**
     * Only download 
     */
    @Bean
    public Job scJudgmentImportDownloadJob() {
        return jobs.get("IMPORT_SC_JUDGMENTS_download").start(scJudgmentImportDownloadStep()).incrementer(new RunIdIncrementer()).build();
    }
    
    
    @Bean
    protected Step scJudgmentImportDownloadStep() {
        return steps.get("scJudgmentImportDownloadStep").<String, RawSourceScJudgment> chunk(20)
            .reader(scjImportDownloadReader)
            .processor(scjImportDownloadProcessor)
            .writer(scjImportDownloadWriter)
            .listener(scjImportDownloadStepExecutionListener)
            .build();
    } 
    

    /**
     * Only process
     */
    @Bean
    public Job scJudgmentImportProcessJob() {
        return jobs.get("IMPORT_SC_JUDGMENTS_process").start(scJudgmentImportProcessStep()).incrementer(new RunIdIncrementer()).build();
    }
   
    @Bean
    protected Step scJudgmentImportProcessStep() {
        return steps.get("scJudgmentImportProcessStep").<RawSourceScJudgment, JudgmentWithCorrectionList<SupremeCourtJudgment>> chunk(20)
            .reader(scjImportProcessReader)
            .processor(scjImportProcessProcessor)
            .writer(scjImportProcessWriter())
            .listener(scjImportProcessStepExecutionListener)
            .build();
    } 
    
    /**
     * Download and process
     */
    @Bean
    public Job scJudgmentImportJob() {
        return jobs.get("IMPORT_SC_JUDGMENTS").start(scJudgmentImportDownloadStep()).next(scJudgmentImportProcessStep()).incrementer(new RunIdIncrementer()).build();
    }
   
    
}
