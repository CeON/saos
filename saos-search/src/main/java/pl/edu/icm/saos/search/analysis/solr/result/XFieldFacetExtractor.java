package pl.edu.icm.saos.search.analysis.solr.result;

import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.solr.XFieldNameMapper;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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
     * Extracts {@link FacetCount}s associated with {@link XField}
     * from solr {@link QueryResponse response}.
     * It supports extracting from solr range facets and field facets.
     */
    public List<FacetCount> extractFacetCounts(QueryResponse response, XField xField) {
        
        Preconditions.checkNotNull(response);
        Preconditions.checkNotNull(xField);
        
        String fieldName = xFieldNameMapper.mapXField(xField);
        
        
        RangeFacet<?, ?> facetRange = extractRangeFacet(response, fieldName);
        FacetField facetField = extractFieldFacet(response, fieldName);
        
        if (facetRange != null) {
            return convertRangeFacetCounts(facetRange);
        }
        if (facetField != null) {
            return convertFieldFacetCounts(facetField);
        }
        
        throw new RuntimeException("No field or range facet for field name " + fieldName + " in solr query response");
    }

    
    //------------------------ PRIVATE --------------------------
    
    private RangeFacet<?, ?> extractRangeFacet(QueryResponse response, String fieldName) {
        if (response.getFacetRanges() == null) {
            return null;
        }
        return response.getFacetRanges()
                .stream()
                .filter(rf -> fieldName.equals(rf.getName()))
                .findFirst().orElse(null);
    }
    
    private FacetField extractFieldFacet(QueryResponse response, String fieldName) {
        return response.getFacetField(fieldName);
    }
    
    private List<FacetCount> convertRangeFacetCounts(RangeFacet<?, ?> rangeFacet) {
        List<FacetCount> facetCounts = Lists.newLinkedList();
        
        for (RangeFacet.Count count : rangeFacet.getCounts()) {
            facetCounts.add(new FacetCount(count.getValue(), count.getCount()));
        }
        
        return facetCounts;
    }
    
    private List<FacetCount> convertFieldFacetCounts(FacetField fieldFacet) {
        List<FacetCount> facetCounts = Lists.newLinkedList();
        
        for (FacetField.Count count : fieldFacet.getValues()) {
            facetCounts.add(new FacetCount(count.getName(), Long.valueOf(count.getCount()).intValue()));
        }
        
        return facetCounts;
    }
    
    
    
    //------------------------ SETTERS --------------------------
    
    @Resource
    public void setxFieldNameMapper(XFieldNameMapper xFieldNameMapper) {
        this.xFieldNameMapper = xFieldNameMapper;
    }
}
