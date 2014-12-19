package pl.edu.icm.saos.common;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.common.json.JsonUtils;
import pl.edu.icm.saos.common.validation.CommonValidator;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author Łukasz Dumiszewski
 */

/**
 * Common configuration
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
public class CommonConfiguration {

    @Bean
    public CommonValidator commonValidator() {
        CommonValidator commonValidator = new CommonValidator();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        commonValidator.setValidator(factory.getValidator());
        return commonValidator;
    }
    
    @Bean
    public MappingJsonFactory jsonFactory() {
        MappingJsonFactory factory = new MappingJsonFactory();
        factory.enable(Feature.ALLOW_COMMENTS);
        return factory;
    }
    
    @Bean
    public JsonUtils jsonUtils() {
        return new JsonUtils();
    }
    
}
