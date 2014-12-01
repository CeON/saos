package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;

/**
 * @author Łukasz Dumiszewski
 */

public interface CcJudgmentKeywordRepository  extends JpaRepository<CcJudgmentKeyword, Integer> {    
    
    CcJudgmentKeyword findOneByPhrase(String phrase);
    
    @Query("select keyword from #{#entityName} keyword where keyword.phrase like :phrasePart%")
    List<CcJudgmentKeyword> findAllByPhrasePart(@Param("phrasePart") String phrasePart);
    
}
