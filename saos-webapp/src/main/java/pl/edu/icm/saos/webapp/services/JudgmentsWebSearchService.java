package pl.edu.icm.saos.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.search.search.service.SearchService;
import pl.edu.icm.saos.webapp.judgment.JudgmentCriteriaForm;
import pl.edu.icm.saos.webapp.judgment.JudgmentCriteriaFormConverter;
import pl.edu.icm.saos.webapp.search.PagingConverter;

/**
 * @author Łukasz Pawełczak
 *
 */
@Service
public class JudgmentsWebSearchService {

    @Autowired
    private SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService;
    
    @Autowired
    private JudgmentCriteriaFormConverter judgmentCriteriaFormConverter;
	
    @Autowired 
    private PagingConverter pagingConverter;
    
    
	//------------------------ LOGIC --------------------------
    
	public SearchResults<JudgmentSearchResult> search(JudgmentCriteriaForm judgmentCriteriaForm, Pageable pageable) {
		
		JudgmentCriteria judgmentCriteria = judgmentCriteriaFormConverter.convert(judgmentCriteriaForm);
		
		Paging paging = pagingConverter.convert(pageable);
		
        SearchResults<JudgmentSearchResult> resultSearchResults = judgmentsSearchService.search(judgmentCriteria, paging);
        
        return resultSearchResults;
	}
	
	
}
