package pl.edu.icm.saos.search.indexing;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * Resets all judgments indexed flag before step execution.
 * It can use job parameter <code>sourceCode</code> to limit
 * reseting to judgments with specified {@link SourceCode}.
 * 
 * @author madryk
 */
@Service
@StepScope
public class ResetJudgmentIndexFlagStepExecutionListener implements StepExecutionListener {

    @Value("#{jobParameters[sourceCode]}")
    private String sourceCode;
    
    private JudgmentRepository judgmentRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        if (StringUtils.isNotBlank(sourceCode)) {
            SourceCode sc = SourceCode.valueOf(StringUtils.upperCase(sourceCode));
            judgmentRepository.markAsNotIndexedBySourceCode(sc);
        } else {
            judgmentRepository.markAllAsNotIndexed();
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }


    //------------------------ SETTERS --------------------------

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

}
