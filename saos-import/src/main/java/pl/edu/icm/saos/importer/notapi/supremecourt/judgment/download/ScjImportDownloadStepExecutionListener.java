package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.repository.RawSourceScJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scjImportDownloadStepExecutionListener")
public class ScjImportDownloadStepExecutionListener implements StepExecutionListener {

    private static Logger log = LoggerFactory.getLogger(ScjImportDownloadStepExecutionListener.class);
    
    
    @Autowired
    private RawSourceScJudgmentRepository rJudgmentRepository;
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Deleting all simple raw source supreme court judgments");
        rJudgmentRepository.deleteAll();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

}
