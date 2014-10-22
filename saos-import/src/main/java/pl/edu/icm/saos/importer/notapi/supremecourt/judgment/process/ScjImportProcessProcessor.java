package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.JudgmentConverter;
import pl.edu.icm.saos.importer.common.JudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgmentParser;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceScJudgmentRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scjImportProcessProcessor")
public class ScjImportProcessProcessor implements ItemProcessor<RawSourceScJudgment, SupremeCourtJudgment> {

    private Logger log = LoggerFactory.getLogger(ScjImportProcessProcessor.class);
    
    
    private SourceScJudgmentParser sourceScJudgmentParser;
    
    private JudgmentConverter<SupremeCourtJudgment, SourceScJudgment> sourceScJudgmentConverter;
    
    private ScJudgmentRepository scJudgmentRepository;
    
    private JudgmentOverwriter<SupremeCourtJudgment> judgmentOverwriter;
    
    private RawSourceScJudgmentRepository rawSourceScJudgmentRepository;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public SupremeCourtJudgment process(RawSourceScJudgment rJudgment) {
        
        log.debug("Processing: {}", rJudgment);

        markProcessed(rJudgment);
        
        
        SourceScJudgment sourceScJudgment = sourceScJudgmentParser.parse(rJudgment.getJsonContent());
        
        SupremeCourtJudgment scJudgment = sourceScJudgmentConverter.convertJudgment(sourceScJudgment);
        

        SupremeCourtJudgment oldScJudgment = scJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, scJudgment.getSourceInfo().getSourceJudgmentId());
        
        if (oldScJudgment != null) {
            
            log.debug("same found (rJudgmentId:{}, judgmentId: {}), updating...", rJudgment.getId(), oldScJudgment.getId());
            
            judgmentOverwriter.overwriteJudgment(oldScJudgment, scJudgment);
            
            return oldScJudgment;
            
        }
        
        return scJudgment;
    }


    
    //------------------------ PRIVATE --------------------------
    
    
    private void markProcessed(RawSourceScJudgment rJudgment) {
        rJudgment.markProcessed();
        rawSourceScJudgmentRepository.saveAndFlush(rJudgment);
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setSourceScJudgmentParser(SourceScJudgmentParser sourceScJudgmentParser) {
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
