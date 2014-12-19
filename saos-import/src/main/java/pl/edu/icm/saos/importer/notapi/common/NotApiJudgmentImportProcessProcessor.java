package pl.edu.icm.saos.importer.notapi.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverter;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.JsonRawSourceJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.RawSourceJudgmentRepository;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * Spring batch import - process - processor for json based raw judgments
 * 
 * @author Łukasz Dumiszewski
 * @param <S> - model class for json objects
 * @param <J> - type of target judgment 
 */
public class NotApiJudgmentImportProcessProcessor<S, J extends Judgment> implements ItemProcessor<JsonRawSourceJudgment, JudgmentWithCorrectionList<J>> {

    private Logger log = LoggerFactory.getLogger(NotApiJudgmentImportProcessProcessor.class);
    
    
    private JsonItemParser<S> sourceJudgmentParser;
    
    private JudgmentConverter<J, S> sourceJudgmentConverter;
    
    private JudgmentRepository judgmentRepository;
    
    private JudgmentOverwriter<J> judgmentOverwriter;
    
    private RawSourceJudgmentRepository rawSourceJudgmentRepository;
    
    
    private Class<J> judgmentClass;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public NotApiJudgmentImportProcessProcessor(Class<J> judgmentClass) {
        this.judgmentClass = judgmentClass;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public JudgmentWithCorrectionList<J> process(JsonRawSourceJudgment rJudgment) throws JsonParseException {
        
        log.trace("Processing: {} id={}", rJudgment.getClass().getName(), rJudgment.getId());

        
        S sourceJudgment = sourceJudgmentParser.parseAndValidate(rJudgment.getJsonContent());
        
        JudgmentWithCorrectionList<J> judgmentWithCorrectionList = sourceJudgmentConverter.convertJudgment(sourceJudgment);
        
        
        J judgment = judgmentWithCorrectionList.getJudgment();

        J oldJudgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                judgment.getSourceInfo().getSourceCode(),
                judgment.getSourceInfo().getSourceJudgmentId(),
                judgmentClass);
        
        if (oldJudgment != null) {
            
            log.trace("same found (rJudgmentId:{}, judgmentId: {}), updating...", rJudgment.getId(), oldJudgment.getId());
            
            judgmentOverwriter.overwriteJudgment(oldJudgment, judgment, judgmentWithCorrectionList.getCorrectionList());
            
            judgmentWithCorrectionList.setJudgment(oldJudgment);
            
        }
        
        
        markProcessed(rJudgment);
        
        return judgmentWithCorrectionList;
    }


    
    //------------------------ PRIVATE --------------------------
    
    
    private void markProcessed(JsonRawSourceJudgment rJudgment) {
        rJudgment.markProcessed();
        rawSourceJudgmentRepository.save(rJudgment);
    }


    //------------------------ SETTERS --------------------------
    
    public void setSourceJudgmentParser(JsonItemParser<S> sourceJudgmentParser) {
        this.sourceJudgmentParser = sourceJudgmentParser;
    }

    public void setSourceJudgmentConverter(JudgmentConverter<J, S> sourceJudgmentConverter) {
        this.sourceJudgmentConverter = sourceJudgmentConverter;
    }

    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    public void setJudgmentOverwriter(JudgmentOverwriter<J> judgmentOverwriter) {
        this.judgmentOverwriter = judgmentOverwriter;
    }

    @Autowired
    public void setRawSourceJudgmentRepository(RawSourceJudgmentRepository rawSourceJudgmentRepository) {
        this.rawSourceJudgmentRepository = rawSourceJudgmentRepository;
    }

}
