package pl.edu.icm.saos.persistence.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;

/**
 * @author madryk
 */
public interface RawSourceJudgmentRepository extends JpaRepository<RawSourceJudgment, Integer> {

    @Transactional
    @Modifying
    @Query("delete from RawSourceJudgment j where TYPE(j) = :classToDelete")
    void deleteAllWithClass(@Param("classToDelete") Class<? extends RawSourceJudgment> classToDelete);
    
    
}
