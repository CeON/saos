package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */
public interface JudgmentRepository extends JudgmentCommonRepository<Judgment>, JpaRepository<Judgment, Integer>, JudgmentRepositoryCustom {

    
}
