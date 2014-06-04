package pl.edu.icm.saos.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@PropertySource(ignoreResourceNotFound=false, value={"classpath:saos.persistence.test.properties", "file:///${user.home}/.icm/saos-test.local.properties"})
@Import(PersistenceConfiguration.class)
public class PersistenceTestConfiguration {
        
}
