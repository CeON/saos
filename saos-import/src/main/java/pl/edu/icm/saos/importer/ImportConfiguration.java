package pl.edu.icm.saos.importer;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.importer.commoncourt.CommonCourtImportConfiguration;
import pl.edu.icm.saos.importer.simple.supremecourt.SimpleSupremeCourtImportConfiguration;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
@Import({CommonCourtImportConfiguration.class, SimpleSupremeCourtImportConfiguration.class})
public class ImportConfiguration {

    
    
}
