package pl.edu.icm.saos.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;

/**
 * Base spring configuration for all test configurations in SAOS. <br/><br/>
 * It is recommended that test spring configurations (for {@link SlowTest}s) extend from this class. Thanks to that all common
 * attributes (for example properties) will be defined in one place.
 * @author Łukasz Dumiszewski
 */
@Configuration
@PropertySource(ignoreResourceNotFound=true, value={"classpath:saos.default.test.properties", "file:///${user.home}/.icm/saos-test.local.properties"})
public class TestConfigurationBase {

    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        return propertyPlaceholderConfigurer;
    }
    
}
