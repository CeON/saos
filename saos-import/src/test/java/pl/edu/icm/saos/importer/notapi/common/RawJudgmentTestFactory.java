package pl.edu.icm.saos.importer.notapi.common;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.RawSourceJudgmentRepository;

/**
 * @author madryk
 */
@Service
public class RawJudgmentTestFactory {
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private RawSourceJudgmentRepository rawSourceJudgmentRepository;
    
    
    //------------------------ TESTS --------------------------

    public SupremeCourtJudgment createScJudgment(boolean hasCorrespondingSourceJudgment) {
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        scJudgment.getSourceInfo().setSourceCode(SourceCode.SUPREME_COURT);
        scJudgment.getSourceInfo().setSourceJudgmentId(RandomStringUtils.randomAlphanumeric(10));
        scJudgment.addCourtCase(new CourtCase("123"));
        
        
        if (hasCorrespondingSourceJudgment) {
            RawSourceScJudgment rJudgment = new RawSourceScJudgment();
            rJudgment.setJsonContent("{\"key\":\"value\"}");
            rJudgment.setSourceId(scJudgment.getSourceInfo().getSourceJudgmentId());
            rawSourceJudgmentRepository.save(rJudgment);
        }
        
        judgmentRepository.save(scJudgment);
        
        
        
        return scJudgment;
    }
    
    public ConstitutionalTribunalJudgment createCtJudgment(boolean hasCorrespondingSourceJudgment) {
        ConstitutionalTribunalJudgment ctJudgment = new ConstitutionalTribunalJudgment();
        ctJudgment.getSourceInfo().setSourceCode(SourceCode.CONSTITUTIONAL_TRIBUNAL);
        ctJudgment.getSourceInfo().setSourceJudgmentId(RandomStringUtils.randomAlphanumeric(10));
        ctJudgment.addCourtCase(new CourtCase("123"));
        
        
        if (hasCorrespondingSourceJudgment) {
            RawSourceCtJudgment rJudgment = new RawSourceCtJudgment();
            rJudgment.setJsonContent("{\"key\":\"value\"}");
            rJudgment.setSourceId(ctJudgment.getSourceInfo().getSourceJudgmentId());
            rawSourceJudgmentRepository.save(rJudgment);
        }
        
        judgmentRepository.save(ctJudgment);
        
        
        
        return ctJudgment;
    }
}
