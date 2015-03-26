package pl.edu.icm.saos.search.analysis.solr.request;

import java.util.Map;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.search.analysis.request.XRange;
import pl.edu.icm.saos.search.util.SearchDateTimeUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * Converter of {@link XDateRange} to solr params defining range facet 
 * 
 * @author madryk
 */
@Service
public class XDateRangeConverter implements XRangeConverter {

    private final static Map<PeriodUnit, String> PERIOD_UNIT_MAPPINGS = Maps.newHashMap();
    
    static {
        PERIOD_UNIT_MAPPINGS.put(PeriodUnit.DAY, "DAYS");
        PERIOD_UNIT_MAPPINGS.put(PeriodUnit.MONTH, "MONTHS");
        PERIOD_UNIT_MAPPINGS.put(PeriodUnit.YEAR, "YEARS");
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public boolean isApplicable(Class<? extends XRange> clazz) {
        
        Preconditions.checkNotNull(clazz);
        
        return XDateRange.class.isAssignableFrom(clazz);
    }

    @Override
    public String convertStart(XRange xRange) {
        
        Preconditions.checkNotNull(xRange);
        Preconditions.checkArgument(isApplicable(xRange.getClass()));
        
        XDateRange xDateRange = (XDateRange) xRange;
        LocalDate startDate = xDateRange.getStartDate();
        
        return SearchDateTimeUtils.convertDateToISOString(startDate);
    }

    @Override
    public String convertEnd(XRange xRange) {
        
        Preconditions.checkNotNull(xRange);
        Preconditions.checkArgument(isApplicable(xRange.getClass()));
        
        XDateRange xDateRange = (XDateRange) xRange;
        LocalDate endDate = xDateRange.getEndDate();
        
        return SearchDateTimeUtils.convertDateToISOStringAtEndOfDay(endDate);
    }

    @Override
    public String convertGap(XRange xRange) {
        
        Preconditions.checkNotNull(xRange);
        Preconditions.checkArgument(isApplicable(xRange.getClass()));
        
        XDateRange xDateRange = (XDateRange) xRange;
        Period gap = xDateRange.getGap();
        
        int gapValue = gap.getValue();
        PeriodUnit gapUnit = gap.getUnit();
        
        String periodUnit = PERIOD_UNIT_MAPPINGS.get(gapUnit);
        
        if (periodUnit == null) {
            throw new IllegalArgumentException("PeriodUnit " + gapUnit.name() + " is not supported");
        }

          
        return "+" + gapValue + periodUnit;
    }
    
    
    

}
