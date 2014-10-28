package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.importer.common.ImportException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;

/**
 * @author Łukasz Dumiszewski
 */
@Service("sourceScJudgmentParser")
public class SourceScJudgmentParser {

    private JsonFactory jsonFactory;
    
    private CommonValidator commonValidator;
    
    /**
     * Parses json content into SourceScJudgment <br/> 
     * Allows the json content to have comments - see: {@link Feature#ALLOW_COMMENTS}
     * 
     */
    public SourceScJudgment parse(String jsonContent) throws ImportException {
        try {
            JsonParser jsonParser = jsonFactory.createParser(jsonContent);
            jsonParser.enable(Feature.ALLOW_COMMENTS);
            SourceScJudgment scJudgment = jsonParser.readValueAs(SourceScJudgment.class);
            commonValidator.validateEx(scJudgment);
            
            return scJudgment;
        } catch (IOException e) {
            throw new ImportException(e);
        }
    }
    
    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setJsonFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }
    
    @Autowired
    public void setCommonValidator(CommonValidator commonValidator) {
        this.commonValidator = commonValidator;
    }

    
}
