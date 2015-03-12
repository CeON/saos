package pl.edu.icm.saos.search.search.service;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.FacetParams;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

/**
 * Applies range facet request to {@link SolrQuery}
 * 
 * @author madryk
 */
@Service
public class RangeFacetToQueryApplier {

    //------------------------ LOGIC --------------------------
    
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
}
