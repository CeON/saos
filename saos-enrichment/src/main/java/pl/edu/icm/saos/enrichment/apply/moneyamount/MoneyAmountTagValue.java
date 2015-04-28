package pl.edu.icm.saos.enrichment.apply.moneyamount;

import java.math.BigDecimal;

/**
 * @author madryk
 */
public class MoneyAmountTagValue {

    private BigDecimal amount;
    
    private String text;
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * Returns amount of money
     */
    public BigDecimal getAmount() {
        return amount;
    }
    
    /**
     * Returns textual representation of money amount
     */
    public String getText() {
        return text;
    }
    
    //------------------------ SETTERS --------------------------
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public void setText(String text) {
        this.text = text;
    }
}
