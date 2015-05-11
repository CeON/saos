package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import pl.edu.icm.saos.persistence.common.InitializingVisitor;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent;

/**
 * @author Łukasz Dumiszewski
 */
@Service("judgmentRepositoryCustom")
public class JudgmentRepositoryCustomImpl implements JudgmentRepositoryCustom {

    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    @Transactional
    public <T extends Judgment> T findOneAndInitialize(long id) {
        Judgment judgment = entityManager.find(Judgment.class, id);
        
        if (judgment != null) {
            judgment.accept(new InitializingVisitor());
        } else {
        	throw new EntityNotFoundException();
        }
        
        @SuppressWarnings("unchecked")
        T retJudgment = (T)judgment;
        return retJudgment;
        
    }

    @Override
    @Transactional
    public void delete(List<Long> judgmentIds) {
        
        deleteJudgmentAttributes(JudgmentCorrection.class, judgmentIds);
        
        deleteJudgmentAttributes(CourtCase.class, judgmentIds);
        deleteJudgmentAttributesSql("judgment_court_reporter", judgmentIds);
        
        deleteJudgmentAttributes(JudgmentReferencedRegulation.class, judgmentIds);
        
        deleteJudgmentAttributesSql("assigned_judgment_keyword", judgmentIds);
        deleteJudgmentAttributesSql("judgment_legal_bases", judgmentIds);
        
        deleteJudgmentAttributesSql("supreme_court_judgment_chamber", judgmentIds);
        
        deleteCtJudgmentOpinionAuthors(judgmentIds);
        deleteJudgmentAttributes(ConstitutionalTribunalJudgmentDissentingOpinion.class, judgmentIds);
        
        deleteJudgeRoles(judgmentIds);
        deleteJudgmentAttributes(Judge.class, judgmentIds);
        
        deleteJudgmentAttributesSql("judgment_lower_court_judgments", judgmentIds);
        
        deleteJudgmentAttributes(JudgmentTextContent.class, judgmentIds);
        
        deleteJudgments(judgmentIds);
        
    }

    @Override
    @Transactional
    public void delete(Judgment judgment) {
        
        entityManager.remove(entityManager.contains(judgment) ? judgment : entityManager.merge(judgment));
        
    }
   
    @Override
    @Transactional
    public void delete(Long judgmentId) {
        
        Assert.notNull(judgmentId, "The given id must not be null!");

        Judgment judgment = judgmentRepository.findOne(judgmentId);

        if (judgment == null) {
            throw new EmptyResultDataAccessException(String.format("No %s entity with id %s exists!",
                    Judgment.class, judgmentId), 1);
        }

        delete(judgment);
        
    }

    
    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException();
        
    }

    @Override
    public void deleteInBatch(Iterable<Judgment> entities) {
        throw new UnsupportedOperationException();
        
    }

    @Override
    public void deleteAll() {
        for (Judgment element : judgmentRepository.findAll()) {
             delete(element);
        }
    }

    
    //------------------------ PRIVATE --------------------------
    
    private void deleteJudgmentAttributes(Class<?> judgmentAttrClass, List<Long> judgmentIds) {
        Query q = entityManager.createQuery("delete from " + judgmentAttrClass.getName() + " a where a.judgment.id in (:judgmentIds)").setParameter("judgmentIds", judgmentIds);
        q.executeUpdate();
    }
    
    private void deleteJudgmentAttributesSql(String tableName, List<Long> judgmentIds) {
        Query q = entityManager.createNativeQuery("delete from " + tableName + " where fk_judgment in (:judgmentIds)").setParameter("judgmentIds", judgmentIds);
        q.executeUpdate();
    }
    
    private void deleteCtJudgmentOpinionAuthors(List<Long> judgmentIds) {
        Query q = entityManager.createNativeQuery("delete from ct_judgment_opinion_author where fk_ct_judgment_opinion in (select id from ct_judgment_opinion where fk_judgment in (:judgmentIds))").setParameter("judgmentIds", judgmentIds);
        q.executeUpdate();
    }
    
    private void deleteJudgeRoles(List<Long> judgmentIds) {
        Query q = entityManager.createNativeQuery("delete from judge_role jr where fk_judge in (select id from judge where fk_judgment in (:judgmentIds))"); 
        q.setParameter("judgmentIds", judgmentIds);
        q.executeUpdate();
    }
    
    private void deleteJudgments(List<Long> judgmentIds) {
        Query q = entityManager.createQuery("delete from " + Judgment.class.getName() + " j where j.id in (:judgmentIds)").setParameter("judgmentIds", judgmentIds);
        q.executeUpdate();
    }

  
    
}
