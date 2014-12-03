package pl.edu.icm.saos.persistence.repository;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestInMemoryObjectFactory;
import pl.edu.icm.saos.persistence.model.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class CcJudgmentRepositoryTest  extends PersistenceTestSupport {

    private static Logger log = LoggerFactory.getLogger(JudgmentRepositoryTest.class);
    
    @Autowired
    private CcJudgmentRepository ccJudgmentRepository;
    
    
    
    @Test
    public void findOneBySourceCodeAndSourceJudgmentId_NOT_FOUND() {
        log.info("======= findOneBySourceCodeAndSourceJudgmentId");
        Judgment ccJudgment = ccJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, "111122");
        assertNull(ccJudgment);
        log.info("======= END OF findOneBySourceCodeAndSourceJudgmentId");
        
    }
    
    @Test
    public void findOneBySourceCodeAndSourceJudgmentId_FOUND() {
        CommonCourtJudgment ccJudgment = TestInMemoryObjectFactory.createSimpleCcJudgment();
        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setSourceCode(SourceCode.COMMON_COURT);
        sourceInfo.setSourceJudgmentId("1123");
        ccJudgment.setSourceInfo(sourceInfo);
        ccJudgmentRepository.save(ccJudgment);
        CommonCourtJudgment dbCcJudgment = ccJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, "1123");
        assertNotNull(dbCcJudgment);
        assertEquals(ccJudgment.getId(), dbCcJudgment.getId());
    }
    
    
    @Test
    public void findBySourceCodeAndCaseNumber_NOT_FOUND() {
        List<CommonCourtJudgment> ccJudgments = ccJudgmentRepository.findBySourceCodeAndCaseNumber(SourceCode.COMMON_COURT, "111122");
        assertEquals(0, ccJudgments.size());
    }
    
    @Test
    public void findBySourceCodeAndCaseNumber_FOUND() {
        CommonCourtJudgment ccJudgment1 = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        createCcJudgment(SourceCode.COMMON_COURT, "2", "AAA2");
        createCcJudgment(SourceCode.COMMON_COURT, "3", "AAA3");
        createCcJudgment(SourceCode.ADMINISTRATIVE_COURT, "1", "AAA1");
        
        List<CommonCourtJudgment> ccJudgments = ccJudgmentRepository.findBySourceCodeAndCaseNumber(SourceCode.COMMON_COURT, ccJudgment1.getCaseNumbers().get(0));
        assertEquals(1, ccJudgments.size());
        assertEquals(ccJudgment1.getId(), ccJudgments.get(0).getId());
      
    }

    //------------------------ PRIVATE --------------------------
    
    private CommonCourtJudgment createCcJudgment(SourceCode sourceCode, String sourceJudgmentId, String caseNumber) {
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setSourceCode(sourceCode);
        sourceInfo.setSourceJudgmentId(sourceJudgmentId);
        ccJudgment.setSourceInfo(sourceInfo);
        ccJudgment.addCourtCase(new CourtCase(caseNumber));
        ccJudgmentRepository.save(ccJudgment);
        return ccJudgment;
    }
      
}
