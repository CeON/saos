package pl.edu.icm.saos.webapp.keyword;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.webapp.division.SimpleDivision;

/**
 * Service for converting list of CcJudgmentKeyword into SimpleKeyword list.  
 * 
 * @author Łukasz Pawełczak
 */
@Service
public class SimpleKeywordConverter {

	
	//------------------------ LOGIC --------------------------
	
	public List<SimpleKeyword> convertCcJudgmentKeywords(List<CcJudgmentKeyword> ccJudgmentKeywords) {
		return ccJudgmentKeywords.stream()
			.map(ccJudgmentKeyword -> convertFromCcJudgmentKeyword(ccJudgmentKeyword))
			.collect(Collectors.toList());
	}

	
	//------------------------ PRIVATE --------------------------
	
	private SimpleKeyword convertFromCcJudgmentKeyword(CcJudgmentKeyword ccJudgmentKeyword) {
		SimpleKeyword simpleKeyword = new SimpleKeyword();
		simpleKeyword.setId(Integer.toString(ccJudgmentKeyword.getId()));
		simpleKeyword.setPhrase(ccJudgmentKeyword.getPhrase());
		return simpleKeyword;
	}
}
