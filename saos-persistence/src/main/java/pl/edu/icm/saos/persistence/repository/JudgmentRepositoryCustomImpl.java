package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.common.InitializingVisitor;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;

/**
 * @author Łukasz Dumiszewski
 */
@Service("judgmentRepositoryCustom")
public class JudgmentRepositoryCustomImpl implements JudgmentRepositoryCustom {

    
    @Autowired
    private EntityManager entityManager;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    @Transactional
    public <T extends Judgment> T findOneAndInitialize(int id) {
        Judgment judgment = entityManager.find(Judgment.class, id);
        
        if (judgment != null) {
            judgment.accept(new InitializingVisitor());
        }
        
        @SuppressWarnings("unchecked")
        T retJudgment = (T)judgment;
        return retJudgment;
        
    }

    @Override
    @Transactional
    public void delete(List<Integer> judgmentIds) {
        
        deleteJudgmentAttributes(JudgmentCorrection.class, judgmentIds);
        
        deleteJudgmentAttributes(CourtCase.class, judgmentIds);
        deleteJudgmentAttributesSql("judgment_court_reporter", judgmentIds);
        
        deleteJudgmentAttributes(JudgmentReferencedRegulation.class, judgmentIds);
        
        deleteJudgmentAttributesSql("assigned_cc_judgment_keyword", judgmentIds);
        deleteJudgmentAttributesSql("judgment_legal_bases", judgmentIds);
        
        deleteJudgmentAttributesSql("supreme_court_judgment_chamber", judgmentIds);
        
        deleteJudgeRoles(judgmentIds);
        deleteJudgmentAttributes(Judge.class, judgmentIds);
        
        deleteJudgments(judgmentIds);
        
    }


   
    
    //------------------------ PRIVATE --------------------------
    
    private void deleteJudgmentAttributes(Class<?> judgmentAttrClass, List<Integer> judgmentIds) {
        Query q = entityManager.createQuery("delete from " + judgmentAttrClass.getName() + " a where a.judgment.id in (:judgmentIds)").setParameter("judgmentIds", judgmentIds);
        q.executeUpdate();
    }
    
    private void deleteJudgmentAttributesSql(String tableName, List<Integer> judgmentIds) {
        Query q = entityManager.createNativeQuery("delete from " + tableName + " where fk_judgment in (:judgmentIds)").setParameter("judgmentIds", judgmentIds);
        q.executeUpdate();
    }
    
    private void deleteJudgeRoles(List<Integer> judgmentIds) {
        Query q = entityManager.createNativeQuery("delete from judge_role jr where fk_judge in (select id from judge where fk_judgment in (:judgmentIds))"); 
        q.setParameter("judgmentIds", judgmentIds);
        q.executeUpdate();
    }
    
    private void deleteJudgments(List<Integer> judgmentIds) {
        Query q = entityManager.createQuery("delete from " + Judgment.class.getName() + " j where j.id in (:judgmentIds)").setParameter("judgmentIds", judgmentIds);
        q.executeUpdate();
    }

}
