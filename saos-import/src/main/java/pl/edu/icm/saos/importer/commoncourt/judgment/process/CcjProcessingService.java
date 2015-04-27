package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverter;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.CcJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjProcessingService")
class CcjProcessingService {
    
    
    private CcJudgmentRepository ccJudgmentRepository;
    
    private JudgmentOverwriter<CommonCourtJudgment> judgmentOverwriter;
    
    private JudgmentConverter<CommonCourtJudgment, SourceCcJudgment> sourceCcJudgmentConverter;
    
    private EnrichmentTagRepository enrichmentTagRepository;
    
    
    
    
    /**
     * Processes judgment<br/>
     * Checks if the judgment has ever been processed. If so, then overwrites the old version of the
     * judgment with the new one and returns the changed {@link CommonCourtJudgment} <br/>
     * If the judgment has never been processed then this method returns {@link CommonCourtJudgment} created
     * solely from sourceCcJudgment.
     * See: {@link RawSourceCcJudgment#isJustReasons()} 
     */
    public JudgmentWithCorrectionList<CommonCourtJudgment> processJudgment(SourceCcJudgment sourceCcJudgment) {
        
        JudgmentWithCorrectionList<CommonCourtJudgment> judgmentWithCorrectionList = sourceCcJudgmentConverter.convertJudgment(sourceCcJudgment);
        
        CommonCourtJudgment ccJudgment = judgmentWithCorrectionList.getJudgment();
        
        CommonCourtJudgment oldJudgment = ccJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(ccJudgment.getSourceInfo().getSourceCode(), ccJudgment.getSourceInfo().getSourceJudgmentId()); 
        
        if (oldJudgment != null) {
            judgmentOverwriter.overwriteJudgment(oldJudgment, ccJudgment, judgmentWithCorrectionList.getCorrectionList());
            
            enrichmentTagRepository.deleteAllByJudgmentId(oldJudgment.getId());
            
            judgmentWithCorrectionList.setJudgment(oldJudgment);
        }
        
        return judgmentWithCorrectionList;
    }


    
    //------------------------ PRIVATE --------------------------
    
    

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
    @Qualifier("sourceCcJudgmentConverter")
    public void setSourceCcJudgmentConverter(JudgmentConverter<CommonCourtJudgment, SourceCcJudgment> sourceCcJudgmentConverter) {
        this.sourceCcJudgmentConverter = sourceCcJudgmentConverter;
    }

    @Autowired
    public void setEnrichmentTagRepository(EnrichmentTagRepository enrichmentTagRepository) {
        this.enrichmentTagRepository = enrichmentTagRepository;
    }

    
    
}
