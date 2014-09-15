package pl.edu.icm.saos.search.search.service;

import org.apache.solr.client.solrj.SolrQuery;

import pl.edu.icm.saos.search.search.model.Criteria;
import pl.edu.icm.saos.search.search.model.Paging;

public interface SearchQueryFactory<C extends Criteria> {

    SolrQuery createQuery(C criteria, Paging paging);
}
