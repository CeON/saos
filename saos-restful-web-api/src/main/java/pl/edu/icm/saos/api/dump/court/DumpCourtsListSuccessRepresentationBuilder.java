package pl.edu.icm.saos.api.dump.court;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.icm.saos.api.dump.court.assemblers.DumpCourtAssembler;
import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.api.response.representations.SuccessRepresentation;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * @author pavtel
 */
@Component
public class DumpCourtsListSuccessRepresentationBuilder {


    @Autowired
    private DumpCourtAssembler dumpCourtAssembler;




    public Map<String, Object> build(SearchResult<CommonCourt> searchResult, Pagination pagination, UriComponentsBuilder uriComponentsBuilder){
        SuccessRepresentation.Builder builder = new SuccessRepresentation.Builder();
        builder.links(toLinks(pagination, uriComponentsBuilder, searchResult.isMoreRecordsExist()));
        builder.items(toItems(searchResult.getResultRecords()));
        builder.queryTemplate(toQueryTemplate(pagination));

        return builder.build();
    }



    private List<Link> toLinks(Pagination pagination, UriComponentsBuilder uriComponentsBuilder, boolean hasMore) {
        List<Link> links = new LinkedList<>();


        links.add(buildLink(pagination, SELF, uriComponentsBuilder));

        if(pagination.hasPrevious()){
            links.add(buildLink(pagination.getPrevious(), PREV, uriComponentsBuilder));
        }

        if(hasMore){
            links.add(buildLink(pagination.getNext(), NEXT, uriComponentsBuilder));
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

    private List<Object> toItems(List<CommonCourt> resultRecords) {
        return dumpCourtAssembler.toItemsList(resultRecords);
    }

    private Object toQueryTemplate(Pagination pagination) {
        Map<String, Object> queryTemplate = new LinkedHashMap<>();

        queryTemplate.put(OFFSET, pagination.getOffset());
        queryTemplate.put(LIMIT, pagination.getLimit());

        return queryTemplate;
    }

    //*** setters ***
    public void setDumpCourtAssembler(DumpCourtAssembler dumpCourtAssembler) {
        this.dumpCourtAssembler = dumpCourtAssembler;
    }
}
