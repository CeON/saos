package pl.edu.icm.saos.webapp;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;


@Configuration
@PropertySource(ignoreResourceNotFound=true, value={"classpath:saos.default.properties"})
@ComponentScan(useDefaultFilters=false, includeFilters={@Filter(type=FilterType.ANNOTATION, value=Service.class)})
@EnableScheduling
@PropertySource(ignoreResourceNotFound=true, value={"classpath:saos.default.properties"})
public class GeneralConfiguration {

        
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        return propertyPlaceholderConfigurer;
    }
    
    @Bean 
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(new Locale("pl"));
    }
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:message/common");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600);
        return messageSource;
        
    }
    
    
    
    
     
    
    
    
   
    
}
