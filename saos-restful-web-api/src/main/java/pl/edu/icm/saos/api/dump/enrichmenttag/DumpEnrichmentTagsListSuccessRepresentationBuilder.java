package pl.edu.icm.saos.api.dump.enrichmenttag;

import static pl.edu.icm.saos.api.ApiConstants.NEXT;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;
import static pl.edu.icm.saos.api.ApiConstants.PREV;
import static pl.edu.icm.saos.api.ApiConstants.SELF;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import pl.edu.icm.saos.api.dump.enrichmenttag.mapping.DumpEnrichmentTagItemMapper;
import pl.edu.icm.saos.api.dump.enrichmenttag.views.DumpEnrichmentTagsView;
import pl.edu.icm.saos.api.dump.enrichmenttag.views.DumpEnrichmentTagsView.DumpEnrichmentTagItem;
import pl.edu.icm.saos.api.dump.enrichmenttag.views.DumpEnrichmentTagsView.QueryTemplate;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.api.services.representations.success.template.PageNumberTemplate;
import pl.edu.icm.saos.api.services.representations.success.template.PageSizeTemplate;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

/**
 * Provides functionality for building success object view for dump list of enrichment tags.
 * 
 * @author madryk
 */
@Service
public class DumpEnrichmentTagsListSuccessRepresentationBuilder {

    private DumpEnrichmentTagItemMapper dumpEnrichmentTagItemMapper;
    
    
    //------------------------ LOGIC --------------------------
    
    public DumpEnrichmentTagsView build(SearchResult<EnrichmentTag> searchResult, Pagination pagination, UriComponentsBuilder uriComponentsBuilder) {
        DumpEnrichmentTagsView dumpCourtsView = new DumpEnrichmentTagsView();
        dumpCourtsView.setLinks(toLinks(pagination, uriComponentsBuilder, searchResult.isMoreRecordsExist()));
        dumpCourtsView.setItems(toItems(searchResult.getResultRecords()));
        dumpCourtsView.setQueryTemplate(toQueryTemplate(pagination));

        return dumpCourtsView;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private List<DumpEnrichmentTagItem> toItems(List<EnrichmentTag> enrichmentTags) {
        List<DumpEnrichmentTagItem> items = enrichmentTags.stream()
                .map(enrichmentTag -> toItem(enrichmentTag))
                .collect(Collectors.toList());

        return items;
    }
    
    private DumpEnrichmentTagItem toItem(EnrichmentTag enrichmentTag){
        return dumpEnrichmentTagItemMapper.mapEnrichmentTagFieldsToItemRepresentation(enrichmentTag);
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

        queryTemplate.setPageNumber(new PageNumberTemplate(pagination.getPageNumber()));
        queryTemplate.setPageSize(new PageSizeTemplate(pagination.getPageSize()));

        return queryTemplate;
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setDumpEnrichmentTagItemMapper(
            DumpEnrichmentTagItemMapper dumpEnrichmentTagItemMapper) {
        this.dumpEnrichmentTagItemMapper = dumpEnrichmentTagItemMapper;
    }
}
