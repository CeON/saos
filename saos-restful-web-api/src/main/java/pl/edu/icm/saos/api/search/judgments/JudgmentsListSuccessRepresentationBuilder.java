package pl.edu.icm.saos.api.search.judgments;

import static pl.edu.icm.saos.api.ApiConstants.ALL;
import static pl.edu.icm.saos.api.ApiConstants.CASE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.CC_COURT_CODE;
import static pl.edu.icm.saos.api.ApiConstants.CC_COURT_ID;
import static pl.edu.icm.saos.api.ApiConstants.CC_COURT_NAME;
import static pl.edu.icm.saos.api.ApiConstants.CC_COURT_TYPE;
import static pl.edu.icm.saos.api.ApiConstants.CC_DIVISION_CODE;
import static pl.edu.icm.saos.api.ApiConstants.CC_DIVISION_ID;
import static pl.edu.icm.saos.api.ApiConstants.CC_DIVISION_NAME;
import static pl.edu.icm.saos.api.ApiConstants.COURT_TYPE;
import static pl.edu.icm.saos.api.ApiConstants.JUDGE_NAME;
import static pl.edu.icm.saos.api.ApiConstants.JUDGMENT_DATE_FROM;
import static pl.edu.icm.saos.api.ApiConstants.JUDGMENT_DATE_TO;
import static pl.edu.icm.saos.api.ApiConstants.JUDGMENT_TYPES;
import static pl.edu.icm.saos.api.ApiConstants.KEYWORDS;
import static pl.edu.icm.saos.api.ApiConstants.LEGAL_BASE;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;
import static pl.edu.icm.saos.api.ApiConstants.REFERENCED_REGULATION;
import static pl.edu.icm.saos.api.ApiConstants.SC_CHAMBER_ID;
import static pl.edu.icm.saos.api.ApiConstants.SC_CHAMBER_NAME;
import static pl.edu.icm.saos.api.ApiConstants.SC_DIVISION_ID;
import static pl.edu.icm.saos.api.ApiConstants.SC_DIVISION_NAME;
import static pl.edu.icm.saos.api.ApiConstants.SC_PERSONNEL_TYPE;
import static pl.edu.icm.saos.api.ApiConstants.SELF;
import static pl.edu.icm.saos.api.ApiConstants.SORTING_DIRECTION;
import static pl.edu.icm.saos.api.ApiConstants.SORTING_FIELD;
import static pl.edu.icm.saos.api.ApiConstants.CC_INCLUDE_DEPENDENT_COURT_JUDGMENTS;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import pl.edu.icm.saos.api.search.judgments.item.representation.CommonCourtJudgmentItem;
import pl.edu.icm.saos.api.search.judgments.item.representation.SearchJudgmentItem;
import pl.edu.icm.saos.api.search.judgments.item.representation.SupremeCourtJudgmentItem;
import pl.edu.icm.saos.api.search.judgments.mapping.SearchCommonCourtJudgmentItemMapper;
import pl.edu.icm.saos.api.search.judgments.mapping.SearchConstitutionalTribunalJudgmentItemMapper;
import pl.edu.icm.saos.api.search.judgments.mapping.SearchJudgmentItemMapper;
import pl.edu.icm.saos.api.search.judgments.mapping.SearchSupremeCourtJudgmentItemMapper;
import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.judgments.parameters.Sort;
import pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView;
import pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.CommonCourtTypeTemplate;
import pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.CourtTypeTemplate;
import pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.Info;
import pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.JudgmentTypesTemplate;
import pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.PersonnelTypeTemplate;
import pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.QueryTemplate;
import pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.SortingDirectionTemplate;
import pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView.SortingFieldTemplate;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.api.services.dates.DateMapping;
import pl.edu.icm.saos.api.services.representations.success.template.JudgmentDateFromTemplate;
import pl.edu.icm.saos.api.services.representations.success.template.JudgmentDateToTemplate;
import pl.edu.icm.saos.api.services.representations.success.template.PaginationTemplateFactory;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;

/**
 * Provides functionality for building success object view for list of judgments.
 * @author pavtel
 */
@Service("judgmentsListSuccessRepresentationBuilder")
public class JudgmentsListSuccessRepresentationBuilder {

    @Autowired
    private DateMapping dateMapping;

    @Autowired
    private SearchCommonCourtJudgmentItemMapper commonCourtJudgmentItemMapper;

    @Autowired
    private SearchSupremeCourtJudgmentItemMapper supremeCourtJudgmentItemMapper;

