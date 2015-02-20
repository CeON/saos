package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentResult;

/**
 * @author madryk
 */
public interface JudgmentResultRepository extends JpaRepository<JudgmentResult, Long> {

    JudgmentResult findOneByCourtTypeAndTextIgnoreCase(CourtType courtType, String text);
}
