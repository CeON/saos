package pl.edu.icm.saos.api.dump.supreme.court.chamber;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.icm.saos.api.dump.supreme.court.chamber.views.DumpScChambersView;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.ApiConstants.*;
import static pl.edu.icm.saos.api.dump.supreme.court.chamber.views.DumpScChambersView.*;

/**
 * Provides functionality for building success object view for dump list of supreme court chambers.
 * @author pavtel
 */
@Service
public class DumpScChambersListsSuccessRepresentationBuilder {


    //------------------------ LOGIC --------------------------

    /**
     * Constructs {@link pl.edu.icm.saos.api.dump.supreme.court.chamber.views.DumpScChambersView DumpScChambersView}.
     * @param searchResult to process.
     * @param pagination to process.
     * @param uriComponentsBuilder used to construct links.
     * @return representation.
     */
    public DumpScChambersView build(SearchResult<SupremeCourtChamber> searchResult, Pagination pagination, UriComponentsBuilder uriComponentsBuilder) {
        DumpScChambersView dumpScChambersView = new DumpScChambersView();
        dumpScChambersView.setLinks(toLinks(pagination, uriComponentsBuilder, searchResult.isMoreRecordsExist()));
        dumpScChambersView.setItems(toItems(searchResult.getResultRecords()));
        dumpScChambersView.setQueryTemplate(toQueryTemplate(pagination));

        return dumpScChambersView;
    }

    //------------------------ PRIVATE --------------------------
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

    private List<Item> toItems(List<SupremeCourtChamber> scChambers) {
        return scChambers.stream()
                .map(chamber -> toItem(chamber))
                .collect(Collectors.toList());
    }

    private Item toItem(SupremeCourtChamber chamber) {
        Item item = new Item();
        item.setId(chamber.getId());
        item.setName(chamber.getName());
        item.setDivisions(toDivisions(chamber.getDivisions()));

        return item;
    }

    private List<Division> toDivisions(List<SupremeCourtChamberDivision> divisions) {
        if(divisions==null){
            divisions= Collections.emptyList();
        }

        return divisions.stream()
                .map(division -> toDivisionView(division))
                .collect(Collectors.toList());
    }

    private Division toDivisionView(SupremeCourtChamberDivision division) {
        Division view = new Division();
        view.setId(division.getId());
        view.setName(division.getName());
        view.setFullName(division.getFullName());

        return view;
    }

    private QueryTemplate toQueryTemplate(Pagination pagination) {
        QueryTemplate queryTemplate = new QueryTemplate();

        queryTemplate.setPageNumber(pagination.getPageNumber());
        queryTemplate.setPageSize(pagination.getPageSize());

        return queryTemplate;
    }
}
