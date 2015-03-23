package pl.edu.icm.saos.search.analysis.solr.request;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XRange;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.solr.XFieldNameMapper;
import pl.edu.icm.saos.search.search.service.RangeFacetToQueryApplier;

/**
 * Applier of {@link XSettings} to {@link SolrQuery}
 * 
 * @author madryk
 */
@Service
public class XSettingsToQueryApplier {
    
    private XFieldNameMapper xFieldNameMapper;
    
    private XRangeConverterManager xRangeConverterManager;
    
    private RangeFacetToQueryApplier rangeFacetToQueryApplier;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Applies {@link XSettings} to {@link SolrQuery}
     */
    public void applyXSettingsToQuery(SolrQuery query, XSettings xSettings) {
        
        Preconditions.checkNotNull(query);
        Preconditions.checkNotNull(xSettings);
        
        XField xField = xSettings.getField();
        String fieldName = xFieldNameMapper.mapXField(xField);
        
        XRange xRange = xSettings.getRange();
        XRangeConverter xRangeConverter = xRangeConverterManager.getXRangeConverter(xRange.getClass());
        
        String startParam = xRangeConverter.convertStart(xRange);
        String endParam = xRangeConverter.convertEnd(xRange);
        String gapParam = xRangeConverter.convertGap(xRange);
        
        
        rangeFacetToQueryApplier.applyRangeFacet(query, fieldName, startParam, endParam, gapParam);
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setxFieldNameMapper(XFieldNameMapper xFieldNameMapper) {
        this.xFieldNameMapper = xFieldNameMapper;
    }

    @Autowired
    public void setxRangeConverterManager(XRangeConverterManager xRangeConverterManager) {
        this.xRangeConverterManager = xRangeConverterManager;
    }

    @Autowired
    public void setRangeFacetToQueryApplier(RangeFacetToQueryApplier rangeFacetToQueryApplier) {
        this.rangeFacetToQueryApplier = rangeFacetToQueryApplier;
    }
}
