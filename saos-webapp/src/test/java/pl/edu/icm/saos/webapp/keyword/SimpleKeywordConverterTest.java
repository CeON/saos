package pl.edu.icm.saos.webapp.keyword;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Pawełczak
 */
public class SimpleKeywordConverterTest {

	private SimpleKeywordConverter simpleKeywordConverter = new SimpleKeywordConverter();
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void convertCcJudgmentKeywords_empty_list() {
	    
		//given
		List<JudgmentKeyword> judgmentKeywords = Lists.newArrayList();
		
		//when
		List<SimpleKeyword> convertedSimpleKeywords = simpleKeywordConverter.convertJudgmentKeywords(judgmentKeywords);
		
		//then
		assertNotNull(convertedSimpleKeywords);
	}

	
	@Test
	public void convertCcJudgmentKeywords() {
		
	    //given
		
	    int idOne = 5;
		int idTwo = 15;
		JudgmentKeyword keywordOne = new JudgmentKeyword(CourtType.COMMON, "słowo kluczowe");
		Whitebox.setInternalState(keywordOne, "id", idOne);
		
		JudgmentKeyword keywordTwo = new JudgmentKeyword(CourtType.COMMON, "fajne słowo kluczowe");
		Whitebox.setInternalState(keywordTwo, "id", idTwo);
		
		List<JudgmentKeyword> judgmentKeywords = Arrays.asList(keywordOne, keywordTwo);
		
		
		//when
		
		List<SimpleKeyword> convertedSimpleKeywords = simpleKeywordConverter.convertJudgmentKeywords(judgmentKeywords);
		
		
		//then
		
		assertEquals(2, convertedSimpleKeywords.size());
		assertEquals(idOne, convertedSimpleKeywords.get(0).getId());
		assertEquals(judgmentKeywords.get(0).getPhrase(), convertedSimpleKeywords.get(0).getPhrase());
		assertEquals(idTwo, convertedSimpleKeywords.get(1).getId());
		assertEquals(judgmentKeywords.get(1).getPhrase(), convertedSimpleKeywords.get(1).getPhrase());
	}
	
}
