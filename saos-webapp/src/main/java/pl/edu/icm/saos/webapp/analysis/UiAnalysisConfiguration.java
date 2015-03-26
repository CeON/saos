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
import pl.edu.icm.saos.common.chart.formatter.PointValueFormatter;
import pl.edu.icm.saos.common.chart.formatter.PointValueFormatterManager;
import pl.edu.icm.saos.common.chart.formatter.PointYearPeriodValueFormatter;
import pl.edu.icm.saos.webapp.analysis.result.PointBrAddingValueFormatter;

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
    public PointValueFormatter pointDayPeriodValueFormatter() {
        return new PointBrAddingValueFormatter(new PointDayPeriodValueFormatter());
    }

    
    @Bean
    public PointValueFormatter pointMonthPeriodValueFormatter() {
        return new PointBrAddingValueFormatter(new PointMonthPeriodValueFormatter());
    }

    
    @Bean
    public PointValueFormatter pointYearPeriodValueFormatter() {
        return new PointBrAddingValueFormatter(new PointYearPeriodValueFormatter());
    }
    
    @Bean
    public PointValueFormatter pointObjectValueFormatter() {
        return new PointObjectValueFormatter();
    }
    

    @Bean
    public PointValueFormatterManager pointValueFormatterManager() {
        
        PointValueFormatterManager pointValueFormatterManager = new PointValueFormatterManager();
        pointValueFormatterManager.setPointValueFormatters(
                Lists.newArrayList(pointDayPeriodValueFormatter(),
                                   pointMonthPeriodValueFormatter(),
                                   pointYearPeriodValueFormatter(),
                                   pointObjectValueFormatter()
                )
        );
        
        return pointValueFormatterManager;
        
        
        
        
    }
}
