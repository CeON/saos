package pl.edu.icm.saos.search.search.service;

import pl.edu.icm.saos.search.search.model.Criteria;

/**
 * Creates part of solr query that depends on criteria  
 * @author madryk
 * @param <C> type of criteria that can be transformed
 */
public interface CriteriaTransformer<C extends Criteria> {

    /**
     * Creates part of solr query that depends on criteria
     * @param criteria
     * @return
     */
    String transformCriteria(C criteria);

}