    @Autowired
    private SearchConstitutionalTribunalJudgmentItemMapper constitutionalTribunalJudgmentItemMapper;

    @Autowired
    private SearchJudgmentItemMapper judgmentItemMapper;
    
    @Autowired
    private PaginationTemplateFactory paginationTemplateFactory;




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

        if(StringUtils.isNotBlank(params.getLegalBase())){
            uriComponentsBuilder.replaceQueryParam(LEGAL_BASE, params.getLegalBase());
        }

        if(StringUtils.isNotBlank(params.getReferencedRegulation())){
            uriComponentsBuilder.replaceQueryParam(REFERENCED_REGULATION, params.getReferencedRegulation());
        }

        if(StringUtils.isNotBlank(params.getCaseNumber())){
            uriComponentsBuilder.replaceQueryParam(CASE_NUMBER, params.getCaseNumber());
        }

        if(StringUtils.isNotBlank(params.getJudgeName())){
            uriComponentsBuilder.replaceQueryParam(JUDGE_NAME, params.getJudgeName());
        }

        if(params.getCourtType() != null){
            uriComponentsBuilder.replaceQueryParam(COURT_TYPE, params.getCourtType());
        }

        if(params.getScPersonnelType() != null){
            uriComponentsBuilder.replaceQueryParam(SC_PERSONNEL_TYPE, params.getScPersonnelType());
        }

        if(params.getCcCourtType() != null){
            uriComponentsBuilder.replaceQueryParam(CC_COURT_TYPE, params.getCcCourtType());
        }

        if(params.getCcCourtId() != null){
            uriComponentsBuilder.replaceQueryParam(CC_COURT_ID, params.getCcCourtId());
        }

        if(StringUtils.isNotBlank(params.getCcCourtCode())){
            uriComponentsBuilder.replaceQueryParam(CC_COURT_CODE, params.getCcCourtCode());
        }

        if(StringUtils.isNotBlank(params.getCcCourtName())){
            uriComponentsBuilder.replaceQueryParam(CC_COURT_NAME, params.getCcCourtName());
        }

        if(params.getCcDivisionId() != null){
            uriComponentsBuilder.replaceQueryParam(CC_DIVISION_ID, params.getCcDivisionId());
        }

        if(StringUtils.isNotBlank(params.getCcDivisionCode())){
            uriComponentsBuilder.replaceQueryParam(CC_DIVISION_CODE, params.getCcDivisionCode());
        }

        if(StringUtils.isNotBlank(params.getCcDivisionName())){
            uriComponentsBuilder.replaceQueryParam(CC_DIVISION_NAME, params.getCcDivisionName());
        }
        
        if(params.getCcIncludeDependentCourtJudgments() != null){
            uriComponentsBuilder.replaceQueryParam(CC_INCLUDE_DEPENDENT_COURT_JUDGMENTS, params.getCcIncludeDependentCourtJudgments());
        }

        if(params.getScChamberId() != null){
            uriComponentsBuilder.replaceQueryParam(SC_CHAMBER_ID, params.getScChamberId());
        }

        if(StringUtils.isNotBlank(params.getScChamberName())){
            uriComponentsBuilder.replaceQueryParam(SC_CHAMBER_NAME, params.getScChamberName());
        }

        if(params.getScDivisionId() != null){
            uriComponentsBuilder.replaceQueryParam(SC_DIVISION_ID, params.getScDivisionId());
        }

        if(StringUtils.isNotBlank(params.getScDivisionName())){
            uriComponentsBuilder.replaceQueryParam(SC_DIVISION_NAME, params.getScDivisionName());
        }

        if(params.getJudgmentDateFrom()!=null){
            uriComponentsBuilder.replaceQueryParam(JUDGMENT_DATE_FROM, params.getJudgmentDateFrom().toString());
        }

        if(params.getJudgmentDateTo() != null){
            uriComponentsBuilder.replaceQueryParam(JUDGMENT_DATE_TO, params.getJudgmentDateTo().toString());
        }

        if(!params.getKeywords().isEmpty()){
            uriComponentsBuilder.replaceQueryParam(KEYWORDS, params.getKeywords().toArray());
        }

        if(!params.getJudgmentTypes().isEmpty()){
            uriComponentsBuilder.replaceQueryParam(JUDGMENT_TYPES, params.getJudgmentTypes().toArray());
        }

