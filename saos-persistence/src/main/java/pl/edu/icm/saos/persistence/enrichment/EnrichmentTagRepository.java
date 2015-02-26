package pl.edu.icm.saos.persistence.enrichment;

import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * @author Łukasz Dumiszewski
 */

public interface EnrichmentTagRepository extends JpaRepository<EnrichmentTag, Long> {
    
    @Query("select max(creationDate) from #{#entityName}")
    public DateTime findMaxCreationDate();
   
    
    public List<EnrichmentTag> findAllByJudgmentId(long judgmentId);
    
    @Query("select enrichmentTag from #{#entityName} enrichmentTag where enrichmentTag.judgmentId in (:judgmentIds) order by enrichmentTag.judgmentId")
    public List<EnrichmentTag> findAllByJudgmentIds(@Param("judgmentIds") List<Long> judgmentIds);
    
    
    @Transactional
    public void deleteAllByJudgmentId(long judgmentId);
}
