package pl.edu.icm.saos.common.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;


/**
 * @author Łukasz Dumiszewski
 */

public class CommonValidator {

    private Validator validator;
    
    
    /**
     * Validates using {@link #setValidator(Validator)}
     * 
     * @param object object to validate
     */
    public <T> void validateEx(T object) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        if (constraintViolations.size()>0) {
            throw new ValidationException(constraintViolations.toString());
        }
    }

    
    public <T> Set<ConstraintViolation<T>> validate(T object) {
        return validator.validate(object);
    }
    
    //------------------------ SETTERS --------------------------
    
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    
}
