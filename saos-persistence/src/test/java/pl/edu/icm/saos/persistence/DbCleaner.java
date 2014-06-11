package pl.edu.icm.saos.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */

@Service
public class DbCleaner {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional
    public void clean() {
        deleteAll(Judgment.class);
        deleteAll(CommonCourt.class);
    }
    
    
    private void deleteAll(Class<?> clazz) {
        Query query = entityManager.createQuery("delete from " + clazz.getName());
        query.executeUpdate();
    }
    
}
