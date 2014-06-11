package pl.edu.icm.saos.persistence.repository;

import java.util.Date;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public class CcJudgmentRepositoryCustomImpl implements CcJudgmentRepositoryCustom {

    @Autowired
    private EntityManager entityManager;
    
    
    @Override
    public Date getMaxSourcePublicationDate() {
        String jpql = "select max(judgmentSource.sourcePublicationDate) from " + CommonCourtJudgment.class.getName();
        return entityManager.createQuery(jpql, Date.class).getSingleResult();
    }

}
