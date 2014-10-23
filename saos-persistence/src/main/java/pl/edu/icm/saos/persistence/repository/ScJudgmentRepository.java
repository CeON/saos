package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public interface ScJudgmentRepository extends JudgmentCommonRepository<SupremeCourtJudgment>, JpaRepository<SupremeCourtJudgment, Integer> {

}
