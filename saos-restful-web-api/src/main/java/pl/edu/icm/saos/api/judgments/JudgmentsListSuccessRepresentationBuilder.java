package pl.edu.icm.saos.api.judgments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.icm.saos.api.judgments.assemblers.JudgmentAssembler;
import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.api.response.representations.SuccessRepresentation;
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

    public Map<String, Object> build(Pagination pagination, UriComponentsBuilder uriComponentsBuilder, List<? extends Judgment> judgments){
        SuccessRepresentation.Builder builder = new SuccessRepresentation.Builder();
        builder.links(toLinks(pagination, uriComponentsBuilder, true));//TODO calculate hasMore parameter
        builder.items(toItems(judgments));
        builder.queryTemplate(toQueryTemplate(pagination));

        return builder.build();
    }

    private List<Link> toLinks(Pagination pagination, UriComponentsBuilder uriComponentsBuilder, boolean hasMore) {
        List<Link> links = new LinkedList<>();

        links.add(buildLink(pagination, SELF, uriComponentsBuilder));

        if(pagination.hasPrevious()){
            links.add(buildLink(pagination.getPrevious(), Link.REL_PREVIOUS, uriComponentsBuilder));
        }

        if(hasMore){
            links.add(buildLink(pagination.getNext(), Link.REL_NEXT, uriComponentsBuilder));
        }


        return links;
    }

    private Link buildLink(Pagination pagination,  String relName, UriComponentsBuilder uriComponentsBuilder) {

        uriComponentsBuilder
                .replaceQueryParam(LIMIT, pagination.getLimit())
                .replaceQueryParam(OFFSET, pagination.getOffset());

        String path = uriComponentsBuilder.build().encode().toUriString();
        return new Link(path, relName);
    }


    private List<Object> toItems(List<? extends Judgment> judgments) {
        return judgmentAssembler.toItemsList(judgments);
    }

    private Object toQueryTemplate(Pagination pagination) {
        Map<String, Object> queryTemplate = new LinkedHashMap<String, Object>();
        queryTemplate.put(OFFSET, pagination.getOffset());
        queryTemplate.put(LIMIT, pagination.getLimit());

        return queryTemplate;
    }
}
