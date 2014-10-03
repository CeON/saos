package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.importer.SimpleRawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public interface SimpleRawSourceScJudgmentRepository extends JpaRepository<SimpleRawSourceScJudgment, Integer> {

}
