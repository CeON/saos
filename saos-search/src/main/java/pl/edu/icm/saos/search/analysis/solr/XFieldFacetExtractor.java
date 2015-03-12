package pl.edu.icm.saos.search.analysis.solr;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pl.edu.icm.saos.search.analysis.request.XField;

/**
 * Extracts {@link RangeFacet} associated with {@link XField}
 * from solr {@link QueryResponse response}
 * 
 * @author madryk
 */
@Service
public class XFieldFacetExtractor {

    private XFieldNameMapper xFieldNameMapper;
    
    
    //------------------------ LOGIC --------------------------
    
    public RangeFacet<?, ?> extractFacet(QueryResponse response, XField xField) {
        
        Preconditions.checkNotNull(response);
        Preconditions.checkNotNull(xField);
        
        String fieldName = xFieldNameMapper.mapXField(xField);
        
        RangeFacet<?, ?> facets = response.getFacetRanges()
                .stream()
                .filter(rf -> fieldName.equals(rf.getName()))
                .findFirst().get();
        
        return facets;
    }

    
    //------------------------ SETTERS --------------------------
    
    @Resource
    public void setxFieldNameMapper(XFieldNameMapper xFieldNameMapper) {
        this.xFieldNameMapper = xFieldNameMapper;
    }
}
