package pl.edu.icm.saos.persistence;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
//@EnableJpaRepositories
@EnableTransactionManagement
public class PersistenceConfiguration {

    
}
