package pl.edu.icm.saos.importer.notapi.common;

import java.io.IOException;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.json.JsonUtils;

import com.fasterxml.jackson.core.JsonParser;

/**
 * @author Łukasz Dumiszewski
 */
@Service("jsonUtilService")
public class JsonUtilService {

    /**
     * Invokes {@link JsonUtils#nextNode(JsonParser)}
     */
    public String nextNode(JsonParser jp) throws IOException {
        return JsonUtils.nextNode(jp);
    }
    
}
