package pl.edu.icm.saos.importer.notapi.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
public class JsonImportDownloadStepExecutionListener implements StepExecutionListener {

    private static Logger log = LoggerFactory.getLogger(JsonImportDownloadStepExecutionListener.class);
    
    
    private RawSourceJudgmentRepository rawJudgmentRepository;
    
    private Class<? extends RawSourceJudgment> rawJudgmentClass;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.debug("Deleting all raw judgments with class {}", rawJudgmentClass.getName());
        rawJudgmentRepository.deleteAllWithClass(rawJudgmentClass);
        rawJudgmentRepository.flush();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
    
    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setRawJudgmentRepository(
            RawSourceJudgmentRepository rawJudgmentRepository) {
        this.rawJudgmentRepository = rawJudgmentRepository;
    }

    public void setRawJudgmentClass(
            Class<? extends RawSourceJudgment> rawJudgmentClass) {
        this.rawJudgmentClass = rawJudgmentClass;
    }

}
