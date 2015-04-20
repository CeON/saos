package pl.edu.icm.saos.webapp.analysis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.formatter.PointDayPeriodValueFormatter;
import pl.edu.icm.saos.common.chart.formatter.PointMonthPeriodValueFormatter;
import pl.edu.icm.saos.common.chart.formatter.PointObjectValueFormatter;
import pl.edu.icm.saos.common.chart.formatter.PointValueFormatterManager;
import pl.edu.icm.saos.common.chart.formatter.PointYearPeriodValueFormatter;
import pl.edu.icm.saos.webapp.analysis.result.PointBrAddingValueFormatter;
import pl.edu.icm.saos.webapp.analysis.result.PointFloatValueFormatter;

import com.google.common.collect.Lists;

/**
 * A configuration of the webapp analysis part
 * 
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan(useDefaultFilters=false, includeFilters={@Filter(type=FilterType.ANNOTATION, value=Service.class)})
public class UiAnalysisConfiguration {

    
    
    
    

    @Bean
    public PointValueFormatterManager flotChartPointValueFormatterManager() {
        
        PointValueFormatterManager pointValueFormatterManager = new PointValueFormatterManager();
        pointValueFormatterManager.setPointValueFormatters(
                Lists.newArrayList(new PointBrAddingValueFormatter(new PointDayPeriodValueFormatter()),
                                   new PointBrAddingValueFormatter(new PointMonthPeriodValueFormatter()),
                                   new PointBrAddingValueFormatter(new PointYearPeriodValueFormatter()),
                                   new PointObjectValueFormatter()
                )
        );
        
        return pointValueFormatterManager;
        
        
    }
    
    
    
    @Bean
    public PointValueFormatterManager csvPointValueFormatterManager() {
                                      
        PointValueFormatterManager pointValueFormatterManager = new PointValueFormatterManager();
        pointValueFormatterManager.setPointValueFormatters(
                Lists.newArrayList(new PointDayPeriodValueFormatter(),
                                   new PointMonthPeriodValueFormatter(),
                                   new PointYearPeriodValueFormatter(),
                                   new PointFloatValueFormatter(),
                                   new PointObjectValueFormatter()
                )
        );
        
        return pointValueFormatterManager;
        
        
        
    }
}
