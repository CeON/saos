package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pl.edu.icm.saos.persistence.model.importer.SimpleRawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public interface SimpleRawSourceScJudgmentRepository extends JpaRepository<SimpleRawSourceScJudgment, Integer> {

    
    /**
     * Finds all {@link SimpleRawSourceScJudgment}s that are not marked as processed ({@link SimpleRawSourceScJudgment#isProcessed()})
     */
    @Query("select rJudgment.id from SimpleRawSourceScJudgment rJudgment where rJudgment.processed=false order by id")
    List<Integer> findAllNotProcessedIds();

}
