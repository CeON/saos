package pl.edu.icm.saos.enrichment.reference;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * Remover of references to judgment from {@link EnrichmentTag} that
 * combines multiple {@link JudgmentReferenceRemover}s.
 * 
 * @author madryk
 */
public class CompositeJudgmentReferenceRemover implements JudgmentReferenceRemover {
    
    private List<JudgmentReferenceRemover> judgmentReferenceRemovers;

    
    //------------------------ LOGIC --------------------------

    @Override
    public void removeReference(List<Long> judgmentIds) {
        
        for (JudgmentReferenceRemover referringRemover : judgmentReferenceRemovers) {
            referringRemover.removeReference(judgmentIds);
        }
        
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setJudgmentReferenceRemovers(List<JudgmentReferenceRemover> judgmentReferenceRemovers) {
        this.judgmentReferenceRemovers = judgmentReferenceRemovers;
    }
}
