package pl.edu.icm.saos.api.response.representations;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ErrorData details representation
 * @author pavtel
 */
public class ErrorData {

    @JsonProperty("httpStatus")
    public String httpStatus;

    @JsonProperty("name")
    public String name;

    @JsonProperty("propertyName")
    public String propertyName;

    @JsonProperty("message")
    public String message;

    @JsonProperty("moreInfo")
    public String moreInfo;
}
