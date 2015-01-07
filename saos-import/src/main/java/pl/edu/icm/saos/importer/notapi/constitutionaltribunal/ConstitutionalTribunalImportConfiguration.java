package pl.edu.icm.saos.importer.notapi.constitutionaltribunal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverter;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverterImpl;
import pl.edu.icm.saos.importer.common.overwriter.DelegatingJudgmentOverwriter;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadReader;
import pl.edu.icm.saos.importer.notapi.common.JsonJudgmentImportProcessProcessor;
import pl.edu.icm.saos.importer.notapi.common.JudgmentImportProcessReader;
import pl.edu.icm.saos.importer.notapi.common.NotApiImportDownloadStepExecutionListener;
import pl.edu.icm.saos.importer.notapi.common.StringItemImportDownloadProcessor;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.download.RawSourceCtJudgmentParser;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process.SourceCtJudgmentExtractor;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;

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
    private RawSourceCtJudgmentParser rawSourceCtJudgmentParser;
    
    @Autowired
    private SourceCtJudgmentExtractor sourceCtJudgmentExtractor;
    
    @Autowired 
    private JudgmentOverwriter<ConstitutionalTribunalJudgment> ctSpecificJudgmentOverwriter;
    
    
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
        
        return ctjImportDownloadReader;
    }
    
    @Bean
    public StringItemImportDownloadProcessor<RawSourceCtJudgment> ctjImportDownloadProcessor() {
        StringItemImportDownloadProcessor<RawSourceCtJudgment> ctjImportDownloadProcessor = new StringItemImportDownloadProcessor<RawSourceCtJudgment>();
        ctjImportDownloadProcessor.setRawSourceJudgmentParser(rawSourceCtJudgmentParser);
        
        return ctjImportDownloadProcessor;
    }
    
    @Bean
    public NotApiImportDownloadStepExecutionListener ctjImportDownloadStepExecutionListener() {
        NotApiImportDownloadStepExecutionListener stepExecutionListener = new NotApiImportDownloadStepExecutionListener();
        stepExecutionListener.setRawJudgmentClass(RawSourceCtJudgment.class);
        
        return stepExecutionListener;
    }
    
    @Bean
    public JudgmentConverter<ConstitutionalTribunalJudgment, SourceCtJudgment> sourceCtJudgmentConverter() {
        JudgmentConverterImpl<ConstitutionalTribunalJudgment, SourceCtJudgment> judgmentConverter = new JudgmentConverterImpl<>();
        judgmentConverter.setJudgmentDataExtractor(sourceCtJudgmentExtractor);
        return judgmentConverter;
    }
    
    
    @Bean
    public JudgmentOverwriter<ConstitutionalTribunalJudgment> ctJudgmentOverwriter() {
        DelegatingJudgmentOverwriter<ConstitutionalTribunalJudgment> ctJudgmentOverwriter = new DelegatingJudgmentOverwriter<>();
        ctJudgmentOverwriter.setSpecificJudgmentOverwriter(ctSpecificJudgmentOverwriter);
        return ctJudgmentOverwriter;
    }



    @Bean
    public JudgmentImportProcessReader<RawSourceCtJudgment> ctjImportProcessReader() {
        JudgmentImportProcessReader<RawSourceCtJudgment> importProcessReader = new JudgmentImportProcessReader<>(RawSourceCtJudgment.class);
        return importProcessReader;
    }
    
    @Bean
    public JsonJudgmentImportProcessProcessor<SourceCtJudgment, ConstitutionalTribunalJudgment> scjImportProcessProcessor() {
        JsonJudgmentImportProcessProcessor<SourceCtJudgment, ConstitutionalTribunalJudgment> ctjImportProcessProcessor = new JsonJudgmentImportProcessProcessor<>(ConstitutionalTribunalJudgment.class);
        ctjImportProcessProcessor.setSourceJudgmentParser(sourceCtJudgmentParser());
        ctjImportProcessProcessor.setSourceJudgmentConverter(sourceCtJudgmentConverter());
        ctjImportProcessProcessor.setJudgmentOverwriter(ctJudgmentOverwriter());
        
        return ctjImportProcessProcessor;
    }
}
