package pl.edu.icm.saos.api.search.judgments;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.icm.saos.api.search.judgments.item.representation.CommonCourtJudgmentItem;
import pl.edu.icm.saos.api.search.judgments.item.representation.SearchJudgmentItem;
import pl.edu.icm.saos.api.search.judgments.mapping.SearchCommonCourtJudgmentItemMapper;
import pl.edu.icm.saos.api.search.judgments.mapping.SearchJudgmentItemMapper;
import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.api.services.dates.DateMapping;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.ApiConstants.*;
import static pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.Info;
import static pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.QueryTemplate;

/**
 * Provides functionality for building success object view for list of judgments.
 * @author pavtel
 */
@Component("judgmentsListSuccessRepresentationBuilder")
public class JudgmentsListSuccessRepresentationBuilder {

    @Autowired
    private DateMapping dateMapping;

    @Autowired
    private SearchCommonCourtJudgmentItemMapper commonCourtJudgmentItemMapper;

    @Autowired
    private SearchJudgmentItemMapper judgmentItemMapper;




    //------------------------ LOGIC --------------------------

    /**
     * Constructs {@link pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView SearchJudgmentView}.
     * @param judgmentsParameters used to create queryTemplate in the view.
     * @param searchResults to process.
     * @param uriComponentsBuilder used to create links in the view.
     * @return representation
     */
    public SearchJudgmentsView build(JudgmentsParameters judgmentsParameters, SearchResults<JudgmentSearchResult> searchResults, UriComponentsBuilder uriComponentsBuilder){

        SearchJudgmentsView searchJudgmentsView = new SearchJudgmentsView();
        searchJudgmentsView.setLinks(toLinks(judgmentsParameters, searchResults, uriComponentsBuilder));
        searchJudgmentsView.setItems(toItems(searchResults.getResults()));
        searchJudgmentsView.setQueryTemplate(toQueryTemplate(judgmentsParameters));
        searchJudgmentsView.setInfo(toInfo(searchResults));

        return searchJudgmentsView;
    }



    //------------------------ PRIVATE --------------------------
    private List<Link> toLinks(JudgmentsParameters judgmentsParameters, SearchResults<JudgmentSearchResult> searchResults, UriComponentsBuilder uriComponentsBuilder) {
        List<Link> links = new LinkedList<>();

        Pagination pagination = judgmentsParameters.getPagination();

        links.add(buildLink(pagination, judgmentsParameters , SELF, uriComponentsBuilder));

        if(pagination.hasPrevious()){
            links.add(buildLink(pagination.getPrevious(), judgmentsParameters, Link.REL_PREVIOUS, uriComponentsBuilder));
        }

        if(pagination.hasNextIn(searchResults.getTotalResults())){
            links.add(buildLink(pagination.getNext(), judgmentsParameters, Link.REL_NEXT, uriComponentsBuilder));
        }


        return links;
    }

    private Link buildLink(Pagination pagination,  JudgmentsParameters params, String relName, UriComponentsBuilder uriComponentsBuilder) {

        uriComponentsBuilder
                .replaceQueryParam(PAGE_SIZE, pagination.getPageSize())
                .replaceQueryParam(PAGE_NUMBER, pagination.getPageNumber());

        if(StringUtils.isNotBlank(params.getAll())){
            uriComponentsBuilder.replaceQueryParam(ALL, params.getAll());
        }

        if(StringUtils.isNotBlank(params.getCourtName())){
            uriComponentsBuilder.replaceQueryParam(COURT_NAME, params.getCourtName());
        }

        if(StringUtils.isNotBlank(params.getJudgeName())){
            uriComponentsBuilder.replaceQueryParam(JUDGE_NAME, params.getJudgeName());
        }

        if(StringUtils.isNotBlank(params.getKeyword())){
            uriComponentsBuilder.replaceQueryParam(KEYWORD, params.getKeyword());
        }

        if(StringUtils.isNotBlank(params.getLegalBase())){
            uriComponentsBuilder.replaceQueryParam(LEGAL_BASE, params.getLegalBase());
        }

        if(StringUtils.isNotBlank(params.getReferencedRegulation())){
            uriComponentsBuilder.replaceQueryParam(REFERENCED_REGULATION, params.getReferencedRegulation());
        }

        if(params.getJudgmentDateFrom()!=null){
            uriComponentsBuilder.replaceQueryParam(JUDGMENT_DATE_FROM, params.getJudgmentDateFrom().toString());
        }

        if(params.getJudgmentDateTo() != null){
            uriComponentsBuilder.replaceQueryParam(JUDGMENT_DATE_TO, params.getJudgmentDateTo().toString());
        }

        String path = uriComponentsBuilder.build().encode().toUriString();
        return new Link(path, relName);
    }


