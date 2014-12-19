package pl.edu.icm.saos.importer.notapi.supremecourt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverter;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverterImpl;
import pl.edu.icm.saos.importer.common.overwriter.DelegatingJudgmentOverwriter;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.common.JudgmentImportProcessReader;
import pl.edu.icm.saos.importer.notapi.common.NotApiJudgmentImportProcessProcessor;
import pl.edu.icm.saos.importer.notapi.common.StringItemImportDownloadProcessor;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadReader;
import pl.edu.icm.saos.importer.notapi.common.NotApiImportDownloadStepExecutionListener;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download.RawSourceScJudgmentParser;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process.SourceScJudgmentExtractor;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * 
 * @author Łukasz Dumiszewski
 */
@Configuration
public class SupremeCourtImportConfiguration {

    
    
    @Autowired
    private SourceScJudgmentExtractor sourceScJudgmentExtractor;
    
    @Autowired 
    private CommonValidator commonValidator;
    
    @Autowired
    private MappingJsonFactory jsonFactory;
    
    @Autowired
    private RawSourceScJudgmentParser rawSourceScJudgmentParser;
    
    
    @Autowired 
    @Qualifier("scSpecificJudgmentOverwriter")
    private JudgmentOverwriter<SupremeCourtJudgment> scSpecificJudgmentOverwriter;
    
    
    
    //------------------------ BEANS --------------------------
    

    
    

    
    @Bean
    public JsonItemParser<SourceScJudgment> sourceScJudgmentParser() {
        JsonItemParser<SourceScJudgment> sourceScJudgmentParser = new JsonItemParser<>(SourceScJudgment.class);
        sourceScJudgmentParser.setCommonValidator(commonValidator);
        sourceScJudgmentParser.setJsonFactory(jsonFactory);
        return sourceScJudgmentParser;
    }
    
    @Bean
    public JsonImportDownloadReader scjImportDownloadReader(@Value("${import.judgments.supremeCourt.dir}") String importDir) {
        JsonImportDownloadReader scjImportDownloadReader = new JsonImportDownloadReader();
        scjImportDownloadReader.setImportDir(importDir);
        
        return scjImportDownloadReader;
    }
    
    @Bean
    public StringItemImportDownloadProcessor<RawSourceScJudgment> scjImportDownloadProcessor() {
        StringItemImportDownloadProcessor<RawSourceScJudgment> scjImportDownloadProcessor = new StringItemImportDownloadProcessor<RawSourceScJudgment>();
        scjImportDownloadProcessor.setRawSourceJudgmentParser(rawSourceScJudgmentParser);
        
        return scjImportDownloadProcessor;
    }
    
    @Bean
    public NotApiImportDownloadStepExecutionListener scjImportDownloadStepExecutionListener() {
        NotApiImportDownloadStepExecutionListener stepExecutionListener = new NotApiImportDownloadStepExecutionListener();
        stepExecutionListener.setRawJudgmentClass(RawSourceScJudgment.class);
        
        return stepExecutionListener;
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

  

    @Bean
    public JudgmentImportProcessReader<RawSourceScJudgment> scjImportProcessReader() {
        JudgmentImportProcessReader<RawSourceScJudgment> importProcessReader = new JudgmentImportProcessReader<RawSourceScJudgment>(RawSourceScJudgment.class);
        return importProcessReader;
    }
    
    @Bean
    public NotApiJudgmentImportProcessProcessor<SourceScJudgment, SupremeCourtJudgment> scjImportProcessProcessor() {
        NotApiJudgmentImportProcessProcessor<SourceScJudgment, SupremeCourtJudgment> scjImportProcessProcessor = new NotApiJudgmentImportProcessProcessor<>(SupremeCourtJudgment.class);
        scjImportProcessProcessor.setSourceJudgmentParser(sourceScJudgmentParser());
        scjImportProcessProcessor.setSourceJudgmentConverter(sourceScJudgmentConverter());
        scjImportProcessProcessor.setJudgmentOverwriter(scJudgmentOverwriter());
        
        return scjImportProcessProcessor;
    }
    
}
