package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.notapi.common.JudgmentObjectDeleter;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;

@Service
public class CtjImportProcessStepExecutionListener implements
        StepExecutionListener {

    @Autowired
    private JudgmentObjectDeleter judgmentObjectDeleter;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        
        judgmentObjectDeleter.deleteJudgmentsWithoutRawSourceJudgment(ConstitutionalTribunalJudgment.class, RawSourceCtJudgment.class);
        
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

}
