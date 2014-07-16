package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.service.RawSourceCcjProcessFlagUpdater;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjImportProcessStepExecutionListener")
public class CcjImportProcessStepExecutionListener implements  StepExecutionListener {

    private static Logger log = LoggerFactory.getLogger(CcjImportProcessStepExecutionListener.class);
    
    
    @Autowired
    private RawSourceCcjProcessFlagUpdater rawSourceCcjProcessFlagUpdater;
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.debug("setting raw judgment processed flag...");
        rawSourceCcjProcessFlagUpdater.markProcessedAllEligible();
        return ExitStatus.COMPLETED;
    }

}
