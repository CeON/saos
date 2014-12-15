package pl.edu.icm.saos.common.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author Łukasz Dumiszewski
 */

public class JsonNormalizer {

    private static final ObjectMapper singleQuotedMapper = new ObjectMapper();
    
    static {
        singleQuotedMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        singleQuotedMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        singleQuotedMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    private JsonNormalizer()  {
        throw new IllegalStateException("may not be instantiated");
    }
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Changes passed json into a standard double quoted one: replaces single quotes with double-quotes, adds
     * double quotes for field names that are missing them, removes whitespaces and comments. <br/>
     * Note: the element values of the passed json have to be in single or double quotes.<br/>
     * <br/>
     * E.g. <br/>
     * {bre:'sss', arr:['1112'\n , 'abc']} into {"bre":"sss","arr":["1112","abc"]} <br/>
     * Or <br/>
     * {'bre':'sss', 'arr':['1112', 'abc']} into {"bre":"sss","arr":["1112","abc"]} <br/>
     * 
     *  
     */
    public static String normalizeJson(String json) throws IllegalArgumentException {
        
        JsonNode df = null;
        
        try {
            
            df = singleQuotedMapper.readValue(json, JsonNode.class);
            
        } catch (IOException e) {
        
            throw new IllegalArgumentException(e);
        
        }
        
        return df.toString();
    }
    
}
