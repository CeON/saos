package pl.edu.icm.saos.persistence.enrichment;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;


/**
 * @author Łukasz Dumiszewski
 */

public interface UploadEnrichmentTagRepository extends JpaRepository<UploadEnrichmentTag, Integer> {

    @Query("select max(creationDate) from #{#entityName}")
    public DateTime findMaxCreationDate();
   
    /** Use wisely, it <b>truncates</b> the table, cannot be rollbacked */
    @Modifying
    @Transactional
    @Query(value="truncate table upload_enrichment_tag", nativeQuery=true)
    public void truncate();
    
    
    
}
