package pl.edu.icm.saos.api.services.representations;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ErrorData details representation
 * @author pavtel
 */
public class ErrorData {

    @JsonProperty("httpStatus")
    public String httpStatus;

    @JsonProperty("reason")
    public String reason;

    @JsonProperty("propertyName")
    public String propertyName;

    @JsonProperty("message")
    public String message;
}
