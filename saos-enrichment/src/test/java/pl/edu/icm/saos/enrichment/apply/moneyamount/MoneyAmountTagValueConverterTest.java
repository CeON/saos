package pl.edu.icm.saos.enrichment.apply.moneyamount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.MoneyAmount;

/**
 * @author madryk
 */
public class MoneyAmountTagValueConverterTest {

    private MoneyAmountTagValueConverter moneyAmountTagValueConverter = new MoneyAmountTagValueConverter();
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void convert_NULL() {
        // execute
        moneyAmountTagValueConverter.convert(null);
    }
    
    @Test
    public void convert() {
        // given
        BigDecimal amount = new BigDecimal(1000.01);
        String amountText = "tysiąc złotych i jeden grosz";
        
        MoneyAmountTagValue moneyAmountTagValue = new MoneyAmountTagValue();
        moneyAmountTagValue.setAmount(amount);
        moneyAmountTagValue.setText(amountText);
        
        // execute
        MoneyAmount moneyAmount = moneyAmountTagValueConverter.convert(moneyAmountTagValue);
        
        // assert
        assertNotNull(moneyAmount);
        assertEquals(amount, moneyAmount.getAmount());
        assertEquals(amountText, moneyAmount.getText());
        assertTrue(moneyAmount.isGenerated());
    }
}
