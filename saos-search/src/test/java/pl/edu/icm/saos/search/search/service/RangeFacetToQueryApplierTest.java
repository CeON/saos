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
}
