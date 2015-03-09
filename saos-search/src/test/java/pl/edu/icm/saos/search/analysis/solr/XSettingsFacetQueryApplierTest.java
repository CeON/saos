package pl.edu.icm.saos.search.analysis.solr;

import static org.junit.Assert.assertEquals;
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


/**
 * @author madryk
 */
public class XSettingsFacetQueryApplierTest {

    private XSettingsFacetQueryApplier xSettingsFacetQueryApplier = new XSettingsFacetQueryApplier();
    
    private XRangeConverter xRangeConverter = mock(XRangeConverter.class);
    
    private XRangeConverter xRangeConverter2 = mock(XRangeConverter.class);
    
    
    
    @Before
    public void setUp() {
        when(xRangeConverter.convertStart(any())).thenReturn("startParam");
        when(xRangeConverter.convertEnd(any())).thenReturn("endParam");
        when(xRangeConverter.convertGap(any())).thenReturn("gapParam");
        when(xRangeConverter2.isApplicable(any())).thenReturn(false);
        
        xSettingsFacetQueryApplier.addXRangeConverter(xRangeConverter2);
        xSettingsFacetQueryApplier.addXRangeConverter(xRangeConverter);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = IllegalArgumentException.class)
    public void applyXSettingsToQuery_NO_RANGE_CONVERTER() {
        // given
        when(xRangeConverter.isApplicable(any())).thenReturn(false);
        xSettingsFacetQueryApplier.addFieldNameMapping(XField.JUDGMENT_DATE, "fieldName");
        
        XSettings xSettings = new XSettings();
        xSettings.setField(XField.JUDGMENT_DATE);
        xSettings.setRange(new XDateRange(new LocalDate(), new LocalDate(), new Period(1, PeriodUnit.MONTH)));
        
        // execute
        xSettingsFacetQueryApplier.applyXSettingsToQuery(new SolrQuery(), xSettings);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void applyXSettingsToQuery_NO_FIELD_MAPPING() {
        // given
        when(xRangeConverter.isApplicable(any())).thenReturn(true);
        
        XSettings xSettings = new XSettings();
        xSettings.setField(XField.JUDGMENT_DATE);
        xSettings.setRange(new XDateRange(new LocalDate(), new LocalDate(), new Period(1, PeriodUnit.MONTH)));
        
        // execute
        xSettingsFacetQueryApplier.applyXSettingsToQuery(new SolrQuery(), xSettings);
    }
    
    @Test
    public void applyXSettingsToQuery() {
        // given
        when(xRangeConverter.isApplicable(any())).thenReturn(true);
        
        xSettingsFacetQueryApplier.addFieldNameMapping(XField.JUDGMENT_DATE, "fieldName");
        
        SolrQuery query = new SolrQuery();
        XSettings xSettings = new XSettings();
        xSettings.setField(XField.JUDGMENT_DATE);
        
        XDateRange xDateRange = new XDateRange(new LocalDate(), new LocalDate(), new Period(1, PeriodUnit.MONTH));
        xSettings.setRange(xDateRange);
        
        
        // execute
        xSettingsFacetQueryApplier.applyXSettingsToQuery(query, xSettings);
        
        // assert
        assertEquals("fieldName", query.get("facet.range"));
        assertEquals("startParam", query.get("f.fieldName.facet.range.start"));
        assertEquals("endParam", query.get("f.fieldName.facet.range.end"));
        assertEquals("gapParam", query.get("f.fieldName.facet.range.gap"));
        
        verify(xRangeConverter).isApplicable(xDateRange);
        verify(xRangeConverter).convertStart(xDateRange);
        verify(xRangeConverter).convertEnd(xDateRange);
        verify(xRangeConverter).convertGap(xDateRange);
        verify(xRangeConverter2).isApplicable(xDateRange);
    }
}
