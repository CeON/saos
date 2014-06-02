package pl.edu.icm.saos.persistence;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.model.Judge;

/**
 * @author Łukasz Dumiszewski
 */

public class HibernateTest extends PersistenceTestSupport {

    @Autowired
    private SessionFactory sessionFactory;
    
    
    @Test
    @Transactional
    public void test() {
        Session session = sessionFactory.getCurrentSession();
        Judge judge = new Judge();
        session.save(judge);
    }
    
}
