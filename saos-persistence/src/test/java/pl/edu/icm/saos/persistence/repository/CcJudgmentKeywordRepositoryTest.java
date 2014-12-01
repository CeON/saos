package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;

import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;

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
    
    
    //------------------------ TEST --------------------------
    
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
    
    @Test
    public void findAllByPhrasePart_null() {
    	//given
    	String phrasePart = "";
    	
    	//when
    	List<CcJudgmentKeyword> actual = ccJudgmentKeywordRepository.findAllByPhrasePart(phrasePart);
    	
    	//then
    	assertEquals(0, actual.size());
    }
    
    @Test
    public void findAllByPhrasePart_correct() {
    	//given
    	List<CcJudgmentKeyword> keywords = Arrays.asList(new CcJudgmentKeyword("abcCos"), new CcJudgmentKeyword("abcDos"), new CcJudgmentKeyword("bbb"));
    	keywords.forEach(keyword -> ccJudgmentKeywordRepository.save(keyword));
    	String phrasePart = "abc";
    	
    	//when
    	List<CcJudgmentKeyword> actual = ccJudgmentKeywordRepository.findAllByPhrasePart(phrasePart);
    	
    	//then
    	assertThat(actual, Matchers.containsInAnyOrder(new CcJudgmentKeyword("abcCos"), new CcJudgmentKeyword("abcDos")));
    	
    }

    
}
