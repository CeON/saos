package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;

/**
 * @author Łukasz Dumiszewski
 */

public class CcJudgmentKeywordRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private CcJudgmentKeywordRepository ccJudgmentKeywordRepository;
    
    @Test
    @Transactional
    public void findOneByName() {
        String keywordName = "Emerytura";
        CcJudgmentKeyword keyword = ccJudgmentKeywordRepository.findOneByName(keywordName);
        assertNull(keyword);
        
        keyword = new CcJudgmentKeyword();
        keyword.setName(keywordName);
        ccJudgmentKeywordRepository.save(keyword);
        
        CcJudgmentKeyword dbKeyword  = ccJudgmentKeywordRepository.findOneByName(keywordName);
        assertNotNull(dbKeyword);
        assertEquals(keywordName, dbKeyword.getName());
    }
    
    
}
