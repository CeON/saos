package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class CcJudgmentKeywordRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private CcJudgmentKeywordRepository ccJudgmentKeywordRepository;
    
    @Test
    @Transactional
    public void findOneByName() {
        String keywordName = "Emerytura";
        CcJudgmentKeyword keyword = ccJudgmentKeywordRepository.findOneByPhrase(keywordName);
        assertNull(keyword);
        
        keyword = new CcJudgmentKeyword();
        keyword.setPhrase(keywordName);
        ccJudgmentKeywordRepository.save(keyword);
        
        CcJudgmentKeyword dbKeyword  = ccJudgmentKeywordRepository.findOneByPhrase(keywordName);
        assertNotNull(dbKeyword);
        assertEquals(keywordName, dbKeyword.getPhrase());
    }
    
    
}
