package pl.edu.icm.saos.webapp;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;


@Configuration
@ComponentScan (basePackages = {"pl.edu.icm.saos.webapp", "pl.edu.icm.saos.api"})
@EnableWebMvc
public class WebappConfiguration extends WebMvcConfigurerAdapter {

    
    
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
    }


    @Bean
    public HttpMessageConverter<?> mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    
    /** Properties exposed to view */
    @Bean
    public PropertiesFactoryBean exposedProperties() {
        PropertiesFactoryBean exposedProperties = new PropertiesFactoryBean();
        exposedProperties.setLocation(new ClassPathResource("saos.default.properties"));
        return exposedProperties;
    }
    
    /** Version exposed to view */
    @Bean
    public PropertiesFactoryBean versionProperties() {
        PropertiesFactoryBean versionProperties = new PropertiesFactoryBean();
        versionProperties.setLocation(new ClassPathResource("saos.version.properties"));
        return versionProperties;
    }

}