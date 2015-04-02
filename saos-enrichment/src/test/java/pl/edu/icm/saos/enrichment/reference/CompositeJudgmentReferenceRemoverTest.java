package pl.edu.icm.saos.enrichment.reference;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class CompositeJudgmentReferenceRemoverTest {

    private CompositeJudgmentReferenceRemover compositeJudgmentReferenceRemover = new CompositeJudgmentReferenceRemover();
    
    
    private JudgmentReferenceRemover judgmentReferenceRemover1 = mock(JudgmentReferenceRemover.class);
    
    private JudgmentReferenceRemover judgmentReferenceRemover2 = mock(JudgmentReferenceRemover.class);
    
    
    @Before
    public void setUp() {
        List<JudgmentReferenceRemover> judgmentReferenceRemovers = Lists.newArrayList(judgmentReferenceRemover1, judgmentReferenceRemover2);
        compositeJudgmentReferenceRemover.setJudgmentReferenceRemovers(judgmentReferenceRemovers);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void removeReference() throws Exception {
        
        // execute
        compositeJudgmentReferenceRemover.removeReference(13L);
        
        // assert
        verify(judgmentReferenceRemover1).removeReference(13L);
        verify(judgmentReferenceRemover2).removeReference(13L);
    }
}
