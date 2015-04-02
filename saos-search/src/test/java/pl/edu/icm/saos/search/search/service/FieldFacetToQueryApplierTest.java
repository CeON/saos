package pl.edu.icm.saos.search.search.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;

import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Test;

/**
 * @author madryk
 */
public class FieldFacetToQueryApplierTest {

    private FieldFacetToQueryApplier fieldFacetToQueryApplier = new FieldFacetToQueryApplier();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void applyFieldFacet() {
        // given
        SolrQuery query = new SolrQuery();
        
        // execute
        fieldFacetToQueryApplier.applyFieldFacet(query, "fieldName");
        
        // assert
        assertThat(query.getParams("facet"), arrayContaining("true"));
        assertThat(query.getParams("facet.field"), arrayContaining("fieldName"));
    }
    
}
