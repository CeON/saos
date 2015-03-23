package pl.edu.icm.saos.api.dump.enrichmenttag;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.Link;
import org.springframework.web.util.UriComponentsBuilder;

import pl.edu.icm.saos.api.ApiConstants;
import pl.edu.icm.saos.api.dump.enrichmenttag.mapping.DumpEnrichmentTagItemMapper;
import pl.edu.icm.saos.api.dump.enrichmenttag.views.DumpEnrichmentTagsView;
import pl.edu.icm.saos.api.dump.enrichmenttag.views.DumpEnrichmentTagsView.DumpEnrichmentTagItem;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class DumpEnrichmentTagsListSuccessRepresentationBuilderTest {

    private DumpEnrichmentTagsListSuccessRepresentationBuilder enrichmentTagsListSuccessRepresentationBuilder =
            new DumpEnrichmentTagsListSuccessRepresentationBuilder();
    
    private DumpEnrichmentTagItemMapper enrichmentTagItemMapper = mock(DumpEnrichmentTagItemMapper.class);
    
    
    private UriComponentsBuilder uriComponentsBuilder;
    
    private String path = "/dump/enrichment";
    
    private EnrichmentTag firstEnrichmentTag = new EnrichmentTag();
    private EnrichmentTag secondEnrichmentTag = new EnrichmentTag();
    
    private DumpEnrichmentTagItem firstEnrichmentTagItem = new DumpEnrichmentTagItem();
    private DumpEnrichmentTagItem secondEnrichmentTagItem = new DumpEnrichmentTagItem();
    
    
    @Before
    public void setUp() {
        enrichmentTagsListSuccessRepresentationBuilder.setDumpEnrichmentTagItemMapper(enrichmentTagItemMapper);
        uriComponentsBuilder = UriComponentsBuilder.fromUriString(path);
        
        firstEnrichmentTagItem.setTagType("TAG_TYPE_1");
        secondEnrichmentTagItem.setTagType("TAG_TYPE_2");
        
        when(enrichmentTagItemMapper.mapEnrichmentTagFieldsToItemRepresentation(same(firstEnrichmentTag))).thenReturn(firstEnrichmentTagItem);
        when(enrichmentTagItemMapper.mapEnrichmentTagFieldsToItemRepresentation(same(secondEnrichmentTag))).thenReturn(secondEnrichmentTagItem);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void build() {
        
        // given
        SearchResult<EnrichmentTag> searchResult = new SearchResult<EnrichmentTag>(
                Lists.newArrayList(firstEnrichmentTag, secondEnrichmentTag), 16L, 5*2, 2);
        Pagination pagination = new Pagination(2, 5);
        
        
        // execute
        
        DumpEnrichmentTagsView view = enrichmentTagsListSuccessRepresentationBuilder
                .build(searchResult, pagination, uriComponentsBuilder);
        
        
        // assert
        
        assertThat(view.getLinks(), contains(
                new Link(path + "?pageSize=2&pageNumber=5", ApiConstants.SELF),
                new Link(path + "?pageSize=2&pageNumber=4", ApiConstants.PREV),
                new Link(path + "?pageSize=2&pageNumber=6", ApiConstants.NEXT)));
        
        assertThat(view.getItems(), contains(firstEnrichmentTagItem, secondEnrichmentTagItem));
        
        assertEquals(Integer.valueOf(5), view.getQueryTemplate().getPageNumber().getValue());
        assertEquals(Integer.valueOf(2), view.getQueryTemplate().getPageSize().getValue());
        
        assertNull(view.getInfo());
        
    }
    
    @Test
    public void build_FIRST_PAGE() {
        
        // given
        SearchResult<EnrichmentTag> searchResult = new SearchResult<EnrichmentTag>(
                Lists.newArrayList(firstEnrichmentTag, secondEnrichmentTag), 16L, 0, 2);
        Pagination pagination = new Pagination(2, 0);
        
        
        // execute
        
        DumpEnrichmentTagsView view = enrichmentTagsListSuccessRepresentationBuilder
                .build(searchResult, pagination, uriComponentsBuilder);
        
        
        // assert
        
        assertThat(view.getLinks(), contains(
                new Link(path + "?pageSize=2&pageNumber=0", ApiConstants.SELF),
                new Link(path + "?pageSize=2&pageNumber=1", ApiConstants.NEXT)));
        
        assertThat(view.getItems(), contains(firstEnrichmentTagItem, secondEnrichmentTagItem));
        
        assertEquals(Integer.valueOf(0), view.getQueryTemplate().getPageNumber().getValue());
        assertEquals(Integer.valueOf(2), view.getQueryTemplate().getPageSize().getValue());
        
        assertNull(view.getInfo());
        
    }
    
    @Test
    public void build_LAST_PAGE() {
        
        // given
        SearchResult<EnrichmentTag> searchResult = new SearchResult<EnrichmentTag>(
                Lists.newArrayList(firstEnrichmentTag), null, 15, 3);
        Pagination pagination = new Pagination(3, 5);
        
        
        // execute
        
        DumpEnrichmentTagsView view = enrichmentTagsListSuccessRepresentationBuilder
                .build(searchResult, pagination, uriComponentsBuilder);
        
        
        // assert
        
        assertThat(view.getLinks(), contains(
                new Link(path + "?pageSize=3&pageNumber=5", ApiConstants.SELF),
                new Link(path + "?pageSize=3&pageNumber=4", ApiConstants.PREV)));
        
        assertThat(view.getItems(), contains(firstEnrichmentTagItem));
        
        assertEquals(Integer.valueOf(5), view.getQueryTemplate().getPageNumber().getValue());
        assertEquals(Integer.valueOf(3), view.getQueryTemplate().getPageSize().getValue());
        
        assertNull(view.getInfo());
        
    }
    
}
