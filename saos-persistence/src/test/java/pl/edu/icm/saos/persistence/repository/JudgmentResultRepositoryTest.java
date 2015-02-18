package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentResult;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class JudgmentResultRepositoryTest extends PersistenceTestSupport {

    @Autowired
    private JudgmentResultRepository judgmentResultRepository;
    
    
    //------------------------ TEST --------------------------
    
    @Test
    public void findOneByCourtTypeAndTextIgnoreCase_FOUND() {
        // given
        JudgmentResult judgmentResult = createAndSaveJudgmentResult(CourtType.SUPREME, "text");
        
        // execute
        JudgmentResult retJudgmentResult = judgmentResultRepository.findOneByCourtTypeAndTextIgnoreCase(CourtType.SUPREME, "text");
        
        // assert
        assertEquals(judgmentResult, retJudgmentResult);
    }
    
    @Test
    public void findOneByCourtTypeAndTextIgnoreCase_FOUND_IGNORED_CASE() {
        // given
        JudgmentResult judgmentResult = createAndSaveJudgmentResult(CourtType.SUPREME, "text");
        
        // execute
        JudgmentResult retJudgmentResult = judgmentResultRepository.findOneByCourtTypeAndTextIgnoreCase(CourtType.SUPREME, "TeXt");
        
        // assert
        assertEquals(judgmentResult, retJudgmentResult);
    }
    
    @Test
    public void findOneByCourtTypeAndTextIgnoreCase_NOT_FOUND() {
        // given
        createAndSaveJudgmentResult(CourtType.SUPREME, "text");
        createAndSaveJudgmentResult(CourtType.COMMON, "text2");
        
        // execute
        JudgmentResult retJudgmentResult = judgmentResultRepository.findOneByCourtTypeAndTextIgnoreCase(CourtType.COMMON, "text");
        
        // assert
        assertNull(retJudgmentResult);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private JudgmentResult createAndSaveJudgmentResult(CourtType courtType, String text) {
        JudgmentResult judgmentResult = new JudgmentResult(courtType, text);
        judgmentResultRepository.save(judgmentResult);
        
        return judgmentResult;
    }
    
}
