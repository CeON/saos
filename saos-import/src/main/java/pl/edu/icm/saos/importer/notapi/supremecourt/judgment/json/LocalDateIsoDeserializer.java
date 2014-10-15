package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json;

import java.io.IOException;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

/**
 * 
 * @author Łukasz Dumiszewski
 */

public class LocalDateIsoDeserializer extends StdScalarDeserializer<LocalDate> {

    
    private static final long serialVersionUID = 1L;

    
    
    protected LocalDateIsoDeserializer() {
        super(LocalDate.class);
    }

    
    /**
     * Deserializes {@link JsonParser#getText()} into {@link LocalDate} using iso format yyyy-MM-dd
     */
    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String str = jp.getText().trim();
        if (str.length() == 0) {
            return null;
        }
        return LocalDate.parse(str);
        
    }

}
