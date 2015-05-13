package pl.edu.icm.saos.search.search.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.FacetParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.util.FieldValuePrefixAdder;

import com.google.common.base.Preconditions;

/**
 * Applier of field facet request to {@link SolrQuery}
 * 
 * @author madryk
 */
@Service
public class FieldFacetToQueryApplier {

    private FieldValuePrefixAdder fieldValuePrefixAdder;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Applies field facet request to {@link SolrQuery} <br/>
     * Sets {@link FacetParams#FACET_SORT} for the given fieldName to {@link FacetParams#FACET_SORT_INDEX}
     */
    public void applyFieldFacet(SolrQuery query, String fieldName, String fieldValuePrefix) {
        
        Preconditions.checkNotNull(query);
        Preconditions.checkNotNull(fieldName);
        Preconditions.checkArgument(StringUtils.isNotBlank(fieldName));
        
        String fieldParamPrefix = "f." + fieldName + ".";
        
        query.setFacet(true);
        query.add(fieldParamPrefix + FacetParams.FACET_SORT, FacetParams.FACET_SORT_INDEX);
        query.add(FacetParams.FACET_FIELD, fieldName);
        if (!StringUtils.isBlank(fieldValuePrefix)) {
            query.add(fieldParamPrefix + FacetParams.FACET_PREFIX, fieldValuePrefixAdder.prefixWithSeparator(fieldValuePrefix));
        }
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setFieldValuePrefixAdder(FieldValuePrefixAdder fieldValuePrefixAdder) {
        this.fieldValuePrefixAdder = fieldValuePrefixAdder;
    }
}
