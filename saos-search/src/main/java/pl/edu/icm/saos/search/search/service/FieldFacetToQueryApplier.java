package pl.edu.icm.saos.search.search.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.FacetParams;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

/**
 * Applier of field facet request to {@link SolrQuery}
 * 
 * @author madryk
 */
@Service
public class FieldFacetToQueryApplier {

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Applies field facet request to {@link SolrQuery}
     */
    public void applyFieldFacet(SolrQuery query, String fieldName) {
        
        Preconditions.checkNotNull(query);
        Preconditions.checkNotNull(fieldName);
        Preconditions.checkArgument(StringUtils.isNotBlank(fieldName));
        
        query.setFacet(true);
        query.add(FacetParams.FACET_FIELD, fieldName);
    }
}
