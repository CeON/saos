package pl.edu.icm.saos.search.analysis.solr.result;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.solr.XFieldNameMapper;

import com.google.common.base.Preconditions;

/**
 * Extractor of {@link RangeFacet} from solr {@link QueryResponse response}
 * 
 * @author madryk
 */
@Service
public class XFieldFacetExtractor {

    private XFieldNameMapper xFieldNameMapper;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Extracts {@link RangeFacet} associated with {@link XField}
     * from solr {@link QueryResponse response}
     */
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
