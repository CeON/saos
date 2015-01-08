package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.download;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.importer.notapi.common.RawSourceJudgmentParser;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * Creates {@link RawSourceCtJudgment} from json
 * 
 * @author madryk
 */
@Service
public class RawSourceCtJudgmentParser implements RawSourceJudgmentParser<RawSourceCtJudgment> {

    @Autowired
    private JsonStringParser<SourceCtJudgment> sourceCtJudgmentParser;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public RawSourceCtJudgment parseRawSourceJudgment(String jsonContent) throws JsonParseException {
        RawSourceCtJudgment rawJudgment = new RawSourceCtJudgment();
        rawJudgment.setJsonContent(jsonContent);
        SourceCtJudgment sourceCtJudgment = sourceCtJudgmentParser.parseAndValidate(jsonContent);
        rawJudgment.setSourceId(sourceCtJudgment.getSource().getSourceJudgmentId());
        return rawJudgment;
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setSourceCtJudgmentParser(
            JsonStringParser<SourceCtJudgment> sourceCtJudgmentParser) {
        this.sourceCtJudgmentParser = sourceCtJudgmentParser;
    }
}
