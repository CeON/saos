package pl.edu.icm.saos.enrichment.apply.refregulations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class ReferencedRegulationsJudgmentUpdaterTest {

    private ReferencedRegulationsJudgmentUpdater referencedRegulationsJudgmentUpdater = new ReferencedRegulationsJudgmentUpdater();
    
    
    private Judgment judgment = mock(Judgment.class);
    
    private JudgmentReferencedRegulation refRegulation1 = mock(JudgmentReferencedRegulation.class);
    private JudgmentReferencedRegulation refRegulation2 = mock(JudgmentReferencedRegulation.class);
    
    private List<JudgmentReferencedRegulation> referencedRegulations = Lists.newArrayList(refRegulation1, refRegulation2);
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected=NullPointerException.class)
    public void addToJudgment_JudgmentNull() {
        
        // execute 
        referencedRegulationsJudgmentUpdater.addToJudgment(null, referencedRegulations);
        
    }
    
    
    @Test(expected=NullPointerException.class)
    public void addToJudgment_ReferencedCourtCasesNull() {
        
        // execute 
        referencedRegulationsJudgmentUpdater.addToJudgment(judgment, null);
        
    }
    
    
    @Test
    public void addToJudgment() {
        
        // execute 
        referencedRegulationsJudgmentUpdater.addToJudgment(judgment, referencedRegulations);
        
        // assert
        verify(judgment).addReferencedRegulation(referencedRegulations.get(0));
        verify(judgment).addReferencedRegulation(referencedRegulations.get(1));
        
    }
    
}
