package pl.edu.icm.saos.persistence.model;

import java.math.BigDecimal;

import pl.edu.icm.saos.persistence.common.GeneratableObject;

/**
 * @author madryk
 */
public class MoneyAmount extends GeneratableObject {

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
