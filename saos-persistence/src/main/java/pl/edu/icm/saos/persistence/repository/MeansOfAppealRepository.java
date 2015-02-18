package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;

/**
 * @author madryk
 */
public interface MeansOfAppealRepository extends JpaRepository<MeansOfAppeal, Long> {

    MeansOfAppeal findOneByCourtTypeAndNameIgnoreCase(CourtType courtType, String name);
}
