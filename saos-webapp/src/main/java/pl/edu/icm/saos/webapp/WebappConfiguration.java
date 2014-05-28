package pl.edu.icm.saos.webapp;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@PropertySource(ignoreResourceNotFound=true, value={"classpath:saos.default.properties"})
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
    
    
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(WebappConfiguration.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

}