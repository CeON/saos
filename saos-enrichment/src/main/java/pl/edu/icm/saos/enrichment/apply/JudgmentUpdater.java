package pl.edu.icm.saos.enrichment.apply;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * A contract for classes that will be responsible for updating a judgment with data generated from {@link EnrichmentTag}
 * 
 * @param MODEL_OBJECT the type of object that will be added to Judgment object tree 
 * 
 * @author Łukasz Dumiszewski
 */

public interface JudgmentUpdater<MODEL_OBJECT> {

    /**
     * Adds the given object to the judgment object tree
     */
    public void addToJudgment(Judgment judgment, MODEL_OBJECT object);
    
}
