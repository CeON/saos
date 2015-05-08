package pl.edu.icm.saos.enrichment.reference;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class PageableDelegatingJudgmentReferenceRemoverTest {

    private PageableDelegatingJudgmentReferenceRemover pageableDelegatingJudgmentReferenceRemover;
    
    private TagJudgmentReferenceRemover delegatedJudgmentReferenceRemover = mock(TagJudgmentReferenceRemover.class);
    
    
    @Before
    public void setUp() {
        pageableDelegatingJudgmentReferenceRemover = new PageableDelegatingJudgmentReferenceRemover(delegatedJudgmentReferenceRemover);
        pageableDelegatingJudgmentReferenceRemover.setPageSize(2);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void removeReference() {
        // given
        List<Long> judgmentIds = Lists.newArrayList(1L, 2L, 3L, 4L, 5L);
        
        // execute
        pageableDelegatingJudgmentReferenceRemover.removeReferences(judgmentIds);
        
        // assert
        verify(delegatedJudgmentReferenceRemover).removeReferences(Lists.newArrayList(1L, 2L));
        verify(delegatedJudgmentReferenceRemover).removeReferences(Lists.newArrayList(3L, 4L));
        verify(delegatedJudgmentReferenceRemover).removeReferences(Lists.newArrayList(5L));
        verifyNoMoreInteractions(delegatedJudgmentReferenceRemover);
    }
    
    
}
