package pl.edu.icm.saos.batch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.importer.commoncourt.judgment.remove.CcjDeleteRemovedProcessor;
import pl.edu.icm.saos.importer.commoncourt.judgment.remove.CcjDeleteRemovedReader;
import pl.edu.icm.saos.importer.commoncourt.judgment.remove.CcjDeleteRemovedWriter;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.RemovedJudgment;

/**
 * @author madryk
 */
@Configuration
public class CcjDeleteRemovedJobConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;


    @Autowired
    private CcjDeleteRemovedReader ccjDeleteRemovedReader;
    
    @Autowired
    private CcjDeleteRemovedProcessor ccjDeleteRemovedProcessor;
    
    @Autowired
    private CcjDeleteRemovedWriter ccjDeleteRemovedWriter;
    
    
    @Bean
    public Job ccJudgmentDeleteRemovedJob() {
        return jobs.get("DELETE_REMOVED_CC_JUDGMENTS").start(ccJudgmentDeleteRemovedProcessStep()).incrementer(new RunIdIncrementer()).build();
    }

    
    @Bean
    @Autowired
    protected Step ccJudgmentDeleteRemovedProcessStep() {
        return steps.get("ccJudgmentDeleteRemovedProcessStep").<Judgment, RemovedJudgment> chunk(20)
            .reader(ccjDeleteRemovedReader)
            .processor(ccjDeleteRemovedProcessor)
            .writer(ccjDeleteRemovedWriter)
            .build();
    }
    
}
