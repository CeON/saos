package pl.edu.icm.saos.enrichment.apply.refcases;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.enrichment.apply.JudgmentUpdater;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.ReferencedCourtCase;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("referencedCourtCasesJudgmentUpdater")
public class ReferencedCourtCasesJudgmentUpdater implements JudgmentUpdater<List<ReferencedCourtCase>> {

    
    
    //------------------------ LOGIC --------------------------
    
    
    @Override
    public void addToJudgment(Judgment judgment, List<ReferencedCourtCase> referencedCourtCases) {
        
        Preconditions.checkNotNull(judgment);
        Preconditions.checkNotNull(referencedCourtCases);
        
        for (ReferencedCourtCase referencedCourtCase : referencedCourtCases) {
            
            judgment.addReferencedCourtCase(referencedCourtCase);
        
        }
        
    }

}
