package pl.edu.icm.saos.search.search.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.containsInAnyOrder;

import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Test;

/**
 * @author madryk
 */
public class RangeFacetToQueryApplierTest {

    private RangeFacetToQueryApplier rangeFacetToQueryApplier = new RangeFacetToQueryApplier();
    
    
    //------------------------ TEST --------------------------
    
    @Test
    public void applyRangeFacet() {
        // given
        SolrQuery query = new SolrQuery();
        
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(query, "fieldName", "startParam", "endParam", "gapParam");
        
        // assert
        assertThat(query.getParams("facet"), arrayContaining("true"));
        assertThat(query.getParams("facet.range"), arrayContaining("fieldName"));
        assertThat(query.getParams("f.fieldName.facet.range.start"), arrayContaining("startParam"));
        assertThat(query.getParams("f.fieldName.facet.range.end"), arrayContaining("endParam"));
        assertThat(query.getParams("f.fieldName.facet.range.gap"), arrayContaining("gapParam"));
        
        assertThat(query.getParameterNames(), containsInAnyOrder("facet", "facet.range",
                "f.fieldName.facet.range.start",
                "f.fieldName.facet.range.end",
                "f.fieldName.facet.range.gap"));
    }
    
    @Test(expected = NullPointerException.class)
    public void applyRangeFacet_NULL_QUERY() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(null, "fieldName", "startParam", "endParam", "gapParam");
    }
    
    
    @Test(expected = NullPointerException.class)
    public void applyRangeFacet_NULL_FIELD_NAME() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), null, "startParam", "endParam", "gapParam");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void applyRangeFacet_BLANK_FIELD_NAME() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), " ", "startParam", "endParam", "gapParam");
    }
    
    
    @Test(expected = NullPointerException.class)
    public void applyRangeFacet_NULL_START() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), "fieldName", null, "endParam", "gapParam");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void applyRangeFacet_BLANK_START() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), "fieldName", "  ", "endParam", "gapParam");
    }
    
    
    @Test(expected = NullPointerException.class)
    public void applyRangeFacet_NULL_END() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), "fieldName", "startParam", null, "gapParam");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void applyRangeFacet_BLANK_END() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), "fieldName", "startParam", "  ", "gapParam");
    }
    
    
    @Test(expected = NullPointerException.class)
    public void applyRangeFacet_NULL_GAP() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), "fieldName", "startParam", "endParam", null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void applyRangeFacet_BLANK_GAP() {
        // execute
        rangeFacetToQueryApplier.applyRangeFacet(new SolrQuery(), "fieldName", "startParam", "endParam", "   ");
    }
    
}
