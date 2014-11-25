package pl.edu.icm.saos.api.search.judgments.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.search.search.service.SearchService;

/**
 * Provides functionality for searching judgment's indexed documents is the Solr index.
 * @author pavtel
 */
@Service
public class JudgmentsApiSearchService {

    @Autowired
    private SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService;

    @Autowired
    private ParametersToCriteriaConverter converter;


    //------------------------ LOGIC --------------------------

    /**
     * Finds judgment's indexed documents that meet search criteria (judgmentsParameters).
     * @param judgmentsParameters search criteria.
     * @return search result.
     */
    public SearchResults<JudgmentSearchResult> performSearch(JudgmentsParameters judgmentsParameters){
        Pagination pagination = judgmentsParameters.getPagination();
        Paging paging = converter.toPaging(pagination);
        JudgmentCriteria judgmentCriteria = converter.toCriteria(judgmentsParameters);

        SearchResults<JudgmentSearchResult> resultSearchResults = judgmentsSearchService.search(judgmentCriteria, paging);
        return resultSearchResults;
    }




    //------------------------ SETTERS --------------------------
    public void setJudgmentsSearchService(SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService) {
        this.judgmentsSearchService = judgmentsSearchService;
    }

    public void setParametersToCriteriaConverter(ParametersToCriteriaConverter parametersToCriteriaConverter) {
        this.converter = parametersToCriteriaConverter;
    }
}
