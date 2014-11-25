package pl.edu.icm.saos.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.common.TestConfigurationBase;
import pl.edu.icm.saos.search.SearchTestConfiguration;

/**
 * @author pavtel
 */
@Configuration
@Import({ApiConfiguration.class, SearchTestConfiguration.class})
public class ApiTestConfiguration extends TestConfigurationBase {
}
