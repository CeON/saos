package pl.edu.icm.saos.importer.notapi.constitutionaltribunal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.importer.notapi.common.ImportFileUtils;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadReader;
import pl.edu.icm.saos.importer.notapi.common.JsonUtilService;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment;

import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author madryk
 */
@Configuration
public class ConstitutionalTribunalImportConfiguration {
    
    @Autowired
    private CommonValidator commonValidator;
    
    @Autowired
    private MappingJsonFactory jsonFactory;
    
    @Autowired
    private ImportFileUtils importFileUtils;
    
    @Autowired
    private JsonUtilService jsonUtilService;
    
    
    //------------------------ BEANS --------------------------
    
    @Bean
    public JsonItemParser<SourceCtJudgment> sourceCtJudgmentParser() {
        JsonItemParser<SourceCtJudgment> sourceCtJudgmentParser = new JsonItemParser<>(SourceCtJudgment.class);
        sourceCtJudgmentParser.setCommonValidator(commonValidator);
        sourceCtJudgmentParser.setJsonFactory(jsonFactory);
        return sourceCtJudgmentParser;
    }
    
    @Bean
    public JsonImportDownloadReader ctjImportDownloadReader(@Value("${import.judgments.constitutionalTribunal.dir}") String importDir) {
        JsonImportDownloadReader ctjImportDownloadReader = new JsonImportDownloadReader();
        ctjImportDownloadReader.setImportDir(importDir);
        ctjImportDownloadReader.setImportFileUtils(importFileUtils);
        ctjImportDownloadReader.setJsonFactory(jsonFactory);
        ctjImportDownloadReader.setJsonUtilService(jsonUtilService);
        
        return ctjImportDownloadReader;
    }
}
