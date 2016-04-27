package pl.edu.icm.saos.batch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
import pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.json.SourceNacJudgment;
import pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.process.NacjImportProcessStepExecutionListener;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceNacJudgment;

/**
 * @author madryk
 */
@Configuration
public class NacjImportJobConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;
    
    
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
    
    @Autowired
    private ContentDownloadStepExecutionListener nacjContentDownloadStepExecutionListener;
    
    
    
    @Autowired
    private JudgmentImportProcessReader<RawSourceNacJudgment> nacjImportProcessReader;

    @Autowired
    private JsonJudgmentImportProcessProcessor<SourceNacJudgment, NationalAppealChamberJudgment> nacjImportProcessProcessor;
    
    @Autowired
    private NacjImportProcessStepExecutionListener nacjImportProcessStepExecutionListener;
    
    @Autowired
    private ContentProcessChunkListener contentProcessChunkListener;
    
    @Bean
    public JudgmentImportProcessWriter<NationalAppealChamberJudgment> nacjImportProcessWriter() {
        return new JudgmentImportProcessWriter<>();
    }
    
    
    
    @Bean
    public Job nacJudgmentImportDownloadJob() {
        return jobs.get("IMPORT_NAC_JUDGMENTS_download")
                .start(nacJudgmentImportDownloadStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }
    
    @Bean
    protected Step nacJudgmentImportDownloadStep() {
        return steps.get("nacJudgmentImportDownloadStep").<JsonJudgmentItem, RawSourceNacJudgment> chunk(20)
            .reader(nacjImportDownloadReader)
            .processor(nacjImportDownloadProcessor)
            .writer(nacjImportDownloadWriter)
            .listener(nacjImportDownloadStepExecutionListener)
            .listener(nacjContentDownloadStepExecutionListener)
            .build();
    }
    
    
    
    @Bean
    public Job nacJudgmentImportProcessJob() {
        return jobs.get("IMPORT_NAC_JUDGMENTS_process").start(nacJudgmentImportProcessStep()).incrementer(new RunIdIncrementer()).build();
    }
    
    @Bean
    protected Step nacJudgmentImportProcessStep() {
        return steps.get("nacJudgmentImportProcessStep").<RawSourceNacJudgment, JudgmentWithCorrectionList<NationalAppealChamberJudgment>> chunk(20)
            .reader(nacjImportProcessReader)
            .processor(nacjImportProcessProcessor)
            .writer(nacjImportProcessWriter())
            .listener(nacjImportProcessStepExecutionListener)
            .listener(contentProcessChunkListener)
            .build();
    }
    
    
    
    @Bean
    public Job nacJudgmentImportJob() {
        return jobs.get("IMPORT_NAC_JUDGMENTS")
                .start(nacJudgmentImportDownloadStep())
                .next(nacJudgmentImportProcessStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
