package pl.edu.icm.saos.persistence.enrichment;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;


/**
 * @author Łukasz Dumiszewski
 */

public interface UploadEnrichmentTagRepository extends JpaRepository<UploadEnrichmentTag, Integer> {

    @Query("select max(creationDate) from #{#entityName}")
    public DateTime findMaxCreationDate();
   
}
