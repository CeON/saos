package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pl.edu.icm.saos.persistence.model.DeletedJudgment;
/**
 * @author Łukasz Dumiszewski
 */
public interface DeletedJudgmentRepository extends JpaRepository<DeletedJudgment, Long> {

    /**
     * Returns list of all {@link DeletedJudgment#getJudgmentId()}s
     */
    @Query("select j.judgmentId from #{#entityName} j")
    List<Long> findAllJudgmentIds();
    
    
}
