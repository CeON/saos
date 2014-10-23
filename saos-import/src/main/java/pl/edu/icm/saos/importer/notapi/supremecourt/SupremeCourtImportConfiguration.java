package pl.edu.icm.saos.importer.notapi.supremecourt;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.importer.common.DelegatingJudgmentOverwriter;
import pl.edu.icm.saos.importer.common.ImportDateTimeFormatter;
import pl.edu.icm.saos.importer.common.JudgmentConverter;
import pl.edu.icm.saos.importer.common.JudgmentConverterImpl;
import pl.edu.icm.saos.importer.common.JudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.common.ImportFileUtils;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.DateTimeDeserializer;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process.SourceScJudgmentExtractor;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * 
 * @author Łukasz Dumiszewski
 */
@Configuration
public class SupremeCourtImportConfiguration {

    
    @Value("${import.mainDir}") 
    private String importMainDir;
    
    @Autowired
    private SourceScJudgmentExtractor sourceScJudgmentExtractor;
    
    
    @Autowired 
    @Qualifier("scSpecificJudgmentOverwriter")
    private JudgmentOverwriter<SupremeCourtJudgment> scSpecificJudgmentOverwriter;
    
    
    
    //------------------------ BEANS --------------------------
    
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
    
    
    @Bean
    public ImportDateTimeFormatter scjImportDateTimeFormatter() {
        ImportDateTimeFormatter importDateTimeFormatter = new ImportDateTimeFormatter();
        importDateTimeFormatter.setImportDatePattern("yyyy-MM-dd HH:mm");
        return importDateTimeFormatter;
    }
    
    
    @Bean
    public JudgmentConverter<SupremeCourtJudgment, SourceScJudgment> sourceScJudgmentConverter() {
        JudgmentConverterImpl<SupremeCourtJudgment, SourceScJudgment> judgmentConverter = new JudgmentConverterImpl<>();
        judgmentConverter.setJudgmentDataExtractor(sourceScJudgmentExtractor);
        return judgmentConverter;
    }
    
    
    @Bean
    public JudgmentOverwriter<SupremeCourtJudgment> scJudgmentOverwriter() {
        DelegatingJudgmentOverwriter<SupremeCourtJudgment> scJudgmentOverwriter = new DelegatingJudgmentOverwriter<>();
        scJudgmentOverwriter.setSpecificJudgmentOverwriter(scSpecificJudgmentOverwriter);
        return scJudgmentOverwriter;
    }
    
    
    //------------------------ POST_CONSTRUCT --------------------------
    
    @PostConstruct
    public void postConstruct() {
        DateTimeDeserializer.setScjImportDateTimeFormatter(scjImportDateTimeFormatter());
    }
    
  

    
}
