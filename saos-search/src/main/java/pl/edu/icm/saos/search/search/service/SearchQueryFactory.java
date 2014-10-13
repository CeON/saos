package pl.edu.icm.saos.search.search.service;

import org.apache.solr.client.solrj.SolrQuery;

import pl.edu.icm.saos.search.search.model.Criteria;
import pl.edu.icm.saos.search.search.model.Paging;

/**
 * Creates {@link SolrQuery} based on criteria and paging
 * @author madryk
 * @param <C> type of criteria that is supported
 */
public interface SearchQueryFactory<C extends Criteria> {

    /**
     * Creates {@link SolrQuery} based on criteria and paging
     * @param criteria
     * @param paging
     * @return Solr query
     */
    SolrQuery createQuery(C criteria, Paging paging);
}
