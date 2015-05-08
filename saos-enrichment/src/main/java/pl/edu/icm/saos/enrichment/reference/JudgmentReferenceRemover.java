package pl.edu.icm.saos.enrichment.reference;

import java.util.List;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;


/**
 * Remover of references to judgment from {@link EnrichmentTag}.
 * 
 * @author madryk
 */
public interface JudgmentReferenceRemover {
    
    /**
     * Removes reference to judgment with provided id as argument.
     */
    void removeReference(List<Long> judgmentId);
}
