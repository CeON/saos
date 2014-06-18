package pl.edu.icm.saos.persistence.repository;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public class CcJudgmentRepositoryCustomImpl implements CcJudgmentRepositoryCustom {

    @Autowired
    private EntityManager entityManager;
    
    
    @Override
    public DateTime getMaxSourcePublicationDate() {
        String jpql = "select max(judgmentSource.sourcePublicationDate) from " + CommonCourtJudgment.class.getName();
        return entityManager.createQuery(jpql, DateTime.class).getSingleResult();
    }

}
