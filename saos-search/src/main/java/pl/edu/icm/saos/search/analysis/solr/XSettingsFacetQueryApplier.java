package pl.edu.icm.saos.search.analysis.solr;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.FacetParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XRange;
import pl.edu.icm.saos.search.analysis.request.XSettings;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author madryk
 */
@Service
public class XSettingsFacetQueryApplier {

    private Map<XField, String> fieldNamesMappings = Maps.newHashMap();
    
    private List<XRangeConverter> xRangeConverters = Lists.newArrayList();
    
    
    //------------------------ LOGIC --------------------------
    
    public void applyXSettingsToQuery(SolrQuery query, XSettings xSettings) {
        
        XField xField = xSettings.getField();
        String fieldName = fieldNamesMappings.get(xField);
        
        if (fieldName == null) {
            throw new IllegalArgumentException("No field mapping found for " + xField.name());
        }
        
        XRange xRange = xSettings.getRange();
        XRangeConverter xRangeConverter = pickApplicableRangeConverter(xRange);
        
        String startParam = xRangeConverter.convertStart(xRange);
        String endParam = xRangeConverter.convertEnd(xRange);
        String gapParam = xRangeConverter.convertGap(xRange);
        
        
        query.setFacet(true);
        applyRangeFacet(query, fieldName, startParam, endParam, gapParam);
    }
    
    
    public void addFieldNameMapping(XField xField, String fieldName) {
        fieldNamesMappings.put(xField, fieldName);
    }
    
    public void addXRangeConverter(XRangeConverter xRangeConverter) {
        xRangeConverters.add(xRangeConverter);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void applyRangeFacet(SolrQuery query, String fieldName, String start, String end, String gap) {
        String fieldParamPrefix = "f." + fieldName + ".";
        
        
        query.add(FacetParams.FACET_RANGE, fieldName);
        query.add(fieldParamPrefix + FacetParams.FACET_RANGE_START, start);
        query.add(fieldParamPrefix + FacetParams.FACET_RANGE_END, end);
        query.add(fieldParamPrefix + FacetParams.FACET_RANGE_GAP, gap);
    }
    
    private XRangeConverter pickApplicableRangeConverter(XRange xRange) {
        for (XRangeConverter xRangeConverter : xRangeConverters) {
            if (xRangeConverter.isApplicable(xRange)) {
                return xRangeConverter;
            }
        }
        

        throw new IllegalArgumentException("No XRangeConverter applicable for " + xRange.getClass() + " found.");
    }

    
    //------------------------ SETTERS --------------------------
    
    @Resource
    public void setFieldNamesMappings(Map<XField, String> fieldNamesMappings) {
        this.fieldNamesMappings = fieldNamesMappings;
    }

    @Autowired
    public void setxRangeConverters(List<XRangeConverter> xRangeConverters) {
        this.xRangeConverters = xRangeConverters;
    }
}
