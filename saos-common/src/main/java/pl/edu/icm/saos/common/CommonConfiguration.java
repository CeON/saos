package pl.edu.icm.saos.common;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.common.validation.CommonValidator;

/**
 * @author Łukasz Dumiszewski
 */

/**
 * Common configuration
 * @author Łukasz Dumiszewski
 */
@Configuration
public class CommonConfiguration {

    @Bean
    public CommonValidator commonValidator() {
        CommonValidator commonValidator = new CommonValidator();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        commonValidator.setValidator(factory.getValidator());
        return commonValidator;
    }
    
}
