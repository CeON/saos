package pl.edu.icm.saos.enrichment.reference;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * Remover of references to judgment from {@link EnrichmentTag} that
 * combines multiple {@link JudgmentReferenceRemover}s.
 * 
 * @author madryk
 */
@Service("defaultJudgmentReferenceRemover")
public class CompositeJudgmentReferenceRemover implements JudgmentReferenceRemover {
    
    private List<JudgmentReferenceRemover> judgmentReferenceRemovers;

    
    //------------------------ LOGIC --------------------------

    @Override
    @Transactional
    public void removeReference(long judgmentId) {
        
        for (JudgmentReferenceRemover referringRemover : judgmentReferenceRemovers) {
            referringRemover.removeReference(judgmentId);
        }
        
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setJudgmentReferenceRemovers(List<JudgmentReferenceRemover> judgmentReferenceRemovers) {
        this.judgmentReferenceRemovers = judgmentReferenceRemovers;
    }
}
