package pl.edu.icm.saos.webapp.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.repository.CcJudgmentKeywordRepository;

/**
 * @author Łukasz Pawełczak
 *
 */ 
@RunWith(MockitoJUnitRunner.class) 
public class KeywordsSearchServiceTest {

	
	private KeywordSearchService keywordsSearchService = new KeywordSearchService();
	
	@Mock
	private CcJudgmentKeywordRepository ccJudgmentKeywordRepository;
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void findCcJudgmentKeywords_correct_order() {
		//given
		CcJudgmentKeyword keywordOne = new CcJudgmentKeyword();
		keywordOne.setPhrase("paserstwo");
		CcJudgmentKeyword keywordTwo = new CcJudgmentKeyword();
		keywordTwo.setPhrase("pisma procesowe");
		
		List<CcJudgmentKeyword> keywordsWrongOrder = Arrays.asList(keywordTwo, keywordOne);
		when(ccJudgmentKeywordRepository.findAllByPhrasePart("p")).thenReturn(keywordsWrongOrder);
		keywordsSearchService.setCcJudgmentKeywordRepository(ccJudgmentKeywordRepository);
		
		
		//when
		List<CcJudgmentKeyword> keywords = keywordsSearchService.findCcJudgmentKeywords("p");
		
		
		//then
		assertEquals(2, keywords.size());
		assertEquals(keywordOne.getPhrase(), keywords.get(0).getPhrase());
		assertEquals(keywordTwo.getPhrase(), keywords.get(1).getPhrase());
	}

	@Test
	public void findCcJudgmentKeywords_polish_words_correct_order() {
		//given
		CcJudgmentKeyword keywordOne = new CcJudgmentKeyword();
		keywordOne.setPhrase("słowo służebność");
		CcJudgmentKeyword keywordTwo = new CcJudgmentKeyword();
		keywordTwo.setPhrase("słowo zadośćuczynienie");
		
		List<CcJudgmentKeyword> keywordsWrongOrder = Arrays.asList(keywordTwo, keywordOne);
		when(ccJudgmentKeywordRepository.findAllByPhrasePart("słowo")).thenReturn(keywordsWrongOrder);
		keywordsSearchService.setCcJudgmentKeywordRepository(ccJudgmentKeywordRepository);
		
		
		//when
		List<CcJudgmentKeyword> keywords = keywordsSearchService.findCcJudgmentKeywords("słowo");
		
		
		//then
		assertEquals(2, keywords.size());
		assertEquals(keywordOne.getPhrase(), keywords.get(0).getPhrase());
		assertEquals(keywordTwo.getPhrase(), keywords.get(1).getPhrase());
	}
}
