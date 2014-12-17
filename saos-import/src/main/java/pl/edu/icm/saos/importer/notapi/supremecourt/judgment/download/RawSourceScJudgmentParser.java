package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.importer.notapi.common.RawSourceJudgmentParser;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Service("rawSourceScJudgmentParser")
public class RawSourceScJudgmentParser implements RawSourceJudgmentParser<RawSourceScJudgment> {

    
    private JsonItemParser<SourceScJudgment> sourceScJudgmentParser;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public RawSourceScJudgment parseRawSourceJudgment(String jsonContent) {
        RawSourceScJudgment judgment = new RawSourceScJudgment();
        judgment.setJsonContent(jsonContent);
        SourceScJudgment sourceScJudgment = sourceScJudgmentParser.parse(jsonContent);
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
