package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */
public interface JudgmentRepository extends JudgmentCommonRepository<Judgment>, JpaRepository<Judgment, Long>, JudgmentRepositoryCustom {

    
    /**
     * Deletes fast all the {@link Judgment}s with the given ids using just a few jpql/sql queries
     */
    public void delete(List<Long> judgmentIds);
    
    
}
