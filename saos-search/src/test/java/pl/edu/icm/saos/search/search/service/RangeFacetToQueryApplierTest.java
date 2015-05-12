package pl.edu.icm.saos.search.search.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.edu.icm.saos.search.util.FieldValuePrefixAdder;

/**
 * @author madryk
 */
public class RangeFacetToQueryApplierTest {

    @InjectMocks
    private RangeFacetToQueryApplier rangeFacetToQueryApplier = new RangeFacetToQueryApplier();
    
    @Mock
    private FieldValuePrefixAdder fieldValuePrefixAdder;
    
    
    @Before
    public void before() {
        initMocks(this);
    }
    
    
    //------------------------ TEST --------------------------
    
    @Test
    public void applyRangeFacet() {
        // given
        SolrQuery query = new SolrQuery();
        when(fieldValuePrefixAdder.prefixWithSeparator("valuePrefix")).thenReturn("valuePrefix#");
        
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(query, "fieldName", "valuePrefix", "startParam", "endParam", "gapParam");
        
        // assert
        assertThat(query.getParams("facet"), arrayContaining("true"));
        assertThat(query.getParams("facet.prefix"), arrayContaining("valuePrefix#"));
        assertThat(query.getParams("facet.range"), arrayContaining("fieldName"));
        assertThat(query.getParams("f.fieldName.facet.range.start"), arrayContaining("startParam"));
        assertThat(query.getParams("f.fieldName.facet.range.end"), arrayContaining("endParam"));
        assertThat(query.getParams("f.fieldName.facet.range.gap"), arrayContaining("gapParam"));
        
        assertThat(query.getParameterNames(), containsInAnyOrder("facet", "facet.prefix", "facet.range",
                "f.fieldName.facet.range.start",
                "f.fieldName.facet.range.end",
                "f.fieldName.facet.range.gap"));
    }
    

    @Test
    public void applyRangeFacet_fieldValuePrefixBlank() {
        
        // given
        SolrQuery query = new SolrQuery();
        when(fieldValuePrefixAdder.prefixWithSeparator("valuePrefix")).thenReturn("valuePrefix#");
        
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(query, "fieldName", " ", "startParam", "endParam", "gapParam");
        
        // assert
        assertThat(query.getParameterNames(), containsInAnyOrder("facet", "facet.range",
                "f.fieldName.facet.range.start",
                "f.fieldName.facet.range.end",
                "f.fieldName.facet.range.gap"));
    }

    
    @Test(expected = NullPointerException.class)
    public void applyRangeFacet_NULL_QUERY() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(null, "fieldName", "prefix", "startParam", "endParam", "gapParam");
    }
    
    
    @Test(expected = NullPointerException.class)
    public void applyRangeFacet_NULL_FIELD_NAME() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), null, "prefix", "startParam", "endParam", "gapParam");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void applyRangeFacet_BLANK_FIELD_NAME() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), " ", "prefix", "startParam", "endParam", "gapParam");
    }
    
    
    @Test(expected = NullPointerException.class)
    public void applyRangeFacet_NULL_START() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), "fieldName", "prefix", null, "endParam", "gapParam");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void applyRangeFacet_BLANK_START() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), "fieldName", "prefix", "  ", "endParam", "gapParam");
    }
    
    
    @Test(expected = NullPointerException.class)
    public void applyRangeFacet_NULL_END() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), "fieldName", "prefix", "startParam", null, "gapParam");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void applyRangeFacet_BLANK_END() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), "fieldName", "prefix", "startParam", "  ", "gapParam");
    }
    
    
    @Test(expected = NullPointerException.class)
    public void applyRangeFacet_NULL_GAP() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), "fieldName", "prefix", "startParam", "endParam", null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void applyRangeFacet_BLANK_GAP() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), "fieldName", "prefix", "startParam", "endParam", "   ");
    }
    
}
