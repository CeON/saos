package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.ImportProcessingStatus;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjImportProcessProcessor")
public class CcjImportProcessProcessor implements ItemProcessor<RawSourceCcJudgment, CommonCourtJudgment> {

    private static Logger log = LoggerFactory.getLogger(CcjImportProcessProcessor.class);
    
    private RawSourceCcJudgmentConverter rawSourceCcJudgmentConverter;
    private CcjProcessingService ccjProcessingService;
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository;
    
    @Override
    public CommonCourtJudgment process(RawSourceCcJudgment rawJudgment) throws Exception {
        
        boolean onlyReasoning = rawJudgment.isJustReasons();
        
        SourceCcJudgment sourceCcJudgment = rawSourceCcJudgmentConverter.convertSourceCcJudgment(rawJudgment);
        log.debug("process: \n {}", sourceCcJudgment);
        
        /* (1) if processed judgment contains only reasons for judgment */
        
        CommonCourtJudgment ccJudgment = null;
        
        if (onlyReasoning) {
            ccJudgment = ccjProcessingService.processReasoningJudgment(sourceCcJudgment);
        }
        
        /* (2) if processed judgment is 'normal' judgment */
        else {
            ccJudgment = ccjProcessingService.processNormalJudgment(sourceCcJudgment);
        }
        
        updateProcessingStatus(rawJudgment, ImportProcessingStatus.OK);
        
        return ccJudgment;
        
    }


       
    //------------------------ PRIVATE --------------------------
   
    
    private void updateProcessingStatus(RawSourceCcJudgment rawJudgment, ImportProcessingStatus processingStatus) {
        rawJudgment.updateProcessingStatus(processingStatus);
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
