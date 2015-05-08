package pl.edu.icm.saos.enrichment.reference;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class PageableJudgmentReferenceRemoverTest {

    private PageableJudgmentReferenceRemover pageableJudgmentReferenceRemover;
    
    private JudgmentReferenceRemover judgmentReferenceRemover = mock(JudgmentReferenceRemover.class);
    
    
    @Before
    public void setUp() {
        pageableJudgmentReferenceRemover = new PageableJudgmentReferenceRemover(judgmentReferenceRemover);
        pageableJudgmentReferenceRemover.setPageSize(2);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void removeReference() {
        // given
        List<Long> judgmentIds = Lists.newArrayList(1L, 2L, 3L, 4L, 5L);
        
        // execute
        pageableJudgmentReferenceRemover.removeReference(judgmentIds);
        
        // assert
        verify(judgmentReferenceRemover).removeReference(Lists.newArrayList(1L, 2L));
        verify(judgmentReferenceRemover).removeReference(Lists.newArrayList(3L, 4L));
        verify(judgmentReferenceRemover).removeReference(Lists.newArrayList(5L));
        verifyNoMoreInteractions(judgmentReferenceRemover);
    }
    
    
}
