package pl.edu.icm.saos.importer.notapi.common;

import java.io.IOException;

import org.joda.time.DateTime;

import pl.edu.icm.saos.importer.common.ImportDateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

/**
 * @author Łukasz Dumiszewski
 */

public class DateTimeDeserializer extends StdScalarDeserializer<DateTime> {

    
    private static final long serialVersionUID = 1L;

    private static ImportDateTimeFormatter importDateTimeFormatter;
    
    
    protected DateTimeDeserializer() {
        super(DateTime.class);
    }

    
    /**
     * Deserializes {@link JsonParser#getText()} into {@link DateTime} using {{@link #setImportDateTimeFormatter(ImportDateTimeFormatter)}
     */
    @Override
    public DateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String str = jp.getText().trim();
        if (str.length() == 0) {
            return null;
        }
        return importDateTimeFormatter.parse(str);
    }


    //------------------------ SETTERS --------------------------
    
    public static void setImportDateTimeFormatter(ImportDateTimeFormatter scjImportDateTimeFormatter) {
        DateTimeDeserializer.importDateTimeFormatter = scjImportDateTimeFormatter;
    }

}