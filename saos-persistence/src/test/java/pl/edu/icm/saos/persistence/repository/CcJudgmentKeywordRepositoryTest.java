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
    public void findAllByPhrasePart_search_ok() {
    	//given
    	String keyword = "dobra osobiste";
    	
    	List<CcJudgmentKeyword> keywords = Arrays.asList(new CcJudgmentKeyword(keyword));
    	keywords.forEach(k -> ccJudgmentKeywordRepository.save(k));
    	
    	//when
    	List<CcJudgmentKeyword> actual = ccJudgmentKeywordRepository.findAllByPhrasePart(keyword);
    	
    	//then
    	assertThat(actual, Matchers.containsInAnyOrder(new CcJudgmentKeyword(keyword)));
    }
    
    
    @Test
    public void findAllByPhrasePart_search_by_phrase_starting_part_ok() {
    	//given
    	String keywordOne = "dobra osobiste";
    	String keywordTwo = "nieznalezione słowo";
    	
    	List<CcJudgmentKeyword> keywords = Arrays.asList(new CcJudgmentKeyword(keywordOne), new CcJudgmentKeyword(keywordTwo));
    	keywords.forEach(keyword -> ccJudgmentKeywordRepository.save(keyword));
    	String phrasePartOne = "dob";
    	String phrasePartTwo = "dobra os";
    	
    	//when
    	List<CcJudgmentKeyword> actualOne = ccJudgmentKeywordRepository.findAllByPhrasePart(phrasePartOne);
    	List<CcJudgmentKeyword> actualTwo = ccJudgmentKeywordRepository.findAllByPhrasePart(phrasePartTwo);
    	
    	//then
    	assertThat(actualOne, Matchers.containsInAnyOrder(new CcJudgmentKeyword(keywordOne)));
    	assertThat(actualTwo, Matchers.containsInAnyOrder(new CcJudgmentKeyword(keywordOne)));
    }
    
    @Test
    public void findAllByPhrasePart_search_by_phrase_part_in_middle() {
    	//given
    	String keywordOne = "dobra osobiste";
    	
    	List<CcJudgmentKeyword> keywords = Arrays.asList(new CcJudgmentKeyword(keywordOne));
    	keywords.forEach(keyword -> ccJudgmentKeywordRepository.save(keyword));
    	String phrasePart = "bra";
    	
    	//when
    	List<CcJudgmentKeyword> actual = ccJudgmentKeywordRepository.findAllByPhrasePart(phrasePart);
    	
    	//then
    	assertEquals(0, actual.size());
    }

    @Test
    public void findAllByPhrasePart_case_insensitive() {
    	//given
    	String keywordOne = "dobra osobiste";
    	String keywordTwo = "DOBRA OSOBISTE";
    	String keywordThree = "DoBrA OsObIsTe";
    	
    	String phraseOne = "DOBRA";
    	String phraseTwo = "dobra";
    	String phraseThree = "dObRa";
    			
    	List<CcJudgmentKeyword> keywords = Arrays.asList(new CcJudgmentKeyword(keywordOne), new CcJudgmentKeyword(keywordTwo), new CcJudgmentKeyword(keywordThree));
    	keywords.forEach(keyword -> ccJudgmentKeywordRepository.save(keyword));
    	
    	//when
    	List<CcJudgmentKeyword> actualOne = ccJudgmentKeywordRepository.findAllByPhrasePart(phraseOne);
    	List<CcJudgmentKeyword> actualTwo = ccJudgmentKeywordRepository.findAllByPhrasePart(phraseTwo);
    	List<CcJudgmentKeyword> actualThree = ccJudgmentKeywordRepository.findAllByPhrasePart(phraseThree);
    	
    	//then
    	assertThat(actualOne, Matchers.containsInAnyOrder(new CcJudgmentKeyword(keywordOne), new CcJudgmentKeyword(keywordTwo), new CcJudgmentKeyword(keywordThree)));
    	assertThat(actualTwo, Matchers.containsInAnyOrder(new CcJudgmentKeyword(keywordOne), new CcJudgmentKeyword(keywordTwo), new CcJudgmentKeyword(keywordThree)));
    	assertThat(actualThree, Matchers.containsInAnyOrder(new CcJudgmentKeyword(keywordOne), new CcJudgmentKeyword(keywordTwo), new CcJudgmentKeyword(keywordThree)));
    } 
}
