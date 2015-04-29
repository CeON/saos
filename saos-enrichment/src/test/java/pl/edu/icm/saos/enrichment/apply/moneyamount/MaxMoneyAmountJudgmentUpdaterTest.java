package pl.edu.icm.saos.enrichment.apply.moneyamount;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.MoneyAmount;

/**
 * @author madryk
 */
public class MaxMoneyAmountJudgmentUpdaterTest {

    private MaxMoneyAmountJudgmentUpdater maxMoneyAmountJudgmentUpdater = new MaxMoneyAmountJudgmentUpdater();
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void addToJudgment_NULL_JUDGMENT() {
        // execute
        maxMoneyAmountJudgmentUpdater.addToJudgment(null, new MoneyAmount());
    }
    
    
    @Test(expected = NullPointerException.class)
    public void addToJudgment_NULL_MONEY_AMOUNT() {
        // execute
        maxMoneyAmountJudgmentUpdater.addToJudgment(new CommonCourtJudgment(), null);
    }
    
    
    @Test
    public void addToJudgment() {
        // given
        Judgment judgment = new CommonCourtJudgment();
        MoneyAmount maxMoneyAmount = new MoneyAmount();
        
        // execute
        maxMoneyAmountJudgmentUpdater.addToJudgment(judgment, maxMoneyAmount);
        
        // assert
        assertEquals(maxMoneyAmount, judgment.getMaxMoneyAmount());
    }
    
}
