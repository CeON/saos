package pl.edu.icm.saos.batch.admin;

import org.springframework.batch.admin.web.filter.ParameterUnpackerFilter;
import org.springframework.batch.admin.web.resources.DefaultResourceService;
import org.springframework.batch.admin.web.resources.ResourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ImportResource("classpath:admin/applicationContext-batchAdmin.xml")
@PropertySource(ignoreResourceNotFound=true, value={"classpath:/org/springframework/batch/admin/bootstrap/batch.properties", "classpath:saos.default.properties"})
public class BatchAdminConfiguration extends WebMvcConfigurerAdapter {

    
         
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        return propertyPlaceholderConfigurer;
    }
    
    
    
    
    /** Servlet path to the batch app, used internally by springBatchAdmin to prepend to app urls */
    @Bean
    public ResourceService resourceService() {
        DefaultResourceService resourceService = new DefaultResourceService();
        resourceService.setServletPath("/batch");
        return resourceService;
    }
    
    
    /** just copied from  spring-batch-admin-resources.jar/resources/WEB-INF */
    @Bean
    public ParameterUnpackerFilter paramUnpackingFilter() {
        ParameterUnpackerFilter unpackingFilter = new ParameterUnpackerFilter();
        unpackingFilter.setPrefix("unpack_");
        unpackingFilter.setPutEmptyParamsInPath(true);
        return unpackingFilter;
    }

}
