package pl.edu.icm.saos.webapp.keyword;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.JudgmentKeyword;

/**
 * Service for converting list of CcJudgmentKeyword into SimpleKeyword list.  
 * 
 * @author Łukasz Pawełczak
 */
@Service
public class SimpleKeywordConverter {

	
	//------------------------ LOGIC --------------------------
	
	public List<SimpleKeyword> convertJudgmentKeywords(List<JudgmentKeyword> judgmentKeywords) {
		return judgmentKeywords.stream()
			.map(judgmentKeyword -> convertFromJudgmentKeyword(judgmentKeyword))
			.collect(Collectors.toList());
	}

	
	//------------------------ PRIVATE --------------------------
	
	private SimpleKeyword convertFromJudgmentKeyword(JudgmentKeyword judgmentKeyword) {
		SimpleKeyword simpleKeyword = new SimpleKeyword();
		simpleKeyword.setId(judgmentKeyword.getId());
		simpleKeyword.setPhrase(judgmentKeyword.getPhrase());
		return simpleKeyword;
	}
}
