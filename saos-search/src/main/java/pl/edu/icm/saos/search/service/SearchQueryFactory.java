package pl.edu.icm.saos.search.service;

import org.apache.solr.client.solrj.SolrQuery;

import pl.edu.icm.saos.search.model.Criteria;
import pl.edu.icm.saos.search.model.Paging;

public interface SearchQueryFactory<C extends Criteria> {

    SolrQuery createQuery(C criteria, Paging paging);
}
