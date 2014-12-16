package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.download;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.importer.notapi.common.RawSourceJudgmentFactory;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;

/**
 * Creates {@link RawSourceCtJudgment} from json
 * 
 * @author madryk
 */
@Service
public class RawSourceCtJudgmentFactory implements RawSourceJudgmentFactory<RawSourceCtJudgment> {

    @Autowired
    private JsonItemParser<SourceCtJudgment> sourceCtJudgmentParser;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public RawSourceCtJudgment createRawSourceJudgment(String jsonContent) {
        RawSourceCtJudgment rawJudgment = new RawSourceCtJudgment();
        rawJudgment.setJsonContent(jsonContent);
        SourceCtJudgment sourceCtJudgment = sourceCtJudgmentParser.parse(jsonContent);
        rawJudgment.setSourceId(sourceCtJudgment.getSource().getSourceJudgmentId());
        return rawJudgment;
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setSourceCtJudgmentParser(
            JsonItemParser<SourceCtJudgment> sourceCtJudgmentParser) {
        this.sourceCtJudgmentParser = sourceCtJudgmentParser;
    }
}
