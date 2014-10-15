package pl.edu.icm.saos.api.judgments.services;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.api.search.ApiSearchService;
import pl.edu.icm.saos.api.search.ElementsSearchResults;
import pl.edu.icm.saos.api.transformers.SearchResultApiTransformer;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.search.search.model.*;
import pl.edu.icm.saos.search.search.service.SearchService;

import java.util.List;

import static pl.edu.icm.saos.search.config.model.JudgmentIndexField.ID;

/**
 * {@inheritDoc}
 * @author pavtel
 */
@Service
public class JudgmentsApiSearchService implements ApiSearchService<Judgment, JudgmentsParameters> {

    //********* fields **********
    @Autowired
    private SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService;


    @Autowired
    private SearchResultApiTransformer<JudgmentSearchResult, Judgment> resultApiTransformer;

    //********* END fields **********

    //******* business methods ***********
    @Override
    public ElementsSearchResults<Judgment, JudgmentsParameters> performSearch(JudgmentsParameters judgmentsParameters) {
        Preconditions.checkNotNull(judgmentsParameters, "judgmentsParameters can't be null");

        Pagination pagination = judgmentsParameters.getPagination();
        Paging paging = toPaging(pagination);
        JudgmentCriteria judgmentCriteria = toCriteria(judgmentsParameters);

        SearchResults<JudgmentSearchResult> resultSearchResults = judgmentsSearchService.search(judgmentCriteria, paging);

        List<Judgment> judgments = resultApiTransformer.transform(resultSearchResults.getResults());

        return new ElementsSearchResults<>(judgments, judgmentsParameters)
                .totalResults(resultSearchResults.getTotalResults())
                ;
    }


    private Paging toPaging(Pagination pagination){
        return new Paging(pagination.getPageNumber(), pagination.getPageSize(), new Sorting(ID.getFieldName(), Sorting.Direction.ASC));
    }

    private JudgmentCriteria toCriteria(JudgmentsParameters params){
        JudgmentCriteria criteria = new JudgmentCriteria();

        criteria.setAll(params.getAll());
        criteria.setCourtName(params.getCourtName());
        criteria.setReferencedRegulation(params.getReferencedRegulation());
        criteria.setJudgeName(params.getJudgeName());
        criteria.setLegalBase(params.getLegalBase());
        criteria.setKeyword(params.getKeyword());

        criteria.setDateFrom(params.getJudgmentDateFrom());
        criteria.setDateTo(params.getJudgmentDateTo());

        return criteria;
    }

    //******* END business methods ***********

    //*** setters ***
    public void setJudgmentsSearchService(SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService) {
        this.judgmentsSearchService = judgmentsSearchService;
    }

    public void setResultApiTransformer(SearchResultApiTransformer<JudgmentSearchResult, Judgment> resultApiTransformer) {
        this.resultApiTransformer = resultApiTransformer;
    }


}
