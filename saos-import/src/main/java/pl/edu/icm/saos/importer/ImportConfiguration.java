package pl.edu.icm.saos.importer;

import javax.annotation.PostConstruct;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.MappingJsonFactory;

import pl.edu.icm.saos.importer.common.ImportDateTimeFormatter;
import pl.edu.icm.saos.importer.commoncourt.CommonCourtImportConfiguration;
import pl.edu.icm.saos.importer.notapi.common.DateTimeDeserializer;
import pl.edu.icm.saos.importer.notapi.common.ImportFileUtils;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.ConstitutionalTribunalImportConfiguration;
import pl.edu.icm.saos.importer.notapi.supremecourt.SupremeCourtImportConfiguration;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
@Import({CommonCourtImportConfiguration.class, SupremeCourtImportConfiguration.class, ConstitutionalTribunalImportConfiguration.class})
public class ImportConfiguration {

    
    @Bean
    public Validator validator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }
    
    @Bean
    public MappingJsonFactory jsonFactory() {
        MappingJsonFactory factory = new MappingJsonFactory();
        factory.enable(Feature.ALLOW_COMMENTS);
        return factory;
    }
    
    @Bean
    public ImportFileUtils importFileUtils() {
        ImportFileUtils scjImportFileUtils = new ImportFileUtils();
        scjImportFileUtils.setEligibleFileExtensions(new String[]{"json", "json.gz"});
        return scjImportFileUtils;
    }
    
    
    @Bean
    public ImportDateTimeFormatter importDateTimeFormatter() {
        ImportDateTimeFormatter importDateTimeFormatter = new ImportDateTimeFormatter();
        importDateTimeFormatter.setImportDatePattern("yyyy-MM-dd HH:mm");
        return importDateTimeFormatter;
    }
    
    
    
    //------------------------ POST_CONSTRUCT --------------------------
    
    @PostConstruct
    public void postConstruct() {
        DateTimeDeserializer.setImportDateTimeFormatter(importDateTimeFormatter());
    }
    
}
