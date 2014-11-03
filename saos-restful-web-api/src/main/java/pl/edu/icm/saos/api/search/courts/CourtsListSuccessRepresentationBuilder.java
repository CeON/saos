package pl.edu.icm.saos.api.search.courts;

import org.springframework.stereotype.Component;

/**
 * TODO create new implementation when courts will be added to the solr index.
 * Provides functionality for building success object view for list of courts. Success object can be easily serialized as json.
 * @author pavtel
 */
@Deprecated
@Component
public class CourtsListSuccessRepresentationBuilder {

//    //****** fields *********
//    @Autowired
//    private CourtAssembler courtAssembler;
//
//    @Autowired
//    private LinksBuilder linksBuilder;
//
//    //******* END fields *********
//
//
//    //******** business methods ****************
//    /**
//     * Constructs, from searchResults,  the success view representation (representation details: {@link pl.edu.icm.saos.api.services.representations.SuccessRepresentationDep SuccessRepresentation}).
//     * @param searchResults to process.
//     * @return map - success representation
//     */
//    public Map<String, Object> build(ElementsSearchResults<CommonCourt,RequestParameters> searchResults){
//        SuccessRepresentationDep.Builder builder = new SuccessRepresentationDep.Builder();
//
//        builder.items(courtAssembler.toItemsList(searchResults.getElements()));
//
//        builder.links(toLinks(searchResults.getRequestParameters()));
//
//        builder.queryTemplate(toQueryTemplate(searchResults.getRequestParameters()));
//
//
//        return builder.build();
//    }
//
//    private List<Link> toLinks(RequestParameters requestParameters) {
//        return toLinks(requestParameters, linksBuilder.courtsUriBuilder(), true);
//    }
//
//    private List<Link> toLinks(RequestParameters requestParameters, UriComponentsBuilder uriComponentsBuilder, boolean hasMore) {
//        List<Link> links = new LinkedList<>();
//
//        Pagination pagination = requestParameters.getPagination();
//
//        links.add(buildLink(pagination , SELF, uriComponentsBuilder));
//
//        if(requestParameters.getPagination().hasPrevious()){
//            links.add(buildLink(pagination.getPrevious(), Link.REL_PREVIOUS, uriComponentsBuilder));
//        }
//
//        if(hasMore){
//            links.add(buildLink(pagination.getNext(), Link.REL_NEXT, uriComponentsBuilder));
//        }
//
//
//        return links;
//    }
//
//    private Link buildLink(Pagination pagination, String relName, UriComponentsBuilder uriComponentsBuilder) {
//
//        uriComponentsBuilder
//                .replaceQueryParam(PAGE_SIZE, pagination.getPageSize())
//                .replaceQueryParam(PAGE_NUMBER, pagination.getPageNumber());
//
//
//        String path = uriComponentsBuilder.build().encode().toUriString();
//        return new Link(path, relName);
//    }
//
//    private Object toQueryTemplate(RequestParameters requestParameters) {
//        Map<String, Object> queryTemplate = new LinkedHashMap<String, Object>();
//
//        Pagination pagination = requestParameters.getPagination();
//        queryTemplate.put(PAGE_NUMBER, pagination.getPageNumber());
//        queryTemplate.put(PAGE_SIZE, pagination.getPageSize());
//
//        return queryTemplate;
//    }
//
//    //********* END business methods *************
//
//
//    // *** setters ***
//    public void setCourtAssembler(CourtAssembler courtAssembler) {
//        this.courtAssembler = courtAssembler;
//    }
//
//    public void setLinksBuilder(LinksBuilder linksBuilder) {
//        this.linksBuilder = linksBuilder;
//    }
}
