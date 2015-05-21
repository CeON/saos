package pl.edu.icm.saos.search.analysis;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.solr.result.FacetCcCourtAreaValueConverter;
import pl.edu.icm.saos.search.analysis.solr.result.FacetDayPeriodValueConverter;
import pl.edu.icm.saos.search.analysis.solr.result.FacetMonthPeriodValueConverter;
import pl.edu.icm.saos.search.analysis.solr.result.FacetValueConverter;
import pl.edu.icm.saos.search.analysis.solr.result.FacetValueConverterManager;
import pl.edu.icm.saos.search.analysis.solr.result.FacetYearPeriodValueConverter;
import pl.edu.icm.saos.search.analysis.solr.result.FacetPrefixedValueConverter;
import pl.edu.icm.saos.search.analysis.solr.result.FacetSimpleValueConverter;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author madryk
 */
@Configuration
public class AnalysisConfiguration {

    
    
    @Bean
    public Map<XField, String> fieldNamesMappings() {
    
        Map<XField, String> fieldNamesMappings = Maps.newHashMap();
        
        fieldNamesMappings.put(XField.JUDGMENT_DATE, JudgmentIndexField.JUDGMENT_DATE.getFieldName());
        
        fieldNamesMappings.put(XField.CC_APPEAL, JudgmentIndexField.CC_APPEAL_AREA.getFieldName());
        
        fieldNamesMappings.put(XField.CC_REGION, JudgmentIndexField.CC_REGION_AREA.getFieldName());
        
        fieldNamesMappings.put(XField.CC_DISTRICT, JudgmentIndexField.CC_DISTRICT_AREA.getFieldName());
        
        return fieldNamesMappings;
        
    }
    
    
    
    
    
    
    @Autowired
    private FacetYearPeriodValueConverter facetYearPeriodValueConverter;
    
    @Autowired
    private FacetMonthPeriodValueConverter facetMonthPeriodValueConverter;
    
    @Autowired
    private FacetDayPeriodValueConverter facetDayPeriodValueConverter;

    @Autowired
    private FacetCcCourtAreaValueConverter facetCcCourtAreaValueConverter;
    
    @Autowired
    private FacetPrefixedValueConverter prefixedFacetValueConverter;
    
    @Autowired
    private FacetSimpleValueConverter simpleFacetValueConverter;

    
    
    @Bean
    public FacetValueConverterManager facetValueConverterManager() {
        
        List<FacetValueConverter> facetValueConverters = Lists.newArrayList(
                                                                facetYearPeriodValueConverter, 
                                                                facetMonthPeriodValueConverter, 
                                                                facetDayPeriodValueConverter,
                                                                facetCcCourtAreaValueConverter,
                                                                prefixedFacetValueConverter,
                                                                simpleFacetValueConverter);
        
        FacetValueConverterManager facetValueConverterManager = new FacetValueConverterManager();
        
        facetValueConverterManager.setConverters(facetValueConverters);
        
        return facetValueConverterManager;
        
    }
    
}
