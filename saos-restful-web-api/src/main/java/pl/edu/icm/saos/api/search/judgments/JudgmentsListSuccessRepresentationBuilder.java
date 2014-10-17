package pl.edu.icm.saos.api.search.judgments;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.icm.saos.api.single.judgment.assemblers.JudgmentAssembler;
import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.api.services.representations.SuccessRepresentation;
import pl.edu.icm.saos.api.search.services.ElementsSearchResults;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.*;

import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * Provides functionality for building success object view for list of judgments. Success object can be easily serialized as json.
 * @author pavtel
 */
@Component("judgmentsListSuccessRepresentationBuilder")
public class JudgmentsListSuccessRepresentationBuilder {

    //******** fields *************

    @Autowired
    private JudgmentAssembler judgmentAssembler;

    //********** END fields **********


    //************* business methods **************

    /**
     * Constructs, from searchResults, the success view representation (representation details: {@link pl.edu.icm.saos.api.services.representations.SuccessRepresentation SuccessRepresentation}).
     * @param searchResults to process.
     * @param uriComponentsBuilder used for create links in {@link pl.edu.icm.saos.api.services.representations.SuccessRepresentation success} object.
     * @return map - success representation
     */
    public Map<String, Object> build(ElementsSearchResults<Judgment, JudgmentsParameters> searchResults, UriComponentsBuilder uriComponentsBuilder){
        SuccessRepresentation.Builder builder = new SuccessRepresentation.Builder();
        builder.links(toLinks(searchResults, uriComponentsBuilder));
        builder.items(toItems(searchResults.getElements()));
        builder.queryTemplate(toQueryTemplate(searchResults.getRequestParameters()));
        builder.info(toInfo(searchResults));

        return builder.build();
    }



    private List<Link> toLinks(ElementsSearchResults<Judgment, JudgmentsParameters> searchResults , UriComponentsBuilder uriComponentsBuilder) {
        List<Link> links = new LinkedList<>();
        JudgmentsParameters requestParameters = searchResults.getRequestParameters();

        Pagination pagination = requestParameters.getPagination();

        links.add(buildLink(pagination, requestParameters , SELF, uriComponentsBuilder));

        if(requestParameters.getPagination().hasPrevious()){
            links.add(buildLink(pagination.getPrevious(), requestParameters, Link.REL_PREVIOUS, uriComponentsBuilder));
        }

        if(pagination.hasNextIn(searchResults.getTotalResults())){
            links.add(buildLink(pagination.getNext(), requestParameters, Link.REL_NEXT, uriComponentsBuilder));
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


    private List<Object> toItems(List<? extends Judgment> judgments) {
        return judgmentAssembler.toItemsList(judgments);
    }

    private Object toQueryTemplate(JudgmentsParameters params) {
        Map<String, Object> queryTemplate = new LinkedHashMap<String, Object>();

        Pagination pagination = params.getPagination();
        queryTemplate.put(PAGE_NUMBER, pagination.getPageNumber());
        queryTemplate.put(PAGE_SIZE, pagination.getPageSize());

        queryTemplate.put(ALL, removeNull(params.getAll()));
        queryTemplate.put(LEGAL_BASE, removeNull(params.getLegalBase()));
        queryTemplate.put(REFERENCED_REGULATION, removeNull(params.getReferencedRegulation()));
        queryTemplate.put(KEYWORD, removeNull(params.getKeyword()));
        queryTemplate.put(COURT_NAME, removeNull(params.getCourtName()));
        queryTemplate.put(JUDGE_NAME, removeNull(params.getJudgeName()));

        queryTemplate.put(JUDGMENT_DATE_FROM, toString(params.getJudgmentDateFrom()));
        queryTemplate.put(JUDGMENT_DATE_TO, toString(params.getJudgmentDateTo()));

        return queryTemplate;
    }

    private Object toInfo(ElementsSearchResults<Judgment, JudgmentsParameters> searchResults) {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put(TOTAL_RESULTS, searchResults.getTotalResults());
        return info;
    }

    private String toString(LocalDate localDate) {
        if(localDate == null){
            return "";
        } else {
            return localDate.toString();
        }
    }

    private String removeNull(String str){
        if(str == null){
            return "";
        } else {
            return str;
        }
    }

    //******* END business methods **********

    //******** setters **********
    public void setJudgmentAssembler(JudgmentAssembler judgmentAssembler) {
        this.judgmentAssembler = judgmentAssembler;
    }
}
