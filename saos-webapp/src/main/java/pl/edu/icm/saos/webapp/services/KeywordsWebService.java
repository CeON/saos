package pl.edu.icm.saos.webapp.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.repository.CcJudgmentKeywordRepository;
import pl.edu.icm.saos.webapp.common.StringComparator;

/**
 * @author Łukasz Pawełczak
 *
 */
@Service
public class KeywordsWebService {

	
	@Autowired
	private CcJudgmentKeywordRepository ccJudgmentKeywordRepository;
	
	
	//------------------------ LOGIC --------------------------
	
	public List<CcJudgmentKeyword> getCcJudgmentKeywords(String phrase) {
		
		List<CcJudgmentKeyword> keywords = ccJudgmentKeywordRepository.findAllByPhrasePart(phrase);
		CcJudgmentKeywordComparator ccJudgmentKeywordComparator = new CcJudgmentKeywordComparator();
		
		Collections.sort(keywords, ccJudgmentKeywordComparator);
		
		return keywords;
	}
	
	
	//------------------------ PRIVATE --------------------------
	
	private class CcJudgmentKeywordComparator implements Comparator<CcJudgmentKeyword> {
		
		public int compare(CcJudgmentKeyword keywordOne, CcJudgmentKeyword keywordTwo) {
            return StringComparator.compare(keywordOne.getPhrase(), keywordTwo.getPhrase());
        }  
	} 
	
	
	//------------------------ SETTERS --------------------------
	
	//For unit tests
	public void setCcJudgmentKeywordRepository(CcJudgmentKeywordRepository ccJudgmentKeywordRepository) {
		this.ccJudgmentKeywordRepository = ccJudgmentKeywordRepository;
	}
	
}
