package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjImportProcessProcessor")
public class CcjImportProcessProcessor implements ItemProcessor<RawSourceCcJudgment, JudgmentWithCorrectionList<CommonCourtJudgment>> {

    private static Logger log = LoggerFactory.getLogger(CcjImportProcessProcessor.class);
    
    
    private RawSourceCcJudgmentConverter rawSourceCcJudgmentConverter;
    
    private CcjProcessingService ccjProcessingService;
    
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository;
    
    
    
    
    
    @Override
    public JudgmentWithCorrectionList<CommonCourtJudgment> process(RawSourceCcJudgment rawJudgment) throws Exception {
        
        // refetch the rawJudgment from db - without this we can run into OptimisticLockingEx
        // in case of skipping items due errors, see more detailed explanation: https://github.com/CeON/saos/issues/22
        rawJudgment = rawSourceCcJudgmentRepository.findOne(rawJudgment.getId());
        
        SourceCcJudgment sourceCcJudgment = rawSourceCcJudgmentConverter.convertSourceCcJudgment(rawJudgment);
        log.trace("process: \n {}", sourceCcJudgment);
        
        JudgmentWithCorrectionList<CommonCourtJudgment> judgmentWithCorrectionList = ccjProcessingService.processJudgment(sourceCcJudgment);
        
        markProcessed(rawJudgment);
        
        return judgmentWithCorrectionList;
        
        
    }


       
    //------------------------ PRIVATE --------------------------
   
    
    private void markProcessed(RawSourceCcJudgment rawJudgment) {
        rawJudgment.markProcessed();
        rawSourceCcJudgmentRepository.save(rawJudgment);
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setRawSourceCcjConverter(RawSourceCcJudgmentConverter rawSourceCcJudgmentConverter) {
        this.rawSourceCcJudgmentConverter = rawSourceCcJudgmentConverter;
    }

    
    @Autowired
    public void setCcjProcessingService(CcjProcessingService ccjProcessingService) {
        this.ccjProcessingService = ccjProcessingService;
    }


    @Autowired
    public void setRawSourceCcJudgmentRepository(RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository) {
        this.rawSourceCcJudgmentRepository = rawSourceCcJudgmentRepository;
    }


    
}
