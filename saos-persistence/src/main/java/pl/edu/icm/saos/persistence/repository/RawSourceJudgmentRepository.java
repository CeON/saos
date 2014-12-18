package pl.edu.icm.saos.persistence.repository;

import java.util.List;

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
  
    @Query("select rJudgment from RawSourceJudgment rJudgment where TYPE(rJudgment)=:clazz and rJudgment.id=:id")
    <T extends RawSourceJudgment> T getOneWithClass(@Param("id") Integer id, @Param("clazz") Class<T> clazz);    

    @Query("select rJudgment.id from RawSourceJudgment rJudgment where TYPE(rJudgment) = :clazz and rJudgment.processed=false order by id")
    List<Integer> findAllNotProcessedIdsWithClass(@Param("clazz") Class<? extends RawSourceJudgment> clazz);
  
    @Transactional
    @Modifying
    @Query("delete from RawSourceJudgment j where TYPE(j) = :classToDelete")
    void deleteAllWithClass(@Param("classToDelete") Class<? extends RawSourceJudgment> classToDelete);
    
    
}
