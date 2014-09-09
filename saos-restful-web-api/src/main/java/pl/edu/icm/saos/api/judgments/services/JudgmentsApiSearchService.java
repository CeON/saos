package pl.edu.icm.saos.api.judgments.services;

import com.google.common.base.Preconditions;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.api.search.ApiSearchService;
import pl.edu.icm.saos.api.search.ElementsSearchResults;
import pl.edu.icm.saos.api.transformers.SearchResultApiTransformer;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.search.model.*;
import pl.edu.icm.saos.search.service.SearchService;
import static pl.edu.icm.saos.search.config.model.JudgmentIndexField.ID;

import java.util.List;

/**
 * @author pavtel
 */
@Service
public class JudgmentsApiSearchService implements ApiSearchService<Judgment> {

    @Autowired
    private SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService;


    @Autowired
    private SearchResultApiTransformer<JudgmentSearchResult, Judgment> resultApiTransformer;


    @Override
    public ElementsSearchResults<Judgment> performSearch(RequestParameters requestParameters) {
        Preconditions.checkNotNull(requestParameters, "requestParameters can't be null");

        Paging paging = toPaging(requestParameters.getPagination());
        JudgmentCriteria judgmentCriteria = new JudgmentCriteria();

        SearchResults<JudgmentSearchResult> resultSearchResults = judgmentsSearchService.search(judgmentCriteria, paging);

        List<Judgment> judgments = resultApiTransformer.transform(resultSearchResults.getResults());

        return new ElementsSearchResults<>(requestParameters, judgments);

    }

    private Paging toPaging(Pagination pagination){
        return new Paging(pagination.getOffset(), pagination.getLimit(), new Sorting(ID.getFieldName(), Sorting.Direction.ASC));
    }

    //*** setters ***
    public void setJudgmentsSearchService(SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService) {
        this.judgmentsSearchService = judgmentsSearchService;
    }

    public void setResultApiTransformer(SearchResultApiTransformer<JudgmentSearchResult, Judgment> resultApiTransformer) {
        this.resultApiTransformer = resultApiTransformer;
    }
}
