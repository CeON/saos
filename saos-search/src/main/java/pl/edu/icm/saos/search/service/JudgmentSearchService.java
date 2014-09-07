package pl.edu.icm.saos.search.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.model.Paging;
import pl.edu.icm.saos.search.model.SearchResults;

@Service
public class JudgmentSearchService implements SearchService<JudgmentSearchResult, JudgmentCriteria> {
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    SolrServer solrServer;
    
    @Autowired
    JudgmentSearchQueryFactory queryFactory;
    
    @Autowired
    JudgmentSearchResultsTranslator resultsTranslator;

    @Override
    public SearchResults<JudgmentSearchResult> search(JudgmentCriteria query,
            Paging paging) {

        SolrQuery solrQuery = queryFactory.createQuery(query, paging);
        
        QueryResponse response = null;
        try {
            response = solrServer.query(solrQuery);
        } catch (SolrServerException e) {
            e.printStackTrace();
            return new SearchResults<JudgmentSearchResult>();
        }
        
        SearchResults<JudgmentSearchResult> results = resultsTranslator.translate(response);
        
        
        return results;
    }

}
