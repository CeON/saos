package pl.edu.icm.saos.persistence.repository;

/**
 * Repository class for indexable objects. It contains methods written 'by hand'.
 * 
 * @author madryk
 */
public interface IndexableObjectRepositoryCustom {
    
    /**
     * Marks indexable object with given id as indexed.
     * It also updates indexedDate of object.
     */
    void markAsIndexed(Long id);
}
