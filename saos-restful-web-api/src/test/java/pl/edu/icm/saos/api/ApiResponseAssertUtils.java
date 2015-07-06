package pl.edu.icm.saos.api;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import pl.edu.icm.saos.api.services.exceptions.status.ErrorReason;

/**
 * @author madryk
 */
public class ApiResponseAssertUtils {

    
    //------------------------ LOGIC --------------------------
    
    public static void assertOk(ResultActions result) throws Exception {
        assertOk(result, "UTF-8");
    }
    
    public static void assertOk(ResultActions result, String charset) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(header().string("Content-Type",
                        allOf(
                                containsString("application/json"),
                                containsString("charset=" + charset)
                        )))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }
    
    public static void assertError(ResultActions result, HttpStatus httpStatus, String reason,
            String propertyName, Matcher<String> messageMatcher) throws Exception {
        
        assertError(result, httpStatus, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", reason, propertyName, messageMatcher);
    }
    
    public static void assertError(ResultActions result, HttpStatus httpStatus, String contentType, String reason,
            String propertyName, Matcher<String> messageMatcher) throws Exception {
        
        result.andExpect(status().is(httpStatus.value()))
                .andExpect(header().string("Content-Type", contentType))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                
                .andExpect(jsonPath("$.error.httpStatus").value(String.valueOf(httpStatus.value())))
                .andExpect(jsonPath("$.error.reason").value(reason))
                .andExpect(jsonPath("$.error.propertyName").value(propertyName))
                .andExpect(jsonPath("$.error.message").value(messageMatcher));
    }
    
    
    public static void assertTooSmallPageSizeError(ResultActions result, int minPageSize) throws Exception {
        
        assertError(result, HttpStatus.BAD_REQUEST, ErrorReason.WRONG_REQUEST_PARAMETER_ERROR.errorReason(),
                ApiConstants.PAGE_SIZE, allOf(containsString(ApiConstants.PAGE_SIZE), containsString("may not be less than " + minPageSize)));
        
    }
    
    public static void assertTooBigPageSizeError(ResultActions result, int maxPageSize) throws Exception {
        
        assertError(result, HttpStatus.BAD_REQUEST, ErrorReason.WRONG_REQUEST_PARAMETER_ERROR.errorReason(),
                ApiConstants.PAGE_SIZE, allOf(containsString(ApiConstants.PAGE_SIZE), containsString("may not be greater than " + maxPageSize)));
        
    }
    
    public static void assertInvalidPageSizeError(ResultActions result, String value) throws Exception {

        assertError(result, HttpStatus.BAD_REQUEST, ErrorReason.WRONG_REQUEST_PARAMETER_ERROR.errorReason(),
                null, containsString(value));
    }
    
    public static void assertNegativePageNumberError(ResultActions result) throws Exception {
        
        assertError(result, HttpStatus.BAD_REQUEST, ErrorReason.WRONG_REQUEST_PARAMETER_ERROR.errorReason(),
                ApiConstants.PAGE_NUMBER, allOf(containsString(ApiConstants.PAGE_NUMBER), containsString("may not be negative")));
        
    }
    
    public static void assertInvalidPageNumberError(ResultActions result, String value) throws Exception {

        assertError(result, HttpStatus.BAD_REQUEST, ErrorReason.WRONG_REQUEST_PARAMETER_ERROR.errorReason(),
                null, containsString(value));
    }
    
    
    public static void assertIncorrectParamNameError(ResultActions result, String paramName) throws Exception {
        
        assertError(result, HttpStatus.BAD_REQUEST, ErrorReason.WRONG_REQUEST_PARAMETER_ERROR.errorReason(),
                paramName, allOf(containsString(paramName), containsString("name is incorrect")));
        
    }
    
    public static void assertIncorrectValueError(ResultActions result, String param, String value) throws Exception {
        
        assertError(result, HttpStatus.BAD_REQUEST, ErrorReason.WRONG_REQUEST_PARAMETER_ERROR.errorReason(),
                param, allOf(containsString(param), containsString("can't have value") , containsString(value)));
        
    }
    
    public static void assertNotFoundError(ResultActions result, long id) throws Exception {
        
        assertError(result, HttpStatus.NOT_FOUND, ErrorReason.ELEMENT_DOES_NOT_EXIST_ERROR.errorReason(),
                null, allOf(containsString(String.valueOf(id)), containsString("does not exists")));
        
    }
    
    public static void assertNotSupportedMethod(ResultActions result, String requestedMethod, String supportedMethod) throws Exception {
        
        assertError(result, HttpStatus.METHOD_NOT_ALLOWED,
                ErrorReason.UNSUPPORTED_HTTP_METHOD_ERROR.errorReason(), null,
                allOf(
                        containsString("Not supported method '" + requestedMethod + "'"),
                        containsString("only " + supportedMethod + " allowed")
                )
        );
    }
    
    public static void assertNotSupportedMediaType(ResultActions result, String requestedMediaType, String supportedMediaType) throws Exception {
        
        assertError(result, HttpStatus.NOT_ACCEPTABLE, MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8",
                ErrorReason.UNSUPPORTED_MEDIA_TYPE_ERROR.errorReason(), null,
                allOf(
                        containsString("Not acceptable media type '" + requestedMediaType + "'"),
                        containsString("only " + supportedMediaType + " allowed")
                )
        );
    }

    public static void assertPageNotFoundError(ResultActions result) throws Exception {
        assertError(result, HttpStatus.NOT_FOUND, ErrorReason.PAGE_DOES_NOT_EXIST_ERROR.errorReason(), null,
                        containsString("Requested page does not exist")
        );
    }

}
