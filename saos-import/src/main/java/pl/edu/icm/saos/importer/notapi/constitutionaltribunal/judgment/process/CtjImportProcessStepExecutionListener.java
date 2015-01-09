package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CtjImportProcessStepExecutionListener implements
        StepExecutionListener {

    @Autowired
    private CtObjectDeleter ctObjectDeleter;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        
        ctObjectDeleter.deleteJudgmentsWithoutRawSourceCtJudgment();
        
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

}
