package pl.edu.icm.saos.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;



/**
 * Common test configuration for slow tests (using spring context)
 * @author Łukasz Dumiszewski
 */
@Configuration
@Import(CommonConfiguration.class)
@PropertySource(ignoreResourceNotFound=true, value={"classpath:saos.default.test.properties", "file:///${user.home}/.icm/saos-test.local.properties"})
public class CommonTestConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        return propertyPlaceholderConfigurer;
    }
}
