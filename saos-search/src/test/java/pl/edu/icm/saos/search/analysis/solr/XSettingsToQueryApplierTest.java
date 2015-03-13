package pl.edu.icm.saos.search.analysis.solr;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.solr.client.solrj.SolrQuery;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.search.service.RangeFacetToQueryApplier;


/**
 * @author madryk
 */
public class XSettingsToQueryApplierTest {

    private XSettingsToQueryApplier xSettingsFacetQueryApplier = new XSettingsToQueryApplier();
        
    
    private XFieldNameMapper xFieldNameMapper = mock(XFieldNameMapper.class);
    
    private XRangeConverterManager xRangeConverterManager = mock(XRangeConverterManager.class);
    
    private RangeFacetToQueryApplier rangeFacetToQueryApplier = mock(RangeFacetToQueryApplier.class);
    
    private XRangeConverter xRangeConverter = mock(XRangeConverter.class);
    
    
    @Before
    public void setUp() {
        xSettingsFacetQueryApplier.setxFieldNameMapper(xFieldNameMapper);
        xSettingsFacetQueryApplier.setxRangeConverterManager(xRangeConverterManager);
        xSettingsFacetQueryApplier.setRangeFacetToQueryApplier(rangeFacetToQueryApplier);
        
        when(xRangeConverter.convertStart(any())).thenReturn("startParam");
        when(xRangeConverter.convertEnd(any())).thenReturn("endParam");
        when(xRangeConverter.convertGap(any())).thenReturn("gapParam");
        
        when(xFieldNameMapper.mapXField(XField.JUDGMENT_DATE)).thenReturn("judgmentDate");
        when(xRangeConverterManager.getXRangeConverter(XDateRange.class)).thenReturn(xRangeConverter);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void applyXSettingsToQuery() {
        // given
        XSettings xSettings = new XSettings();
        xSettings.setField(XField.JUDGMENT_DATE);
        
        XDateRange xDateRange = new XDateRange(new LocalDate(), new LocalDate(), new Period(1, PeriodUnit.MONTH));
        xSettings.setRange(xDateRange);
        
        SolrQuery query = new SolrQuery();
        
        // execute
        xSettingsFacetQueryApplier.applyXSettingsToQuery(query, xSettings);
        
        // assert
        verify(xFieldNameMapper).mapXField(XField.JUDGMENT_DATE);
        verify(xRangeConverterManager).getXRangeConverter(XDateRange.class);
        verify(xRangeConverter).convertStart(xDateRange);
        verify(xRangeConverter).convertEnd(xDateRange);
        verify(xRangeConverter).convertGap(xDateRange);
        verify(rangeFacetToQueryApplier).applyRangeFacet(query, "judgmentDate", "startParam", "endParam", "gapParam");
    }
    
    @Test(expected = NullPointerException.class)
    public void applyXSettingsToQuery_NULL_QUERY() {
        // execute
        xSettingsFacetQueryApplier.applyXSettingsToQuery(new SolrQuery(), null);
    }
    
    @Test(expected = NullPointerException.class)
    public void applyXSettingsToQuery_NULL_XSETTINGS() {
        // execute
        xSettingsFacetQueryApplier.applyXSettingsToQuery(null, new XSettings());
    }
}
