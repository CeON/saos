package pl.edu.icm.saos.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

/**
 * @author pavtel
 */
@Configuration
@ComponentScan(basePackages = "pl.edu.icm.saos.api", excludeFilters = {@ComponentScan.Filter(Configuration.class), @ComponentScan.Filter(Controller.class)})
public class TestsConfig {

}
