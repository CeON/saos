package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverter;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceScJudgmentRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scjImportProcessProcessor")
public class ScjImportProcessProcessor implements ItemProcessor<RawSourceScJudgment, JudgmentWithCorrectionList<SupremeCourtJudgment>> {

    private Logger log = LoggerFactory.getLogger(ScjImportProcessProcessor.class);
    
    
    private JsonItemParser<SourceScJudgment> sourceScJudgmentParser;
    
    private JudgmentConverter<SupremeCourtJudgment, SourceScJudgment> sourceScJudgmentConverter;
    
    private ScJudgmentRepository scJudgmentRepository;
    
    private JudgmentOverwriter<SupremeCourtJudgment> judgmentOverwriter;
    
    private RawSourceScJudgmentRepository rawSourceScJudgmentRepository;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public JudgmentWithCorrectionList<SupremeCourtJudgment> process(RawSourceScJudgment rJudgment) {
        
        log.trace("Processing: rawSourceScJudgment id={}", rJudgment.getId());

        
        SourceScJudgment sourceScJudgment = sourceScJudgmentParser.parse(rJudgment.getJsonContent());
        
        JudgmentWithCorrectionList<SupremeCourtJudgment> judgmentWithCorrectionList = sourceScJudgmentConverter.convertJudgment(sourceScJudgment);
        
        
        SupremeCourtJudgment scJudgment = judgmentWithCorrectionList.getJudgment();  

        SupremeCourtJudgment oldScJudgment = scJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, scJudgment.getSourceInfo().getSourceJudgmentId());
        
        if (oldScJudgment != null) {
            
            log.trace("same found (rJudgmentId:{}, judgmentId: {}), updating...", rJudgment.getId(), oldScJudgment.getId());
            
            judgmentOverwriter.overwriteJudgment(oldScJudgment, scJudgment, judgmentWithCorrectionList.getCorrectionList());
            
            judgmentWithCorrectionList.setJudgment(oldScJudgment);
            
        }
        
        
        markProcessed(rJudgment);
        
        return judgmentWithCorrectionList;
    }


    
    //------------------------ PRIVATE --------------------------
    
    
    private void markProcessed(RawSourceScJudgment rJudgment) {
        rJudgment.markProcessed();
        rawSourceScJudgmentRepository.save(rJudgment);
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setSourceScJudgmentParser(JsonItemParser<SourceScJudgment> sourceScJudgmentParser) {
        this.sourceScJudgmentParser = sourceScJudgmentParser;
    }

    @Autowired
    public void setSourceScJudgmentConverter(JudgmentConverter<SupremeCourtJudgment, SourceScJudgment> sourceScJudgmentConverter) {
        this.sourceScJudgmentConverter = sourceScJudgmentConverter;
    }

    @Autowired
    public void setScJudgmentRepository(ScJudgmentRepository scJudgmentRepository) {
        this.scJudgmentRepository = scJudgmentRepository;
    }

    @Autowired
    @Qualifier("scJudgmentOverwriter")
    public void setJudgmentOverwriter(JudgmentOverwriter<SupremeCourtJudgment> judgmentOverwriter) {
        this.judgmentOverwriter = judgmentOverwriter;
    }

    @Autowired
    public void setRawSourceScJudgmentRepository(RawSourceScJudgmentRepository rawSourceScJudgmentRepository) {
        this.rawSourceScJudgmentRepository = rawSourceScJudgmentRepository;
    }

}