        uriComponentsBuilder.replaceQueryParam(SORTING_FIELD, params.getSort().getSortingField().name());
        uriComponentsBuilder.replaceQueryParam(SORTING_DIRECTION, params.getSort().getSortingDirection().name());

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
        if(isCommonCourtJudgment(judgmentSearchResult)) {
            CommonCourtJudgmentItem item = new CommonCourtJudgmentItem();
            commonCourtJudgmentItemMapper.fillJudgmentsFieldsToItemRepresentation(item, judgmentSearchResult);
            return item;
        } else if(isSupremeCourtJudgment(judgmentSearchResult)){
            SupremeCourtJudgmentItem item = new SupremeCourtJudgmentItem();
            supremeCourtJudgmentItemMapper.fillJudgmentsFieldsToItemRepresentation(item, judgmentSearchResult);
            return item;
        } else {
            //default
            return new SearchJudgmentItem();
        }
    }

    private boolean isCommonCourtJudgment(JudgmentSearchResult judgmentSearchResult){
        return StringUtils.isNotBlank(judgmentSearchResult.getCcCourtDivisionCode());
    }

    private boolean isSupremeCourtJudgment(JudgmentSearchResult judgmentSearchResult){
        return StringUtils.isNotBlank(judgmentSearchResult.getScCourtDivisionName());
    }

    private QueryTemplate toQueryTemplate(JudgmentsParameters params) {
        QueryTemplate queryTemplate = new QueryTemplate();

        queryTemplate.setAll(params.getAll());
        queryTemplate.setLegalBase(params.getLegalBase());
        queryTemplate.setReferencedRegulation(params.getReferencedRegulation());
        queryTemplate.setCaseNumber(params.getCaseNumber());
        queryTemplate.setJudgeName(params.getJudgeName());
        queryTemplate.setCourtType(new CourtTypeTemplate(params.getCourtType()));


        queryTemplate.setCcCourtType(new CommonCourtTypeTemplate(params.getCcCourtType()));
        queryTemplate.setCcCourtId(params.getCcCourtId());
        queryTemplate.setCcCourtCode(params.getCcCourtCode());
        queryTemplate.setCcCourtName(params.getCcCourtName());

        queryTemplate.setCcDivisionId(params.getCcDivisionId());
        queryTemplate.setCcDivisionCode(params.getCcDivisionCode());
        queryTemplate.setCcDivisionName(params.getCcDivisionName());
        
        queryTemplate.setCcIncludeDependentCourtJudgments(params.getCcIncludeDependentCourtJudgments());

        queryTemplate.setScPersonnelType(new PersonnelTypeTemplate(params.getScPersonnelType()));
        queryTemplate.setScJudgmentForm(params.getScJudgmentForm());
        queryTemplate.setScChamberId(params.getScChamberId());
        queryTemplate.setScChamberName(params.getScChamberName());
        queryTemplate.setScDivisionId(params.getScDivisionId());
        queryTemplate.setScDivisionName(params.getScDivisionName());

        queryTemplate.setJudgmentTypes(new JudgmentTypesTemplate(params.getJudgmentTypes()));
        queryTemplate.setKeywords(params.getKeywords());

        Pagination pagination = params.getPagination();
        
        queryTemplate.setPageNumber(paginationTemplateFactory.createPageNumberTemplate(pagination));
        queryTemplate.setPageSize(paginationTemplateFactory.createPageSizeTemplate(pagination));

        Sort sort = params.getSort();
        queryTemplate.setSortingField(new SortingFieldTemplate(sort.getSortingField()));
        queryTemplate.setSortingDirection(new SortingDirectionTemplate(sort.getSortingDirection()));

        queryTemplate.setJudgmentDateFrom(new JudgmentDateFromTemplate(dateMapping.toISO8601Format(params.getJudgmentDateFrom())));
        queryTemplate.setJudgmentDateTo(new JudgmentDateToTemplate(dateMapping.toISO8601Format(params.getJudgmentDateTo())));

        return queryTemplate;
    }

    private Info toInfo(SearchResults<JudgmentSearchResult> searchResults) {
        Info info = new Info();
        info.setTotalResults(searchResults.getTotalResults());

        return info;
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

    public void setSupremeCourtJudgmentItemMapper(SearchSupremeCourtJudgmentItemMapper supremeCourtJudgmentItemMapper) {
        this.supremeCourtJudgmentItemMapper = supremeCourtJudgmentItemMapper;
    }

    public void setConstitutionalTribunalJudgmentItemMapper(SearchConstitutionalTribunalJudgmentItemMapper constitutionalTribunalJudgmentItemMapper) {
        this.constitutionalTribunalJudgmentItemMapper = constitutionalTribunalJudgmentItemMapper;
    }
}
