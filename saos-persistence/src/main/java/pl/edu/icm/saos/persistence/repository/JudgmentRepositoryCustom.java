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

public interface JudgmentRepositoryCustom extends IndexableObjectRepositoryCustom {

    
    
    /**
     * Finds {@link Judgment} with the given id ({@link Judgment#getId()}), initializes it (the whole tree)
     * and returns it.<br/>
     * Note that in order to have the {@link Generatable} objects added, you should use a proper service from
     * the saos-enrichment module.
     */
    public <T extends Judgment> T findOneAndInitialize(long id);

    
    /**
     * Saves the given {@link Judgment}.
     * Additionally it updates modification date of this judgment and
     * resets indexed flag.
     */
    public <S extends Judgment> S save(S judgment);
    
    /**
     * Saves all given {@link Judgment}s.
     * Additionally it updates modification date and resets indexed flag
     * for all of these judgments.
     */
    public <S extends Judgment> List<S> save(Iterable<S> judgments);
    
    /**
     * Saves the given {@link Judgment} and flushes changes instantly.
     * Additionally it updates modification date of this judgment and
     * resets indexed flag.
     */
    public <S extends Judgment> S saveAndFlush(S judgment);
    
    
    /**
     * Deletes {@link Judgment}s with the given judgmentIds.<br/>
     * Note that in order to delete corresponding <code>{@link EnrichmentTag}s</code> and
     * references from <code>{@link EnrichmentTag}s</code> to judgment, you should use
     * a proper service  (JudgmentWithEnrichmentDeleter) from the saos-enrichment module. 
     */
    public void delete(List<Long> judgmentIds);
    
    /**
     * Deletes the given judgment<br/>
     * Note that in order to delete corresponding <code>{@link EnrichmentTag}s</code> and
     * references from <code>{@link EnrichmentTag}s</code> to judgment, you should use
     * a proper service (JudgmentWithEnrichmentDeleter) from the saos-enrichment module.
     */
    public void delete(Judgment judgment);
       
    /**
     * Deletes a {@link Judgment} with the given judgmentId<br/>
     * Note that in order to delete corresponding <code>{@link EnrichmentTag}s</code> and
     * references from <code>{@link EnrichmentTag}s</code> to judgment, you should use
     * a proper service (JudgmentWithEnrichmentDeleter) from the saos-enrichment module.
     */
    public void delete(Long judgmentId);
    
    /**
     * Deletes all {@link Judgment}s<br/>
     * Note that in order to delete corresponding <code>{@link EnrichmentTag}s</code> and
     * references from <code>{@link EnrichmentTag}s</code> to judgment, you should use
     * a proper service (JudgmentWithEnrichmentDeleter) from the saos-enrichment module.
     */
    public void deleteAll();
    
    /** Unsupported, use: {@link #deleteAll()} */
    public void deleteAllInBatch();
    
    /** Unsupported, use: {@link #delete(List)} */
    public void deleteInBatch(Iterable<Judgment> entities);

}
