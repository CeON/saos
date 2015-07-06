package pl.edu.icm.saos.api;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.json.JsonFormatter;

/**
 * @author pavtel
 */
@Configuration
@ComponentScan(useDefaultFilters=false, includeFilters={@Filter(type=FilterType.ANNOTATION, value=Service.class)})
public class ApiConfiguration {

    @Bean
    public MessageSource apiMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:message/restfulapi");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600);
        return messageSource;

    }
    
    @Bean
    public JsonFormatter jsonFormatter() {
        JsonFormatter jsonFormatter = new JsonFormatter();
        return jsonFormatter;
    }
    
    
}
