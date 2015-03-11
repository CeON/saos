package pl.edu.icm.saos.search.search.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.FacetParams;
import org.springframework.stereotype.Service;

/**
 * @author madryk
 */
@Service
public class RangeFacetToQueryApplier {

    //------------------------ LOGIC --------------------------
    
    public void applyRangeFacet(SolrQuery query, String fieldName, String start, String end, String gap) {
        query.setFacet(true);
        
        String fieldParamPrefix = "f." + fieldName + ".";
        
        query.add(FacetParams.FACET_RANGE, fieldName);
        query.add(fieldParamPrefix + FacetParams.FACET_RANGE_START, start);
        query.add(fieldParamPrefix + FacetParams.FACET_RANGE_END, end);
        query.add(fieldParamPrefix + FacetParams.FACET_RANGE_GAP, gap);
    }
}
