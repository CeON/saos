package pl.edu.icm.saos.persistence;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service("dbUtils")
public class DbUtils {

    @Autowired
    private EntityManager entityManager;
    
    
    public void persist(Object... objects) {
        for (Object object : objects) {
            entityManager.persist(object);
        }
    }
    
}
