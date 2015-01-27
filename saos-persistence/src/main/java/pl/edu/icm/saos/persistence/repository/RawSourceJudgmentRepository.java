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
public interface RawSourceJudgmentRepository extends JpaRepository<RawSourceJudgment, Long> {
  
    @Query("select rJudgment from RawSourceJudgment rJudgment where TYPE(rJudgment)=:clazz and rJudgment.id=:id")
    <T extends RawSourceJudgment> T findOne(@Param("id") Long id, @Param("clazz") Class<T> clazz);
    
    
    @Query("select rJudgment from RawSourceJudgment rJudgment where TYPE(rJudgment)=:clazz and rJudgment.sourceId=:sourceId")
    <T extends RawSourceJudgment> T findOneBySourceId(@Param("sourceId") String sourceJudgmentId, @Param("clazz") Class<T> clazz);
    
    
    @Query("select count(rJudgment) from RawSourceJudgment rJudgment where TYPE(rJudgment)=:clazz")
    long count(@Param("clazz") Class<? extends RawSourceJudgment> clazz);

    
    @Query("select rJudgment.id from RawSourceJudgment rJudgment where TYPE(rJudgment) = :clazz and rJudgment.processed=false order by id")
    List<Long> findAllNotProcessedIds(@Param("clazz") Class<? extends RawSourceJudgment> clazz);
  
    
    @Transactional
    @Modifying
    @Query("delete from RawSourceJudgment j where TYPE(j) = :classToDelete")
    void deleteAll(@Param("classToDelete") Class<? extends RawSourceJudgment> classToDelete);
    
    
}
