package pl.edu.icm.saos.importer;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import pl.edu.icm.saos.importer.common.ImportDateTimeFormatter;
import pl.edu.icm.saos.importer.commoncourt.CommonCourtImportConfiguration;
import pl.edu.icm.saos.importer.notapi.common.DateTimeDeserializer;
import pl.edu.icm.saos.importer.notapi.common.ImportFileUtils;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.ConstitutionalTribunalImportConfiguration;
import pl.edu.icm.saos.importer.notapi.supremecourt.SupremeCourtImportConfiguration;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
@Import({CommonCourtImportConfiguration.class, SupremeCourtImportConfiguration.class, ConstitutionalTribunalImportConfiguration.class})
public class ImportConfiguration {

    
    
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
