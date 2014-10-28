package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scjImportProcessStepExecutionListener")
public class ScjImportProcessStepExecutionListener implements StepExecutionListener {

    private static Logger log = LoggerFactory.getLogger(ScjImportProcessStepExecutionListener.class);
    
    private ScObjectDeleter scjObjectDeleter;
    
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        
        log.debug("before step tasks: ");
        
        scjObjectDeleter.deleteJudgmentsWithoutRawSourceScJudgment();
    }

    
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        
        log.debug("after step tasks: ");
        
        scjObjectDeleter.deleteScChambersWithoutJudgments();
        scjObjectDeleter.deleteScChamberDivisionsWithoutJudgments();
        scjObjectDeleter.deleteScjFormsWithoutJudgments();

        return ExitStatus.COMPLETED;
    }


    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setSupremeCourtJudgmentDeleter(ScObjectDeleter supremeCourtJudgmentDeleter) {
        this.scjObjectDeleter = supremeCourtJudgmentDeleter;
    }

}
