package pl.edu.icm.saos.api.response.representations;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Error's view representation.
 * @author pavtel
 */
public class ErrorRepresentation {

    private static class CS {
        public static final String ERROR_KEY = "error";
    }

    private Map<String, Object> representation = new LinkedHashMap<String, Object>();

    public ErrorRepresentation(Builder builder) {
        representation.put(CS.ERROR_KEY, builder.errorData);
    }

    private Map<String, Object> getRepresentation() {
        return representation;
    }

    public static class Builder {
        private ErrorData errorData = new ErrorData();

        public Builder status(String status){
            errorData.status = status;
            return this;
        }

        public Builder code(String code){
            errorData.code = code;
            return this;
        }

        public Builder property(String property){
            errorData.property = property;
            return this;
        }

        public Builder message(String message){
            errorData.message=message;
            return this;
        }


        public Map<String, Object> build(){
            ErrorRepresentation errorRepresentation = new ErrorRepresentation(this);
            return errorRepresentation.getRepresentation();
        }
    }
}
