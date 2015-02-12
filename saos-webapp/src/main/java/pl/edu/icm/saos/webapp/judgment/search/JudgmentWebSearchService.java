package pl.edu.icm.saos.webapp.judgment.search;

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

/**
 * @author Łukasz Pawełczak
 *
 */
@Service
public class JudgmentWebSearchService {

    @Autowired
    private SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService;
    
    @Autowired
    private JudgmentCriteriaFormConverter judgmentCriteriaFormConverter;
	
    @Autowired 
    private PagingConverter pagingConverter;
    
	@Autowired
	private JudgmentSearchResultSortService judgmentSearchResultSortService;
    
	
	//------------------------ LOGIC --------------------------
    
	public SearchResults<JudgmentSearchResult> search(JudgmentCriteriaForm judgmentCriteriaForm, Pageable pageable) {
		
		JudgmentCriteria judgmentCriteria = judgmentCriteriaFormConverter.convert(judgmentCriteriaForm);
		
		Paging paging = pagingConverter.convert(pageable);
		
        return judgmentSearchResultSortService.sortJudges(judgmentsSearchService.search(judgmentCriteria, paging));
	}
	
	
}
