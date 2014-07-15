package pl.edu.icm.saos.importer;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.importer.commoncourt.CommonCourtImportConfiguration;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
@Import(CommonCourtImportConfiguration.class)
public class ImportConfiguration {

    
    
}
