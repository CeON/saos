package pl.edu.icm.saos.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import pl.edu.icm.saos.importer.commoncourt.download.CcjImportDownloadProcessor;
import pl.edu.icm.saos.importer.commoncourt.download.CcjImportDownloadReader;
import pl.edu.icm.saos.importer.commoncourt.download.CcjImportDownloadWriter;
import pl.edu.icm.saos.importer.commoncourt.download.SourceCcJudgmentTextData;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;


@Configuration
@ComponentScan
public class JobConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    
    
    @Autowired
    private CcjImportDownloadWriter ccjImportDownloadWriter;
    
    @Autowired
    private CcjImportDownloadProcessor ccjImportDownloadProcessor;
    
    
    @Scope(value="step", proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Bean
    public CcjImportDownloadReader ccjImportDownloadReader() {
        return new CcjImportDownloadReader();
    }
    
    
    @Bean
    public Job judgmentImportJob() {
        return jobs.get("judgmentImportJob").start(ccJudgmentImportStep()).incrementer(new RunIdIncrementer()).build();
    }

    
    
    @Bean
    @Autowired
    protected Step ccJudgmentImportStep() {
        return steps.get("ccJudgmentImportDownloadStep").<SourceCcJudgmentTextData, RawSourceCcJudgment> chunk(20)
            .faultTolerant()
            .reader(ccjImportDownloadReader())
            .processor(ccjImportDownloadProcessor)
            .writer(ccjImportDownloadWriter)
            .build();
    } 
    
    
    
    
  
    
   
}