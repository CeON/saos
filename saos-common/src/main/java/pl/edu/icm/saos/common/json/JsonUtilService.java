package pl.edu.icm.saos.common.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

/**
 * @author Łukasz Dumiszewski
 */
public class JsonUtilService {

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Invokes {@link JsonUtils#nextNode(JsonParser)}
     */
    public String nextNode(JsonParser jp) throws IOException {
        return JsonUtils.nextNode(jp);
    }
    
}
