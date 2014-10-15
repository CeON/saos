package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.JudgmentConverter;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgmentParser;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scjImportProcessProcessor")
public class ScjImportProcessProcessor implements ItemProcessor<RawSourceScJudgment, SupremeCourtJudgment> {

    
    private SourceScJudgmentParser sourceScJudgmentParser;
    
    private JudgmentConverter<SupremeCourtJudgment, SourceScJudgment> sourceScJudgmentConverter;
    
    
    @Override
    public SupremeCourtJudgment process(RawSourceScJudgment rJudgment) {
        
        SourceScJudgment sourceScJudgment = sourceScJudgmentParser.parse(rJudgment.getJsonContent());
        
        SupremeCourtJudgment scJudgment = sourceScJudgmentConverter.convertJudgment(sourceScJudgment);
        
        return scJudgment;
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

}
