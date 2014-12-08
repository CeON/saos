package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;

/**
 * @author Łukasz Dumiszewski
 */

public interface JudgmentKeywordRepository extends JpaRepository<JudgmentKeyword, Integer>, JudgmentKeywordRepositoryCustom {    
    
    JudgmentKeyword findOneByCourtTypeAndPhraseIgnoreCase(CourtType courtType, String phrase);
    
}
