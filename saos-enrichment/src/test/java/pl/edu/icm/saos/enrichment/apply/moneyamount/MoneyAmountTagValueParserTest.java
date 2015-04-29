package pl.edu.icm.saos.enrichment.apply.moneyamount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.validation.CommonValidator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author madryk
 */
public class MoneyAmountTagValueParserTest {

    private final String jsonTagValue =  JsonNormalizer.normalizeJson(
            "{" +
            "   amount: 123000.27, " +
            "   text : '123 tys zł 27 gr'" +
            "}");
    
    private JsonStringParser<MoneyAmountTagValue> jsonStringParser = new JsonStringParser<>(MoneyAmountTagValue.class);
    
    
    @Before
    public void setUp() {
        jsonStringParser.setJsonFactory(new MappingJsonFactory());
        jsonStringParser.setCommonValidator(mock(CommonValidator.class));
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void parseAndValidate() throws JsonParseException {

        // execute
        MoneyAmountTagValue moneyAmountTagValue = jsonStringParser.parseAndValidate(jsonTagValue);
        
        // assert
        assertNotNull(moneyAmountTagValue);
        assertEquals(new BigDecimal("123000.27"), moneyAmountTagValue.getAmount());
        assertEquals("123 tys zł 27 gr", moneyAmountTagValue.getText());
    }
    
}
