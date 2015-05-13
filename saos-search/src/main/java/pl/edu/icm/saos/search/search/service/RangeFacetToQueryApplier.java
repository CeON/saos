package pl.edu.icm.saos.search.search.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.FacetParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.util.FieldValuePrefixAdder;

import com.google.common.base.Preconditions;

/**
 * Applier of range facet request to {@link SolrQuery}
 * 
 * @author madryk
 */
@Service
public class RangeFacetToQueryApplier {

    private FieldValuePrefixAdder fieldValuePrefixAdder;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Applies range facet request to {@link SolrQuery}
     */
    public void applyRangeFacet(SolrQuery query, String fieldName, String start, String end, String gap) {
        
        checkArguments(query, fieldName, start, end, gap);
        
        query.setFacet(true);
        
        String fieldParamPrefix = "f." + fieldName + ".";
        
        query.add(FacetParams.FACET_RANGE, fieldName);
        query.add(fieldParamPrefix + FacetParams.FACET_RANGE_START, start);
        query.add(fieldParamPrefix + FacetParams.FACET_RANGE_END, end);
        query.add(fieldParamPrefix + FacetParams.FACET_RANGE_GAP, gap);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void checkArguments(SolrQuery query, String fieldName, String start, String end, String gap) {
        Preconditions.checkNotNull(query);
        Preconditions.checkNotNull(fieldName);
        Preconditions.checkArgument(StringUtils.isNotBlank(fieldName));
        Preconditions.checkNotNull(start);
        Preconditions.checkArgument(StringUtils.isNotBlank(start));
        Preconditions.checkNotNull(end);
        Preconditions.checkArgument(StringUtils.isNotBlank(end));
        Preconditions.checkNotNull(gap);
        Preconditions.checkArgument(StringUtils.isNotBlank(gap));
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setFieldValuePrefixAdder(FieldValuePrefixAdder fieldValuePrefixAdder) {
        this.fieldValuePrefixAdder = fieldValuePrefixAdder;
    }
}
