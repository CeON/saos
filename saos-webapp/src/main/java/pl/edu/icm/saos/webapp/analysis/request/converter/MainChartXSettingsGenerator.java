package pl.edu.icm.saos.webapp.analysis.request.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentGlobalFilter;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;

import com.google.common.base.Preconditions;

/**
 * An {@link XSettingsGenerator} implementation that creates {@link XSettings} for
 * {@link ChartCode#MAIN_CHART} charts
 * 
 * @author Łukasz Dumiszewski
 */
@Service("mainChartXSettingsGenerator")
public class MainChartXSettingsGenerator implements XSettingsGenerator {

    private MonthYearRangeConverter monthYearRangeConverter;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public XSettings generateXSettings(JudgmentGlobalFilter judgmentGlobalFilter) {
        
        Preconditions.checkArgument(canGenerateXSettings(judgmentGlobalFilter));
        
        XSettings xsettings = new XSettings();

        xsettings.setRange(monthYearRangeConverter.convert(judgmentGlobalFilter.getJudgmentDateRange()));
        
        xsettings.setField(XField.JUDGMENT_DATE);
        
        return xsettings;
    
    }
    

    @Override
    public boolean canGenerateXSettings(JudgmentGlobalFilter judgmentGlobalFilter) {
    
        Preconditions.checkNotNull(judgmentGlobalFilter);
        
        return judgmentGlobalFilter.getJudgmentDateRange() != null;
    
    }

    @Override
    public boolean handles(ChartCode chartCode) {
        
        Preconditions.checkNotNull(chartCode);
        
        return chartCode == ChartCode.MAIN_CHART;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setMonthYearRangeConverter(MonthYearRangeConverter monthYearRangeConverter) {
        this.monthYearRangeConverter = monthYearRangeConverter;
    }


   

}
