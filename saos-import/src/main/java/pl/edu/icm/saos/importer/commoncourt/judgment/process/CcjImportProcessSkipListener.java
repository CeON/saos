package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import java.util.List;

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
        
        List<RawSourceCcJudgment> rJudgments = rawJudgmentRepository.findBySourceIdAndProcessed(ccJudgment.getSourceInfo().getSourceJudgmentId(), false);
        
        if (rJudgments.size() != 1) {
            return;
        }
        
        RawSourceCcJudgment rJudgment = rJudgments.get(0);
        rJudgment.markProcessedError(t.getMessage());
        log.debug("saving error to: " + rJudgment.getId() + ", err desc: " + rJudgment.getProcessErrorDesc());
        rawJudgmentRepository.save(rJudgment);
        rawJudgmentRepository.flush();
        
        
    }

    @Override
    public void onSkipInProcess(RawSourceCcJudgment rJudgment, Throwable t) {
        log.debug("skipping: " + rJudgment);
        RawSourceCcJudgment rawJudgment = rawJudgmentRepository.findOne(rJudgment.getId());
        rawJudgment.markProcessedError(t.getMessage());
        rawJudgmentRepository.save(rawJudgment);
        rawJudgmentRepository.flush();
        
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setRawJudgmentRepository(RawSourceCcJudgmentRepository rawJudgmentRepository) {
        this.rawJudgmentRepository = rawJudgmentRepository;
    }

   

}
