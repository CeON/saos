package pl.edu.icm.saos.search.analysis;

import java.util.Map;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.util.SearchDateTimeUtils;

/**
 * @author madryk
 */
@Configuration
public class AnalysisConfiguration {

    @Bean
    public Map<XField, String> fieldNamesMappings() {
        Map<XField, String> fieldNamesMappings = Maps.newHashMap();
        fieldNamesMappings.put(XField.JUDGMENT_DATE, JudgmentIndexField.JUDGMENT_DATE.getFieldName());
        
        return fieldNamesMappings;
    }
    
    @Bean
    public Map<XField, Function<String, Object>> xConvertStrategy() {
        Map<XField, Function<String, Object>> xConvertStrategy = Maps.newHashMap();
        xConvertStrategy.put(XField.JUDGMENT_DATE, (x -> SearchDateTimeUtils.convertISOStringToDate(x)));
        
        return xConvertStrategy;
    }
}
