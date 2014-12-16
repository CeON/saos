package pl.edu.icm.saos.enrichment;

/**
 * @author Łukasz Dumiszewski
 */


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.enrichment.upload.EnrichmentTagItem;

import com.fasterxml.jackson.core.JsonFactory;

/**
 * 
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan(useDefaultFilters=false, includeFilters={@Filter(type=FilterType.ANNOTATION, value=Service.class)})
public class EnrichmentConfiguration {


    
    
    @Autowired 
    private CommonValidator commonValidator;   
    
    @Autowired
    private JsonFactory jsonFactory;
   
    
    
    //------------------------ BEANS --------------------------
    
        
    @Bean
    public JsonItemParser<EnrichmentTagItem> enrichmentTagItemParser() {
        JsonItemParser<EnrichmentTagItem> enrichmentTagItemParser = new JsonItemParser<>(EnrichmentTagItem.class);
        enrichmentTagItemParser.setCommonValidator(commonValidator);
        enrichmentTagItemParser.setJsonFactory(jsonFactory);
        return enrichmentTagItemParser;
    }
    
    
    
  

    
}
