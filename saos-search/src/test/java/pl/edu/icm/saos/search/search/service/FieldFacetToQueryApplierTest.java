package pl.edu.icm.saos.search.search.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.FacetParams;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.edu.icm.saos.search.util.FieldValuePrefixAdder;

/**
 * @author madryk
 */
public class FieldFacetToQueryApplierTest {

    @InjectMocks
    private FieldFacetToQueryApplier fieldFacetToQueryApplier = new FieldFacetToQueryApplier();
    
    @Mock
    private FieldValuePrefixAdder fieldValuePrefixAdder;
    
    
    @Before
    public void before() {
        initMocks(this);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void applyFieldFacet_FieldPrefixNotEmpty() {
        
        // given
        SolrQuery query = new SolrQuery();
        when(fieldValuePrefixAdder.prefixWithSeparator("fieldPrefix")).thenReturn("fieldPrefix#");
        
        // execute
        fieldFacetToQueryApplier.applyFieldFacet(query, "fieldName", "fieldPrefix");
        
        // assert
        assertThat(query.getParams("facet"), arrayContaining("true"));
        assertThat(query.getParams("facet.field"), arrayContaining("fieldName"));
        assertThat(query.getParams("f.fieldName.facet.prefix"), arrayContaining("fieldPrefix#"));
        assertThat(query.getParams("f.fieldName.facet.sort"), arrayContaining(FacetParams.FACET_SORT_INDEX));
    }
    
    
    @Test
    public void applyFieldFacet_FieldPrefixBlank() {
        
        // given
        SolrQuery query = new SolrQuery();
        
        // execute
        fieldFacetToQueryApplier.applyFieldFacet(query, "fieldName", " ");
        
        // assert
        assertThat(query.getParams("facet"), arrayContaining("true"));
        assertThat(query.getParams("facet.field"), arrayContaining("fieldName"));
        assertThat(query.getParams("f.fieldName.facet.sort"), arrayContaining(FacetParams.FACET_SORT_INDEX));
        assertNull(query.getParams("f.fieldName.facet.prefix"));
    }
    
}
