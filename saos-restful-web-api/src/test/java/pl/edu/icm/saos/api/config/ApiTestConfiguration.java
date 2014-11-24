package pl.edu.icm.saos.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import pl.edu.icm.saos.common.CommonTestConfiguration;
import pl.edu.icm.saos.persistence.PersistenceConfiguration;
import pl.edu.icm.saos.search.SearchTestConfiguration;

/**
 * @author pavtel
 */
@Configuration
@Import({PersistenceConfiguration.class, SearchTestConfiguration.class})
@ComponentScan(basePackages = "pl.edu.icm.saos.api", excludeFilters = {@ComponentScan.Filter(Configuration.class), @ComponentScan.Filter(Controller.class)})
public class ApiTestConfiguration  extends CommonTestConfiguration{
}
