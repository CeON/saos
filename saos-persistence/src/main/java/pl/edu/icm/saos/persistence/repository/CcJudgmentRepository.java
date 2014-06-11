package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public interface CcJudgmentRepository extends JpaRepository<CommonCourtJudgment, Integer>, CcJudgmentRepositoryCustom {

    
    
}
