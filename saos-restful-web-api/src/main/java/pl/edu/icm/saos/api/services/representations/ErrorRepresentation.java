package pl.edu.icm.saos.api.services.representations;


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

    private Map<String, Object> representation = new LinkedHashMap<>();

    public ErrorRepresentation(Builder builder) {
        representation.put(CS.ERROR_KEY, builder.errorData);
    }

    private Map<String, Object> getRepresentation() {
        return representation;
    }

    public static class Builder {
        private ErrorData errorData = new ErrorData();

        public Builder httpStatus(String httpStatus){
            errorData.httpStatus = httpStatus;
            return this;
        }

        public Builder reason(String reason){
            errorData.reason = reason;
            return this;
        }

        public Builder propertyName(String propertyName){
            errorData.propertyName = propertyName;
            return this;
        }

        public Builder message(String message){
            errorData.message=message;
            return this;
        }

        public Builder moreInfo(String moreInfo){
            errorData.moreInfo = moreInfo;
            return this;
        }


        public Map<String, Object> build(){
            ErrorRepresentation errorRepresentation = new ErrorRepresentation(this);
            return errorRepresentation.getRepresentation();
        }
    }
}
