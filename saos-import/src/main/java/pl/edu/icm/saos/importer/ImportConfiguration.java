package pl.edu.icm.saos.importer;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.importer.commoncourt.CommonCourtImportConfiguration;
import pl.edu.icm.saos.importer.notapi.supremecourt.SupremeCourtImportConfiguration;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
@Import({CommonCourtImportConfiguration.class, SupremeCourtImportConfiguration.class})
public class ImportConfiguration {

    
    @Bean
    public Validator validator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }
    
}
