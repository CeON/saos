package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SourceCode;

/**
 * @author Łukasz Dumiszewski
 */

public interface JudgmentCommonRepository<T extends Judgment> extends IndexableObjectRepository<T> {

    @Query("select count(j) from #{#entityName} j where TYPE(j)=:clazz")
    long count(@Param("clazz") Class<? extends T> clazz);
    
    @Query("select j from #{#entityName} j where j.sourceInfo.sourceCode=:sourceCode and j.sourceInfo.sourceJudgmentId=:sourceJudgmentId ")
    T findOneBySourceCodeAndSourceJudgmentId(@Param("sourceCode") SourceCode sourceCode, @Param("sourceJudgmentId") String sourceJudgmentId);
    
    
    @Query("select j from #{#entityName} j where TYPE(j)=:clazz and j.sourceInfo.sourceCode=:sourceCode and j.sourceInfo.sourceJudgmentId=:sourceJudgmentId ")
    <E extends T> E findOneBySourceCodeAndSourceJudgmentId(@Param("sourceCode") SourceCode sourceCode, @Param("sourceJudgmentId") String sourceJudgmentId, @Param("clazz") Class<E> clazz);

    /**
     * 
     * Returns list of {@link Judgment}s of the given type, origin source and case number.<br/>
     * Returns empty list if no appropriate judgments have been found <br/><br/>
     * Does not return a single judgment because there is no certainty that judgment case numbers are always unique <br/>
     * 
     */
    @Query("select j from #{#entityName} j join j.courtCases_ courtCase where courtCase.caseNumber=:caseNumber and j.sourceInfo.sourceCode=:sourceCode")
    List<T> findBySourceCodeAndCaseNumber(@Param("sourceCode") SourceCode sourceCode, @Param("caseNumber") String caseNumber);

    /**
     * Change {@link Judgment judgments} indexed flag with given
     * {@link SourceCode} to <code>false</code>.
     * @param sourceCode - if <code>null</code> then marks all judgments as
     * not indexed
     */
    @Modifying
    @Transactional
    @Query("update #{#entityName} j set j.indexed=false where j.indexed=true and (j.sourceInfo.sourceCode=:sourceCode or :sourceCode is null)")
    void markAsNotIndexedBySourceCode(@Param("sourceCode") SourceCode sourceCode);
    
    
    @Query("select j.id from #{#entityName} j where j.id in(:ids)")
    List<Long> filterIdsToExisting(@Param("ids") List<Long> ids);
    
    
}
