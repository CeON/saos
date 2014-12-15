package pl.edu.icm.saos.common.service;

import org.apache.commons.lang3.StringUtils;

public final class ServiceResponseFactory {

    private ServiceResponseFactory() {throw new IllegalStateException("not to instantiate");}
    
    
    
    //------------------------ LOGIC --------------------------
    
    /** 
     * Creates {@link ServiceResponse} with {@link ServiceResponse#setStatus(ServiceExecutionStatus)} set to {@link ServiceExecutionStatus#ERROR}
     * and {@link ServiceResponse#setMessage(String)} set to 'mainMessage: details'
     * */
    public static ServiceResponse createErrorResponse(String mainMessage, String details) {
        ServiceResponse response = new ServiceResponse();
        response.setStatus(ServiceExecutionStatus.ERROR);
        response.setMessage(formatMessage(mainMessage, details));
        return response;
    }


    /** 
     * Creates {@link ServiceResponse} with {@link ServiceResponse#setStatus(ServiceExecutionStatus)} set to {@link ServiceExecutionStatus#OK}
     * and {@link ServiceResponse#setMessage(String)} set to 'mainMessage: details'
     * */
    public static ServiceResponse createOkResponse(String mainMessage, String details) {
        ServiceResponse response = new ServiceResponse();
        response.setStatus(ServiceExecutionStatus.OK);
        response.setMessage(formatMessage(mainMessage, details));
        return response;
    }
    
    
    
    //------------------------ PRIVATE --------------------------
    
    private static String formatMessage(String mainMessage, String details) {
        mainMessage = StringUtils.defaultIfBlank(mainMessage, "");
        details = StringUtils.defaultIfBlank(details, "");
        return mainMessage + (details.equals("")?"":": "+details);
    }
    
}
