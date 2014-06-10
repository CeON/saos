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

import pl.edu.icm.saos.importer.commoncourt.CcJudgmentImportProcessor;
import pl.edu.icm.saos.importer.commoncourt.CcJudgmentImportReader;
import pl.edu.icm.saos.importer.commoncourt.CcJudgmentImportWriter;


@Configuration
@ComponentScan
public class JobConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    
    @Autowired
    private CcJudgmentImportReader ccJudgmentImportReader;
    
    @Autowired
    private CcJudgmentImportWriter ccJudgmentImportWriter;
    
    @Autowired
    private CcJudgmentImportProcessor ccJudgmentImportProcessor;
    
    
    @Bean
    public Job judgmentImportJob() {
        return jobs.get("judgmentImportJob").start(ccJudgmentImportStep()).incrementer(new RunIdIncrementer()).build();
    }

    @Bean
    @Autowired
    protected Step ccJudgmentImportStep() {
        return steps.get("ccJudgmentImportStep").<String, String> chunk(10)
            .reader(ccJudgmentImportReader)
            .processor(ccJudgmentImportProcessor)
            .writer(ccJudgmentImportWriter)
            .build();
    } 
    
    
    
    
  
    
   
}