package pl.edu.icm.saos.common.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

/**
 * @author Łukasz Dumiszewski
 */
public class JsonUtilService {

    /**
     * Invokes {@link JsonUtils#nextNode(JsonParser)}
     */
    public String nextNode(JsonParser jp) throws JsonParseException, IOException, IllegalStateException {
        return JsonUtils.nextNode(jp);
    }
    
}
