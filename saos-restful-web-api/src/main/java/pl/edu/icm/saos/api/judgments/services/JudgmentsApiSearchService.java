package pl.edu.icm.saos.api.judgments.services;

import static pl.edu.icm.saos.search.config.model.JudgmentIndexField.ID;

import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.api.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.api.search.ApiSearchService;
import pl.edu.icm.saos.api.search.ElementsSearchResults;
import pl.edu.icm.saos.api.transformers.SearchResultApiTransformer;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.search.search.model.Sorting;
import pl.edu.icm.saos.search.search.service.SearchService;

import com.google.common.base.Preconditions;

/**
 * @author pavtel
 */
@Service
public class JudgmentsApiSearchService implements ApiSearchService<Judgment, JudgmentsParameters> {

    @Autowired
    private SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService;


    @Autowired
    private SearchResultApiTransformer<JudgmentSearchResult, Judgment> resultApiTransformer;

    @Override
    public ElementsSearchResults<Judgment, JudgmentsParameters> performSearch(JudgmentsParameters judgmentsParameters) {
        Preconditions.checkNotNull(judgmentsParameters, "judgmentsParameters can't be null");

        Paging paging = toPaging(judgmentsParameters.getPagination());
        JudgmentCriteria judgmentCriteria = toCriteria(judgmentsParameters);

        SearchResults<JudgmentSearchResult> resultSearchResults = judgmentsSearchService.search(judgmentCriteria, paging);

        List<Judgment> judgments = resultApiTransformer.transform(resultSearchResults.getResults());

        return new ElementsSearchResults<>(judgments, judgmentsParameters);
    }



    private Paging toPaging(Pagination pagination){
        return new Paging(pagination.getOffset(), pagination.getLimit(), new Sorting(ID.getFieldName(), Sorting.Direction.ASC));
    }

    private JudgmentCriteria toCriteria(JudgmentsParameters params){
        JudgmentCriteria criteria = new JudgmentCriteria();

        criteria.setAll(params.getAll());
        criteria.setCourtName(params.getCourtName());
        criteria.setReferencedRegulation(params.getReferencedRegulation());
        criteria.setJudgeName(params.getJudgeName());
        criteria.setLegalBase(params.getLegalBase());
        criteria.setKeyword(params.getKeyword());

        criteria.setDateFrom(toDate(params.getJudgmentDateFrom()));
        criteria.setDateTo(toDate(params.getJudgmentDateTo()));

        return criteria;
    }

    private Date toDate(LocalDate localDate){
        if(localDate == null){
            return null;
        } else {
            return localDate.toDate();
        }
    }

    //*** setters ***
    public void setJudgmentsSearchService(SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService) {
        this.judgmentsSearchService = judgmentsSearchService;
    }

    public void setResultApiTransformer(SearchResultApiTransformer<JudgmentSearchResult, Judgment> resultApiTransformer) {
        this.resultApiTransformer = resultApiTransformer;
    }


}
