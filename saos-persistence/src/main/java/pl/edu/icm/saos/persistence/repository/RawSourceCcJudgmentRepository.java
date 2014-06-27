package pl.edu.icm.saos.persistence.repository;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public interface RawSourceCcJudgmentRepository extends JpaRepository<RawSourceCcJudgment, Integer> {

    
    RawSourceCcJudgment findOneBySourceIdAndDataMd5(String sourceId, String dataMd5);
    
    
    @Query("select max(rJudgment.publicationDate) from RawSourceCcJudgment rJudgment")
    DateTime findMaxPublicationDate();
    
}