    private List<SearchJudgmentItem> toItems(List<JudgmentSearchResult> judgmentSearchResults) {
        if(judgmentSearchResults == null){
            judgmentSearchResults = Collections.emptyList();
        }

        List<SearchJudgmentItem> items = judgmentSearchResults.stream()
                .map(judgmentSearchResult -> judgmentItem(judgmentSearchResult))
                .collect(Collectors.toList());

        return items;
    }

    private SearchJudgmentItem judgmentItem(JudgmentSearchResult judgmentSearchResult) {
        SearchJudgmentItem item = initializeItemViewAndFillSpecificFields(judgmentSearchResult);
        judgmentItemMapper.fillJudgmentsFieldsToItemRepresentation(item, judgmentSearchResult);

        return item;
    }

    private SearchJudgmentItem initializeItemViewAndFillSpecificFields(JudgmentSearchResult judgmentSearchResult) {
        if(isCommonCourtJudgment(judgmentSearchResult)){
            CommonCourtJudgmentItem item = new CommonCourtJudgmentItem();
            commonCourtJudgmentItemMapper.fillJudgmentsFieldsToItemRepresentation(item, judgmentSearchResult);
            return item;
        } else {
            //default
            return new SearchJudgmentItem();
        }
    }

    private boolean isCommonCourtJudgment(JudgmentSearchResult judgmentSearchResult){
        return StringUtils.isNotBlank(judgmentSearchResult.getCourtDivisionCode());
    }

    private QueryTemplate toQueryTemplate(JudgmentsParameters params) {
        QueryTemplate queryTemplate = new QueryTemplate();

        Pagination pagination = params.getPagination();

        queryTemplate.setPageNumber(pagination.getPageNumber());
        queryTemplate.setPageSize(pagination.getPageSize());

        queryTemplate.setAll(removeNull(params.getAll()));
        queryTemplate.setLegalBase(removeNull(params.getLegalBase()));
        queryTemplate.setReferencedRegulation(removeNull(params.getReferencedRegulation()));
        queryTemplate.setKeyword(removeNull(params.getKeyword()));
        queryTemplate.setCourtName(removeNull(params.getCourtName()));
        queryTemplate.setJudgeName(removeNull(params.getJudgeName()));

        queryTemplate.setJudgmentDateFrom(dateMapping.toISO8601Format(params.getJudgmentDateFrom()));
        queryTemplate.setJudgmentDateTo(dateMapping.toISO8601Format(params.getJudgmentDateTo()));

        return queryTemplate;
    }

    private Info toInfo(SearchResults<JudgmentSearchResult> searchResults) {
        Info info = new Info();
        info.setTotalResults(searchResults.getTotalResults());

        return info;
    }

    private String removeNull(String str){
        if(str == null){
            return "";
        } else {
            return str;
        }
    }

    //------------------------ SETTERS --------------------------


    public void setDateMapping(DateMapping dateMapping) {
        this.dateMapping = dateMapping;
    }

    public void setCommonCourtJudgmentItemMapper(SearchCommonCourtJudgmentItemMapper commonCourtJudgmentItemMapper) {
        this.commonCourtJudgmentItemMapper = commonCourtJudgmentItemMapper;
    }

    public void setJudgmentItemMapper(SearchJudgmentItemMapper judgmentItemMapper) {
        this.judgmentItemMapper = judgmentItemMapper;
    }
}
