package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;


/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjImportProcessSkipListener")
public class CcjImportProcessSkipListener implements SkipListener<RawSourceCcJudgment, CommonCourtJudgment> {

    private RawSourceCcJudgmentRepository rawJudgmentRepository;
    
    private static Logger log = LoggerFactory.getLogger(CcjImportProcessStepExecutionListener.class);
    
    
    @Override
    public void onSkipInRead(Throwable t) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onSkipInWrite(CommonCourtJudgment ccJudgment, Throwable t) {
        log.debug("writing skipping: " + ccJudgment);
        
        
    }

    
    @Override
    public void onSkipInProcess(RawSourceCcJudgment rJudgment, Throwable t) {
        CcjImportProcessSkippableException e = (CcjImportProcessSkippableException)t;
        
        logSkipReason(rJudgment, e);
        
        RawSourceCcJudgment rawJudgment = rawJudgmentRepository.findOne(rJudgment.getId());
        rawJudgment.markProcessingSkipped(e.getSkipReason());
        rawJudgmentRepository.save(rawJudgment);
        rawJudgmentRepository.flush();
    }

    
    // ------------------------ PRIVATE --------------------------
    
    private void logSkipReason(RawSourceCcJudgment rJudgment, CcjImportProcessSkippableException e) {
        
        log.error("skipping: " + rJudgment);
        log.error("skip reason: " + e.getMessage());
        
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setRawJudgmentRepository(RawSourceCcJudgmentRepository rawJudgmentRepository) {
        this.rawJudgmentRepository = rawJudgmentRepository;
    }

   

}
