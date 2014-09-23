package pl.edu.icm.saos.api.courts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.icm.saos.api.courts.assemblers.CourtAssembler;
import pl.edu.icm.saos.api.links.LinksBuilder;
import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.api.response.representations.SuccessRepresentation;
import pl.edu.icm.saos.api.search.ElementsSearchResults;
import pl.edu.icm.saos.persistence.model.CommonCourt;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * Provides functionality for building success object view for list of courts. Success object can be easily serialized as json.
 * @author pavtel
 */
@Component
public class CourtsListSuccessRepresentationBuilder {

    //****** fields *********
    @Autowired
    private CourtAssembler courtAssembler;

    @Autowired
    private LinksBuilder linksBuilder;

    //******* END fields *********


    //******** business methods ****************
    /**
     * Constructs, from searchResults,  the success view representation (representation details: {@link pl.edu.icm.saos.api.response.representations.SuccessRepresentation SuccessRepresentation}).
     * @param searchResults to process.
     * @return map - success representation
     */
    public Map<String, Object> build(ElementsSearchResults<CommonCourt,RequestParameters> searchResults){
        SuccessRepresentation.Builder builder = new SuccessRepresentation.Builder();

        builder.items(courtAssembler.toItemsList(searchResults.getElements()));

        builder.links(toLinks(searchResults.getRequestParameters()));

        builder.queryTemplate(toQueryTemplate(searchResults.getRequestParameters()));


        return builder.build();
    }

    private List<Link> toLinks(RequestParameters requestParameters) {
        return toLinks(requestParameters, linksBuilder.courtsUriBuilder(), true);
    }

    private List<Link> toLinks(RequestParameters requestParameters, UriComponentsBuilder uriComponentsBuilder, boolean hasMore) {
        List<Link> links = new LinkedList<>();

        Pagination pagination = requestParameters.getPagination();

        links.add(buildLink(pagination , SELF, uriComponentsBuilder));

        if(requestParameters.getPagination().hasPrevious()){
            links.add(buildLink(pagination.getPrevious(), Link.REL_PREVIOUS, uriComponentsBuilder));
        }

        if(hasMore){
            links.add(buildLink(pagination.getNext(), Link.REL_NEXT, uriComponentsBuilder));
        }


        return links;
    }

    private Link buildLink(Pagination pagination, String relName, UriComponentsBuilder uriComponentsBuilder) {

        uriComponentsBuilder
                .replaceQueryParam(LIMIT, pagination.getLimit())
                .replaceQueryParam(OFFSET, pagination.getOffset());


        String path = uriComponentsBuilder.build().encode().toUriString();
        return new Link(path, relName);
    }

    private Object toQueryTemplate(RequestParameters requestParameters) {
        Map<String, Object> queryTemplate = new LinkedHashMap<String, Object>();

        Pagination pagination = requestParameters.getPagination();
        queryTemplate.put(OFFSET, pagination.getOffset());
        queryTemplate.put(LIMIT, pagination.getLimit());

        return queryTemplate;
    }

    //********* END business methods *************


    // *** setters ***
    public void setCourtAssembler(CourtAssembler courtAssembler) {
        this.courtAssembler = courtAssembler;
    }

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
