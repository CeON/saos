package pl.edu.icm.saos.persistence.common;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang3.StringUtils;

/**
 * @author madryk
 */
@Converter(autoApply = true)
public class StringAttributeConverter implements AttributeConverter<String, String> {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String convertToDatabaseColumn(String attribute) {
        return StringUtils.remove(attribute, '\u0000');
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }

}
