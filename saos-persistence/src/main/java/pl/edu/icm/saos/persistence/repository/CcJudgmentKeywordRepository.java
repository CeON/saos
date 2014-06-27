package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;

/**
 * @author Łukasz Dumiszewski
 */

public interface CcJudgmentKeywordRepository  extends JpaRepository<CcJudgmentKeyword, Integer> {

    
    CcJudgmentKeyword findOneByPhrase(String phrase);
    
}
