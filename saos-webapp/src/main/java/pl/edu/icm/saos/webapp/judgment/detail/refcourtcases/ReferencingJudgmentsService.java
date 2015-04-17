package pl.edu.icm.saos.webapp.judgment.detail.refcourtcases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.search.search.service.JudgmentSearchService;

/**
 * @author madryk
 */
@Service
public class ReferencingJudgmentsService {

    private JudgmentSearchService judgmentSearchService;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns number of judgments referencing judgment with provided id as argument
     */
    public long fetchReferencingJudgmentsCount(long judgmentId) {
        
        JudgmentCriteria criteria = new JudgmentCriteria();
        criteria.setReferencedCourtCaseId(judgmentId);
        
        Paging paging = new Paging(0, 0);
        
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, paging);
        
        return results.getTotalResults();
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setJudgmentSearchService(JudgmentSearchService judgmentSearchService) {
        this.judgmentSearchService = judgmentSearchService;
    }
    
}
