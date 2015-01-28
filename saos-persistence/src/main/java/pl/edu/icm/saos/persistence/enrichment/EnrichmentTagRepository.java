package pl.edu.icm.saos.persistence.enrichment;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * @author Łukasz Dumiszewski
 */

public interface EnrichmentTagRepository extends JpaRepository<EnrichmentTag, Long> {
    
    @Query("select max(creationDate) from #{#entityName}")
    public DateTime findMaxCreationDate();
   
    
    public List<EnrichmentTag> findAllByJudgmentId(long judgmentId);
}
