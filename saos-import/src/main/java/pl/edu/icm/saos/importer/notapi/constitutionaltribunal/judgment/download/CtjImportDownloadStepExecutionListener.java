package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.repository.RawSourceCtJudgmentRepository;

/**
 * @author madryk
 */
@Service
public class CtjImportDownloadStepExecutionListener implements StepExecutionListener {

    private static Logger log = LoggerFactory.getLogger(CtjImportDownloadStepExecutionListener.class);
    
    
    private RawSourceCtJudgmentRepository rawSourceCtJudgmentRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.debug("Deleting all raw source constitutional tribunal judgments");
        rawSourceCtJudgmentRepository.deleteAll();
        rawSourceCtJudgmentRepository.flush();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setRawSourceCtJudgmentRepository(
            RawSourceCtJudgmentRepository rawSourceCtJudgmentRepository) {
        this.rawSourceCtJudgmentRepository = rawSourceCtJudgmentRepository;
    }
}
