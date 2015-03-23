package pl.edu.icm.saos.webapp.analysis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.formatter.PointLocalDateValueFormatter;
import pl.edu.icm.saos.common.chart.formatter.PointMonthYearValueFormatter;
import pl.edu.icm.saos.common.chart.formatter.PointObjectValueFormatter;
import pl.edu.icm.saos.common.chart.formatter.PointValueFormatter;
import pl.edu.icm.saos.common.chart.formatter.PointValueFormatterManager;
import pl.edu.icm.saos.webapp.analysis.result.PointWeekBrValueFormatter;

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
    public PointValueFormatter pointLocalDateValueFormatter() {
        return new PointLocalDateValueFormatter();
    }

    
    @Bean
    public PointValueFormatter pointMonthYearValueFormatter() {
        return new PointMonthYearValueFormatter();
    }

    
    @Bean
    public PointValueFormatter pointWeekBrValueFormatter() {
        return new PointWeekBrValueFormatter();
    }
    
    @Bean
    public PointValueFormatter pointObjectValueFormatter() {
        return new PointObjectValueFormatter();
    }
    

    @Bean
    public PointValueFormatterManager pointValueFormatterManager() {
        
        PointValueFormatterManager pointValueFormatterManager = new PointValueFormatterManager();
        pointValueFormatterManager.setPointValueFormatters(
                Lists.newArrayList(pointLocalDateValueFormatter(),
                                   pointMonthYearValueFormatter(),
                                   pointWeekBrValueFormatter(),
                                   pointObjectValueFormatter()
                )
        );
        
        return pointValueFormatterManager;
        
        
        
        
    }
}
