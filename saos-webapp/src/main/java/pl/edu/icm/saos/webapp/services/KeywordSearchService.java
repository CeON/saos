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
public class KeywordSearchService {

	
	private CcJudgmentKeywordRepository ccJudgmentKeywordRepository;
	
	
	//------------------------ LOGIC --------------------------
	
	
	/**
     * Finds list of {@link CcJudgmentKeyword} with the given part of phrase ({@link CcJudgmentKeyword#getPhrase()})
     * and returns it. Returned list contains keywords sorted alphabetically by phrase {@link CcJudgmentKeyword#getPhrase()}.
     */
	public List<CcJudgmentKeyword> findCcJudgmentKeywords(String phrase) {
		
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
	
	@Autowired
	public void setCcJudgmentKeywordRepository(CcJudgmentKeywordRepository ccJudgmentKeywordRepository) {
		this.ccJudgmentKeywordRepository = ccJudgmentKeywordRepository;
	}
	
}
