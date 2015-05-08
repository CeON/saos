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
public class CompositeTagJudgmentReferenceRemoverTest {

    private CompositeTagJudgmentReferenceRemover compositeTagJudgmentReferenceRemover = new CompositeTagJudgmentReferenceRemover();
    
    
    private TagJudgmentReferenceRemover tagJudgmentReferenceRemover1 = mock(TagJudgmentReferenceRemover.class);
    
    private TagJudgmentReferenceRemover tagJudgmentReferenceRemover2 = mock(TagJudgmentReferenceRemover.class);
    
    
    @Before
    public void setUp() {
        List<TagJudgmentReferenceRemover> judgmentReferenceRemovers = Lists.newArrayList(tagJudgmentReferenceRemover1, tagJudgmentReferenceRemover2);
        compositeTagJudgmentReferenceRemover.setTagJudgmentReferenceRemovers(judgmentReferenceRemovers);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void removeReference() throws Exception {
        
        // execute
        compositeTagJudgmentReferenceRemover.removeReferences(Lists.newArrayList(12L, 13L));
        
        // assert
        verify(tagJudgmentReferenceRemover1).removeReferences(Lists.newArrayList(12L, 13L));
        verify(tagJudgmentReferenceRemover2).removeReferences(Lists.newArrayList(12L, 13L));
    }
}
