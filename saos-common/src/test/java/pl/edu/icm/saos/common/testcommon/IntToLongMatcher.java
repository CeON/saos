package pl.edu.icm.saos.common.testcommon;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
* Hamcrest Integer matcher with long. Convenient for example in cases like: <br/>
* <code>ResultActions.andExpect(jsonPath("id").value(IntToLongMatcher.equals(testObjectContext.getCcCourtId())))</code>
* in which the value method doesn't allow to put long arg indirectly - in such a case you get something like this:
* <pre>java.lang.AssertionError: For JSON path $.data.parentCourt.id type of value expected:<class java.lang.Long> but was:<class java.lang.Integer></pre>
* 
* @author Łukasz Dumiszewski
* */
public class IntToLongMatcher extends TypeSafeMatcher<Integer> {  
  
    private int actualIntValue;  
    private final long expectedLongValue;
    
    
    //------------------------ CONSTRUCTORS --------------------------
  
    private IntToLongMatcher(long expectedLongValue) {  
        this.expectedLongValue = expectedLongValue;  
    }  
  
    
    //------------------------ LOGIC --------------------------
    
    public static IntToLongMatcher equalsLong(Long expectedLongValue) {  
        return new IntToLongMatcher(expectedLongValue);  
    }  
  
  
    @Override  
    protected boolean matchesSafely(Integer actualIntValue) {  
        this.actualIntValue = actualIntValue;  
        return new Long(actualIntValue).equals(expectedLongValue);  
    }  
  
    @Override  
    public void describeTo(Description description) {  
        description.appendValue(actualIntValue)  
                .appendText(" has been found instead of ")  
                .appendValue(expectedLongValue);  
    }  
}  
