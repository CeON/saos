package pl.edu.icm.saos.search.search.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.SearchResults;

@Service
public class JudgmentSearchService implements SearchService<JudgmentSearchResult, JudgmentCriteria> {
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer solrServer;
    
    @Autowired
    private SearchQueryFactory<JudgmentCriteria> queryFactory;
    
    @Autowired
    private SearchResultsTranslator<JudgmentSearchResult> resultsTranslator;

    @Override
    public SearchResults<JudgmentSearchResult> search(JudgmentCriteria criteria,
            Paging paging) {

        SolrQuery solrQuery = queryFactory.createQuery(criteria, paging);
        
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
