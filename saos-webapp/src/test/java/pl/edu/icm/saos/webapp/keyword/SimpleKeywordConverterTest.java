package pl.edu.icm.saos.webapp.keyword;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.webapp.division.SimpleDivision;
import pl.edu.icm.saos.webapp.division.SimpleDivisionConverter;

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
		List<CcJudgmentKeyword> ccJudgmentKeywords = Lists.newArrayList();
		
		//when
		List<SimpleKeyword> convertedSimpleKeywords = simpleKeywordConverter.convertCcJudgmentKeywords(ccJudgmentKeywords);
		
		//then
		assertNotNull(convertedSimpleKeywords);
	}

	@Test
	public void convertCcJudgmentKeywords_correct() {
		//given
		int idOne = 5;
		int idTwo = 15;
		CcJudgmentKeyword keywordOne = new CcJudgmentKeyword();
		keywordOne.setPhrase("słowo kluczowe");
		Whitebox.setInternalState(keywordOne, "id", idOne);
		CcJudgmentKeyword keywordTwo = new CcJudgmentKeyword();
		keywordTwo.setPhrase("fajne słowo kluczowe");
		Whitebox.setInternalState(keywordTwo, "id", idTwo);
		
		List<CcJudgmentKeyword> ccJudgmentKeywords = Arrays.asList(keywordOne, keywordTwo);
		
		//when
		List<SimpleKeyword> convertedSimpleKeywords = simpleKeywordConverter.convertCcJudgmentKeywords(ccJudgmentKeywords);
		
		//then
		assertEquals(2, convertedSimpleKeywords.size());
		assertEquals(String.valueOf(idOne), convertedSimpleKeywords.get(0).getId());
		assertEquals(ccJudgmentKeywords.get(0).getPhrase(), convertedSimpleKeywords.get(0).getPhrase());
		assertEquals(String.valueOf(idTwo), convertedSimpleKeywords.get(1).getId());
		assertEquals(ccJudgmentKeywords.get(1).getPhrase(), convertedSimpleKeywords.get(1).getPhrase());
	}
	
}
