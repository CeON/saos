package pl.edu.icm.saos.common.json;


/**
 * A runtime exception thrown by {@link JsonItemParser#parse(String)}
 * 
 * @author Łukasz Dumiszewski
 */

public class JsonItemParseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JsonItemParseException(Throwable cause) {
        super(cause);
    }

    
}
