package pl.edu.icm.saos.persistence.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CommonCourtJudgmentTest extends JudgmentTestSupport {
	
	private CommonCourtJudgment commonCourtJudgment = new CommonCourtJudgment();
	
	private Map<String, CcJudgmentKeyword> keywordMap = new HashMap<String, CcJudgmentKeyword>(); 
	
	private String[] words = {"key", "word"};
	
	@Before
	public void before() {
		judgment = new CommonCourtJudgment();
		super.before();
		initializeKeywords();
	}
	
	@Test
	public void getKeyword_NotFound() {
		assertNull(commonCourtJudgment.getKeyword("notFound"));
	}
	
	@Test
	public void getKeyword_Found() {
		CcJudgmentKeyword testKeywordOne = commonCourtJudgment.getKeyword(words[0]);
		CcJudgmentKeyword testKeywordTwo = commonCourtJudgment.getKeyword(words[1]); 
		
		assertNotNull(testKeywordOne);
		assertNotNull(testKeywordTwo);
		assertEquals(testKeywordOne, keywordMap.get(words[0]));
		assertEquals(testKeywordTwo, keywordMap.get(words[1]));
	}

	
	private void initializeKeywords() {
		for (String word: this.words) {
			createKeywordAndAdd(word);
		}
	}
	
	private void createKeywordAndAdd(String phrase) {
		CcJudgmentKeyword keyword = new CcJudgmentKeyword(phrase);
		commonCourtJudgment.addKeyword(keyword);
		keywordMap.put(phrase, keyword);
	}
}
