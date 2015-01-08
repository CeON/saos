package pl.edu.icm.saos.common.service;


public final class ServiceResponseFactory {

    private ServiceResponseFactory() {throw new IllegalStateException("not to instantiate");}
    
    
    
    //------------------------ LOGIC --------------------------
    
    /** 
     * Creates {@link ServiceResponse} with {@link ServiceResponse#setStatus(ServiceExecutionStatus)} set to {@link ServiceExecutionStatus#ERROR}
     * and {@link ServiceResponse#setMessage(String)} set to message and {@link ServiceResponse#setDetails(String)}
     * set to details 
     * */
    public static ServiceResponse createErrorResponse(String message, String details) {
        ServiceResponse response = new ServiceResponse();
        response.setStatus(ServiceExecutionStatus.ERROR);
        response.setMessage(message);
        response.setDetails(details);
        return response;
    }


    /** 
     * Creates {@link ServiceResponse} with {@link ServiceResponse#setStatus(ServiceExecutionStatus)} set to {@link ServiceExecutionStatus#OK}
     * and {@link ServiceResponse#setMessage(String)} set to message and {@link ServiceResponse#setDetails(String)}
     * set to details 
     * */
    public static ServiceResponse createOkResponse(String mainMessage, String details) {
        ServiceResponse response = new ServiceResponse();
        response.setStatus(ServiceExecutionStatus.OK);
        response.setMessage(mainMessage);
        response.setDetails(details);
        return response;
    }
    
    
    
}
