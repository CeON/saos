package pl.edu.icm.saos.webapp.judgment.detail.refcourtcases;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.search.search.service.JudgmentCriteriaBuilder;
import pl.edu.icm.saos.search.search.service.JudgmentSearchService;

/**
 * @author madryk
 */
public class ReferencingJudgmentsServiceTest {

    private ReferencingJudgmentsService referencingJudgmentsService = new ReferencingJudgmentsService();
    
    private JudgmentSearchService judgmentSearchService = mock(JudgmentSearchService.class);
    
    @Before
    public void setUp() {
        referencingJudgmentsService.setJudgmentSearchService(judgmentSearchService);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void fetchReferencingJudgmentsCount() {
        // given
        SearchResults<JudgmentSearchResult> results = new SearchResults<JudgmentSearchResult>();
        results.setTotalResults(28L);
        
        when(judgmentSearchService.search(any(), any())).thenReturn(results);
        
        // execute
        long count = referencingJudgmentsService.fetchReferencingJudgmentsCount(15L);
        
        // assert
        assertEquals(28L, count);
        verify(judgmentSearchService).search(JudgmentCriteriaBuilder.create().withReferencedCourtCaseId(15L).build(), new Paging(0, 0));
    }
}
