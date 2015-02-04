package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * 
 * @author Łukasz Dumiszewski
 */
public interface JudgmentRepository extends JudgmentCommonRepository<Judgment>, JpaRepository<Judgment, Long>, JudgmentRepositoryCustom {

    
    /**
     * Deletes fast all the {@link Judgment}s with the given ids using just a few jpql/sql queries
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
