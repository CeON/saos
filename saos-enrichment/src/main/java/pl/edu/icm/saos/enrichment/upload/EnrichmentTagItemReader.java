package pl.edu.icm.saos.enrichment.upload;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.common.json.JsonUtilService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

/**
 * @author Łukasz Dumiszewski
 */
@Service("enrichmentTagItemReader")
public class EnrichmentTagItemReader {

    
    private JsonItemParser<EnrichmentTagItem> enrichmentTagItemParser;
    
    private JsonUtilService jsonUtilService;
    
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Reads next json object from the given jsonParser by using {@link JsonUtilService#nextNode(JsonParser)}
     * and parses it to {@link EnrichmentTagItem}
     * @throws IOException if some I/O error occurred
     * @throws JsonParseException if the next json token cannot be parsed to EnrichmentTagItem
     * @throws ValidationException if the next EnrichmentTagItem is not valid
     */
    public EnrichmentTagItem nextEnrichmentTagItem(JsonParser jsonParser) throws IOException, JsonParseException {
     
        String nextNode = jsonUtilService.nextNode(jsonParser);
    
        if (nextNode == null) {
            
            return null;
        
        }
        
        return enrichmentTagItemParser.parseAndValidate(nextNode);
        
        
    }

    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setEnrichmentTagItemParser(JsonItemParser<EnrichmentTagItem> enrichmentTagItemParser) {
        this.enrichmentTagItemParser = enrichmentTagItemParser;
    }


    @Autowired
    public void setJsonUtilService(JsonUtilService jsonUtilService) {
        this.jsonUtilService = jsonUtilService;
    }
    
    
}
