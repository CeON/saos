package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */

public interface JudgmentRepository extends CrudRepository<Judgment, Integer> {

    

}
