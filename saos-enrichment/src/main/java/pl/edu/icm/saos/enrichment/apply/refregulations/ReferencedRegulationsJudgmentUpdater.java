package pl.edu.icm.saos.enrichment.apply.refregulations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pl.edu.icm.saos.enrichment.apply.JudgmentUpdater;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;

/**
 * @author madryk
 */
@Service
public class ReferencedRegulationsJudgmentUpdater implements JudgmentUpdater<List<JudgmentReferencedRegulation>> {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void addToJudgment(Judgment judgment, List<JudgmentReferencedRegulation> referencedRegulations) {
        
        Preconditions.checkNotNull(judgment);
        Preconditions.checkNotNull(referencedRegulations);
        
        for (JudgmentReferencedRegulation referencedRegulation : referencedRegulations) {
            judgment.addReferencedRegulation(referencedRegulation);
        }
        
    }

}
