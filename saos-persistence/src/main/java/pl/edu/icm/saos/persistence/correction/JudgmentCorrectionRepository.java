package pl.edu.icm.saos.persistence.correction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;

/**
 * @author Łukasz Dumiszewski
 */

public interface JudgmentCorrectionRepository extends JpaRepository<JudgmentCorrection, Integer> {

    @Modifying
    @Query("delete from #{#entityName} jc where jc.judgment.id in (:judgmentIds)")
    @Transactional
    public void deleteByJudgmentIds(@Param("judgmentIds") List<Integer> judgmentIds);
}
