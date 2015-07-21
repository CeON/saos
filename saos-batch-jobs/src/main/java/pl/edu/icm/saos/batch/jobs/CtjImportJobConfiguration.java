package pl.edu.icm.saos.batch.jobs;

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
import pl.edu.icm.saos.importer.common.JudgmentImportProcessWriter;
import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadProcessor;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadReader;
import pl.edu.icm.saos.importer.notapi.common.JsonJudgmentImportProcessProcessor;
import pl.edu.icm.saos.importer.notapi.common.JsonJudgmentItem;
import pl.edu.icm.saos.importer.notapi.common.JudgmentImportProcessReader;
import pl.edu.icm.saos.importer.notapi.common.NotApiImportDownloadStepExecutionListener;
import pl.edu.icm.saos.importer.notapi.common.content.ContentDownloadStepExecutionListener;
import pl.edu.icm.saos.importer.notapi.common.content.ContentProcessChunkListener;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process.CtjImportProcessStepExecutionListener;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
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
    private JsonImportDownloadProcessor<RawSourceCtJudgment> ctjImportDownloadProcessor;
    
    @Autowired
    private JudgmentImportDownloadWriter ctjImportDownloadWriter;
    
    @Autowired
    private NotApiImportDownloadStepExecutionListener ctjImportDownloadStepExecutionListener;
    
    @Autowired
    private ContentDownloadStepExecutionListener ctjContentDownloadStepExecutionListener;
    
    
    
    @Autowired
    private JudgmentImportProcessReader<RawSourceCtJudgment> ctjImportProcessReader;

    @Autowired
    private JsonJudgmentImportProcessProcessor<SourceCtJudgment, ConstitutionalTribunalJudgment> ctjImportProcessProcessor;
    
    @Autowired
    private CtjImportProcessStepExecutionListener ctjImportProcessStepExecutionListener;

    @Autowired
    private ContentProcessChunkListener contentProcessChunkListener;
    
    @Bean
    public JudgmentImportProcessWriter<ConstitutionalTribunalJudgment> ctjImportProcessWriter() {
        return new JudgmentImportProcessWriter<>();
    }
    
    
    
    @Bean
    public Job ctJudgmentImportDownloadJob() {
        return jobs.get("IMPORT_CT_JUDGMENTS_download").start(ctJudgmentImportDownloadStep()).incrementer(new RunIdIncrementer()).build();
    }
    
    @Bean
    protected Step ctJudgmentImportDownloadStep() {
        return steps.get("ctJudgmentImportDownloadStep").<JsonJudgmentItem, RawSourceCtJudgment> chunk(20)
            .reader(ctjImportDownloadReader)
            .processor(ctjImportDownloadProcessor)
            .writer(ctjImportDownloadWriter)
            .listener(ctjImportDownloadStepExecutionListener)
            .listener(ctjContentDownloadStepExecutionListener)
            .build();
    }
    
    
    
    @Bean
    public Job ctJudgmentImportProcessJob() {
        return jobs.get("IMPORT_CT_JUDGMENTS_process").start(ctJudgmentImportProcessStep()).incrementer(new RunIdIncrementer()).build();
    }
    
    @Bean
    protected Step ctJudgmentImportProcessStep() {
        return steps.get("ctJudgmentImportProcessStep").<RawSourceCtJudgment, JudgmentWithCorrectionList<ConstitutionalTribunalJudgment>> chunk(20)
            .reader(ctjImportProcessReader)
            .processor(ctjImportProcessProcessor)
            .writer(ctjImportProcessWriter())
            .listener(ctjImportProcessStepExecutionListener)
            .listener(contentProcessChunkListener)
            .build();
    }
    
    
    
    @Bean
    public Job ctJudgmentImportJob() {
        return jobs.get("IMPORT_CT_JUDGMENTS")
                .start(ctJudgmentImportDownloadStep())
                .next(ctJudgmentImportProcessStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
