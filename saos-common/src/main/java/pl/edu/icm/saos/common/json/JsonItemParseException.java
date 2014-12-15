package pl.edu.icm.saos.common.json;

/**
 * @author Łukasz Dumiszewski
 */

public class JsonItemParseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String jsonContent;

    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JsonItemParseException(String jsonContent, String message, Throwable cause) {
        super(message, cause);
        this.jsonContent = jsonContent;
    }

    public JsonItemParseException(String jsonContent, String message) {
        super(message);
        this.jsonContent = jsonContent;
    }

        
    //------------------------ GETTERS --------------------------
    
    /**
     * Json content of which parsing caused the exception 
     */
    public String getJsonContent() {
        return jsonContent;
    }


    
        
    
}
