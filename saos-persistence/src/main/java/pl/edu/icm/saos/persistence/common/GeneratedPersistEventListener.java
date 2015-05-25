package pl.edu.icm.saos.persistence.common;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;

/**
 * Hibernate persist listener detecting attempts to persist generated entities.
 * 
 * @author madryk
 */
public class GeneratedPersistEventListener implements PersistEventListener {

    private static final long serialVersionUID = 1L;

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void onPersist(PersistEvent event) throws HibernateException {
        
        Object originalEntity = event.getObject();
        
        if (originalEntity instanceof Generatable && ((Generatable)originalEntity).isGenerated()) {
            throw new GeneratedEntityPersistException(originalEntity);
        }
    }

    @Override
    public void onPersist(PersistEvent event, @SuppressWarnings("rawtypes") Map createdAlready) throws HibernateException {
        
        Object originalEntity = event.getObject();
        
        if (originalEntity instanceof Generatable && ((Generatable)originalEntity).isGenerated()) {
            throw new GeneratedEntityPersistException(originalEntity);
        }
    }

}
