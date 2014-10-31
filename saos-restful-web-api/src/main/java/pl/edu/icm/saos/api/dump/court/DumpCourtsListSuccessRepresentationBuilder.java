package pl.edu.icm.saos.api.dump.court;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.icm.saos.api.dump.court.mapping.DumpCourtItemMapper;
import pl.edu.icm.saos.api.dump.court.views.DumpCourtsView;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.ApiConstants.*;
import static pl.edu.icm.saos.api.dump.court.views.DumpCourtsView.Item;
import static pl.edu.icm.saos.api.dump.court.views.DumpCourtsView.QueryTemplate;

/**
 * @author pavtel
 */
@Component
public class DumpCourtsListSuccessRepresentationBuilder {



    @Autowired
    private DumpCourtItemMapper dumpCourtItemMapper;


    public DumpCourtsView build(SearchResult<CommonCourt> searchResult, Pagination pagination, UriComponentsBuilder uriComponentsBuilder){
        DumpCourtsView dumpCourtsView = new DumpCourtsView();
        dumpCourtsView.setLinks(toLinks(pagination, uriComponentsBuilder, searchResult.isMoreRecordsExist()));
        dumpCourtsView.setItems(toItems(searchResult.getResultRecords()));
        dumpCourtsView.setQueryTemplate(toQueryTemplate(pagination));

        return dumpCourtsView;
    }

    private List<Item> toItems(List<CommonCourt> commonCourts) {
        List<Item> items = commonCourts.stream()
                .map(court -> toItem(court))
                .collect(Collectors.toList());

        return items;
    }

    private Item toItem(CommonCourt court){
        Item item = new Item();
        dumpCourtItemMapper.fillCommonCourtFieldsToItemRepresentation(item, court);
        return item;
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
                .replaceQueryParam(PAGE_SIZE, pagination.getPageSize())
                .replaceQueryParam(PAGE_NUMBER, pagination.getPageNumber());


        String path = uriComponentsBuilder.build().encode().toUriString();
        return new Link(path, relName);
    }


    private QueryTemplate toQueryTemplate(Pagination pagination) {
        QueryTemplate queryTemplate = new QueryTemplate();

        queryTemplate.setPageNumber(pagination.getPageNumber());
        queryTemplate.setPageSize(pagination.getPageSize());

        return queryTemplate;
    }


}
