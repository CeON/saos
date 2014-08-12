package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import pl.edu.icm.saos.persistence.model.importer.ImportProcessingSkipReason;

/**
 * @author Łukasz Dumiszewski
 */
  
public class CcjSkippableExceptionMatcher extends TypeSafeMatcher<CcjImportProcessSkippableException> {  
  
    public static CcjSkippableExceptionMatcher hasSkipReason(ImportProcessingSkipReason skipReason) {  
        return new CcjSkippableExceptionMatcher(skipReason);  
    }  
  
    private ImportProcessingSkipReason foundSkipReason;  
    private final ImportProcessingSkipReason expectedSkipReason;  
  
    private CcjSkippableExceptionMatcher(ImportProcessingSkipReason expectedSkipReason) {  
        this.expectedSkipReason = expectedSkipReason;  
    }  
  
    @Override  
    protected boolean matchesSafely(final CcjImportProcessSkippableException exception) {  
        foundSkipReason = exception.getSkipReason();  
        return foundSkipReason.equals(expectedSkipReason);  
    }  
  
    @Override  
    public void describeTo(Description description) {  
        description.appendValue(foundSkipReason)  
                .appendText(" has been found instead of ")  
                .appendValue(expectedSkipReason);  
    }  
}  
