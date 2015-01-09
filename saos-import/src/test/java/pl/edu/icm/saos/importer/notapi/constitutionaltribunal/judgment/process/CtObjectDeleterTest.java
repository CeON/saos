package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.importer.ImportTestSupport;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.RawSourceJudgmentRepository;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class CtObjectDeleterTest extends ImportTestSupport {

    @Autowired
    private CtObjectDeleter ctObjectDeleter;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private RawSourceJudgmentRepository rawSourceJudgmentRepository;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void deleteJudgmentsWithoutRawSourceCtJudgment() {
        
        // given
        ConstitutionalTribunalJudgment ctJudgment = createCtJudgment(true);
        createCtJudgment(false);
        createCtJudgment(false);
        
        // execute
        ctObjectDeleter.deleteJudgmentsWithoutRawSourceCtJudgment();
        
        // then
        List<Judgment> dbCtJudgments = judgmentRepository.findAll();
        
        assertEquals(1, dbCtJudgments.size());
        assertEquals(ctJudgment.getId(), dbCtJudgments.get(0).getId());
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private ConstitutionalTribunalJudgment createCtJudgment(boolean hasCorrespondingSourceJudgment) {
        
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
