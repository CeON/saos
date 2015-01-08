package pl.edu.icm.saos.common.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class ServiceResponseFactoryTest {

    private String message = "message";
    private String details = "details";
    
    
    @Test
    public void createErrorMessage() {
    
        // execute
        
        ServiceResponse response = ServiceResponseFactory.createErrorResponse(message, details);
        
        
        // assert
        
        assertEquals(ServiceExecutionStatus.ERROR, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(details, response.getDetails());
        
    }

    
    @Test
    public void createOkMessage() {
    
        // execute
        
        ServiceResponse response = ServiceResponseFactory.createOkResponse(message, details);
        
        
        // assert
        
        assertEquals(ServiceExecutionStatus.OK, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(details, response.getDetails());
        
    }

}
