package pl.edu.icm.saos.batch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.importer.commoncourt.judgment.remove.CcjRemoverProcessor;
import pl.edu.icm.saos.importer.commoncourt.judgment.remove.CcjRemoverReader;
import pl.edu.icm.saos.importer.commoncourt.judgment.remove.CcjRemoverWriter;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.RemovedJudgment;

/**
 * @author madryk
 */
@Configuration
public class CcjRemoveJobConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;


    @Autowired
    private CcjRemoverReader ccjRemoverReader;
    
    @Autowired
    private CcjRemoverProcessor ccjRemoverProcessor;
    
    @Autowired
    private CcjRemoverWriter ccjRemoverWriter;
    
    
    @Bean
    public Job ccJudgmentRemoveObsoleteJob() {
        return jobs.get("REMOVE_OBSOLETE_CC_JUDGMENTS").start(ccJudgmentRemoveObsoleteProcessStep()).incrementer(new RunIdIncrementer()).build();
    }

    
    @Bean
    @Autowired
    protected Step ccJudgmentRemoveObsoleteProcessStep() {
        return steps.get("ccJudgmentRemoveObsoleteProcessStep").<Judgment, RemovedJudgment> chunk(20)
            .reader(ccjRemoverReader)
            .processor(ccjRemoverProcessor)
            .writer(ccjRemoverWriter)
            .build();
    }
    
}
