package pl.edu.icm.saos.batch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import pl.edu.icm.saos.importer.common.JudgmentImportDownloadWriter;
import pl.edu.icm.saos.importer.common.JudgmentImportProcessWriter;
import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.commoncourt.court.CcImportJobExecutionListener;
import pl.edu.icm.saos.importer.commoncourt.court.CommonCourtImportProcessor;
import pl.edu.icm.saos.importer.commoncourt.court.CommonCourtImportWriter;
import pl.edu.icm.saos.importer.commoncourt.court.XmlCommonCourt;
import pl.edu.icm.saos.importer.commoncourt.judgment.download.CcjImportDownloadProcessor;
import pl.edu.icm.saos.importer.commoncourt.judgment.download.CcjImportDownloadReader;
import pl.edu.icm.saos.importer.commoncourt.judgment.download.SourceCcJudgmentTextData;
import pl.edu.icm.saos.importer.commoncourt.judgment.process.CcjImportProcessProcessor;
import pl.edu.icm.saos.importer.commoncourt.judgment.process.CcjImportProcessSkipListener;
import pl.edu.icm.saos.importer.commoncourt.judgment.process.CcjImportProcessSkipPolicy;
import pl.edu.icm.saos.importer.notapi.common.JudgmentImportProcessReader;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;


@Configuration
public class CcjImportJobConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobRepository jobRepository;
    
       
    //======== Common Court Judgment importer ========
    
    
    // import download beans
    
    @Scope(value="step", proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Bean
    public CcjImportDownloadReader ccjImportDownloadReader() {
        return new CcjImportDownloadReader();
    }
    
    @Autowired
    private JudgmentImportDownloadWriter ccjImportDownloadWriter;
    
    @Autowired
    private CcjImportDownloadProcessor ccjImportDownloadProcessor;
    
    
    // import process beans
    
    @Autowired
    private JudgmentImportProcessReader<RawSourceCcJudgment> ccjImportProcessReader;
    
    @Autowired
    private CcjImportProcessProcessor ccjImportProcessProcessor;
    
    @Bean
    public JudgmentImportProcessWriter<CommonCourtJudgment> ccjImportProcessWriter() {
        return new JudgmentImportProcessWriter<>();
    }
    
    @Autowired
    private CcjImportProcessSkipListener ccjImportProcessSkipListener;
    
    @Autowired
    private CcjImportProcessSkipPolicy ccjImportProcessSkipPolicy;
    
    
    @Bean
    public Job ccJudgmentImportJob() {
        return jobs.get("IMPORT_CC_JUDGMENTS").start(ccJudgmentImportDownloadStep()).next(ccJudgmentImportProcessStep()).incrementer(new RunIdIncrementer()).build();
    }

    
    
    @Bean
    public Job ccJudgmentImportDownloadJob() {
        return jobs.get("IMPORT_CC_JUDGMENTS_download").start(ccJudgmentImportDownloadStep()).incrementer(new RunIdIncrementer()).build();
    }
    
    
    @Bean
    public Job ccJudgmentImportProcessJob() {
        return jobs.get("IMPORT_CC_JUDGMENTS_process").start(ccJudgmentImportProcessStep()).incrementer(new RunIdIncrementer()).build();
    }
    
    
    
    @Bean
    @Autowired
    protected Step ccJudgmentImportDownloadStep() {
        return steps.get("ccJudgmentImportDownloadStep").<SourceCcJudgmentTextData, RawSourceCcJudgment> chunk(20)
            .reader(ccjImportDownloadReader())
            .processor(ccjImportDownloadProcessor)
            .writer(ccjImportDownloadWriter)
            .build();
    } 
    
    @Bean
    @Autowired
    protected Step ccJudgmentImportProcessStep() {
        TaskletStep step = steps.get("ccJudgmentImportProcessStep")
                .<RawSourceCcJudgment, JudgmentWithCorrectionList<CommonCourtJudgment>> chunk(20).faultTolerant()
               .skipPolicy(ccjImportProcessSkipPolicy).listener(ccjImportProcessSkipListener)
               .reader(ccjImportProcessReader)
               .processor(ccjImportProcessProcessor)
               .writer(ccjImportProcessWriter())
               .transactionAttribute(new DefaultTransactionAttribute())
               .build();
           step.setTransactionAttribute(new DefaultTransactionAttribute());
           return step;
    } 
    
    
    
    //======== Common Court importer ========
    
    @Autowired
    private CommonCourtImportProcessor ccImportProcessor;
    
    @Autowired
    private CommonCourtImportWriter ccImportWriter;
    
    @Autowired
    private StaxEventItemReader<XmlCommonCourt> ccImportReader;

    @Autowired
    private CcImportJobExecutionListener ccImportJobExecutionListener;
    
   
    @Bean
    public Job commonCourtImportJob() {
        return jobs.get("IMPORT_CC_COURTS").start(commonCourtImportProcessStep()).listener(ccImportJobExecutionListener).incrementer(new RunIdIncrementer()).build();
    }

    
    @Bean
    @Autowired
    protected Step commonCourtImportProcessStep() {
        return steps.get("commonCourtImportStep").<XmlCommonCourt, CommonCourt> chunk(20)
            .reader(ccImportReader)
            .processor(ccImportProcessor)
            .writer(ccImportWriter)
            .build();
    } 
    

}