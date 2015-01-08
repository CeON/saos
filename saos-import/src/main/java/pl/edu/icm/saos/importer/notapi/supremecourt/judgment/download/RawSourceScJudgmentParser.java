package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.importer.notapi.common.RawSourceJudgmentParser;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * @author Łukasz Dumiszewski
 */
@Service("rawSourceScJudgmentParser")
public class RawSourceScJudgmentParser implements RawSourceJudgmentParser<RawSourceScJudgment> {

    
    private JsonStringParser<SourceScJudgment> sourceScJudgmentParser;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public RawSourceScJudgment parseRawSourceJudgment(String jsonContent) throws JsonParseException {
        RawSourceScJudgment judgment = new RawSourceScJudgment();
        judgment.setJsonContent(jsonContent);
        SourceScJudgment sourceScJudgment = sourceScJudgmentParser.parseAndValidate(jsonContent);
        judgment.setSourceId(sourceScJudgment.getSource().getSourceJudgmentId());
        return judgment;
    }


    
    //------------------------ PRIVATE --------------------------
    
    @Autowired
    public void setSourceScJudgmentParser(JsonStringParser<SourceScJudgment> sourceScJudgmentParser) {
        this.sourceScJudgmentParser = sourceScJudgmentParser;
    }
    
}
