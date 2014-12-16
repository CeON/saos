package pl.edu.icm.saos.common.testcommon;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import pl.edu.icm.saos.common.service.ServiceException;

/**
 * Hamcrest matcher of {@link ServiceException} for use in tests
 * 
 * @author Łukasz Dumiszewski
 */

public class ServiceExceptionMatcher extends TypeSafeMatcher<ServiceException> {  
  
    private String foundMainMessage;  
    private final String expectedMainMessage;
    
    
    //------------------------ CONSTRUCTORS --------------------------
  
    private ServiceExceptionMatcher(String expectedMainMessage) {  
        this.expectedMainMessage = expectedMainMessage;  
    }  
  
    
    //------------------------ LOGIC --------------------------
    
    public static ServiceExceptionMatcher hasMainMessage(String mainMessage) {  
        return new ServiceExceptionMatcher(mainMessage);  
    }  
  
  
    @Override  
    protected boolean matchesSafely(final ServiceException exception) {  
        foundMainMessage = exception.getMainMessage();  
        return foundMainMessage.equals(expectedMainMessage);  
    }  
  
    @Override  
    public void describeTo(Description description) {  
        description.appendValue(foundMainMessage)  
                .appendText(" has been found instead of ")  
                .appendValue(expectedMainMessage);  
    }  
}  
