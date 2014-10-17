package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

/**
 * @author Łukasz Dumiszewski
 */

public interface ScJudgmentFormRepository extends JpaRepository<SupremeCourtJudgmentForm, Integer> {

    
    SupremeCourtJudgmentForm findOneByName(String name);

}
