package pl.edu.icm.saos.persistence.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class JudgmentKeywordRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private JudgmentKeywordRepository judgmentKeywordRepository;
    
    
    //------------------------ TEST --------------------------
    
    @Test
    public void findOneByCourtTypeAndPhraseIgnoreCase() {
        
        // given
        
        String phrase = "Emerytura";
        JudgmentKeyword keyword = judgmentKeywordRepository.findOneByCourtTypeAndPhraseIgnoreCase(CourtType.SUPREME, phrase);
        assertNull(keyword);
        
        keyword = new JudgmentKeyword(CourtType.SUPREME, phrase);
        judgmentKeywordRepository.save(keyword);
        
        // execute
        
        JudgmentKeyword dbKeyword  = judgmentKeywordRepository.findOneByCourtTypeAndPhraseIgnoreCase(CourtType.SUPREME, phrase);
        
        
        // assert
        
        assertNotNull(dbKeyword);
        assertFalse(keyword == dbKeyword);
        assertEquals(keyword, dbKeyword);
        
    }
    
    @Test
    public void findAllByCourtTypeAndPhrasePart() {
        
    	// given
        
        JudgmentKeyword keyword1 = new JudgmentKeyword(CourtType.COMMON, "dobra osobiste");
        JudgmentKeyword keyword2 = new JudgmentKeyword(CourtType.COMMON, "dobra inne");
        
    	List<JudgmentKeyword> keywords = Arrays.asList(keyword1, keyword2);
    	keywords.forEach(k -> judgmentKeywordRepository.save(k));
    	
    	// when
    
    	List<JudgmentKeyword> actual = judgmentKeywordRepository.findAllByCourtTypeAndPhrasePart(CourtType.COMMON, keyword1.getPhrase());
    	
    	// then
    	
    	assertThat(actual, Matchers.containsInAnyOrder(keyword1));
    }
    
    
    @Test
    public void findAllByPhrasePart_search_by_phrase_starting_part_ok() {
    	
        //given
    	String keywordOne = "dobra osobiste";
    	String keywordTwo = "nieznalezione słowo";
    	
    	List<JudgmentKeyword> keywords = Arrays.asList(new JudgmentKeyword(CourtType.COMMON, keywordOne), new JudgmentKeyword(CourtType.COMMON, keywordTwo));
    	keywords.forEach(keyword -> judgmentKeywordRepository.save(keyword));
    	String phrasePartOne = "dob";
    	String phrasePartTwo = "dobra os";
    	
    	//when
    	List<JudgmentKeyword> actualOne = judgmentKeywordRepository.findAllByCourtTypeAndPhrasePart(CourtType.COMMON, phrasePartOne);
    	List<JudgmentKeyword> actualTwo = judgmentKeywordRepository.findAllByCourtTypeAndPhrasePart(CourtType.COMMON, phrasePartTwo);
    	
    	//then
    	assertThat(actualOne, Matchers.containsInAnyOrder(new JudgmentKeyword(CourtType.COMMON, keywordOne)));
    	assertThat(actualTwo, Matchers.containsInAnyOrder(new JudgmentKeyword(CourtType.COMMON, keywordOne)));
    }
    
    @Test
    public void findAllByPhrasePart_search_by_phrase_part_in_middle() {
    	
        //given
    	JudgmentKeyword keyword = new JudgmentKeyword(CourtType.COMMON, "dobra osobiste");
    	
    	judgmentKeywordRepository.save(keyword);
    	
    	//when
    	
    	List<JudgmentKeyword> actual = judgmentKeywordRepository.findAllByCourtTypeAndPhrasePart(CourtType.COMMON, "bra");
    	
    	//then
    	
    	assertEquals(0, actual.size());
    }
   
    
    @Test
    public void findAllByPhrasePart_DifferentCourtType() {
        
        //given
        
        JudgmentKeyword keyword = new JudgmentKeyword(CourtType.COMMON, "dobra osobiste");
        
        judgmentKeywordRepository.save(keyword);
        
        
        //when
        
        List<JudgmentKeyword> actual = judgmentKeywordRepository.findAllByCourtTypeAndPhrasePart(CourtType.ADMINISTRATIVE, "dobra");
        
        
        //then
        
        assertEquals(0, actual.size());
    }
    

    @Test
    public void findAllByPhrasePart_case_insensitive() {

        //given
        
        JudgmentKeyword keyword = new JudgmentKeyword(CourtType.COMMON, "Dobra osobiste");
    			
    	judgmentKeywordRepository.save(keyword);
    	
    	
    	//when
    	
    	List<JudgmentKeyword> actualOne = judgmentKeywordRepository.findAllByCourtTypeAndPhrasePart(CourtType.COMMON, "DOBRA OSOBIste");
    	
    	
    	//then
    	
    	assertThat(actualOne, Matchers.containsInAnyOrder(keyword));
    } 
    
    
    @Test
    public void findAllByPhrasePart_correct_order() {
    	
        //given
    	JudgmentKeyword keywordOne = new JudgmentKeyword(CourtType.COMMON, "paserstwo");
    	JudgmentKeyword keywordTwo = new JudgmentKeyword(CourtType.COMMON, "pasma procesowe");
    	
    	judgmentKeywordRepository.save(keywordOne);
    	judgmentKeywordRepository.save(keywordTwo);
        
    	//when
    	List<JudgmentKeyword> actual = judgmentKeywordRepository.findAllByCourtTypeAndPhrasePart(CourtType.COMMON, "pas");
    	
    	//then
    	assertEquals(2, actual.size());
    	assertEquals(keywordOne, actual.get(0));
    	assertEquals(keywordTwo, actual.get(1));
    }
    
    
    @Test
    public void findAllByPhrasePart_polish_words() {
        
        // given
        
        JudgmentKeyword keywordOne = new JudgmentKeyword(CourtType.COMMON, "słowo służebnoś");
        JudgmentKeyword keywordTwo = new JudgmentKeyword(CourtType.COMMON, "słowo zadośćuczynienies");
        
        judgmentKeywordRepository.save(keywordOne);
        judgmentKeywordRepository.save(keywordTwo);
        
        
        // when
    	
        List<JudgmentKeyword> actual = judgmentKeywordRepository.findAllByCourtTypeAndPhrasePart(CourtType.COMMON, "słowo");
    	
    	
        // then
        
    	assertEquals(2, actual.size());
    	assertEquals(keywordOne, actual.get(0));
    	assertEquals(keywordTwo, actual.get(1));
    }
    
    
}
