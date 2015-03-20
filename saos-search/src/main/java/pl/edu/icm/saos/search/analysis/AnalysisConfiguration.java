package pl.edu.icm.saos.search.analysis;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

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
        
        return fieldNamesMappings;
    }
    
    
}
