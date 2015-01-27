package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public interface RawSourceScJudgmentRepository extends JpaRepository<RawSourceScJudgment, Long> {

    
    /**
     * Finds all {@link RawSourceScJudgment}s that are not marked as processed ({@link RawSourceScJudgment#isProcessed()})
     */
    @Query("select rJudgment.id from RawSourceScJudgment rJudgment where rJudgment.processed=false order by id")
    List<Long> findAllNotProcessedIds();

    /**
     * Finds a {@link RawSourceScJudgment} with the given sourceJudgmentId ({@link RawSourceScJudgment#getSourceId})
     * @return
     */
    RawSourceScJudgment findOneBySourceId(String sourceJudgmentId);
}
