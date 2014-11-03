package pl.edu.icm.saos.persistence.correction;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;

/**
 * @author Łukasz Dumiszewski
 */

public interface JudgmentCorrectionRepository extends JpaRepository<JudgmentCorrection, Integer> {

}
