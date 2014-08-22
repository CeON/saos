package pl.edu.icm.saos.api.judgments;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.icm.saos.api.judgments.assemblers.JudgmentAssembler;
import pl.edu.icm.saos.api.parameters.JoinedParameter;
import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.api.response.representations.SuccessRepresentation;
import pl.edu.icm.saos.api.search.JudgmentsSearchResults;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.*;

import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * @author pavtel
 */
@Component("judgmentsListSuccessRepresentationBuilder")
public class JudgmentsListSuccessRepresentationBuilder {

    @Autowired
    private JudgmentAssembler judgmentAssembler;

    public JudgmentsListSuccessRepresentationBuilder() {
    }

    public Map<String, Object> build(JudgmentsSearchResults searchResults, UriComponentsBuilder uriComponentsBuilder){
        SuccessRepresentation.Builder builder = new SuccessRepresentation.Builder();
        builder.links(toLinks(searchResults.getRequestParameters(), uriComponentsBuilder, true)); //TODO calculate hasMore value
        builder.items(toItems(searchResults));
        builder.queryTemplate(toQueryTemplate(searchResults.getRequestParameters()));

        return builder.build();
    }

    private List<Link> toLinks(RequestParameters requestParameters, UriComponentsBuilder uriComponentsBuilder, boolean hasMore) {
        List<Link> links = new LinkedList<>();

        Pagination pagination = requestParameters.getPagination();
        String expandValue = requestParameters.getExpandParameter().getJoinedValue();

        links.add(buildLink(pagination, expandValue , SELF, uriComponentsBuilder));

        if(requestParameters.getPagination().hasPrevious()){
            links.add(buildLink(pagination.getPrevious(), expandValue, Link.REL_PREVIOUS, uriComponentsBuilder));
        }

        if(hasMore){
            links.add(buildLink(pagination.getNext(), expandValue, Link.REL_NEXT, uriComponentsBuilder));
        }


        return links;
    }

    private Link buildLink(Pagination pagination,  String expandValue, String relName, UriComponentsBuilder uriComponentsBuilder) {

        uriComponentsBuilder
                .replaceQueryParam(LIMIT, pagination.getLimit())
                .replaceQueryParam(OFFSET, pagination.getOffset());

        if(StringUtils.isNotEmpty(expandValue)){
            uriComponentsBuilder.replaceQueryParam(EXPAND, expandValue);
        }

        String path = uriComponentsBuilder.build().encode().toUriString();
        return new Link(path, relName);
    }


    private List<Object> toItems(JudgmentsSearchResults searchResults) {
        List<? extends Judgment> judgments = searchResults.getJudgments();
        return judgmentAssembler.toItemsList(judgments, searchResults.getRequestParameters().getExpandParameter());
    }

    private Object toQueryTemplate(RequestParameters requestParameters) {
        Map<String, Object> queryTemplate = new LinkedHashMap<String, Object>();

        Pagination pagination = requestParameters.getPagination();
        queryTemplate.put(OFFSET, pagination.getOffset());
        queryTemplate.put(LIMIT, pagination.getLimit());
        queryTemplate.put(EXPAND, requestParameters.getExpandParameter().getJoinedValue());

        return queryTemplate;
    }
}
