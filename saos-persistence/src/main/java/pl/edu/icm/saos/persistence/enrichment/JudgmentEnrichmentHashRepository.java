package pl.edu.icm.saos.persistence.enrichment;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import pl.edu.icm.saos.persistence.enrichment.model.JudgmentEnrichmentHash;

/**
 * @author madryk
 */
public interface JudgmentEnrichmentHashRepository extends JpaRepository<JudgmentEnrichmentHash, Long> {
    
    @Modifying
    @Transactional
    @Query("update #{#entityName} hash set hash.processed=true WHERE hash.processed=false")
    void markAllAsProcessed();
    
    @Query("select hash.judgmentId from #{#entityName} hash WHERE hash.processed=false")
    List<Long> findAllJudgmentsIdsToProcess();
    
    JudgmentEnrichmentHash findByJudgmentId(long judgmentId);
    
}
