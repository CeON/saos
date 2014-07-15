package pl.edu.icm.saos.importer.commoncourt.process;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.JudgmentOverwriter;
import pl.edu.icm.saos.importer.commoncourt.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.CcJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjProcessingService")
public class CcjProcessingService {
    
    private static Logger log = LoggerFactory.getLogger(CcjProcessingService.class);
    
    private CcJudgmentRepository ccJudgmentRepository;
    private JudgmentOverwriter<CommonCourtJudgment> judgmentOverwriter;
    private CcjReasoningMerger ccjReasoningMerger;
    private SourceCcJudgmentConverter sourceCcJudgmentConverter;
    
    /**
     * Processes 'normal' judgment, i.e. judgment that is not only a mere reasoning <br/>
     * Checks if the judgment has ever been processed. If so, then overwrites the old version of the
     * judgment with the new one and returns the changed {@link CommonCourtJudgment} <br/>
     * If the judgment has never been processed then this method returns {@link CommonCourtJudgment} created
     * solely from sourceCcJudgment.
     * See: {@link RawSourceCcJudgment#isJustReasons()} 
     */
    public CommonCourtJudgment processNormalJudgment(SourceCcJudgment sourceCcJudgment) {
        log.debug("processNormal:\n{}", sourceCcJudgment);
        CommonCourtJudgment ccJudgment = sourceCcJudgmentConverter.convertJudgment(sourceCcJudgment);
        
        CommonCourtJudgment oldJudgment = ccJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(ccJudgment.getSourceInfo().getSourceCode(), ccJudgment.getSourceInfo().getSourceJudgmentId()); 
        if (oldJudgment != null) {
            judgmentOverwriter.overwriteJudgment(oldJudgment, ccJudgment);
            ccJudgment = oldJudgment;
        }
        return ccJudgment;
    }


    /**
     * Processes judgment that is just a reasoning of another judgment <br/> 
     * Checks if the judgment for the given reasoning has ever been processed. If so, then it is merged
     * with the passed reasoning and returned. In other case this method returns null.
     * See: {@link RawSourceCcJudgment#isJustReasons()} 
     */
    public CommonCourtJudgment processReasoningJudgment(SourceCcJudgment sourceCcjReasoning) {
        log.debug("processReasoning:\n{}", sourceCcjReasoning);
        CommonCourtJudgment ccjReasoning = sourceCcJudgmentConverter.convertJudgment(sourceCcjReasoning);
        
        CommonCourtJudgment relatedJudgment = findRelatedJudgment(ccjReasoning);
        if (relatedJudgment == null) {
            log.debug("could not find related judgment");
            return null;
        }
        ccjReasoningMerger.mergeReasoning(relatedJudgment, ccjReasoning);
        return relatedJudgment;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    
    private CommonCourtJudgment findRelatedJudgment(CommonCourtJudgment ccReasoningJudgment) {
        List<CommonCourtJudgment> ccJudgments = ccJudgmentRepository.findBySourceCodeAndCaseNumber(ccReasoningJudgment.getSourceInfo().getSourceCode(), ccReasoningJudgment.getCaseNumber());
        if (ccJudgments.size() == 1) {
            return ccJudgments.get(0);
        }
        return null;
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setCcJudgmentRepository(CcJudgmentRepository ccJudgmentRepository) {
        this.ccJudgmentRepository = ccJudgmentRepository;
    }

    @Autowired
    @Qualifier("ccJudgmentOverwriter")
    public void setJudgmentOverwriter(JudgmentOverwriter<CommonCourtJudgment> ccJudgmentOverwriter) {
        this.judgmentOverwriter = ccJudgmentOverwriter;
    }

    @Autowired
    public void setCcjReasoningMerger(CcjReasoningMerger ccjReasoningMerger) {
        this.ccjReasoningMerger = ccjReasoningMerger;
    }

    @Autowired
    public void setSourceCcJudgmentConverter(SourceCcJudgmentConverter sourceCcJudgmentConverter) {
        this.sourceCcJudgmentConverter = sourceCcJudgmentConverter;
    }

    
    
}
