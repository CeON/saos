package pl.edu.icm.saos.api.search.judgments.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.search.search.model.*;
import pl.edu.icm.saos.search.search.service.SearchService;

import static pl.edu.icm.saos.search.config.model.JudgmentIndexField.ID;

/**
 * {@inheritDoc}
 * @author pavtel
 */
@Service
public class JudgmentsApiSearchService {

    @Autowired
    private SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService;


    //------------------------ LOGIC --------------------------
    public SearchResults<JudgmentSearchResult> performSearch(JudgmentsParameters judgmentsParameters){
        Pagination pagination = judgmentsParameters.getPagination();
        Paging paging = toPaging(pagination);
        JudgmentCriteria judgmentCriteria = toCriteria(judgmentsParameters);

        SearchResults<JudgmentSearchResult> resultSearchResults = judgmentsSearchService.search(judgmentCriteria, paging);
        return resultSearchResults;
    }

    //------------------------ PRIVATE --------------------------

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


    //------------------------ SETTERS --------------------------
    public void setJudgmentsSearchService(SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService) {
        this.judgmentsSearchService = judgmentsSearchService;
    }



}
