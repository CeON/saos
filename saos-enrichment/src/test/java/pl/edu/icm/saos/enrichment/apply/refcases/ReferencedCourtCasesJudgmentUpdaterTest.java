package pl.edu.icm.saos.enrichment.apply.refcases;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.ReferencedCourtCase;

/**
 * @author Łukasz Dumiszewski
 */

public class ReferencedCourtCasesJudgmentUpdaterTest {

    
    private ReferencedCourtCasesJudgmentUpdater referencedCourtCasesJudgmentUpdater = new ReferencedCourtCasesJudgmentUpdater();

    private Judgment judgment = mock(Judgment.class);
    
    private ReferencedCourtCase refCourtCase1 = mock(ReferencedCourtCase.class);
    private ReferencedCourtCase refCourtCase2 = mock(ReferencedCourtCase.class);
    
    private List<ReferencedCourtCase> referencedCourtCases = Lists.newArrayList(refCourtCase1, refCourtCase2);
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected=NullPointerException.class)
    public void addToJudgment_JudgmentNull() {
        
        // execute 
        referencedCourtCasesJudgmentUpdater.addToJudgment(null, referencedCourtCases);
        
    }
    
    
    @Test(expected=NullPointerException.class)
    public void addToJudgment_ReferencedCourtCasesNull() {
        
        // execute 
        referencedCourtCasesJudgmentUpdater.addToJudgment(judgment, null);
        
    }
    
    
    @Test
    public void addToJudgment() {
        
        // execute 
        referencedCourtCasesJudgmentUpdater.addToJudgment(judgment, referencedCourtCases);
        
        // assert
        verify(judgment).addReferencedCourtCase(referencedCourtCases.get(0));
        verify(judgment).addReferencedCourtCase(referencedCourtCases.get(1));
    }
    
}

