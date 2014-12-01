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
public class KeywordsWebServiceTest {

	
	private KeywordsWebService keywordsWebService = new KeywordsWebService();
	
	@Mock
	private CcJudgmentKeywordRepository ccJudgmentKeywordRepository;
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void getCcJudgmentKeywords_empty_phrase() {
		//given
		List<CcJudgmentKeyword> emptyList = Arrays.asList();
		when(ccJudgmentKeywordRepository.findAllByPhrasePart("")).thenReturn(emptyList);
		keywordsWebService.setCcJudgmentKeywordRepository(ccJudgmentKeywordRepository);
		
		//when
		List<CcJudgmentKeyword> keywords = keywordsWebService.getCcJudgmentKeywords("");

		//then
		assertEquals(0, keywords.size());
	}
	
	@Test
	public void getCcJudgmentKeywords_correct_order() {
		//given
		CcJudgmentKeyword keywordOne = new CcJudgmentKeyword();
		keywordOne.setPhrase("bankowe prawo");
		CcJudgmentKeyword keywordTwo = new CcJudgmentKeyword();
		keywordTwo.setPhrase("waloryzacja");
		
		List<CcJudgmentKeyword> keywordsWrongOrder = Arrays.asList(keywordTwo, keywordOne);
		when(ccJudgmentKeywordRepository.findAllByPhrasePart("a")).thenReturn(keywordsWrongOrder);
		keywordsWebService.setCcJudgmentKeywordRepository(ccJudgmentKeywordRepository);
		
		
		//when
		List<CcJudgmentKeyword> keywords = keywordsWebService.getCcJudgmentKeywords("a");
		
		
		//then
		assertEquals(2, keywords.size());
		assertEquals(keywordOne.getPhrase(), keywords.get(0).getPhrase());
		assertEquals(keywordTwo.getPhrase(), keywords.get(1).getPhrase());
	}

	
	
}
