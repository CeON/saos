package pl.edu.icm.saos.webapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import pl.edu.icm.saos.api.formatter.DateTimeWithZoneFormatterFactory;
import pl.edu.icm.saos.api.formatter.LawJournalEntryCodeFormatterFactory;
import pl.edu.icm.saos.api.services.interceptor.AccessControlHeaderHandlerInterceptor;
import pl.edu.icm.saos.api.services.interceptor.RestrictParamsHandlerInterceptor;
import pl.edu.icm.saos.persistence.service.LawJournalEntryCodeExtractor;
import pl.edu.icm.saos.webapp.common.RequestURLInterceptor;
import pl.edu.icm.saos.webapp.format.MultiWordFormatterFactory;
import pl.edu.icm.saos.webapp.format.StringTrimmingFormatter;


@Configuration
@ComponentScan(basePackages={"pl.edu.icm.saos"}, useDefaultFilters=false, includeFilters={@Filter(type=FilterType.ANNOTATION, value=Controller.class), 
                                    @Filter(type=FilterType.ANNOTATION, value=RestController.class), 
                                    @Filter(type=FilterType.ANNOTATION, value=ControllerAdvice.class)})
@EnableWebMvc
public class WebappConfiguration extends SpringDataWebConfiguration {

    @Autowired
    private HttpMessageConverter<?> mappingJackson2HttpMessageConverter;
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private LocaleResolver localeResolver;
    
    @Autowired
    private LawJournalEntryCodeExtractor lawJournalEntryCodeExtractor;
    
    @Value("${judgments.content.dir}")
    private String judgmentsContentPath; 
    
    @Bean
    public TilesViewResolver viewResolver() {
        TilesViewResolver viewResolver = new TilesViewResolver();
        return viewResolver;
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions(new String[]{"/WEB-INF/tiles.xml"});
        return tilesConfigurer;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/static/").setCachePeriod(3600*24*7);
        registry.addResourceHandler("/robots.txt").addResourceLocations("/WEB-INF/").setCachePeriod(0);
        registry.addResourceHandler("/sitemap.xml").addResourceLocations("/WEB-INF/sitemap.xml").setCachePeriod(0);
        registry.addResourceHandler("/files/judgments/**").addResourceLocations(ResourceUtils.FILE_URL_PREFIX + judgmentsContentPath).setCachePeriod(600);
        
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
    	registry.addFormatterForFieldType(String.class, new StringTrimmingFormatter());
    	registry.addFormatterForFieldAnnotation(new DateTimeWithZoneFormatterFactory());
    	registry.addFormatterForFieldAnnotation(new LawJournalEntryCodeFormatterFactory(lawJournalEntryCodeExtractor));
    	registry.addFormatterForFieldAnnotation(new MultiWordFormatterFactory());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new AccessControlHeaderHandlerInterceptor())
            .addPathPatterns("/api/**")
            .addPathPatterns("/analysis/generate*")
            .addPathPatterns("/cc/courts/**")
            .addPathPatterns("/sc/chambers/**")
            .addPathPatterns("/sc/judgmentForms/list")
            .addPathPatterns("/search/lawJournalEntries*")
            .addPathPatterns("/keywords/**");
        registry.addInterceptor(new RestrictParamsHandlerInterceptor());
        registry.addInterceptor(new RequestURLInterceptor());
        
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> httpMessageConverters) {
        httpMessageConverters.add(mappingJackson2HttpMessageConverter);
    }
    
    /** Properties exposed to view */
    @Bean
    public PropertiesFactoryBean exposedProperties(@Value("${user.home}") String userHomeDir) {
        PropertiesFactoryBean exposedProperties = new PropertiesFactoryBean();
        exposedProperties.setIgnoreResourceNotFound(true);
        exposedProperties.setLocations(new Resource[]{new ClassPathResource("saos.default.properties"), new FileSystemResource(userHomeDir + "/.icm/saos.local.properties")});
        return exposedProperties;
    }
    
    /** Version exposed to view */
    @Bean
    public PropertiesFactoryBean versionProperties() {
        PropertiesFactoryBean versionProperties = new PropertiesFactoryBean();
        versionProperties.setLocation(new ClassPathResource("saos.version.properties"));
        return versionProperties;
    }
    
    @Bean
    public PageableHandlerMethodArgumentResolver pageableResolver() {
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver(sortResolver());
        pageableResolver.setOneIndexedParameters(true);
        pageableResolver.setMaxPageSize(100);
        return pageableResolver;
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(false);
        return propertyPlaceholderConfigurer;
    }


    

}

