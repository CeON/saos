package pl.edu.icm.saos.persistence.search.implementor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pl.edu.icm.saos.persistence.search.dto.SearchFilter;

/**
 * A {@link SearchImplementor} hql abstract implementation. It is recommended to use it as a base class for classes that perform specific paging searches using the hql query language.
 *
 * @author lukdumi
 *
 * @param <X> SearchFilter
 * @param <T> Type of the searched object
 */

public abstract class AbstractJpqlSearchImplementor<X extends SearchFilter, T> extends AbstractStringQuerySearchImplementor<X, T> {

    @PersistenceContext
    private EntityManager entityManager;
   
    @Override
    protected List<T> executeQuery(String query, Map<String, Object> parametersMap, int first, int limit) {
        if(parametersMap == null) {
            parametersMap = Collections.emptyMap();
        }

        Query queryObject = entityManager.createQuery(query);

        parametersMap.forEach(queryObject::setParameter);
        queryObject.setMaxResults(limit);  // !!!won't work with join fetch!!!
        queryObject.setFirstResult(first);
        @SuppressWarnings("unchecked")
        List<T> list = queryObject.getResultList();
        return list;
   
   }
    
    @Override
    protected long executeCountQuery(String query, Map<String, Object> parametersMap) {
        return (Long)executeQuery(query, parametersMap, 0, 1).get(0);     
    }

    //**** setters ***
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
