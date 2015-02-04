package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import pl.edu.icm.saos.persistence.common.Generatable;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * 
 * Defines contract for repository methods that can not be specified by annotations or specific
 * method names and have to be written 'by hand'.
 * 
 * 
 * @author Łukasz Dumiszewski
 */

public interface JudgmentRepositoryCustom {

    /**
     * Finds {@link Judgment} with the given id ({@link Judgment#getId()}), initializes it (the whole tree)
     * and returns it.<br/>
     * Note that in order to have the {@link Generatable} objects added, you should use a proper service from
     * the saos-enrichment module.
     */
    public <T extends Judgment> T findOneAndInitialize(long id);

    /**
     * Deletes {@link Judgment}s with the given judgmentIds. 
     */
    public void delete(List<Long> judgmentIds);
    
    /**
     * Deletes the given judgment and corresponding <code>{@link EnrichmentTag}s</code>
     */
    public void delete(Judgment judgment);
       
    /**
     * Deletes a {@link Judgment} with the given judgmentId and corresponding <code>{@link EnrichmentTag}s</code>
     */
    public void delete(Long judgmentId);
    
    /**
     * Deletes all {@link Judgment}s and corresponding {@link EnrichmentTag}s
     */
    public void deleteAll();
    
    /** Unsupported, use: {@link #deleteAll()} */
    public void deleteAllInBatch();
    
    /** Unsupported, use: {@link #delete(List)} */
    public void deleteInBatch(Iterable<Judgment> entities);

}
