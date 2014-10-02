package pl.edu.icm.saos.importer.simple.supremecourt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.importer.simple.common.ImportFileUtils;

import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * 
 * @author Łukasz Dumiszewski
 */
@Configuration
public class SimpleSupremeCourtImportConfiguration {

    @Value("${import.mainDir}") 
    private String importMainDir;
    
    
    @Bean 
    public ImportFileUtils scjImportFileService(@Value("${import.relDir.supremeCourt.judgment}") String importRelDir) {
        ImportFileUtils scjImportFileUtils = new ImportFileUtils();
        scjImportFileUtils.setEligibleFileExtensions(new String[]{"json", "json.gz"});
        scjImportFileUtils.setImportMainDir(importMainDir);
        scjImportFileUtils.setImportRelDir(importRelDir);
        return scjImportFileUtils;
    }
    
    @Bean
    public MappingJsonFactory jsonFactory() {
        return new MappingJsonFactory();
    }
    
    
}
