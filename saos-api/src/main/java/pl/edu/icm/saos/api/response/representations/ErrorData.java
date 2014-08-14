package pl.edu.icm.saos.api.response.representations;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author pavtel
 */
public class ErrorData {

    @JsonProperty("status")
    public String status;

    @JsonProperty("code")
    public String code;

    @JsonProperty("property")
    public String property;

    @JsonProperty("message")
    public String message;

    @JsonProperty("developerMessage")
    public String developerMessage;

    @JsonProperty("moreInfo")
    public String moreInfo;
}
