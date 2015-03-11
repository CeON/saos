package pl.edu.icm.saos.search.analysis.solr;

import java.util.Map;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.search.analysis.request.XRange;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.util.SearchDateTimeUtils;

/**
 * Converter of {@link XDateRange} to solr params defining range facet 
 * 
 * @author madryk
 */
@Service
public class XDateRangeConverter implements XRangeConverter {

    private final static Map<PeriodUnit, PeriodUnitMapping> PERIOD_UNIT_MAPPINGS = Maps.newHashMap();
    
    static {
        PERIOD_UNIT_MAPPINGS.put(PeriodUnit.WEEK, new PeriodUnitMapping("DAYS", 7));
        PERIOD_UNIT_MAPPINGS.put(PeriodUnit.MONTH, new PeriodUnitMapping("MONTHS", 1));
        PERIOD_UNIT_MAPPINGS.put(PeriodUnit.YEAR, new PeriodUnitMapping("YEARS", 1));
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public boolean isApplicable(Class<? extends XRange> clazz) {
        return XDateRange.class.isAssignableFrom(clazz);
    }

    @Override
    public String convertStart(XRange xRange) {
        XDateRange xDateRange = (XDateRange) xRange;
        LocalDate startDate = xDateRange.getStartDate();
        
        return SearchDateTimeUtils.convertDateToISOString(startDate);
    }

    @Override
    public String convertEnd(XRange xRange) {
        XDateRange xDateRange = (XDateRange) xRange;
        LocalDate endDate = xDateRange.getEndDate();
        
        return SearchDateTimeUtils.convertDateToISOString(endDate);
    }

    @Override
    public String convertGap(XRange xRange) {
        XDateRange xDateRange = (XDateRange) xRange;
        Period gap = xDateRange.getGap();
        
        int gapValue = gap.getValue();
        PeriodUnit gapUnit = gap.getUnit();
        
        
        PeriodUnitMapping periodMapping = PERIOD_UNIT_MAPPINGS.get(gapUnit);
        
        return "+" + (gapValue * periodMapping.getUnitMultiplier()) + periodMapping.getUnit();
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private static class PeriodUnitMapping {
        private String unit;
        private int unitMultiplier;
        
        
        //------------------------ CONSTRUCTORS --------------------------
        
        public PeriodUnitMapping(String unit, int unitMultiplier) {
            this.unit = unit;
            this.unitMultiplier = unitMultiplier;
        }
        
        
        //------------------------ GETTERS --------------------------

        public String getUnit() {
            return unit;
        }

        public int getUnitMultiplier() {
            return unitMultiplier;
        }
        
    }

}
