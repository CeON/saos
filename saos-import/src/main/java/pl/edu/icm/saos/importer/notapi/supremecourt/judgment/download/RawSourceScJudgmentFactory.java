package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * @author Łukasz Dumiszewski
 */
@Service("rawSourceScJudgmentFactory")
class RawSourceScJudgmentFactory {

    
    private JsonItemParser<SourceScJudgment> sourceScJudgmentParser;
    
    
    
    //------------------------ LOGIC --------------------------
    
    public RawSourceScJudgment createRawSourceScJudgment(String jsonContent) throws JsonParseException {
        RawSourceScJudgment judgment = new RawSourceScJudgment();
        judgment.setJsonContent(jsonContent);
        SourceScJudgment sourceScJudgment = sourceScJudgmentParser.parseAndValidate(jsonContent);
        judgment.setMultiChambers(sourceScJudgment.getSupremeCourtChambers().size()>1);
        judgment.setSourceId(sourceScJudgment.getSource().getSourceJudgmentId());
        return judgment;
    }


    
    //------------------------ PRIVATE --------------------------
    
    @Autowired
    public void setSourceScJudgmentParser(JsonItemParser<SourceScJudgment> sourceScJudgmentParser) {
        this.sourceScJudgmentParser = sourceScJudgmentParser;
    }
    
}
