package pl.edu.icm.saos.persistence.common;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;

/**
 * Hibernate merge listener detecting attempts to merge into session generated entities.
 * 
 * @author madryk
 */
public class GeneratedMergeEventListener implements MergeEventListener {

    private static final long serialVersionUID = 1L;

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void onMerge(MergeEvent event) throws HibernateException {
        
        Object originalEntity = event.getOriginal();
        
        if (originalEntity instanceof Generatable && ((Generatable)originalEntity).isGenerated()) {
            throw new GeneratedEntityMergeException(originalEntity);
        }
    }

    @Override
    public void onMerge(MergeEvent event, @SuppressWarnings("rawtypes") Map copiedAlready) throws HibernateException {
        
        Object originalEntity = event.getOriginal();
        
        if (originalEntity instanceof Generatable && ((Generatable)originalEntity).isGenerated()) {
            throw new GeneratedEntityMergeException(originalEntity);
        }
    }
    
    


}
